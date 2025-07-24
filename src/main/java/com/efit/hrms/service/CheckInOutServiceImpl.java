package com.efit.hrms.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.CheckInOutBiometricDTO;
import com.efit.hrms.entity.AttendanceProcessVO;
import com.efit.hrms.entity.CheckInOutBiometricVO;
import com.efit.hrms.entity.CheckInOutUploadVO;
import com.efit.hrms.entity.CheckInStatusVO;
import com.efit.hrms.entity.CompanyVO;
import com.efit.hrms.entity.LocationUtils;
import com.efit.hrms.entity.OtCalculationVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.AttendanceProcessRepo;
import com.efit.hrms.repo.CheckInOutBiometricRepo;
import com.efit.hrms.repo.CheckInOutUploadRepo;
import com.efit.hrms.repo.CheckInStatusRepo;
import com.efit.hrms.repo.CompanyRepo;
import com.efit.hrms.repo.OtCalculationRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CheckInOutServiceImpl implements CheckInOutService {


	
	@Autowired
	CheckInOutBiometricRepo checkInOutRepo;
	

	@Autowired
	CheckInStatusRepo checkInStatusRepo;

	@Autowired
	CompanyRepo companyRepo;


	@Autowired
	AttendanceProcessRepo attendanceProcessRepo;
	
	@Autowired
	CheckInOutUploadRepo checkInOutUploadRepo;
  
	
	@Autowired
	OtCalculationRepo otCalculationRepo;
	
	@Override
	@Transactional
	public Map<String, Object> createCheckInOutBiometric(CheckInOutBiometricDTO checkInOutBiometricDTO) throws ApplicationException {
	    Map<String, Object> response = new HashMap<>();

	    LocalDate today = LocalDate.now();
	    LocalTime now = LocalTime.now();

	    // Save to CheckInOutBiometricVO
	    CheckInOutBiometricVO todayCheck = new CheckInOutBiometricVO();
	    todayCheck.setEmpCode(checkInOutBiometricDTO.getEmpCode());
	    todayCheck.setEmpName(checkInOutBiometricDTO.getEmpName());
	    todayCheck.setBranch(checkInOutBiometricDTO.getBranch());
	    todayCheck.setBranchCode(checkInOutBiometricDTO.getBranchCode());
	    todayCheck.setCheckInDate(today);
	    todayCheck.setEntryTime(now);
	    todayCheck.setOrgId(checkInOutBiometricDTO.getOrgId());
	    todayCheck.setStatus(checkInOutBiometricDTO.isStatus() ? "In" : "Out");
	    todayCheck.setEmail(checkInOutBiometricDTO.getEmail());
	    todayCheck.setFinyear(checkInOutBiometricDTO.getFinyear());
	    todayCheck.setAttendanceMode("BIOMETRIC");

	    checkInOutRepo.save(todayCheck);

	    // Save to AttendanceProcessVO
	    AttendanceProcessVO attendanceProcessVO = new AttendanceProcessVO();
	    attendanceProcessVO.setEmpCode(checkInOutBiometricDTO.getEmpCode());
	    attendanceProcessVO.setEmpName(checkInOutBiometricDTO.getEmpName());
	    attendanceProcessVO.setBranch(checkInOutBiometricDTO.getBranch());
	    attendanceProcessVO.setBranchCode(checkInOutBiometricDTO.getBranchCode());
	    attendanceProcessVO.setFinyear(checkInOutBiometricDTO.getFinyear());
	    attendanceProcessVO.setCheckInDate(today);
	    attendanceProcessVO.setEntryTime(now);
	    attendanceProcessVO.setStatus(checkInOutBiometricDTO.isStatus() ? "In" : "Out");
	    attendanceProcessVO.setAttendanceMode("BIOMETRIC");
	    attendanceProcessVO.setOrgId(checkInOutBiometricDTO.getOrgId());

	    attendanceProcessRepo.save(attendanceProcessVO);

	    // Build response
	    response.put("message", checkInOutBiometricDTO.isStatus() ? "Check-in created successfully" : "Check-out created successfully");
	    response.put("checkInBiometricVO", todayCheck);

	    return response;
	}

	
	
	//checkinoutupload

	@Transactional
	@Override
	public String checkInOutUploadExcel(MultipartFile file, Long orgId, String createdBy) throws Exception {
	    List<CheckInOutUploadVO> validList = new ArrayList<>();
	    List<Map<String, Object>> failures = new ArrayList<>();
	    int successCount = 0;

	    try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
	        Sheet sheet = workbook.getSheetAt(0);

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            try {
	                CheckInOutUploadVO vo = new CheckInOutUploadVO();

	                String empName = getStringCell(row, 0);
	                String empCode = getStringCell(row, 1);
	                String branchCode = getStringCell(row, 2);
	                String branch = getStringCell(row, 3);
	                String finYear = getStringCell(row, 4);
	                String status = getStringCell(row, 7);

	                if (empCode == null || empCode.trim().isEmpty()) {
	                    throw new IllegalArgumentException("Employee Code is missing");
	                }

	                LocalDate checkInDate;
	                Cell dateCell = row.getCell(5);
	                if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
	                    checkInDate = dateCell.getLocalDateTimeCellValue().toLocalDate();
	                } else {
	                    throw new IllegalArgumentException("Invalid format for checkInDate at row " + (i + 1));
	                }

	                LocalTime entryTime;
	                try {
	                    entryTime = row.getCell(6).getLocalDateTimeCellValue().toLocalTime();
	                } catch (Exception e) {
	                    throw new IllegalArgumentException("Invalid or missing Entry Time at row " + (i + 1));
	                }

	                if (!"In".equalsIgnoreCase(status) && !"Out".equalsIgnoreCase(status)) {
	                    throw new IllegalArgumentException("Status must be 'In' or 'Out'");
	                }

	                vo.setEmpname(empName);
	                vo.setEmpcode(empCode);
	                vo.setOrgId(orgId);
	                vo.setBranchCode(branchCode);
	                vo.setBranch(branch);
	                vo.setFinYear(finYear);
	                vo.setCheckInDate(checkInDate);
	                vo.setEntryTime(entryTime);
	                vo.setStatus(status);
	                vo.setCreatedBy(createdBy);

	                validList.add(vo);
	                successCount++;

	            } catch (Exception e) {
	                Map<String, Object> error = new HashMap<>();
	                error.put("row", i + 1);
	                error.put("error", e.getMessage());
	                failures.add(error);
	            }
	        }

	        Map<String, Object> result = new LinkedHashMap<>();
	        result.put("successCount", successCount);
	        result.put("failedCount", failures.size());
	        result.put("failures", failures);

	        if (failures.isEmpty()) {
	            // ✅ Save CheckInOutUploadVOs
	            checkInOutUploadRepo.saveAll(validList);

	            // ✅ Save AttendanceProcessVOs
	            for (CheckInOutUploadVO dto : validList) {
	                AttendanceProcessVO vo = new AttendanceProcessVO();
	                vo.setEmpCode(dto.getEmpcode());
	                vo.setEmpName(dto.getEmpname());
	                vo.setOrgId(dto.getOrgId());
	                vo.setBranchCode(dto.getBranchCode());
	                vo.setBranch(dto.getBranch());
	                vo.setFinyear(dto.getFinYear());
	                vo.setCheckInDate(dto.getCheckInDate());
	                vo.setEntryTime(dto.getEntryTime());
	                vo.setStatus(dto.getStatus());
	                vo.setAttendanceMode("FILES");
	                

	                attendanceProcessRepo.save(vo);
	            }

	            result.put("message", "All records uploaded and saved successfully.");
	        } else {
	            result.put("message", "Upload failed. No records saved. Found " + failures.size() + " errors.");
	        }

	        return new ObjectMapper().writeValueAsString(result);
	    }
	}


	private String getStringCell(Row row, int col) {
	    try {
	        Cell cell = row.getCell(col);
	        if (cell == null) return "";
	        cell.setCellType(CellType.STRING);
	        return cell.getStringCellValue().trim();
	    } catch (Exception e) {
	        return "";
	    }
	}

	

	@Override
	public List<Map<String, Object>> getLeaveDetailsForAttendanceProcess(String fromDate, String toDate, Long orgId, String department, String branch) {

	    Set<Object[]> result = attendanceProcessRepo.getLeaveDetailsForAttendanceProcess(fromDate, toDate, orgId, department, branch);
	    return mapLeaveDetails(result, fromDate, toDate);
	}


	private List<Map<String, Object>> mapLeaveDetails(Set<Object[]> result, String fromDate, String toDate) {
		List<Map<String, Object>> detailsList = new ArrayList<>();
		 if (result == null || result.isEmpty()) {
		        // Compare fromDate and toDate
		        String monthName = getMonthWithMoreDays(fromDate, toDate);
		        throw new RuntimeException("NO DATA FOUND IN " + monthName.toUpperCase() + " MONTH.");
		    }
		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeName", record[0] != null ? record[0].toString() : "");
			map.put("employeeCode", record[1] != null ? record[1].toString() : "");
			map.put("branch", record[2] != null ? record[2].toString() : "");
			map.put("department", record[3] != null ? record[3].toString() : "");
			map.put("month", record[4] != null ? record[4].toString() : "0");
			map.put("year", record[5] != null ? record[5].toString() : "0");
			map.put("totalDays", record[6] != null ? record[6].toString() : "0");
			map.put("holidays", record[7] != null ? record[7].toString() : "0");
			map.put("weekOffs", record[8] != null ? record[8].toString() : "0");
			map.put("leaves", record[9] != null ? record[9].toString() : "0");
			map.put("absent", record[10] != null ? record[10].toString() : "0");
			map.put("lop", record[11] != null ? record[11].toString() : "0");
			map.put("presentDays", record[12] != null ? record[12].toString() : "0");
			map.put("salaryDays", record[13] != null ? record[13].toString() : "0");
//			map.put("otHours", record[14] != null ? record[14].toString() : "00:00");


			detailsList.add(map);
		}
		return detailsList;
	}

	
	private String getMonthWithMoreDays(String fromDate, String toDate) {
	    try {
	        LocalDate from = LocalDate.parse(fromDate);
	        LocalDate to = LocalDate.parse(toDate);

	        YearMonth fromMonth = YearMonth.from(from);
	        YearMonth toMonth = YearMonth.from(to);

	        int fromDays = fromMonth.lengthOfMonth();
	        int toDays = toMonth.lengthOfMonth();

	        YearMonth selectedMonth = (fromDays >= toDays) ? fromMonth : toMonth;

	        return selectedMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	    } catch (DateTimeParseException e) {
	        return "Unknown";
	    }
	}
	
	@Override
	public List<OtCalculationVO> generateOtAndSave(Long orgId) {
	    List<Object[]> rows = otCalculationRepo.getFinalOtRecords(orgId);

	    List<OtCalculationVO> resultList = new ArrayList<>();
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	    for (Object[] row : rows) {
	        String empcode = (String) row[0];
	        LocalDate checkindate = ((java.sql.Date) row[2]).toLocalDate();

	        // Check if record already exists
	        Optional<OtCalculationVO> existingOpt = otCalculationRepo.findByEmpcodeAndCheckindate(empcode, checkindate);
	        OtCalculationVO vo = existingOpt.orElse(new OtCalculationVO());

	        vo.setEmpcode(empcode);
	        vo.setEmpname((String) row[1]);
	        vo.setCheckindate(checkindate);

	        // Set IN/OUT time
	        String intimeStr = (String) row[3];
	        String outtimeStr = (String) row[4];
	        vo.setIntime(LocalTime.parse(intimeStr, timeFormatter));
	        vo.setOuttime(LocalTime.parse(outtimeStr, timeFormatter));

	        // Set OT hours as integer
	        Integer otHours = row[5] != null ? Integer.parseInt(row[5].toString()) : 0;
	        vo.setOthours(otHours); 

	        vo.setOtamount(new BigDecimal(String.valueOf(row[6])));

	        // ✅ Fix: rate as BigDecimal from String
	        vo.setRate(row[7] != null ? new BigDecimal(row[7].toString()) : BigDecimal.ZERO);

	        vo.setOttype(row[8] != null ? row[8].toString() : null);
	        vo.setOtcategory(row[9] != null ? row[9].toString() : null);


	        vo.setCreatedon(LocalDateTime.now());

	        resultList.add(vo);
	    }

	    return otCalculationRepo.saveAll(resultList);
	}


//monthlyprocess

	@Override
	public List<Map<String, Object>> getMonthlyProcess(int month, int year, Long orgId, String branch, String department,String type, String contractor) {
	    List<Map<String, Object>> rawList = attendanceProcessRepo.findMonthlyProcess(month, year, orgId, branch, department, type,  contractor);

	    List<Map<String, Object>> orderedList = new ArrayList<>();

	    for (Map<String, Object> row : rawList) {
	        Map<String, Object> orderedMap = new LinkedHashMap<>();
	        orderedMap.put("code", row.get("code"));
	        orderedMap.put("name", row.get("name"));
	        orderedMap.put("branch", row.get("branch"));
	        orderedMap.put("department", row.get("department"));
	        orderedMap.put("shifttype", row.get("shifttype"));
	        for (int i = 1; i <= 31; i++) {
	            orderedMap.put("day_" + i, row.get("day_" + i));
	        }
	        orderedList.add(orderedMap);
	    }

	    return orderedList;
	}


}
