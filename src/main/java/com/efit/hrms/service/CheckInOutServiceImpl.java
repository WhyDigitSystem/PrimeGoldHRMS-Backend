package com.efit.hrms.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.efit.hrms.entity.EmployeeVO;
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
import com.efit.hrms.repo.EmployeeRepo;
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


	@Autowired
	EmployeeRepo employeeRepo;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Map<String, Object> createCheckInOutBiometric(CheckInOutBiometricDTO checkInOutBiometricDTO)
			throws ApplicationException {
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
		List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo.findApplicableShifts(
				checkInOutBiometricDTO.getEmpCode(), attendanceDate, checkInOutBiometricDTO.getOrgId());

		ShiftAssignDetailsVO latestShift = shifts.stream()
				.max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom)).orElse(null);

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
								checkInOutBiometricDTO.getEmpCode(), "In", checkInOutBiometricDTO.getOrgId(),
								checkInOutBiometricDTO.getBranch(), today);
				baseDate = lastIn.map(AttendanceProcessVO::getCheckInDate)
						.orElse(now.isBefore(shiftOutTime) ? today.minusDays(1) : today);
			}
		} else {
			baseDate = today;
		}

		// Step 4: Fetch records within that date + 1
		List<AttendanceProcessVO> recs = attendanceProcessRepo.findByEmpCodeAndDateRange(
				checkInOutBiometricDTO.getEmpCode(), baseDate, baseDate.plusDays(1), checkInOutBiometricDTO.getOrgId(),
				checkInOutBiometricDTO.getBranch());

		List<LocalDateTime> inList = new ArrayList<>();
		List<LocalDateTime> outList = new ArrayList<>();

		for (AttendanceProcessVO rec : recs) {
			LocalDateTime dt = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
			if ("In".equalsIgnoreCase(rec.getStatus()))
				inList.add(dt);
			else if ("Out".equalsIgnoreCase(rec.getStatus()))
				outList.add(dt);
		}

		Collections.sort(inList);
		Collections.sort(outList);

		long effectiveSeconds = 0;
		int outIdx = 0;
		for (LocalDateTime inTime : inList) {
			while (outIdx < outList.size() && outList.get(outIdx).isBefore(inTime))
				outIdx++;
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
		long grossSeconds = (firstIn != null && lastOut != null && lastOut.isAfter(firstIn))
				? Duration.between(firstIn, lastOut).getSeconds()
				: 0;

		// Step 5: Save/update AttendanceDaily
		AttendanceDailyVO ad = attendanceDailyRepo.findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
				checkInOutBiometricDTO.getEmpCode(), baseDate, checkInOutBiometricDTO.getOrgId(),
				checkInOutBiometricDTO.getBranch());

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

		if (firstIn != null)
			ad.setInTime(firstIn.toLocalTime());
		if (lastOut != null) {
			ad.setOutTime(lastOut.toLocalTime());
			ad.setCheckOutDate(lastOut.toLocalDate());
		}

		ad.setEffectiveHours((int) (effectiveSeconds / 3600));
		ad.setGrossHours((int) (grossSeconds / 3600));

		attendanceDailyRepo.save(ad);
		// Build response
		response.put("message",
				checkInOutBiometricDTO.isStatus() ? "Check-in created successfully" : "Check-out created successfully");
		response.put("checkInBiometricVO", todayCheck);

		return response;
	}

	// checkinoutupload
//old
//	@Transactional(rollbackOn = Exception.class)
//	@Override
//	public String checkInOutUploadExcel(MultipartFile file, Long orgId, String createdBy) throws Exception {
//		List<CheckInOutUploadVO> validList = new ArrayList<>();
//		List<Map<String, Object>> failures = new ArrayList<>();
//		int successCount = 0;
//
//		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
//			Sheet sheet = workbook.getSheetAt(0);
//
//			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//				Row row = sheet.getRow(i);
//				if (row == null)
//					continue;
//
//				try {
//					CheckInOutUploadVO vo = new CheckInOutUploadVO();
//
////					String empName = getStringCell(row, 0);
//					String empCode = getStringCell(row, 0);
//					
//					EmployeeVO employeeVO = employeeRepo.findByEmployeeVO(empCode);
//					String empName = employeeVO.getEmployeeName();
//					
//					
////					String branchCode = getStringCell(row, 2);
////					String branch = getStringCell(row, 3);
////					String finYear = getStringCell(row, 4);
//					String status = getStringCell(row, 7);
//
//					if (empCode == null || empCode.trim().isEmpty()) {
//						throw new IllegalArgumentException("Employee Code is missing");
//					}
//
//					LocalDate checkInDate;
//					Cell dateCell = row.getCell(5);
//					if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
//						checkInDate = dateCell.getLocalDateTimeCellValue().toLocalDate();
//					} else {
//						throw new IllegalArgumentException("Invalid format for checkInDate at row " + (i + 1));
//					}
//
//					LocalTime entryTime;
//					try {
//						entryTime = row.getCell(6).getLocalDateTimeCellValue().toLocalTime();
//					} catch (Exception e) {
//						throw new IllegalArgumentException("Invalid or missing Entry Time at row " + (i + 1));
//					}
//
//					if (!"In".equalsIgnoreCase(status) && !"Out".equalsIgnoreCase(status)) {
//						throw new IllegalArgumentException("Status must be 'In' or 'Out'");
//					}
//
//					vo.setEmpname(empName);
//					vo.setEmpcode(empCode);
//					vo.setOrgId(orgId);
////					vo.setBranchCode(branchCode);
////					vo.setBranch(branch);
////					vo.setFinYear(finYear);
//					vo.setCheckInDate(checkInDate);
//					vo.setEntryTime(entryTime);
//					vo.setStatus(status);
//					vo.setCreatedBy(createdBy);
//
//					validList.add(vo);
//					successCount++;
//
//				} catch (Exception e) {
//					Map<String, Object> error = new HashMap<>();
//					error.put("row", i + 1);
//					error.put("error", e.getMessage());
//					failures.add(error);
//				}
//			}
//
//			Map<String, Object> result = new LinkedHashMap<>();
//			result.put("successCount", successCount);
//			result.put("failedCount", failures.size());
//			result.put("failures", failures);
//
//			if (failures.isEmpty()) {
//				// ✅ Save CheckInOutUploadVOs
//				checkInOutUploadRepo.saveAll(validList);
//
//				// ✅ Save AttendanceProcessVOs
//				for (CheckInOutUploadVO dto : validList) {
//					AttendanceProcessVO vo = new AttendanceProcessVO();
//					vo.setEmpCode(dto.getEmpcode());
//					vo.setEmpName(dto.getEmpname());
//					vo.setOrgId(dto.getOrgId());
//					vo.setBranchCode(dto.getBranchCode());
//					vo.setBranch(dto.getBranch());
//					vo.setFinyear(dto.getFinYear());
//					vo.setCheckInDate(dto.getCheckInDate());
//					vo.setEntryTime(dto.getEntryTime());
//					vo.setStatus(dto.getStatus());
//					vo.setSourceId(dto.getId());
//					vo.setAttendanceMode("FILES");
//
//					attendanceProcessRepo.save(vo);
//
//					LocalDate attendanceDate = dto.getCheckInDate();
//
//					// Step 1: Fetch Shift based on attendance date
//					List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo.findApplicableShifts(dto.getEmpcode(),
//							attendanceDate, dto.getOrgId());
//
//					ShiftAssignDetailsVO latestShift = shifts.stream()
//							.max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom)).orElse(null);
//
//					String shiftType = (latestShift != null) ? latestShift.getShiftType() : "General";
//
//					// Step 2: Determine shiftOutTime
//					LocalTime shiftOutTime;
//					if (latestShift != null && latestShift.getOutTime() != null) {
//						shiftOutTime = LocalTime.parse(latestShift.getOutTime());
//					} else {
//    throw new IllegalStateException("Missing OUT time for active shift. " + dto.getEmpcode());
//					}
//
//					// Step 3: Calculate baseDate for pairing
//					LocalDate baseDate;
//					if ("NIGHT".equalsIgnoreCase(shiftType)) {
//						if ("In".equalsIgnoreCase(dto.getStatus())) {
//							baseDate = dto.getEntryTime().isBefore(shiftOutTime) ? dto.getCheckInDate().minusDays(1)
//									: dto.getCheckInDate();
//						} else {
//							Optional<AttendanceProcessVO> lastIn = attendanceProcessRepo
//									.findTopByEmpCodeAndStatusAndOrgIdAndBranchAndCheckInDateLessThanEqualOrderByCheckInDateDescEntryTimeDesc(
//											dto.getEmpcode(), "In", dto.getOrgId(), dto.getBranch(),
//											dto.getCheckInDate());
//							baseDate = lastIn.map(AttendanceProcessVO::getCheckInDate)
//									.orElse(dto.getEntryTime().isBefore(shiftOutTime)
//											? dto.getCheckInDate().minusDays(1)
//											: dto.getCheckInDate());
//						}
//					} else {
//						baseDate = dto.getCheckInDate();
//					}
//
//					// Step 4: Fetch records within that date + 1
//					List<AttendanceProcessVO> recs = attendanceProcessRepo.findByEmpCodeAndDateRange(dto.getEmpcode(),
//							baseDate, baseDate.plusDays(1), dto.getOrgId(), dto.getBranch());
//
//					List<LocalDateTime> inList = new ArrayList<>();
//					List<LocalDateTime> outList = new ArrayList<>();
//
//					for (AttendanceProcessVO rec : recs) {
//						LocalDateTime dt = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
//						if ("In".equalsIgnoreCase(rec.getStatus()))
//							inList.add(dt);
//						else if ("Out".equalsIgnoreCase(rec.getStatus()))
//							outList.add(dt);
//					}
//
//					Collections.sort(inList);
//					Collections.sort(outList);
//
//					long effectiveSeconds = 0;
//					int outIdx = 0;
//					for (LocalDateTime inTime : inList) {
//						while (outIdx < outList.size() && outList.get(outIdx).isBefore(inTime))
//							outIdx++;
//						if (outIdx < outList.size()) {
//							LocalDateTime outTime = outList.get(outIdx);
//							if (!outTime.isBefore(inTime)) {
//								effectiveSeconds += Duration.between(inTime, outTime).getSeconds();
//								outIdx++;
//							}
//						}
//					}
//
//					LocalDateTime firstIn = inList.stream().min(LocalDateTime::compareTo).orElse(null);
//					LocalDateTime lastOut = outList.stream().max(LocalDateTime::compareTo).orElse(null);
//					long grossSeconds = (firstIn != null && lastOut != null && lastOut.isAfter(firstIn))
//							? Duration.between(firstIn, lastOut).getSeconds()
//							: 0;
//
//					// Step 5: Save/update AttendanceDaily
//					AttendanceDailyVO ad = attendanceDailyRepo.findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
//							dto.getEmpcode(), baseDate, dto.getOrgId(), dto.getBranch());
//
//					if (ad == null) {
//						ad = new AttendanceDailyVO();
//						ad.setEmpCode(dto.getEmpcode());
//						ad.setEmpName(dto.getEmpname());
//						ad.setBranch(dto.getBranch());
//						ad.setBranchCode(dto.getBranchCode());
//						ad.setOrgId(dto.getOrgId());
//						ad.setCheckInDate(baseDate);
//						ad.setFinyear(String.valueOf(baseDate.getYear()));
//						ad.setAttendanceMode("FILES");
//					}
//
//					if (firstIn != null)
//						ad.setInTime(firstIn.toLocalTime());
//					if (lastOut != null) {
//						ad.setOutTime(lastOut.toLocalTime());
//						ad.setCheckOutDate(lastOut.toLocalDate());
//					}
//
//					ad.setEffectiveHours((int) (effectiveSeconds / 3600));
//					ad.setGrossHours((int) (grossSeconds / 3600));
//
//					attendanceDailyRepo.save(ad);
//				}
//
//				result.put("message", "All records uploaded and saved successfully.");
//			} else {
//				result.put("message", "Upload failed. No records saved. Found " + failures.size() + " errors.");
//			}
//
//			return new ObjectMapper().writeValueAsString(result);
//		}
//	}
//
//	private String getStringCell(Row row, int col) {
//		try {
//			Cell cell = row.getCell(col);
//			if (cell == null)
//				return "";
//			cell.setCellType(CellType.STRING);
//			return cell.getStringCellValue().trim();
//		} catch (Exception e) {
//			return "";
//		}
//	}



	// almost

//	@Transactional(rollbackOn = Exception.class)
//	@Override
//	public String checkInOutUploadExcel(MultipartFile file, Long orgId, String createdBy) throws Exception {
//		List<CheckInOutUploadVO> validList = new ArrayList<>();
//		List<Map<String, Object>> failures = new ArrayList<>();
//		int successCount = 0;
//
//		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
//			Sheet sheet = workbook.getSheetAt(0);
//
//			// Start at row index 2 (skip first 2 header rows)
//			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
//				Row row = sheet.getRow(i);
//				if (row == null) {
//					continue;
//				}
//
//				try {
//					String empCode = getStringCell(row, 0);
//					if (empCode == null || empCode.trim().isEmpty()) {
//						throw new IllegalArgumentException("Employee Code is missing");
//					}
//
//					EmployeeVO employeeVO = employeeRepo.findByEmployeeCodeAndOrgId(empCode, orgId);
//					String empName = (employeeVO != null) ? employeeVO.getEmployeeName() : "";
//					String branch = employeeVO.getBranch();
//					String branchCode = employeeVO.getBranchCode();
//
//					LocalDate checkInDate = getDateCell(row, 2);
//
//					LocalTime inTime = getTimeCell(row, 3);
//					LocalTime outTime = getTimeCell(row, 4);
//
//					// "In" record
//					if (inTime != null) {
//						CheckInOutUploadVO vo = new CheckInOutUploadVO();
//						vo.setEmpname(empName);
//						vo.setEmpcode(empCode);
//						vo.setOrgId(orgId);
//						vo.setCheckInDate(checkInDate);
//						vo.setBranch(branch);
//						vo.setBranchCode(branchCode);
//
//						vo.setEntryTime(inTime);
//						vo.setStatus("In");
//						vo.setCreatedBy(createdBy);
//						validList.add(vo);
//						successCount++;
//					}
//
//					// For the "Out" record:
//					if (outTime != null) {
//						CheckInOutUploadVO vo = new CheckInOutUploadVO();
//						vo.setEmpname(empName);
//						vo.setEmpcode(empCode);
//						vo.setOrgId(orgId);
//
//						// Adjust checkInDate if outTime is before inTime (night shift logic)
//						LocalDate adjustedCheckInDate = checkInDate;
//						if (inTime != null && outTime.isBefore(inTime)) {
//							// Night shift: Out time belongs to next day
//							adjustedCheckInDate = checkInDate.plusDays(1);
//						}
//
//						vo.setCheckInDate(adjustedCheckInDate);
//						vo.setEntryTime(outTime);
//						vo.setStatus("Out");
//						vo.setCreatedBy(createdBy);
//						vo.setBranch(branch);
//						vo.setBranchCode(branchCode);
//						validList.add(vo);
//						successCount++;
//					}
//
//				} catch (Exception e) {
//					Map<String, Object> error = new HashMap<>();
//					error.put("row", i + 1); // Excel row number
//
//					// Add employee name to error output
//					String empCodeForError = getStringCell(row, 0);
//					EmployeeVO empVOForError = null;
//					if (empCodeForError != null && !empCodeForError.trim().isEmpty()) {
//						empVOForError = employeeRepo.findByEmployeeCodeAndOrgId(empCodeForError, orgId);
//					}
//					String empNameForError = (empVOForError != null) ? empVOForError.getEmployeeName() : "";
//					error.put("empName", empNameForError);
//
//					String errMsg = e.getMessage();
//					if (errMsg == null || errMsg.trim().isEmpty()) {
//						errMsg = e.toString();
//					}
//					error.put("error", errMsg);
//					failures.add(error);
//
//				}
//			}
//
//			Map<String, Object> result = new LinkedHashMap<>();
//			result.put("successCount", successCount);
//			result.put("failedCount", failures.size());
//			result.put("failures", failures);
//			result.put("debugValidListSize", validList.size());
//
//			if (failures.isEmpty()) {
//				checkInOutUploadRepo.saveAll(validList);
//
//				List<AttendanceProcessVO> attendanceList = new ArrayList<>();
//
//				for (CheckInOutUploadVO dto : validList) {
//
//					LocalDate attendanceDate = dto.getCheckInDate();
//
//					// Fetch Shift
//					List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo.findApplicableShifts(dto.getEmpcode(),
//							attendanceDate, dto.getOrgId());
//
//					ShiftAssignDetailsVO latestShift = shifts.stream()
//							.max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom)).orElse(null);
//
//					String shiftType = (latestShift != null) ? latestShift.getShiftType() : "General";
//
//					LocalTime shiftOutTime;
//					if (latestShift != null && latestShift.getOutTime() != null) {
//						shiftOutTime = LocalTime.parse(latestShift.getOutTime());
//					} else {
//						String msg = "Missing OUT time for active shift. EmpCode=" + dto.getEmpcode();
//						System.err.println(msg);
//						throw new IllegalStateException(msg);
//					}
//
//					LocalDate baseDate;
//					if ("NIGHT".equalsIgnoreCase(shiftType)) {
//						if ("In".equalsIgnoreCase(dto.getStatus())) {
//							baseDate = dto.getEntryTime().isBefore(shiftOutTime) ? dto.getCheckInDate().minusDays(1)
//									: dto.getCheckInDate();
//						} else {
//							Optional<AttendanceProcessVO> lastIn = attendanceList.stream()
//									.filter(a -> "In".equalsIgnoreCase(a.getStatus())
//											&& dto.getEmpcode().equals(a.getEmpCode())
//											&& Objects.equals(dto.getOrgId(), a.getOrgId())
//											&& Objects.equals(dto.getBranch(), a.getBranch()))
//									.max(Comparator.comparing(AttendanceProcessVO::getCheckInDate)
//											.thenComparing(AttendanceProcessVO::getEntryTime));
//
//							if (lastIn.isPresent()) {
//								baseDate = lastIn.get().getCheckInDate().plusDays(1);
//							} else {
//								baseDate = dto.getCheckInDate().plusDays(1);
//							}
//						}
//					} else {
//						baseDate = dto.getCheckInDate();
//					}
//
//					AttendanceProcessVO vo = new AttendanceProcessVO();
//					vo.setEmpCode(dto.getEmpcode());
//					vo.setEmpName(dto.getEmpname());
//					vo.setOrgId(dto.getOrgId());
//					vo.setBranchCode(dto.getBranchCode());
//					vo.setBranch(dto.getBranch());
//					vo.setFinyear(String.valueOf(baseDate.getYear()));
//					vo.setCheckInDate(baseDate);
//					vo.setEntryTime(dto.getEntryTime());
//					vo.setStatus(dto.getStatus());
//					vo.setSourceId(dto.getId());
//					vo.setAttendanceMode("FILES");
//
//					attendanceProcessRepo.save(vo);
//					attendanceList.add(vo);
//
//					// Calculate daily attendance
//					List<AttendanceProcessVO> recs = attendanceList.stream()
//							.filter(a -> Objects.equals(dto.getEmpcode(), a.getEmpCode())
//									&& Objects.equals(dto.getOrgId(), a.getOrgId())
//									&& Objects.equals(dto.getBranch(), a.getBranch()) && a.getCheckInDate() != null
//									&& !a.getCheckInDate().isBefore(baseDate)
//									&& !a.getCheckInDate().isAfter(baseDate.plusDays(1)))
//							.collect(Collectors.toList());
//
//					List<LocalDateTime> inList = new ArrayList<>();
//					List<LocalDateTime> outList = new ArrayList<>();
//
//					for (AttendanceProcessVO rec : recs) {
//						LocalDateTime dt = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
//						if ("In".equalsIgnoreCase(rec.getStatus()))
//							inList.add(dt);
//						else if ("Out".equalsIgnoreCase(rec.getStatus()))
//							outList.add(dt);
//					}
//
//					 inList.sort(LocalDateTime::compareTo);
//		                outList.sort(LocalDateTime::compareTo);
//
//		                boolean isNightShift = "NIGHT".equalsIgnoreCase(shiftType);
//
//		                long effectiveSeconds = 0;
//		                int outIndex = 0;
//
//		                // Pair IN with matching OUT properly
//		                for (int i = 0; i < inList.size(); i++) {
//		                    LocalDateTime inTime = inList.get(i);
//
//		                    // Find first OUT after IN time
//		                    LocalDateTime matchedOut = null;
//		                    while (outIndex < outList.size()) {
//		                        LocalDateTime candidateOut = outList.get(outIndex);
//		                        if (!candidateOut.isBefore(inTime)) { // candidateOut >= inTime
//		                            matchedOut = candidateOut;
//		                            outIndex++; // move to next OUT for next iteration
//		                            break;
//		                        }
//		                        outIndex++; // skip OUT before IN time
//		                    }
//
//		                    if (matchedOut == null) {
//		                        System.out.println("No matching OUT found for IN at index " + i + ": " + inTime);
//		                        break;
//		                    }
//
//		                    if (isNightShift) {
//		                        if (!matchedOut.isAfter(inTime)) {
//		                            matchedOut = matchedOut.plusDays(1);
//		                        }
//
//		                        LocalDateTime midnight = inTime.toLocalDate().plusDays(1).atStartOfDay();
//
//		                        if (matchedOut.isBefore(midnight)) {
//		                            effectiveSeconds += Duration.between(inTime, matchedOut).getSeconds();
//		                        } else if (inTime.isAfter(midnight)) {
//		                            effectiveSeconds += Duration.between(inTime, matchedOut).getSeconds();
//		                        } else {
//		                            long beforeMidnight = Duration.between(inTime, midnight).getSeconds();
//		                            long afterMidnight = Duration.between(midnight, matchedOut).getSeconds();
//		                            effectiveSeconds += beforeMidnight + afterMidnight;
//		                        }
//		                    } else {
//		                        if (matchedOut.isAfter(inTime)) {
//		                            effectiveSeconds += Duration.between(inTime, matchedOut).getSeconds();
//		                        }
//		                    }
//		                }
//
//		                LocalDateTime firstIn = inList.isEmpty() ? null : inList.get(0);
//		                LocalDateTime lastOut = outList.isEmpty() ? null : outList.get(outList.size() - 1);
//
//		                if (isNightShift && lastOut != null && firstIn != null && !lastOut.isAfter(firstIn)) {
//		                    lastOut = lastOut.plusDays(1);
//		                }
//
//		                long grossSeconds = 0;
//		                if (firstIn != null && lastOut != null) {
//		                    grossSeconds = Duration.between(firstIn, lastOut).getSeconds();
//		                }
//
//		                int effectiveHours = (int) (effectiveSeconds / 3600);
//		                int grossHours = (int) (grossSeconds / 3600);
//
//		                // Determine attendance record date grouping
//		                LocalDate attendanceRecordDate;
//		                if (isNightShift) {
//		                    if ("In".equalsIgnoreCase(dto.getStatus())) {
//		                        attendanceRecordDate = dto.getCheckInDate();
//		                    } else {
//		                        attendanceRecordDate = dto.getCheckInDate().minusDays(1);
//		                    }
//		                } else {
//		                    attendanceRecordDate = dto.getCheckInDate();
//		                }
//
//		                // Fetch or create attendance daily record
//		                AttendanceDailyVO ad = attendanceDailyRepo.findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
//		                        dto.getEmpcode(), attendanceRecordDate, dto.getOrgId(), dto.getBranch());
//
//		                if (ad == null) {
//		                    ad = new AttendanceDailyVO();
//		                    ad.setEmpCode(dto.getEmpcode());
//		                    ad.setEmpName(dto.getEmpname());
//		                    ad.setBranch(dto.getBranch());
//		                    ad.setBranchCode(dto.getBranchCode());
//		                    ad.setOrgId(dto.getOrgId());
//		                    ad.setCheckInDate(attendanceRecordDate);
//		                    ad.setFinyear(String.valueOf(attendanceRecordDate.getYear()));
//		                    ad.setAttendanceMode("FILES");
//		                    System.out.println("Created new AttendanceDailyVO");
//		                } else {
//		                    System.out.println("Found existing AttendanceDailyVO with id: " + ad.getId());
//		                }
//
//		                // Update IN or OUT time
//		                if ("In".equalsIgnoreCase(dto.getStatus())) {
//		                    ad.setInTime(dto.getEntryTime());
//		                } else {
//		                    ad.setOutTime(dto.getEntryTime());
//		                    ad.setCheckOutDate(dto.getCheckInDate());
//		                }
//
//		                // Set effective and gross hours
//		                ad.setEffectiveHours(effectiveHours);
//		                ad.setGrossHours(grossHours);
//
//		                attendanceDailyRepo.save(ad);
//		                System.out.println("AttendanceDailyVO saved: " + ad.getId());
//
//				}
//
//				result.put("message", "All records uploaded and saved successfully.");
//			} else {
//				result.put("message", "Upload failed. No records saved. Found " + failures.size() + " errors.");
//			}
//
//			return new ObjectMapper().writeValueAsString(result);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e; // rethrow after printing stacktrace
//		}
//	}
//
//	private String getStringCell(Row row, int col) {
//		try {
//			Cell cell = row.getCell(col);
//			if (cell == null)
//				return "";
//			cell.setCellType(CellType.STRING);
//			return cell.getStringCellValue().trim();
//		} catch (Exception e) {
//			return "";
//		}
//	}
//
//	private LocalDate getDateCell(Row row, int colIndex) {
//		Cell cell = row.getCell(colIndex);
//		if (cell == null) {
//			throw new IllegalArgumentException("Check-in date is required");
//		}
//
//		// 1) If it's a true Excel date cell
//		if (cell.getCellType() == CellType.NUMERIC) {
//			if (DateUtil.isCellDateFormatted(cell)) {
//				return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//			} else {
//				// Some exports put a serial number without date formatting
//				double numeric = cell.getNumericCellValue();
//				long days = (long) numeric;
//				return LocalDate.of(1899, 12, 30).plusDays(days);
//			}
//		}
//
//		// 2) If it's text, sanitize and parse
//		String raw = cell.toString().trim().replace("\u00A0", "").replace("\u2011", "-").replace("\u2013", "-")
//				.replace("\u2014", "-").replaceAll("\\s+", " ");
//
//		if (raw.isEmpty()) {
//			throw new IllegalArgumentException("Check-in date is empty");
//		}
//
//		// Try many formats
//		String[] patterns = new String[] { "yyyy-MM-dd", "dd-MM-yyyy", "dd/MM/yyyy", "MM/dd/yyyy", "dd-MMM-yyyy",
//				"dd MMM yyyy", "yyyy/MM/dd", "yyyyMMdd", "dd-MM-yyyy HH:mm", "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm",
//				"yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy HH:mm", "dd-MMM-yyyy hh:mm a" };
//
//		for (String p : patterns) {
//			try {
//				DateTimeFormatter fmt = DateTimeFormatter.ofPattern(p);
//				return LocalDate.parse(raw, fmt);
//			} catch (DateTimeParseException ignored) {
//			}
//			try {
//				DateTimeFormatter fmt = DateTimeFormatter.ofPattern(p);
//				return LocalDateTime.parse(raw, fmt).toLocalDate();
//			} catch (DateTimeParseException ignored) {
//			}
//		}
//
//		// Last ditch: attempt parsing only digits (yyyyMMdd etc)
//		try {
//			if (raw.matches("\\d{8}")) {
//				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
//				return LocalDate.parse(raw, fmt);
//			}
//		} catch (Exception ignored) {
//		}
//
//		throw new IllegalArgumentException("Invalid date format: " + raw);
//	}
//
//	private LocalTime getTimeCell(Row row, int colIndex) {
//		try {
//			Cell cell = row.getCell(colIndex);
//			if (cell == null)
//				return null;
//
//			if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
//				return cell.getLocalDateTimeCellValue().toLocalTime();
//			}
//
//			String raw = cell.toString().trim();
//			if (raw.isEmpty())
//				return null;
//
//			String[] patterns = new String[] { "HH:mm", "HH:mm:ss", "hh:mma", "hh:mm:ssa", "H:mm" // allow single-digit
//																									// hour
//			};
//
//			for (String p : patterns) {
//				try {
//					return LocalTime.parse(raw, DateTimeFormatter.ofPattern(p));
//				} catch (DateTimeParseException ignored) {
//				}
//			}
//		} catch (Exception ignored) {
//		}
//
//		return null;
//	}


	//test
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public String checkInOutUploadExcel(MultipartFile file, Long orgId, String createdBy) throws Exception {
	    List<CheckInOutUploadVO> validList = new ArrayList<>();
	    List<Map<String, Object>> failures = new ArrayList<>();
	    int successCount = 0;

	    try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
	        Sheet sheet = workbook.getSheetAt(0);

	        // Skip first two header rows
	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            try {
	                String empCode = getStringCell(row, 0);
	                if (empCode == null || empCode.trim().isEmpty()) {
	                    throw new IllegalArgumentException("Employee Code is missing");
	                }

	                EmployeeVO employeeVO = employeeRepo.findByEmployeeCodeAndOrgId(empCode, orgId);
	                if (employeeVO == null) {
	                    throw new IllegalArgumentException("Employee not found: " + empCode);
	                }

	                String empName = employeeVO.getEmployeeName();
	                String branch = employeeVO.getBranch();
	                String branchCode = employeeVO.getBranchCode();

	                LocalDate checkInDate = getDateCell(row, 2);
	                LocalTime inTime = getTimeCell(row, 4);
	                LocalTime outTime = getTimeCell(row, 5);

	                // Fetch shift to decide date adjustment for OUT
	                List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo.findApplicableShifts(empCode, checkInDate, orgId);
	                ShiftAssignDetailsVO latestShift = shifts.stream()
	                        .max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom))
	                        .orElse(null);
	                boolean isNightShift = latestShift != null && "NIGHT".equalsIgnoreCase(latestShift.getShiftType());

	                // Create IN record
	                if (inTime != null) {
	                    CheckInOutUploadVO vo = new CheckInOutUploadVO();
	                    vo.setEmpcode(empCode);
	                    vo.setEmpname(empName);
	                    vo.setOrgId(orgId);
	                    vo.setCheckInDate(checkInDate);
	                    vo.setBranch(branch);
	                    vo.setBranchCode(branchCode);
	                    vo.setEntryTime(inTime);
	                    vo.setStatus("In");
	                    vo.setCreatedBy(createdBy);
	    	            vo.setFinYear(String.valueOf(checkInDate.getYear()));
	                    validList.add(vo);
	                    successCount++;
	                }

	                // Create OUT record with correct date for night shift
	                if (outTime != null) {
	                    LocalDate outDate = checkInDate;
	                    if (isNightShift && inTime != null && outTime.isBefore(inTime)) {
	                        outDate = checkInDate.plusDays(1);
	                    }
	                    CheckInOutUploadVO vo = new CheckInOutUploadVO();
	                    vo.setEmpcode(empCode);
	                    vo.setEmpname(empName);
	                    vo.setOrgId(orgId);
	                    vo.setCheckInDate(outDate);
	                    vo.setBranch(branch);
	                    vo.setBranchCode(branchCode);
	                    vo.setEntryTime(outTime);
	                    vo.setStatus("Out");
	                    vo.setCreatedBy(createdBy);
	    	            vo.setFinYear(String.valueOf(outDate.getYear()));
	                    validList.add(vo);
	                    successCount++;
	                }
	            } catch (Exception e) {
	                Map<String, Object> error = new HashMap<>();
	                error.put("row", i + 1);
	                String empCode = getStringCell(row, 0);
	                EmployeeVO emp = null;
	                if (empCode != null && !empCode.trim().isEmpty()) {
	                    emp = employeeRepo.findByEmployeeCodeAndOrgId(empCode, orgId);
	                }
	                String empName = (emp != null) ? emp.getEmployeeName() : "";
	                error.put("empName", empName);
	                error.put("error", e.getMessage());
	                failures.add(error);
	            }
	        }

	        Map<String, Object> result = new LinkedHashMap<>();
	        result.put("successCount", successCount);
	        result.put("failedCount", failures.size());
	        result.put("failures", failures);

	        if (!failures.isEmpty()) {
	            result.put("message", "Upload failed due to errors. No records saved.");
	            return new ObjectMapper().writeValueAsString(result);
	        }

	        // Save uploaded records
	        checkInOutUploadRepo.saveAll(validList);

	        // Build AttendanceProcess records
	        List<AttendanceProcessVO> attendanceList = new ArrayList<>();
	        for (CheckInOutUploadVO dto : validList) {
	            List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo.findApplicableShifts(dto.getEmpcode(), dto.getCheckInDate(), dto.getOrgId());
	            ShiftAssignDetailsVO latestShift = shifts.stream()
	                    .max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom))
	                    .orElse(null);
	            boolean isNightShift = latestShift != null && "NIGHT".equalsIgnoreCase(latestShift.getShiftType());

	            LocalDate baseDate;
	            if (isNightShift) {
	                if ("In".equalsIgnoreCase(dto.getStatus()) && dto.getEntryTime().isBefore(LocalTime.NOON)) {
	                    baseDate = dto.getCheckInDate().minusDays(1);
	                } else if ("Out".equalsIgnoreCase(dto.getStatus()) && dto.getEntryTime().isBefore(LocalTime.NOON)) {
	                    baseDate = dto.getCheckInDate().minusDays(1);
	                } else {
	                    baseDate = dto.getCheckInDate();
	                }
	            } else {
	                baseDate = dto.getCheckInDate();
	            }

	            AttendanceProcessVO vo = new AttendanceProcessVO();
	            vo.setEmpCode(dto.getEmpcode());
	            vo.setEmpName(dto.getEmpname());
	            vo.setOrgId(dto.getOrgId());
	            vo.setBranchCode(dto.getBranchCode());
	            vo.setBranch(dto.getBranch());
	            vo.setFinyear(String.valueOf(baseDate.getYear()));
	            vo.setCheckInDate(baseDate);
	            vo.setEntryTime(dto.getEntryTime());
	            vo.setStatus(dto.getStatus());
	            vo.setSourceId(dto.getId());
	            vo.setAttendanceMode("FILES");

	            attendanceProcessRepo.save(vo);
	            attendanceList.add(vo);
	        }
	        

	        Map<String, List<AttendanceProcessVO>> grouped = attendanceList.stream()
	        	    .collect(Collectors.groupingBy(a ->
	        	        a.getEmpCode() + "_" + a.getOrgId() + "_" + a.getBranchCode() + "_" + a.getCheckInDate()
	        	    ));


	        for (List<AttendanceProcessVO> group : grouped.values()) {
	            // Sort IN/OUT by datetime
	        	List<AttendanceProcessVO> ins = group.stream()
	        		    .filter(a -> "In".equalsIgnoreCase(a.getStatus()))
	        		    .collect(Collectors.toList());
	        		List<AttendanceProcessVO> outs = group.stream()
	        		    .filter(a -> "Out".equalsIgnoreCase(a.getStatus()))
	        		    .collect(Collectors.toList());



	            if (ins.isEmpty() || outs.isEmpty()) continue;

	            AttendanceProcessVO firstIn = ins.get(0);
	            AttendanceProcessVO lastOut = outs.get(outs.size() - 1);

	            // Detect night shift
	            List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo.findApplicableShifts(
	                    firstIn.getEmpCode(), firstIn.getCheckInDate(), firstIn.getOrgId());
	            ShiftAssignDetailsVO latestShift = shifts.stream()
	                    .max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom))
	                    .orElse(null);
	            boolean isNightShift = latestShift != null && "NIGHT".equalsIgnoreCase(latestShift.getShiftType());

	            LocalDateTime inDT = LocalDateTime.of(firstIn.getCheckInDate(), firstIn.getEntryTime());
	            LocalDateTime outDT = LocalDateTime.of(lastOut.getCheckInDate(), lastOut.getEntryTime());
	            if (isNightShift && !outDT.isAfter(inDT)) {
	                outDT = outDT.plusDays(1);
	            }

	            long grossSeconds = Duration.between(inDT, outDT).getSeconds();
	            long effectiveSeconds = grossSeconds; // You can refine with breaks if needed

	            // Save AttendanceDaily
	            AttendanceDailyVO ad = attendanceDailyRepo.findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
	                    firstIn.getEmpCode(),
	                    firstIn.getCheckInDate(), // Base date for daily entry
	                    firstIn.getOrgId(),
	                    firstIn.getBranch()
	            );
	            if (ad == null) {
	                ad = new AttendanceDailyVO();
	                ad.setEmpCode(firstIn.getEmpCode());
	                ad.setEmpName(firstIn.getEmpName());
	                ad.setOrgId(firstIn.getOrgId());
	                ad.setBranch(firstIn.getBranch());
	                ad.setBranchCode(firstIn.getBranchCode());
	                ad.setCheckInDate(firstIn.getCheckInDate());
	                ad.setFinyear(String.valueOf(firstIn.getCheckInDate().getYear()));
	                ad.setAttendanceMode("FILES");
	            }
	            ad.setInTime(inDT.toLocalTime());
	            ad.setOutTime(outDT.toLocalTime());
	            ad.setCheckOutDate(outDT.toLocalDate());
	            
	            int effectiveHours= (int) (effectiveSeconds / 3600);
	            int grossHours = (int) (grossSeconds / 3600);
	            
	            if(!isNightShift) {
	            	if (effectiveHours < 0 && grossHours < 0) {
	            		 ad.setEffectiveHours(0);
	            		 ad.setGrossHours(0);
	            }else{
	            	
	            	ad.setEffectiveHours((int) (effectiveSeconds / 3600));
		            ad.setGrossHours((int) (grossSeconds / 3600));
	            	}
	            }
	            	
	            if(isNightShift) {
	            	ad.setEffectiveHours((int) (effectiveSeconds / 3600));
		            ad.setGrossHours((int) (grossSeconds / 3600));
	            }
	            
	            attendanceDailyRepo.save(ad);
	        	}


	        result.put("message", "Upload and calculation successful.");
	        return new ObjectMapper().writeValueAsString(result);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}



	
	private String getStringCell(Row row, int col) {
		try {
			Cell cell = row.getCell(col);
			if (cell == null)
				return "";
			cell.setCellType(CellType.STRING);
			return cell.getStringCellValue().trim();
		} catch (Exception e) {
			return "";
		}
	}

	private LocalDate getDateCell(Row row, int colIndex) {
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			throw new IllegalArgumentException("Check-in date is required");
		}

		// 1) If it's a true Excel date cell
		if (cell.getCellType() == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			} else {
				// Some exports put a serial number without date formatting
				double numeric = cell.getNumericCellValue();
				long days = (long) numeric;
				return LocalDate.of(1899, 12, 30).plusDays(days);
			}
		}

		// 2) If it's text, sanitize and parse
		String raw = cell.toString().trim().replace("\u00A0", "").replace("\u2011", "-").replace("\u2013", "-")
				.replace("\u2014", "-").replaceAll("\\s+", " ");

		if (raw.isEmpty()) {
			throw new IllegalArgumentException("Check-in date is empty");
		}

		// Try many formats
		String[] patterns = new String[] { "yyyy-MM-dd", "dd-MM-yyyy", "dd/MM/yyyy", "MM/dd/yyyy", "dd-MMM-yyyy",
				"dd MMM yyyy", "yyyy/MM/dd", "yyyyMMdd", "dd-MM-yyyy HH:mm", "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm",
				"yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy HH:mm", "dd-MMM-yyyy hh:mm a" };

		for (String p : patterns) {
			try {
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern(p);
				return LocalDate.parse(raw, fmt);
			} catch (DateTimeParseException ignored) {
			}
			try {
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern(p);
				return LocalDateTime.parse(raw, fmt).toLocalDate();
			} catch (DateTimeParseException ignored) {
			}
		}

		// Last ditch: attempt parsing only digits (yyyyMMdd etc)
		try {
			if (raw.matches("\\d{8}")) {
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
				return LocalDate.parse(raw, fmt);
			}
		} catch (Exception ignored) {
		}

		throw new IllegalArgumentException("Invalid date format: " + raw);
	}

	private LocalTime getTimeCell(Row row, int colIndex) {
		try {
			Cell cell = row.getCell(colIndex);
			if (cell == null)
				return null;

			if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
				return cell.getLocalDateTimeCellValue().toLocalTime();
			}

			String raw = cell.toString().trim();
			if (raw.isEmpty())
				return null;

			String[] patterns = new String[] { "HH:mm", "HH:mm:ss", "hh:mma", "hh:mm:ssa", "H:mm" // allow single-digit
																									// hour
			};

			for (String p : patterns) {
				try {
					return LocalTime.parse(raw, DateTimeFormatter.ofPattern(p));
				} catch (DateTimeParseException ignored) {
				}
			}
		} catch (Exception ignored) {
		}

		return null;
	}


	@Override
	public List<Map<String, Object>> getLeaveDetailsForAttendanceProcess(String fromDate, String toDate, Long orgId,
			String department, String branch, String type, String contractor) {

		Set<Object[]> result = attendanceProcessRepo.getLeaveDetailsForAttendanceProcess(fromDate, toDate, orgId,
				department, branch, type, contractor);
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
			map.put("otHours", record[15] != null ? record[15].toString() : "00:00");

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
				String empname = (String) row[1];
				LocalDate checkindate = ((Date) row[2]).toLocalDate();
				LocalTime intime = LocalTime.parse((String) row[3], timeFormatter);
				LocalTime outtime = LocalTime.parse((String) row[4], timeFormatter);
				Integer othours = row[5] != null ? Integer.parseInt(row[5].toString().trim()) : 0;
				BigDecimal otamount = safeBigDecimal(row[6]);
				BigDecimal rate = safeBigDecimal(row[7]);
				String ottype = row[8] != null ? row[8].toString().trim() : null;
				String otcategory = row[9] != null ? row[9].toString().trim() : null;
				String companyOtPolicy = row[10] != null ? row[10].toString().trim() : null;

				// Try exact match with empcode + checkindate + intime + outtime
				Optional<OtCalculationVO> exactMatchOpt = otCalculationRepo
						.findByEmpcodeAndCheckindateAndIntimeAndOuttime(empcode, checkindate, intime, outtime);

				OtCalculationVO vo;

				if (exactMatchOpt.isPresent()) {
					vo = exactMatchOpt.get();
					if (!isDifferent(vo, othours, otamount, rate, ottype, otcategory, companyOtPolicy)) {
						continue; // All values same, skip
					}
					// Else update the fields
				} else {
					// Check if record exists with empcode + checkindate only (even if in/out
					// changed)
					Optional<OtCalculationVO> partialMatch = otCalculationRepo.findByEmpcodeAndCheckindate(empcode,
							checkindate);
					if (partialMatch.isPresent()) {
						vo = partialMatch.get(); // Update existing
					} else {
						vo = new OtCalculationVO(); // Create new
						vo.setCreatedon(LocalDateTime.now());
					}
				}

				// Create or update values
				vo.setEmpcode(empcode);
				vo.setEmpname(empname);
				vo.setCheckindate(checkindate);
				vo.setIntime(intime);
				vo.setOuttime(outtime);
				vo.setOthours(othours);
				vo.setOtamount(otamount);
				vo.setRate(rate);
				vo.setOttype(ottype);
				vo.setOtcategory(otcategory);
				vo.setCompanyOtPolicy(companyOtPolicy);
				vo.setOrgId(orgId);
				vo.setStatus("PENDING");

				resultList.add(vo);

			} catch (Exception e) {
				System.err.println("Error processing OT row: " + Arrays.toString(row));
				e.printStackTrace();
			}
		}

		return otCalculationRepo.saveAll(resultList);
	}

	private boolean isDifferent(OtCalculationVO vo, Integer othours, BigDecimal otamount, BigDecimal rate,
			String ottype, String otcategory, String companyOtPolicy) {

		return !Objects.equals(vo.getOthours(), othours) || !Objects.equals(vo.getOtamount(), otamount)
				|| !Objects.equals(vo.getRate(), rate) || !Objects.equals(vo.getOttype(), ottype)
				|| !Objects.equals(vo.getOtcategory(), otcategory)
				|| !Objects.equals(vo.getCompanyOtPolicy(), companyOtPolicy);
	}

	private BigDecimal safeBigDecimal(Object obj) {
		try {
			return obj != null ? new BigDecimal(obj.toString().trim()) : BigDecimal.ZERO;
		} catch (NumberFormatException e) {
			System.err.println("Invalid BigDecimal input: " + obj);
			return BigDecimal.ZERO;
		}
	}

	@Override
	public List<OtCalculationVO> getPendingOTHoursByOrgId(String fromDate, String toDate, Long orgId,
			String employeeCode, String branch, String department, String type, String contractor) {
		// TODO Auto-generated method stub
		return otCalculationRepo.getPendingOTHoursByOrgId(fromDate, toDate, orgId, employeeCode, branch, department,
				type, contractor);
	}

	@Override
	public List<OtCalculationVO> getApprovedOTHoursByOrgId(String fromDate, String toDate, Long orgId,
			String employeeCode, String branch, String department, String type, String contractor) {
		// TODO Auto-generated method stub
		return otCalculationRepo.getApprovedOTHoursByOrgId(fromDate, toDate, orgId, employeeCode, branch, department,
				type, contractor);
	}

//monthlyprocess

	@Override
	public List<Map<String, Object>> getMonthlyProcess(int month, int year, Long orgId, String branch,
			String department, String type, String contractor) {
		List<Map<String, Object>> rawList = attendanceProcessRepo.findMonthlyProcess(month, year, orgId, branch,
				department, type, contractor);

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
		return attendanceDailyRepo.getAttendanceDailyByOrgId(fromDate, toDate, orgId, employeeCode, branch);
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
		vo.setOtHours(dto.getOtHours());

		vo.setApproveStatus("PENDING"); // default
	}

	@Override
	public Map<String, Object> createApprovalAttendanceSummary(Long orgId, List<Long> ids, String action,
			String actionBy) throws ApplicationException {
		List<AttendanceSummaryVO> updatedList = new ArrayList<>();
		String message = "";

		for (Long id : ids) {
			AttendanceSummaryVO summaryVO = attendanceSummaryRepo.findById(id)
					.orElseThrow(() -> new ApplicationException("Invalid AttendanceSummary ID: " + id));

			String currentStatus = summaryVO.getApproveStatus();

			if (currentStatus == null
					|| (!currentStatus.equalsIgnoreCase("APPROVED") && !currentStatus.equalsIgnoreCase("REJECTED"))) {

				if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {
					summaryVO.setApproveStatus(action.toUpperCase());
					summaryVO.setApproveBy(actionBy);

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
					summaryVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

					updatedList.add(summaryVO);
				}
			} else if ("APPROVED".equalsIgnoreCase(currentStatus)) {
				throw new ApplicationException(
						"AttendanceSummary already approved for employee: " + summaryVO.getEmpCode());
			} else if ("REJECTED".equalsIgnoreCase(currentStatus)) {
				throw new ApplicationException(
						"AttendanceSummary already rejected for employee: " + summaryVO.getEmpCode());
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
	public List<AttendanceSummaryVO> getPendingAttendanceSummaryByOrgId(Long orgId, String branch) {
		// TODO Auto-generated method stub
		return attendanceSummaryRepo.getPendingAttendanceSummaryByOrgId(orgId, branch);
	}

	@Override
	public List<AttendanceSummaryVO> getAttendanceSummaryByOrgId(String empCode, Integer month, String finYear,
			Long orgId, String branch) {
		// TODO Auto-generated method stub
		return attendanceSummaryRepo.getAttendanceSummaryByOrgId(empCode, month, finYear, orgId, branch);
	}

//	@Override
//	public List<AttendanceRecordVO> processExcel(MultipartFile file) throws IOException {
//	    List<AttendanceRecordVO> records = new ArrayList<>();
//
//	    try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
//	        Sheet sheet = workbook.getSheetAt(0);
//
//	        // Define row indices based on your Excel layout
//	        int dateRowIndex = 1;
//	        int dayRowIndex = 2;
//	        int inTimeRowIndex = 10;
//	        int outTimeRowIndex = 11;
//	        int statusRowIndex = 17;
//
//	        // Dynamically extract employee code and name
//	        String[] empDetails = extractEmployeeDetails(sheet);
//	        String empCode = empDetails[0];
//	        String empName = empDetails[1];
//
//	        // Fetch fixed rows
//	        Row dateRow = sheet.getRow(dateRowIndex);
//	        Row dayRow = sheet.getRow(dayRowIndex);
//	        Row inTimeRow = sheet.getRow(inTimeRowIndex);
//	        Row outTimeRow = sheet.getRow(outTimeRowIndex);
//	        Row statusRow = sheet.getRow(statusRowIndex);
//
//	        // Loop over day-wise columns (starting from 2, as per your sheet)
//	        for (int col = 2; col < dateRow.getLastCellNum(); col++) {
//	            String date = getStringValue(dateRow.getCell(col));
//	            String day = getStringValue(dayRow.getCell(col));
//	            String inTime = getStringValue(inTimeRow.getCell(col));
//	            String outTime = getStringValue(outTimeRow.getCell(col));
//	            String status = getStringValue(statusRow.getCell(col));
//
//	            // Skip blank columns or headers
//	            if (date.trim().isEmpty() || date.equalsIgnoreCase("Day")) continue;
//
//	            AttendanceRecordVO record = new AttendanceRecordVO();
//	            record.setEmployeeCode(empCode);
//	            record.setEmployeeName(empName);
//	            record.setDate(date);
//	            record.setDay(day);
//	            record.setInTime(inTime);
//	            record.setOutTime(outTime);
//	            record.setStatus(status);
//
//	            records.add(record);
//	        }
//
//	        // Optional: Save to DB if needed
//	        attendanceRecordRepo.saveAll(records);
//	    }
//
//	    return records;
//	}
//
//	private String[] extractEmployeeDetails(Sheet sheet) {
//	    String empCode = "";
//	    String empName = "";
//
//	    for (Row row : sheet) {
//	        List<String> cellTexts = new ArrayList<>();
//	        for (Cell cell : row) {
//	            cellTexts.add(getStringValue(cell).trim());
//	        }
//
//	        // Search for "Employee Code" and get value after few cells
//	        for (int i = 0; i < cellTexts.size(); i++) {
//	            String text = cellTexts.get(i).toLowerCase();
//	            if (text.contains("employee code")) {
//	                // Look ahead max 5 columns to find numeric code
//	                for (int j = 1; j <= 5 && (i + j) < cellTexts.size(); j++) {
//	                    String possibleCode = cellTexts.get(i + j);
//	                    if (!possibleCode.isEmpty() && possibleCode.matches("\\d+")) {
//	                        empCode = possibleCode;
//	                        break;
//	                    }
//	                }
//	            }
//
//	            if (text.contains("employee name")) {
//	                // Look ahead max 5 columns to find name
//	                for (int j = 1; j <= 5 && (i + j) < cellTexts.size(); j++) {
//	                    String possibleName = cellTexts.get(i + j);
//	                    if (!possibleName.isEmpty() && !possibleName.toLowerCase().contains("employee name")) {
//	                        empName = possibleName;
//	                        break;
//	                    }
//	                }
//	            }
//	        }
//
//	        // Stop when both found
//	        if (!empCode.isEmpty() && !empName.isEmpty()) {
//	            break;
//	        }
//	    }
//
//	    return new String[]{empCode, empName};
//	}
//
//	private String getStringValue(Cell cell) {
//	    if (cell == null) return "";
//	    switch (cell.getCellType()) {
//	        case STRING:
//	            return cell.getStringCellValue().trim();
//	        case NUMERIC:
//	            if (DateUtil.isCellDateFormatted(cell)) {
//	                return new SimpleDateFormat("dd-MMM").format(cell.getDateCellValue());
//	            } else {
//	                return String.valueOf((int) cell.getNumericCellValue());
//	            }
//	        case BOOLEAN:
//	            return String.valueOf(cell.getBooleanCellValue());
//	        case FORMULA:
//	            try {
//	                return cell.getStringCellValue().trim(); // fallback
//	            } catch (Exception e) {
//	                return String.valueOf(cell.getNumericCellValue()); // fallback
//	            }
//	        default:
//	            return "";
//	    }
//	}
//
//	@Override
//	public List<AttendanceRecordVO> processExcel(MultipartFile file) {
//	    List<AttendanceRecordVO> attendanceList = new ArrayList<>();
//
//	    try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
//	        Sheet sheet = workbook.getSheetAt(0);
//
//	        String employeeCode = "";
//	        String employeeName = "";
//	        boolean insideBlock = false;
//	        List<Row> tempRows = new ArrayList<>();
//
//	        for (Row row : sheet) {
//	            boolean isCodeLine = false;
//	            boolean isNameLine = false;
//
//	            for (Cell cell : row) {
//	                String val = getCellValue(cell);
//
//	                if (val.toLowerCase().contains("employee code:-")) {
//	                    employeeCode = val.split(":-").length > 1 ? val.split(":-")[1].trim() : "";
//	                    isCodeLine = true;
//	                }
//	                if (val.toLowerCase().contains("employee name:-")) {
//	                    employeeName = val.split(":-").length > 1 ? val.split(":-")[1].trim() : "";
//	                    isNameLine = true;
//	                }
//	            }
//
//	            if (isCodeLine || isNameLine) {
//	                if (!tempRows.isEmpty() && !employeeCode.isEmpty() && !employeeName.isEmpty()) {
//	                    extractAttendanceData(tempRows, employeeCode, employeeName, attendanceList);
//	                }
//	                tempRows.clear();
//	                insideBlock = true;
//	            } else if (insideBlock) {
//	                tempRows.add(row);
//	            }
//	        }
//
//	        // Final block
//	        if (!tempRows.isEmpty() && !employeeCode.isEmpty() && !employeeName.isEmpty()) {
//	            extractAttendanceData(tempRows, employeeCode, employeeName, attendanceList);
//	        }
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//
//	    return attendanceList;
//	}
//
//	private void extractAttendanceData(List<Row> rows, String empCode, String empName, List<AttendanceRecordVO> list) {
//	    for (Row row : rows) {
//	        // Only consider rows starting with numeric date (first cell)
//	        Cell firstCell = row.getCell(0);
//	        if (firstCell == null || firstCell.getCellType() != CellType.NUMERIC || !DateUtil.isCellInternalDateFormatted(firstCell)) {
//	            String dateVal = getCellValue(firstCell);
//	            if (!dateVal.matches("\\d{1,2}")) continue;
//	        }
//
//	        AttendanceRecordVO vo = new AttendanceRecordVO();
//	        vo.setEmployeeCode(empCode);
//	        vo.setEmployeeName(empName);
//
//	        vo.setDate(getCellValue(row.getCell(0)));
//	        vo.setDay(getCellValue(row.getCell(1)));
//	        vo.setShift(getCellValue(row.getCell(2)));
//	        vo.setInTime(getCellValue(row.getCell(3)));
//	        vo.setOutTime(getCellValue(row.getCell(4)));
//	        vo.setStatus(getCellValue(row.getCell(5)));
//
//	        list.add(vo);
//	    }
//	}
//
//	private String getCellValue(Cell cell) {
//	    if (cell == null) return "";
//	    switch (cell.getCellType()) {
//	        case STRING:
//	            return cell.getStringCellValue().trim();
//	        case NUMERIC:
//	            if (DateUtil.isCellDateFormatted(cell)) {
//	                return new SimpleDateFormat("dd-MMM").format(cell.getDateCellValue());
//	            } else {
//	                return String.valueOf((int) cell.getNumericCellValue());
//	            }
//	        case BOOLEAN:
//	            return String.valueOf(cell.getBooleanCellValue());
//	        case FORMULA:
//	            try {
//	                return cell.getStringCellValue();
//	            } catch (Exception e) {
//	                return String.valueOf(cell.getNumericCellValue());
//	            }
//	        default:
//	            return "";
//	    }
//	}

}
