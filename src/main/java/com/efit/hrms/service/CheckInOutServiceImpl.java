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

import com.efit.hrms.entity.AttendanceProcessVO;
import com.efit.hrms.entity.CheckInOutUploadVO;
import com.efit.hrms.entity.OtCalculationVO;
import com.efit.hrms.repo.AttendanceProcessRepo;
import com.efit.hrms.repo.CheckInOutRepo;
import com.efit.hrms.repo.CheckInOutUploadRepo;
import com.efit.hrms.repo.CheckInStatusRepo;
import com.efit.hrms.repo.CompanyRepo;
import com.efit.hrms.repo.OtCalculationRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CheckInOutServiceImpl implements CheckInOutService {


	
	@Autowired
	CheckInOutRepo checkInOutRepo;
	

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
	
//	@Override
//	@Transactional
//	public Map<String, Object> createCheckInOut(UserNameDTO userNameDTO) throws ApplicationException {
//		Map<String, Object> response = new HashMap<>();
//		LocalDate today = LocalDate.now();
//		LocalTime now = LocalTime.now();
//		boolean allowedTodayCheckin = true;
//		CheckInOutVO todayCheck = null;
//
//		List<CompanyVO> companyList = companyRepo.findByCompany(userNameDTO.getOrgId());
//		boolean hybridStatus;
//
//		if (!companyList.isEmpty()) {
//		    CompanyVO company = companyList.get(0); // get the first record (assuming only one per orgId)
//		    hybridStatus = company.isHybrid();
//		    // Use hybridStatus as needed
//		} else {
//		    throw new ApplicationException("Company not found for id: " + userNameDTO.getOrgId());
//		}
//
//		
//		if (userNameDTO.getLatitude() == null || userNameDTO.getLongitude() == null) {
//		    throw new ApplicationException("Latitude or Longitude cannot be null");
//		}
//		
//		String workFromHome = hybridStatus ? userNameDTO.getWorkFromHome() : "NO";
//		
//		if ("NO".equals(workFromHome)) {
//		  if (!isWithinCompanyLocation(userNameDTO.getLatitude(), userNameDTO.getLongitude(),userNameDTO.getOrgId())) {
//		        throw new ApplicationException("You are not within the allowed office location. Check-in/out denied");
//
//		    }
//		}
//
//		// 1. Check last known status
//		Optional<CheckInStatusVO> latestStatusOpt = checkInStatusRepo.findTopByEmpcodeAndOrgIdAndBranchOrderByIdDesc(
//				userNameDTO.getEmpcode(), userNameDTO.getOrgId(), userNameDTO.getBranch());
//
//		if (latestStatusOpt.isPresent()) {
//			CheckInStatusVO latestStatus = latestStatusOpt.get();
//
//			if ("In".equalsIgnoreCase(latestStatus.getStatus())) {
//				Optional<CheckInOutVO> lastCheckInOpt = checkInOutRepo.findTopByEmpCodeAndOrgIdAndBranchOrderByIdDesc(
//						userNameDTO.getEmpcode(), userNameDTO.getOrgId(), userNameDTO.getBranch());
//
//				if (lastCheckInOpt.isPresent()) {
//					CheckInOutVO lastCheckIn = lastCheckInOpt.get();
//
//					if (!lastCheckIn.getCheckInDate().isEqual(today)) {
////	                    boolean alreadyCheckedOut = checkInRepo.existsByEmpCodeAndBranchAndOrgIdAndCheckInDateAndStatusAndL(
////	                            userNameDTO.getEmpcode(), userNameDTO.getBranch(), userNameDTO.getOrgId(),
////	                            lastCheckIn.getCheckInDate(), "Out");
////	                    if (!alreadyCheckedOut) {
//						// ✅ Auto-checkout for yesterday
//						CheckInOutVO autoCheckout = new CheckInOutVO();
//						autoCheckout.setEmpCode(userNameDTO.getEmpcode());
//						autoCheckout.setEmpName(userNameDTO.getEmpName());
//						autoCheckout.setBranch(userNameDTO.getBranch());
//						autoCheckout.setBranchCode(userNameDTO.getBranchCode());
//						autoCheckout.setCheckInDate(lastCheckIn.getCheckInDate());
//						autoCheckout.setNotify(userNameDTO.getNotify());
//						autoCheckout.setNotifyCode(userNameDTO.getNotifyCode());
//						autoCheckout.setNotifyEmail(userNameDTO.getNotifyEmail());
//						autoCheckout.setEmail(userNameDTO.getEmail());
//
//
//						autoCheckout.setLatitude(userNameDTO.getLatitude());
//						autoCheckout.setLongitude(userNameDTO.getLongitude());
//						autoCheckout.setWorkFromHome(userNameDTO.getWorkFromHome());
//						autoCheckout.setLocationAddress(userNameDTO.getLocationAddress());
//						autoCheckout.setEntryTime(LocalTime.MIDNIGHT);
//						autoCheckout.setOrgId(userNameDTO.getOrgId());
//						autoCheckout.setFinyear(userNameDTO.getFinyear());
//						autoCheckout.setAttendanceMode("SYSTEM");
//
//						LocalDateTime lastCheckInCreatedOn = lastCheckIn.getCreatedOn(); // Get the createdOn of last
//																							// check-in
//						if (lastCheckInCreatedOn != null) {
//							// Use the same date but with the same time as last check-in's createdOn
//							LocalDateTime correctedCheckout = LocalDateTime.of(lastCheckIn.getCheckInDate(),
//									lastCheckInCreatedOn.toLocalTime());
//							autoCheckout.setCreatedOn(correctedCheckout);
//						} else {
//							// If lastCheckInCreatedOn is null, use the current time as fallback
//							autoCheckout.setCreatedOn(LocalDateTime.now());
//						}
//						autoCheckout.setStatus("Out");
//
//						checkInOutRepo.save(autoCheckout);
//
//						CheckInStatusVO autoCheckoutStatus = new CheckInStatusVO();
//						autoCheckoutStatus.setEmpcode(userNameDTO.getEmpcode());
//						autoCheckoutStatus.setEmpName(userNameDTO.getEmpName());
//						autoCheckoutStatus.setStatus("Out");
//						autoCheckoutStatus.setOrgId(userNameDTO.getOrgId());
//						autoCheckoutStatus.setBranch(userNameDTO.getBranch());
//						allowedTodayCheckin = false;
//
//						checkInStatusRepo.save(autoCheckoutStatus);
//						
//						AttendanceProcessVO attendanceProcessVO = new AttendanceProcessVO();
//						attendanceProcessVO.setEmpName(userNameDTO.getEmpName());
//						attendanceProcessVO.setEmpCode(userNameDTO.getEmpcode());
//						attendanceProcessVO.setBranch(userNameDTO.getBranch());
//						attendanceProcessVO.setBranchCode(userNameDTO.getBranchCode());
//						attendanceProcessVO.setFinyear(userNameDTO.getFinyear());
//						attendanceProcessVO.setCheckInDate(lastCheckIn.getCheckInDate());
//						attendanceProcessVO.setEntryTime(LocalTime.MIDNIGHT);
//						
//						if (lastCheckInCreatedOn != null) {
//							// Use the same date but with the same time as last check-in's createdOn
//							LocalDateTime correctedCheckout = LocalDateTime.of(lastCheckIn.getCheckInDate(),
//									lastCheckInCreatedOn.toLocalTime());
//							autoCheckout.setCreatedOn(correctedCheckout);
//						} else {
//							// If lastCheckInCreatedOn is null, use the current time as fallback
//							autoCheckout.setCreatedOn(LocalDateTime.now());
//						}
//						autoCheckout.setStatus("Out");
//						attendanceProcessVO.setAttendanceMode("SYSTEM");
//						attendanceProcessVO.setOrgId(userNameDTO.getOrgId());
//
//						attendanceProcessRepo.save(attendanceProcessVO);
//
//					}
//				}
//			}
//		}
//
//		if (Boolean.TRUE.equals(allowedTodayCheckin)) {
//			// 2. Proceed with today's check-in or check-out
//			todayCheck = new CheckInOutVO();
//			todayCheck.setEmpCode(userNameDTO.getEmpcode());
//			todayCheck.setEmpName(userNameDTO.getEmpName());
//			todayCheck.setBranch(userNameDTO.getBranch());
//			todayCheck.setBranchCode(userNameDTO.getBranchCode());
//			todayCheck.setCheckInDate(today);
//			todayCheck.setEntryTime(now);
//			todayCheck.setOrgId(userNameDTO.getOrgId());
//			todayCheck.setStatus(userNameDTO.isStatus() ? "In" : "Out");
//			todayCheck.setCreatedOn(LocalDateTime.now());
//			todayCheck.setNotify(userNameDTO.getNotify());
//			todayCheck.setNotifyCode(userNameDTO.getNotifyCode());
//			todayCheck.setNotifyEmail(userNameDTO.getNotifyEmail());
//			todayCheck.setEmail(userNameDTO.getEmail());
//			todayCheck.setLatitude(userNameDTO.getLatitude());
//			todayCheck.setLongitude(userNameDTO.getLongitude());
//			todayCheck.setWorkFromHome(userNameDTO.getWorkFromHome());
//			todayCheck.setLocationAddress(userNameDTO.getLocationAddress());
//			todayCheck.setFinyear(userNameDTO.getFinyear());
//
//			todayCheck.setAttendanceMode("SYSTEM");
//
//			checkInOutRepo.save(todayCheck);
//
//			CheckInStatusVO statusUpdate = new CheckInStatusVO();
//			statusUpdate.setEmpcode(userNameDTO.getEmpcode());
//			statusUpdate.setEmpName(userNameDTO.getEmpName());
//			statusUpdate.setStatus(todayCheck.getStatus());
//			statusUpdate.setOrgId(userNameDTO.getOrgId());
//			statusUpdate.setBranch(userNameDTO.getBranch());
//			
//			
//			AttendanceProcessVO attendanceProcessVO = new AttendanceProcessVO();
//			attendanceProcessVO.setEmpName(userNameDTO.getEmpName());
//			attendanceProcessVO.setEmpCode(userNameDTO.getEmpcode());
//			attendanceProcessVO.setBranch(userNameDTO.getBranch());
//			attendanceProcessVO.setBranchCode(userNameDTO.getBranchCode());
//			attendanceProcessVO.setFinyear(userNameDTO.getFinyear());
//			attendanceProcessVO.setCheckInDate(today);
//			attendanceProcessVO.setEntryTime(now);
//			attendanceProcessVO.setStatus(userNameDTO.isStatus() ? "In" : "Out");
//
//			attendanceProcessVO.setAttendanceMode("SYSTEM");
//			attendanceProcessVO.setOrgId(userNameDTO.getOrgId());
//
//			attendanceProcessRepo.save(attendanceProcessVO);
//			
//
//			checkInStatusRepo.save(statusUpdate);
//		}
//		response.put("message",
//				userNameDTO.isStatus() ? "Check-in created successfully" : "Check-out created successfully");
//		response.put("checkInVO", todayCheck);
//
//		return response;
//	}
//
//	public boolean isWithinCompanyLocation(double empLat, double empLng, long orgId) throws ApplicationException {
//	    double companyLat;
//	    double companyLng;
//
//	    List<CompanyVO> companyVOList = companyRepo.findByCompany(orgId);
//
//	    if (!companyVOList.isEmpty()) {
//	        CompanyVO companyVO = companyVOList.get(0); // assuming only one company per ID
//	         companyLat = companyVO.getLatitude();
//	         companyLng = companyVO.getLongitude();
//	        // use companyLat, companyLng
//	    } else {
//	        throw new ApplicationException("Company not found for id: " + orgId);
//	    }
//
//
//	    double allowedRadius = 500;   // Allow 500 meters
//
//	    double distance = LocationUtils.distanceInMeters(empLat, empLng, companyLat, companyLng);
////	    System.out.println("Distance from company location: " + distance + " meters");
//
//	    return distance <= allowedRadius;
//	}
//	
	
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
		        throw new RuntimeException("Attendance process already done in " + monthName + " month.");
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
	    DateTimeFormatter hourMinFormatter = DateTimeFormatter.ofPattern("HH:mm");

	    for (Object[] row : rows) {
	        String empcode = (String) row[0];
	        LocalDate checkindate = ((java.sql.Date) row[2]).toLocalDate();

	        // Check if a record already exists
	        Optional<OtCalculationVO> existingOpt = otCalculationRepo.findByEmpcodeAndCheckindate(empcode, checkindate);

	        OtCalculationVO vo = existingOpt.orElse(new OtCalculationVO());

	        vo.setEmpcode(empcode);
	        vo.setEmpname((String) row[1]);
	        vo.setCheckindate(checkindate);

	        // Convert string to LocalTime
	        String intimeStr = (String) row[3];
	        String outtimeStr = (String) row[4];
	        String othoursStr = (String) row[5];

	        vo.setIntime(LocalTime.parse(intimeStr, timeFormatter));
	        vo.setOuttime(LocalTime.parse(outtimeStr, timeFormatter));
	        vo.setOthours(LocalTime.parse(othoursStr, hourMinFormatter));

	        vo.setOtamount(new BigDecimal(String.valueOf(row[6])));
	        vo.setRate((String) row[7]);
	        vo.setOttype((String) row[8]);
	        vo.setOtcategory((String) row[9]);

	        vo.setCreatedon(LocalDateTime.now());

	        resultList.add(vo);
	    }

	    return otCalculationRepo.saveAll(resultList);
	}



}
