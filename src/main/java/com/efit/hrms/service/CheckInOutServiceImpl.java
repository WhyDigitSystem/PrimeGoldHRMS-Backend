package com.efit.hrms.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
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

import com.efit.hrms.dto.AttendanceSummaryDTO;
import com.efit.hrms.dto.CheckInOutBiometricDTO;
import com.efit.hrms.entity.AttendanceDailyVO;
import com.efit.hrms.entity.AttendanceProcessVO;
import com.efit.hrms.entity.AttendanceSummaryVO;
import com.efit.hrms.entity.CheckInOutBiometricVO;
import com.efit.hrms.entity.CheckInOutUploadVO;
import com.efit.hrms.entity.OtCalculationVO;
import com.efit.hrms.entity.ShiftAssignDetailsVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.AttendanceDailyRepo;
import com.efit.hrms.repo.AttendanceProcessRepo;
import com.efit.hrms.repo.AttendanceSummaryRepo;
import com.efit.hrms.repo.CheckInOutBiometricRepo;
import com.efit.hrms.repo.CheckInOutUploadRepo;
import com.efit.hrms.repo.CheckInStatusRepo;
import com.efit.hrms.repo.CompanyRepo;
import com.efit.hrms.repo.OtCalculationRepo;
import com.efit.hrms.repo.ShiftAssignDetailsRepo;
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
	
	@Autowired
	ShiftAssignDetailsRepo shiftAssignDetailsRepo;
	
	@Autowired
	AttendanceDailyRepo attendanceDailyRepo;
	
	@Autowired
	AttendanceSummaryRepo attendanceSummaryRepo;
	
	@Override
	@Transactional(rollbackOn = Exception.class)
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
	    attendanceProcessVO.setSourceId(todayCheck.getId());

	    attendanceProcessRepo.save(attendanceProcessVO);
	    
	    LocalDate attendanceDate = today;

	    // Step 1: Fetch Shift based on attendance date
	    List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo
	        .findApplicableShifts(checkInOutBiometricDTO.getEmpCode(), attendanceDate, checkInOutBiometricDTO.getOrgId());

	    ShiftAssignDetailsVO latestShift = shifts.stream()
	        .max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom))
	        .orElse(null);

	    String shiftType = (latestShift != null) ? latestShift.getShiftType() : "General";

	    // Step 2: Determine shiftOutTime
	    LocalTime shiftOutTime;
	    if (latestShift != null && latestShift.getOutTime() != null) {
	        shiftOutTime = LocalTime.parse(latestShift.getOutTime());
	    } else {
	        throw new IllegalStateException("Missing OUT time for active shift.");
	    }

	    // Step 3: Calculate baseDate for pairing
	    LocalDate baseDate;
	    if ("NIGHT".equalsIgnoreCase(shiftType)) {
	        if (checkInOutBiometricDTO.isStatus()) {
	            baseDate = now.isBefore(shiftOutTime) ? today.minusDays(1) : today;
	        } else {
	            Optional<AttendanceProcessVO> lastIn = attendanceProcessRepo
	                .findTopByEmpCodeAndStatusAndOrgIdAndBranchAndCheckInDateLessThanEqualOrderByCheckInDateDescEntryTimeDesc(
	                    checkInOutBiometricDTO.getEmpCode(), "In", checkInOutBiometricDTO.getOrgId(), checkInOutBiometricDTO.getBranch(), today
	                );
	            baseDate = lastIn.map(AttendanceProcessVO::getCheckInDate)
	                             .orElse(now.isBefore(shiftOutTime) ? today.minusDays(1) : today);
	        }
	    } else {
	        baseDate = today;
	    }

	    // Step 4: Fetch records within that date + 1
	    List<AttendanceProcessVO> recs = attendanceProcessRepo.findByEmpCodeAndDateRange(
	        checkInOutBiometricDTO.getEmpCode(), baseDate, baseDate.plusDays(1), checkInOutBiometricDTO.getOrgId(), checkInOutBiometricDTO.getBranch()
	    );

	    List<LocalDateTime> inList = new ArrayList<>();
	    List<LocalDateTime> outList = new ArrayList<>();

	    for (AttendanceProcessVO rec : recs) {
	        LocalDateTime dt = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
	        if ("In".equalsIgnoreCase(rec.getStatus())) inList.add(dt);
	        else if ("Out".equalsIgnoreCase(rec.getStatus())) outList.add(dt);
	    }

	    Collections.sort(inList);
	    Collections.sort(outList);

	    long effectiveSeconds = 0;
	    int outIdx = 0;
	    for (LocalDateTime inTime : inList) {
	        while (outIdx < outList.size() && outList.get(outIdx).isBefore(inTime)) outIdx++;
	        if (outIdx < outList.size()) {
	            LocalDateTime outTime = outList.get(outIdx);
	            if (!outTime.isBefore(inTime)) {
	                effectiveSeconds += Duration.between(inTime, outTime).getSeconds();
	                outIdx++;
	            }
	        }
	    }

	    LocalDateTime firstIn = inList.stream().min(LocalDateTime::compareTo).orElse(null);
	    LocalDateTime lastOut = outList.stream().max(LocalDateTime::compareTo).orElse(null);
	    long grossSeconds = (firstIn != null && lastOut != null && lastOut.isAfter(firstIn)) ?
	        Duration.between(firstIn, lastOut).getSeconds() : 0;

	    // Step 5: Save/update AttendanceDaily
	    AttendanceDailyVO ad = attendanceDailyRepo.findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
	        checkInOutBiometricDTO.getEmpCode(), baseDate, checkInOutBiometricDTO.getOrgId(), checkInOutBiometricDTO.getBranch());

	    if (ad == null) {
	        ad = new AttendanceDailyVO();
	        ad.setEmpCode(checkInOutBiometricDTO.getEmpCode());
	        ad.setEmpName(checkInOutBiometricDTO.getEmpName());
	        ad.setBranch(checkInOutBiometricDTO.getBranch());
	        ad.setBranchCode(checkInOutBiometricDTO.getBranchCode());
	        ad.setOrgId(checkInOutBiometricDTO.getOrgId());
	        ad.setCheckInDate(baseDate);
	        ad.setFinyear(String.valueOf(baseDate.getYear()));
	        ad.setAttendanceMode("BIOMETRIC");
	    }

	    if (firstIn != null) ad.setInTime(firstIn.toLocalTime());
	    if (lastOut != null) {
	        ad.setOutTime(lastOut.toLocalTime());
	        ad.setCheckOutDate(lastOut.toLocalDate());
	    }

	    ad.setEffectiveHours((int) (effectiveSeconds / 3600));
	    ad.setGrossHours((int) (grossSeconds / 3600));

	    attendanceDailyRepo.save(ad);
	    // Build response
	    response.put("message", checkInOutBiometricDTO.isStatus() ? "Check-in created successfully" : "Check-out created successfully");
	    response.put("checkInBiometricVO", todayCheck);

	    return response;
	}

	
	
	//checkinoutupload

	@Transactional(rollbackOn = Exception.class)
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
	                vo.setSourceId(dto.getId());
	                vo.setAttendanceMode("FILES");
	                

	                attendanceProcessRepo.save(vo);
	                
	                
	                
	                LocalDate attendanceDate = dto.getCheckInDate();

	                // Step 1: Fetch Shift based on attendance date
	                List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo
	                    .findApplicableShifts(dto.getEmpcode(), attendanceDate, dto.getOrgId());

	                ShiftAssignDetailsVO latestShift = shifts.stream()
	                    .max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom))
	                    .orElse(null);

	                String shiftType = (latestShift != null) ? latestShift.getShiftType() : "General";

	                // Step 2: Determine shiftOutTime
	                LocalTime shiftOutTime;
	                if (latestShift != null && latestShift.getOutTime() != null) {
	                    shiftOutTime = LocalTime.parse(latestShift.getOutTime());
	                } else {
	                    throw new IllegalStateException("Missing OUT time for active shift.");
	                }

	                // Step 3: Calculate baseDate for pairing
	                LocalDate baseDate;
	                if ("NIGHT".equalsIgnoreCase(shiftType)) {
	                    if ("In".equalsIgnoreCase(dto.getStatus())) {
	                        baseDate = dto.getEntryTime().isBefore(shiftOutTime) ? dto.getCheckInDate().minusDays(1) : dto.getCheckInDate();
	                    } else {
	                        Optional<AttendanceProcessVO> lastIn = attendanceProcessRepo
	                            .findTopByEmpCodeAndStatusAndOrgIdAndBranchAndCheckInDateLessThanEqualOrderByCheckInDateDescEntryTimeDesc(
	                                dto.getEmpcode(), "In", dto.getOrgId(), dto.getBranch(), dto.getCheckInDate()
	                            );
	                        baseDate = lastIn.map(AttendanceProcessVO::getCheckInDate)
	                                         .orElse(dto.getEntryTime().isBefore(shiftOutTime) ? dto.getCheckInDate().minusDays(1) : dto.getCheckInDate());
	                    }
	                } else {
	                    baseDate = dto.getCheckInDate();
	                }

	                // Step 4: Fetch records within that date + 1
	                List<AttendanceProcessVO> recs = attendanceProcessRepo.findByEmpCodeAndDateRange(
	                    dto.getEmpcode(), baseDate, baseDate.plusDays(1), dto.getOrgId(), dto.getBranch()
	                );

	                List<LocalDateTime> inList = new ArrayList<>();
	                List<LocalDateTime> outList = new ArrayList<>();

	                for (AttendanceProcessVO rec : recs) {
	                    LocalDateTime dt = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
	                    if ("In".equalsIgnoreCase(rec.getStatus())) inList.add(dt);
	                    else if ("Out".equalsIgnoreCase(rec.getStatus())) outList.add(dt);
	                }

	                Collections.sort(inList);
	                Collections.sort(outList);

	                long effectiveSeconds = 0;
	                int outIdx = 0;
	                for (LocalDateTime inTime : inList) {
	                    while (outIdx < outList.size() && outList.get(outIdx).isBefore(inTime)) outIdx++;
	                    if (outIdx < outList.size()) {
	                        LocalDateTime outTime = outList.get(outIdx);
	                        if (!outTime.isBefore(inTime)) {
	                            effectiveSeconds += Duration.between(inTime, outTime).getSeconds();
	                            outIdx++;
	                        }
	                    }
	                }

	                LocalDateTime firstIn = inList.stream().min(LocalDateTime::compareTo).orElse(null);
	                LocalDateTime lastOut = outList.stream().max(LocalDateTime::compareTo).orElse(null);
	                long grossSeconds = (firstIn != null && lastOut != null && lastOut.isAfter(firstIn)) ?
	                    Duration.between(firstIn, lastOut).getSeconds() : 0;

	                // Step 5: Save/update AttendanceDaily
	                AttendanceDailyVO ad = attendanceDailyRepo.findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
	                    dto.getEmpcode(), baseDate, dto.getOrgId(), dto.getBranch());

	                if (ad == null) {
	                    ad = new AttendanceDailyVO();
	                    ad.setEmpCode(dto.getEmpcode());
	                    ad.setEmpName(dto.getEmpname());
	                    ad.setBranch(dto.getBranch());
	                    ad.setBranchCode(dto.getBranchCode());
	                    ad.setOrgId(dto.getOrgId());
	                    ad.setCheckInDate(baseDate);
	                    ad.setFinyear(String.valueOf(baseDate.getYear()));
	                    ad.setAttendanceMode("FILES");
	                }

	                if (firstIn != null) ad.setInTime(firstIn.toLocalTime());
	                if (lastOut != null) {
	                    ad.setOutTime(lastOut.toLocalTime());
	                    ad.setCheckOutDate(lastOut.toLocalDate());
	                }

	                ad.setEffectiveHours((int) (effectiveSeconds / 3600));
	                ad.setGrossHours((int) (grossSeconds / 3600));

	                attendanceDailyRepo.save(ad);
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
	public List<Map<String, Object>> getLeaveDetailsForAttendanceProcess(String fromDate, String toDate, Long orgId, String department, String branch,String type,String contractor) {

	    Set<Object[]> result = attendanceProcessRepo.getLeaveDetailsForAttendanceProcess(fromDate, toDate, orgId, department, branch,type,contractor);
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
			map.put("branchCode", record[3] != null ? record[3].toString() : "");
			map.put("department", record[4] != null ? record[4].toString() : "");
			map.put("month", record[5] != null ? new BigInteger(record[5].toString()).toString() : "0");
			map.put("year", record[6] != null ? record[6].toString() : "0"); // Keep as string
			map.put("totalDays", record[7] != null ? new BigInteger(record[7].toString()).toString() : "0");
			map.put("holidays", record[8] != null ? new BigInteger(record[8].toString()).toString() : "0");
			map.put("weekOffs", record[9] != null ? new BigInteger(record[9].toString()).toString() : "0");

			map.put("leaves", record[10] != null ? new BigDecimal(record[10].toString()).toPlainString() : "0");
			map.put("absent", record[11] != null ? ((BigDecimal) record[11]).toPlainString() : "0");
			map.put("lop", record[12] != null ? ((BigDecimal) record[12]).toPlainString() : "0");
			map.put("presentDays", record[13] != null ? ((BigDecimal) record[13]).toPlainString() : "0");
			map.put("salaryDays", record[14] != null ? ((BigDecimal) record[14]).toPlainString() : "0");
//			map.put("otHours", record[15] != null ? record[15].toString() : "00:00");


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
	        try {
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

	            // OT Hours
	            Integer otHours = row[5] != null ? Integer.parseInt(row[5].toString().trim()) : 0;
	            vo.setOthours(otHours);

	            // OT Amount and Rate (safely parsed)
	            vo.setOtamount(safeBigDecimal(row[6]));
	            vo.setRate(safeBigDecimal(row[7]));

	            // OT Type, Category, Company Policy
	            vo.setOttype(row[8] != null ? row[8].toString().trim() : null);
	            vo.setOtcategory(row[9] != null ? row[9].toString().trim() : null);
	            vo.setCompanyOtPolicy(row[10] != null ? row[10].toString().trim() : null);

	            vo.setStatus("PENDING");
	            vo.setCreatedon(LocalDateTime.now());

	            resultList.add(vo);
	        } catch (Exception e) {
	            System.err.println("Error processing OT row: " + Arrays.toString(row));
	            e.printStackTrace(); // Optionally log this instead
	        }
	    }

	    return otCalculationRepo.saveAll(resultList);
	}


	private BigDecimal safeBigDecimal(Object obj) {
	    try {
	        return obj != null ? new BigDecimal(obj.toString().trim()) : BigDecimal.ZERO;
	    } catch (NumberFormatException e) {
	        System.err.println("Invalid BigDecimal input: " + obj);
	        return BigDecimal.ZERO;
	    }
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


	
	@Override
	public List<AttendanceDailyVO> getAttendanceDailyByOrgId(String fromDate, String toDate, Long orgId,
			String employeeCode, String branch) {
		// TODO Auto-generated method stub
		return attendanceDailyRepo.getAttendanceDailyByOrgId( fromDate,  toDate,  orgId,
				 employeeCode,  branch);
	}
	
	
	
	@Override
	public Map<String, Object> createUpdateAttendanceSummary(@Valid List<AttendanceSummaryDTO> attendanceSummaryDTOList)
	        throws ApplicationException {

	    List<AttendanceSummaryVO> attendanceSummaryVOList = new ArrayList<>();
	    String message = "";

	    for (AttendanceSummaryDTO dto : attendanceSummaryDTOList) {
	        AttendanceSummaryVO vo;

	        if (ObjectUtils.isNotEmpty(dto.getId())) {
	            vo = attendanceSummaryRepo.findById(dto.getId())
	                    .orElseThrow(() -> new ApplicationException("Invalid AttendanceSummary details"));
	            vo.setUpdatedBy(dto.getCreatedBy());
	            message = "AttendanceSummary updated successfully";
	        } else {
	            vo = new AttendanceSummaryVO();
	            vo.setCreatedBy(dto.getCreatedBy());
	            vo.setUpdatedBy(dto.getCreatedBy());
	            message = "AttendanceSummary created successfully";
	        }

	        mapAttendanceSummaryDTOToAttendanceSummaryVO(dto, vo); // ✅ Correct parameter order
	        attendanceSummaryVOList.add(vo);
	    }

	    attendanceSummaryRepo.saveAll(attendanceSummaryVOList); // ✅ Single save for multiple records

	    Map<String, Object> response = new HashMap<>();
	    response.put("attendanceSummaryVO", attendanceSummaryVOList);
	    response.put("message", message);
	    return response;
	}

	private void mapAttendanceSummaryDTOToAttendanceSummaryVO(AttendanceSummaryDTO dto, AttendanceSummaryVO vo) {
	    vo.setEmpCode(dto.getEmpCode());
	    vo.setEmpName(dto.getEmpName());
	    vo.setOrgId(dto.getOrgId());
	    vo.setDepartment(dto.getDepartment());
	    vo.setBranch(dto.getBranch());
	    vo.setBranchCode(dto.getBranchCode());

	    vo.setFinyear(dto.getFinyear());
	    vo.setMonth(dto.getMonth());
	    vo.setFinyear(dto.getFinyear()); // ✅ Make sure to include year if needed
	    vo.setTotalDays(dto.getTotalDays());
	    vo.setHolidays(dto.getHolidays());
	    vo.setWeekoff(dto.getWeekoff());
	    vo.setLeaves(dto.getLeaves());
	    vo.setAbsent(dto.getAbsent());
	    vo.setLop(dto.getLop());
	    vo.setPresent(dto.getPresent());
	    vo.setSalarydays(dto.getSalarydays());

	    vo.setApproveStatus("PENDING"); // default
	}
	
	
	@Override
	public Map<String, Object> createApprovalAttendanceSummary(Long orgId, List<Long> ids, String action, String actionBy) throws ApplicationException {
	    List<AttendanceSummaryVO> updatedList = new ArrayList<>();
	    String message = "";

	    for (Long id : ids) {
	        AttendanceSummaryVO summaryVO = attendanceSummaryRepo.findById(id)
	                .orElseThrow(() -> new ApplicationException("Invalid AttendanceSummary ID: " + id));

	        String currentStatus = summaryVO.getApproveStatus();

	        if (currentStatus == null || (!currentStatus.equalsIgnoreCase("APPROVED") && !currentStatus.equalsIgnoreCase("REJECTED"))) {

	            if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {
	                summaryVO.setApproveStatus(action.toUpperCase());
	                summaryVO.setApproveBy(actionBy);

	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
	                summaryVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

	                updatedList.add(summaryVO);
	            }
	        } else if ("APPROVED".equalsIgnoreCase(currentStatus)) {
	            throw new ApplicationException("AttendanceSummary already approved for employee: " + summaryVO.getEmpCode());
	        } else if ("REJECTED".equalsIgnoreCase(currentStatus)) {
	            throw new ApplicationException("AttendanceSummary already rejected for employee: " + summaryVO.getEmpCode());
	        }
	    }

	    attendanceSummaryRepo.saveAll(updatedList);

	    if ("APPROVED".equalsIgnoreCase(action)) {
	        message = "Attendance Summary Approved Successfully";
	    } else if ("REJECTED".equalsIgnoreCase(action)) {
	        message = "Attendance Summary Rejected Successfully";
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("attendanceSummaryVO", updatedList);
	    response.put("message", message);
	    return response;
	}
	
	
	@Override
	public List<AttendanceSummaryVO> getPendingAttendanceSummaryByOrgId(Long orgId,
		String branch) {
		// TODO Auto-generated method stub
		return attendanceSummaryRepo.getPendingAttendanceSummaryByOrgId(  orgId,
				 branch);
	}
	
	@Override
	public List<AttendanceSummaryVO> getAttendanceSummaryByOrgId(String empCode,Integer month, String finYear, Long orgId, String branch) {
		// TODO Auto-generated method stub
		return attendanceSummaryRepo.getAttendanceSummaryByOrgId(   empCode, month, finYear,  orgId,  branch);
	}


}
