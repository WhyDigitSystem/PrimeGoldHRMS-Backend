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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.AdvanceUploadDTO;
import com.efit.hrms.dto.AttendanceSummaryDTO;
import com.efit.hrms.dto.CheckInOutBiometricDTO;
import com.efit.hrms.dto.OtherPaymentsDTO;
import com.efit.hrms.entity.AdvanceUploadVO;
import com.efit.hrms.entity.AttendanceDailyVO;
import com.efit.hrms.entity.AttendanceLogVO;
import com.efit.hrms.entity.AttendanceProcessVO;
import com.efit.hrms.entity.AttendanceSummaryVO;
import com.efit.hrms.entity.CheckInOutBiometricVO;
import com.efit.hrms.entity.CheckInOutUploadVO;
import com.efit.hrms.entity.DeviceLogVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.OtCalculationVO;
import com.efit.hrms.entity.OtherPaymentsVO;
import com.efit.hrms.entity.ShiftAssignDetailsVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.AdvanceUploadRepo;
import com.efit.hrms.repo.AttendanceDailyRepo;
import com.efit.hrms.repo.AttendanceLogRepo;
import com.efit.hrms.repo.AttendanceProcessRepo;
import com.efit.hrms.repo.AttendanceSummaryRepo;
import com.efit.hrms.repo.CheckInOutBiometricRepo;
import com.efit.hrms.repo.CheckInOutUploadRepo;
import com.efit.hrms.repo.CheckInStatusRepo;
import com.efit.hrms.repo.CompanyRepo;
import com.efit.hrms.repo.DeviceLogRepo;
import com.efit.hrms.repo.EmployeeRepo;
import com.efit.hrms.repo.OtCalculationRepo;
import com.efit.hrms.repo.OtMasterRepo;
import com.efit.hrms.repo.OtherPaymentsRepo;
import com.efit.hrms.repo.ShiftAssignDetailsRepo;
import com.efit.hrms.repo.ShiftMasterRepo;
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
	OtMasterRepo otMasterRepo;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	ShiftMasterRepo shiftMasterRepo;

//	@Autowired
//	private SequenceRepo sequenceRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private AttendanceLogRepo attendanceLogRepo;

	@Autowired
	CheckInOutBiometricRepo checkInOutBiometricRepo;
	
	@Autowired
	DeviceLogRepo deviceLogRepo;
	
	@Autowired
	AdvanceUploadRepo advanceUploadRepo;
	
	@Autowired
	OtherPaymentsRepo otherPaymentsRepo;

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
	
	@Scheduled(cron = "0 */10 * * * ?")   // every 10 minutes
	public void fetchAttendanceLogsAtNoon() {
	    try {
	        // Yesterday's date
	        LocalDate yesterday = LocalDate.now()
	                .minusDays(1);
	        
            Long orgId= 1000000001L;
            String createdBy="AUTO SCHEDULAR";
            String branch="HOSUR";
            String branchCode="HOS";
	        
	        // Call your method with yesterday as both from and to date
//	        createCheckInOutBiometricDeviceSchedular(orgId,createdBy,yesterday, yesterday,branch,branchCode);
            createCheckInOutBiometricDevice(orgId,createdBy,yesterday, yesterday,branch,branchCode);

	        
	        System.out.println("Scheduler executed for date: " + yesterday);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
//	AUG 14 CODE ATTENDANCEPULL
//	@Override
//	@Transactional(rollbackOn = Exception.class)
//	public Map<String, Object> createCheckInOutBiometricDevice(Long orgId,
//	                                                           String createdBy,
//	                                                           LocalDate fromDate,
//	                                                           LocalDate toDate,
//	                                                           String branch,
//	                                                           String branchCode) throws Exception {
//
//	    Map<String, Object> result = new LinkedHashMap<>();
//	    Queue<CheckInOutBiometricVO> biometricQueue = new ConcurrentLinkedQueue<>();
//	    Queue<AttendanceDailyVO> dailyListQueue = new ConcurrentLinkedQueue<>();
//	    AtomicInteger successCount = new AtomicInteger(0);
//
//	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//	    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//	    // 1️⃣ Fetch logs
//	    List<AttendanceLogVO> logs = attendanceLogRepo.findByAttendanceDateBetween(fromDate, toDate);
//	    if (logs.isEmpty()) {
//	        throw new RuntimeException("No attendance logs found.");
//	    }
//
//	    // 2️⃣ Fetch existing biometric records once (to skip duplicates)
//	    Set<String> existingKeys = checkInOutBiometricRepo
//	            .findKeysByDateRange(fromDate, toDate, orgId, branchCode)
//	            .stream()
//	            .map(r -> r[0] + "|" + r[1] + "|" + r[2]) // r[0]=empCode, r[1]=checkInDate, r[2]=entryTime
//	            .collect(Collectors.toSet());
//
//
//	    // 3️⃣ Process logs in parallel
//	    logs.parallelStream().forEach(log -> {
//	        try {
//	            LocalDate inDate = null;
//	            LocalTime inTime = null;
//	            LocalDate outDate = null;
//	            LocalTime outTime = null;
//
//	            // Parse IN
//	            if (log.getInTime() != null && !log.getInTime().isEmpty() && !log.getInTime().equals("00:00")) {
//	                try {
//	                    LocalDateTime dt = LocalDateTime.parse(log.getInTime(), dateTimeFormatter);
//	                    inDate = dt.toLocalDate();
//	                    inTime = dt.toLocalTime();
//	                } catch (Exception e) {
//	                    inDate = LocalDate.parse(log.getAttendanceDate(), dateFormatter);
//	                    inTime = LocalTime.parse(log.getInTime(), timeFormatter);
//	                }
//	            }
//
//	            // Parse OUT
//	            if (log.getOutTime() != null && !log.getOutTime().isEmpty() && !log.getOutTime().equals("00:00")) {
//	                try {
//	                    LocalDateTime dt = LocalDateTime.parse(log.getOutTime(), dateTimeFormatter);
//	                    outDate = dt.toLocalDate();
//	                    outTime = dt.toLocalTime();
//	                } catch (Exception e) {
//	                    outDate = LocalDate.parse(log.getAttendanceDate(), dateFormatter);
//	                    outTime = LocalTime.parse(log.getOutTime(), timeFormatter);
//	                }
//	            }
//
//	            // Skip rows without valid IN/OUT
//	            if (inTime == null && outTime == null) return;
//
//	            // Add IN (if not duplicate)
//	            if (inTime != null) {
//	                String key = log.getEmployeeCode() + "|" + inDate + "|" + inTime;
//	                if (!existingKeys.contains(key)) {
//	                    CheckInOutBiometricVO inVo = new CheckInOutBiometricVO();
//	                    inVo.setAttendanceMode("BIOMETRIC");
//	                    inVo.setCheckInDate(inDate);
//	                    inVo.setCreatedBy(createdBy);
//	                    inVo.setBranch(branch);
//	                    inVo.setBranchCode(branchCode);
//	                    inVo.setEmpCode(log.getEmployeeCode());
//	                    inVo.setEmpName(log.getEmployeeName());
//	                    inVo.setEntryTime(inTime);
//	                    inVo.setFinyear(String.valueOf(inDate.getYear()));
//	                    inVo.setOrgId(orgId);
//	                    inVo.setScreenCode("CIOB");
//	                    inVo.setScreenName("CHECKINOUTBIOMETRIC");
//	                    inVo.setStatus("In");
//	                    biometricQueue.add(inVo);
//	                    successCount.incrementAndGet();
//	                }
//	            }
//
//	            // Add OUT (if not duplicate)
//	            if (outTime != null) {
//	                String key = log.getEmployeeCode() + "|" + outDate + "|" + outTime;
//	                if (!existingKeys.contains(key)) {
//	                    CheckInOutBiometricVO outVo = new CheckInOutBiometricVO();
//	                    outVo.setAttendanceMode("BIOMETRIC");
//	                    outVo.setCheckInDate(outDate);
//	                    outVo.setCreatedBy(createdBy);
//	                    outVo.setBranch(branch);
//	                    outVo.setBranchCode(branchCode);
//	                    outVo.setEmpCode(log.getEmployeeCode());
//	                    outVo.setEmpName(log.getEmployeeName());
//	                    outVo.setEntryTime(outTime);
//	                    outVo.setFinyear(String.valueOf(outDate.getYear()));
//	                    outVo.setOrgId(orgId);
//	                    outVo.setScreenCode("CIOB");
//	                    outVo.setScreenName("CHECKINOUTBIOMETRIC");
//	                    outVo.setStatus("Out");
//	                    biometricQueue.add(outVo);
//	                    successCount.incrementAndGet();
//	                }
//	            }
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	    });
//
//	    if (biometricQueue.isEmpty()) {
//	        throw new RuntimeException("No valid biometric records found in the logs.");
//	    }
//
//	    // 4️⃣ Save biometrics in batch
//	    List<CheckInOutBiometricVO> savedBiometric = checkInOutBiometricRepo.saveAll(biometricQueue);
//	    List<Long> currentIds = savedBiometric.stream()
//	            .map(CheckInOutBiometricVO::getId)
//	            .collect(Collectors.toList());
//
//	    if (currentIds.isEmpty()) {
//	        throw new RuntimeException("No biometric records saved.");
//	    }
//
//	    // 5️⃣ Insert AttendanceProcess in batch
//	    BigInteger currentSeq = (BigInteger) entityManager.createNativeQuery(
//	            "SELECT next_val FROM attendanceprocessseq").getSingleResult();
//
//	    entityManager.createNativeQuery(
//	            "INSERT INTO attendanceprocess (" +
//	                    "attendanceprocessid, empcode, empname, orgid, branchcode, branch, finyear, " +
//	                    "checkindate, entrytime, status, sourceid, attendancemode, createdby, createdon, modifiedon, screencode, screenname" +
//	                    ") " +
//	                    "SELECT (:seq + ROW_NUMBER() OVER (ORDER BY checkinoutbiometricid)) AS new_id, " +
//	                    "empcode, empname, orgid, branchcode, branch, finyear, " +
//	                    "checkindate, entrytime, status, checkinoutbiometricid, attendancemode, createdby, NOW(), NOW(), 'AM', 'ATTENDANCE MODE' " +
//	                    "FROM checkinoutbiometric WHERE checkinoutbiometricid IN (:ids)")
//	            .setParameter("seq", currentSeq)
//	            .setParameter("ids", currentIds)
//	            .executeUpdate();
//
//	    entityManager.createNativeQuery(
//	            "UPDATE attendanceprocessseq SET next_val = next_val + " +
//	                    "(SELECT COUNT(*) FROM checkinoutbiometric WHERE checkinoutbiometricid IN (:ids))")
//	            .setParameter("ids", currentIds)
//	            .executeUpdate();
//
//	    // 6️⃣ Build AttendanceDaily (parallel per employee)
//	    List<AttendanceProcessVO> attendanceList = attendanceProcessRepo.findBySourceIdIn(currentIds);
//	    Map<String, List<AttendanceProcessVO>> groupedByEmp = attendanceList.stream()
//	            .collect(Collectors.groupingBy(a -> a.getEmpCode() + "_" + a.getOrgId() + "_" + a.getBranchCode()));
//
//	    groupedByEmp.values().parallelStream().forEach(empRecords -> {
//	        empRecords.sort(Comparator.comparing(AttendanceProcessVO::getCheckInDate)
//	                .thenComparing(AttendanceProcessVO::getEntryTime));
//
//	        Map<LocalDate, List<AttendanceProcessVO>> recordsByDay = new LinkedHashMap<>();
//
//	        for (int i = 0; i < empRecords.size(); i++) {
//	            AttendanceProcessVO rec = empRecords.get(i);
//	            LocalDate workDate = rec.getCheckInDate();
//
//	            // Night shift OUT adjustment
//	            if ("Out".equalsIgnoreCase(rec.getStatus()) && i > 0) {
//	                AttendanceProcessVO prev = empRecords.get(i - 1);
//	                if ("In".equalsIgnoreCase(prev.getStatus()) &&
//	                        rec.getEntryTime().isBefore(prev.getEntryTime())) {
//	                    workDate = prev.getCheckInDate();
//	                }
//	            }
//
//	            recordsByDay.computeIfAbsent(workDate, k -> new ArrayList<>()).add(rec);
//	        }
//
//	        // Create AttendanceDaily per day
//	        for (Map.Entry<LocalDate, List<AttendanceProcessVO>> entry : recordsByDay.entrySet()) {
//	            LocalDate workDate = entry.getKey();
//	            List<AttendanceProcessVO> dayRecords = entry.getValue();
//	            dayRecords.sort(Comparator.comparing(AttendanceProcessVO::getCheckInDate)
//	                    .thenComparing(AttendanceProcessVO::getEntryTime));
//
//	            Deque<AttendanceProcessVO> inQueue = new ArrayDeque<>();
//	            LocalDateTime mergedIn = null;
//	            LocalDateTime mergedOut = null;
//	            int effectiveHours = 0;
//
//	            for (AttendanceProcessVO rec : dayRecords) {
//	                if ("In".equalsIgnoreCase(rec.getStatus())) {
//	                    inQueue.add(rec);
//	                    if (mergedIn == null)
//	                        mergedIn = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
//	                } else if ("Out".equalsIgnoreCase(rec.getStatus()) && !inQueue.isEmpty()) {
//	                    AttendanceProcessVO firstIn = inQueue.removeFirst();
//	                    LocalDateTime inDT = LocalDateTime.of(firstIn.getCheckInDate(), firstIn.getEntryTime());
//	                    LocalDateTime outDT = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
//	                    if (outDT.isBefore(inDT)) outDT = outDT.plusDays(1);
//
//	                    if (mergedOut == null || outDT.isAfter(mergedOut))
//	                        mergedOut = outDT;
//
//	                    effectiveHours += (int) Duration.between(inDT, outDT).toHours();
//	                }
//	            }
//
//	            if (mergedIn != null && mergedOut != null) {
//	                AttendanceProcessVO first = dayRecords.get(0);
//	                AttendanceDailyVO ad = new AttendanceDailyVO();
//	                ad.setEmpCode(first.getEmpCode());
//	                ad.setEmpName(first.getEmpName());
//	                ad.setOrgId(first.getOrgId());
//	                ad.setBranch(first.getBranch());
//	                ad.setBranchCode(first.getBranchCode());
//	                ad.setCheckInDate(mergedIn.toLocalDate());
//	                ad.setCheckOutDate(mergedOut.toLocalDate());
//	                ad.setFinyear(first.getFinyear());
//	                ad.setAttendanceMode("BIOMETRIC");
//	                ad.setInTime(mergedIn.toLocalTime());
//	                ad.setOutTime(mergedOut.toLocalTime());
//	                ad.setGrossHours((int) Duration.between(mergedIn, mergedOut).toHours());
//	                ad.setEffectiveHours(effectiveHours);
//	                ad.setCreatedBy(createdBy);
//
//	                dailyListQueue.add(ad);
//	            }
//	        }
//	    });
//
//	    // 7️⃣ Save AttendanceDaily in batch
//	    attendanceDailyRepo.saveAll(dailyListQueue);
//
//	    result.put("successCount", successCount.get());
//	    result.put("message", "Attendance processed successfully (optimized with parallel processing).");
//	    return result;
//	}

//chaNGES aug 14
	@Override
	@Transactional(rollbackOn = Exception.class)
	public Map<String, Object> createCheckInOutBiometricDevice(
	        Long orgId,
	        String createdBy,
	        LocalDate fromDate,
	        LocalDate toDate,
	        String branch,
	        String branchCode) throws Exception {

	    Map<String, Object> result = new LinkedHashMap<>();

	    Queue<CheckInOutBiometricVO> biometricQueue =
	            new ConcurrentLinkedQueue<>();

	    Queue<AttendanceDailyVO> dailyListQueue =
	            new ConcurrentLinkedQueue<>();

	    AtomicInteger successCount =
	            new AtomicInteger(0);


	    DateTimeFormatter dateFormatter =
	            DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    DateTimeFormatter dateTimeFormatter =
	            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	    DateTimeFormatter timeFormatter =
	            DateTimeFormatter.ofPattern("HH:mm:ss");


	    /* ============================================================
	       1. FETCH ATTENDANCE LOGS
	       ============================================================ */

	    List<AttendanceLogVO> logs =
	            attendanceLogRepo.findByAttendanceDateBetween(
	                    fromDate,
	                    toDate
	            );

	    if (logs.isEmpty()) {

	        throw new RuntimeException(
	                "No attendance logs found."
	        );
	    }


	    /* ============================================================
	       2. FETCH EXISTING BIOMETRIC RECORDS
	       ============================================================ */

	    Set<String> existingKeys =
	            checkInOutBiometricRepo
	                    .findKeysByDateRange(
	                            fromDate,
	                            toDate,
	                            orgId,
	                            branchCode
	                    )
	                    .stream()
	                    .map(r ->
	                            r[0]
	                            + "|"
	                            + r[1]
	                            + "|"
	                            + r[2]
	                    )
	                    .collect(Collectors.toSet());


	    /* ============================================================
	       3. PROCESS ATTENDANCE LOGS

	       KEEPING YOUR EXISTING LOGIC
	       ============================================================ */

	    logs.parallelStream().forEach(log -> {

	        try {

	            LocalDate inDate = null;
	            LocalTime inTime = null;

	            LocalDate outDate = null;
	            LocalTime outTime = null;


	            /* ====================================================
	               PARSE IN
	               ==================================================== */

	            if (
	                    log.getInTime() != null
	                    && !log.getInTime().isEmpty()
	                    && !log.getInTime().equals("00:00")
	            ) {

	                try {

	                    LocalDateTime dt =
	                            LocalDateTime.parse(
	                                    log.getInTime(),
	                                    dateTimeFormatter
	                            );

	                    inDate =
	                            dt.toLocalDate();

	                    inTime =
	                            dt.toLocalTime();

	                } catch (Exception e) {

	                    inDate =
	                            LocalDate.parse(
	                                    log.getAttendanceDate(),
	                                    dateFormatter
	                            );

	                    inTime =
	                            LocalTime.parse(
	                                    log.getInTime(),
	                                    timeFormatter
	                            );
	                }
	            }


	            /* ====================================================
	               PARSE OUT
	               ==================================================== */

	            if (
	                    log.getOutTime() != null
	                    && !log.getOutTime().isEmpty()
	                    && !log.getOutTime().equals("00:00")
	            ) {

	                try {

	                    LocalDateTime dt =
	                            LocalDateTime.parse(
	                                    log.getOutTime(),
	                                    dateTimeFormatter
	                            );

	                    outDate =
	                            dt.toLocalDate();

	                    outTime =
	                            dt.toLocalTime();

	                } catch (Exception e) {

	                    outDate =
	                            LocalDate.parse(
	                                    log.getAttendanceDate(),
	                                    dateFormatter
	                            );

	                    outTime =
	                            LocalTime.parse(
	                                    log.getOutTime(),
	                                    timeFormatter
	                            );
	                }
	            }


	            /* ====================================================
	               SKIP INVALID RECORD
	               ==================================================== */

	            if (
	                    inTime == null
	                    && outTime == null
	            ) {

	                return;
	            }


	            /* ====================================================
	               ADD IN
	               ==================================================== */

	            if (inTime != null) {

	                String key =
	                        log.getEmployeeCode()
	                        + "|"
	                        + inDate
	                        + "|"
	                        + inTime;


	                if (!existingKeys.contains(key)) {

	                    CheckInOutBiometricVO inVo =
	                            new CheckInOutBiometricVO();

	                    inVo.setAttendanceMode(
	                            "BIOMETRIC"
	                    );

	                    inVo.setCheckInDate(
	                            inDate
	                    );

	                    inVo.setCreatedBy(
	                            createdBy
	                    );

	                    inVo.setBranch(
	                            branch
	                    );

	                    inVo.setBranchCode(
	                            branchCode
	                    );

	                    inVo.setEmpCode(
	                            log.getEmployeeCode()
	                    );

	                    inVo.setEmpName(
	                            log.getEmployeeName()
	                    );

	                    inVo.setEntryTime(
	                            inTime
	                    );

	                    inVo.setFinyear(
	                            String.valueOf(
	                                    inDate.getYear()
	                            )
	                    );

	                    inVo.setOrgId(
	                            orgId
	                    );

	                    inVo.setScreenCode(
	                            "CIOB"
	                    );

	                    inVo.setScreenName(
	                            "CHECKINOUTBIOMETRIC"
	                    );

	                    inVo.setStatus(
	                            "In"
	                    );

	                    biometricQueue.add(
	                            inVo
	                    );

	                    successCount.incrementAndGet();
	                }
	            }


	            /* ====================================================
	               ADD OUT
	               ==================================================== */

	            if (outTime != null) {

	                String key =
	                        log.getEmployeeCode()
	                        + "|"
	                        + outDate
	                        + "|"
	                        + outTime;


	                if (!existingKeys.contains(key)) {

	                    CheckInOutBiometricVO outVo =
	                            new CheckInOutBiometricVO();

	                    outVo.setAttendanceMode(
	                            "BIOMETRIC"
	                    );

	                    outVo.setCheckInDate(
	                            outDate
	                    );

	                    outVo.setCreatedBy(
	                            createdBy
	                    );

	                    outVo.setBranch(
	                            branch
	                    );

	                    outVo.setBranchCode(
	                            branchCode
	                    );

	                    outVo.setEmpCode(
	                            log.getEmployeeCode()
	                    );

	                    outVo.setEmpName(
	                            log.getEmployeeName()
	                    );

	                    outVo.setEntryTime(
	                            outTime
	                    );

	                    outVo.setFinyear(
	                            String.valueOf(
	                                    outDate.getYear()
	                            )
	                    );

	                    outVo.setOrgId(
	                            orgId
	                    );

	                    outVo.setScreenCode(
	                            "CIOB"
	                    );

	                    outVo.setScreenName(
	                            "CHECKINOUTBIOMETRIC"
	                    );

	                    outVo.setStatus(
	                            "Out"
	                    );

	                    biometricQueue.add(
	                            outVo
	                    );

	                    successCount.incrementAndGet();
	                }
	            }

	        } catch (Exception e) {

	            e.printStackTrace();
	        }
	    });


	    /* ============================================================
	       4. VALIDATE BIOMETRIC DATA
	       ============================================================ */

	    if (biometricQueue.isEmpty()) {

	        throw new RuntimeException(
	                "No valid biometric records found in the logs."
	        );
	    }


	    /* ============================================================
	       5. SAVE BIOMETRIC
	       ============================================================ */

	    List<CheckInOutBiometricVO> savedBiometric =
	            checkInOutBiometricRepo.saveAll(
	                    biometricQueue
	            );


	    List<Long> currentIds =
	            savedBiometric
	                    .stream()
	                    .map(
	                            CheckInOutBiometricVO::getId
	                    )
	                    .collect(
	                            Collectors.toList()
	                    );


	    if (currentIds.isEmpty()) {

	        throw new RuntimeException(
	                "No biometric records saved."
	        );
	    }


	    /* ============================================================
	       6. INSERT ATTENDANCE PROCESS
	       ============================================================ */

	    BigInteger currentSeq =
	            (BigInteger) entityManager
	                    .createNativeQuery(
	                            "SELECT next_val " +
	                            "FROM attendanceprocessseq"
	                    )
	                    .getSingleResult();


	    entityManager
	            .createNativeQuery(
	                    "INSERT INTO attendanceprocess (" +
	                    "attendanceprocessid, " +
	                    "empcode, " +
	                    "empname, " +
	                    "orgid, " +
	                    "branchcode, " +
	                    "branch, " +
	                    "finyear, " +
	                    "checkindate, " +
	                    "entrytime, " +
	                    "status, " +
	                    "sourceid, " +
	                    "attendancemode, " +
	                    "createdby, " +
	                    "createdon, " +
	                    "modifiedon, " +
	                    "screencode, " +
	                    "screenname" +
	                    ") " +

	                    "SELECT " +
	                    "(:seq + ROW_NUMBER() OVER " +
	                    "(ORDER BY checkinoutbiometricid)) " +
	                    "AS new_id, " +

	                    "empcode, " +
	                    "empname, " +
	                    "orgid, " +
	                    "branchcode, " +
	                    "branch, " +
	                    "finyear, " +
	                    "checkindate, " +
	                    "entrytime, " +
	                    "status, " +
	                    "checkinoutbiometricid, " +
	                    "attendancemode, " +
	                    "createdby, " +
	                    "NOW(), " +
	                    "NOW(), " +
	                    "'AM', " +
	                    "'ATTENDANCE MODE' " +

	                    "FROM checkinoutbiometric " +

	                    "WHERE checkinoutbiometricid " +
	                    "IN (:ids)"
	            )
	            .setParameter(
	                    "seq",
	                    currentSeq
	            )
	            .setParameter(
	                    "ids",
	                    currentIds
	            )
	            .executeUpdate();


	    /* ============================================================
	       7. UPDATE SEQUENCE
	       ============================================================ */

	    entityManager
	            .createNativeQuery(
	                    "UPDATE attendanceprocessseq " +
	                    "SET next_val = next_val + " +
	                    "(" +
	                    "SELECT COUNT(*) " +
	                    "FROM checkinoutbiometric " +
	                    "WHERE checkinoutbiometricid IN (:ids)" +
	                    ")"
	            )
	            .setParameter(
	                    "ids",
	                    currentIds
	            )
	            .executeUpdate();


	    /* ============================================================
	       8. FETCH ATTENDANCE PROCESS

	       IMPORTANT CHANGE:

	       We don't build AttendanceDaily only from currentIds.

	       We need all attendance-process records for the affected
	       employees/date range because an IN and OUT may come from
	       different uploads.
	       ============================================================ */

	    List<String> employeeCodes =
	            savedBiometric
	                    .stream()
	                    .map(
	                            CheckInOutBiometricVO
	                                    ::getEmpCode
	                    )
	                    .filter(
	                            Objects::nonNull
	                    )
	                    .distinct()
	                    .collect(
	                            Collectors.toList()
	                    );


	    if (employeeCodes.isEmpty()) {

	        throw new RuntimeException(
	                "No employee codes found."
	        );
	    }


	    /* ============================================================
	       FETCH ALL PROCESS RECORDS FOR AFFECTED EMPLOYEES

	       IMPORTANT:

	       Use your repository method if you already have one.

	       Example repository method:

	       findByEmpCodeInAndCheckInDateBetweenAndOrgIdAndBranchCode
	       ============================================================ */

	    List<AttendanceProcessVO> attendanceList =
	            attendanceProcessRepo
	                    .findByEmpCodeInAndCheckInDateBetweenAndOrgIdAndBranchCode(
	                            employeeCodes,
	                            fromDate,
	                            toDate,
	                            orgId,
	                            branchCode
	                    );


	    /* ============================================================
	       9. GROUP BY EMPLOYEE
	       ============================================================ */

	    Map<String, List<AttendanceProcessVO>>
	            groupedByEmp =
	            attendanceList
	                    .stream()
	                    .collect(
	                            Collectors.groupingBy(
	                                    a ->
	                                            a.getEmpCode()
	                                            + "_"
	                                            + a.getOrgId()
	                                            + "_"
	                                            + a.getBranchCode()
	                            )
	                    );


	    /* ============================================================
	       10. PROCESS EACH EMPLOYEE
	       ============================================================ */

	    groupedByEmp.values()
	            .forEach(empRecords -> {


	                /* =================================================
	                   SORT BY DATE + TIME
	                   ================================================= */

	                empRecords.sort(
	                        Comparator
	                                .comparing(
	                                        AttendanceProcessVO
	                                                ::getCheckInDate
	                                )
	                                .thenComparing(
	                                        AttendanceProcessVO
	                                                ::getEntryTime
	                                )
	                );


	                /* =================================================
	                   GROUP BY ATTENDANCE DATE
	                   ================================================= */

	                Map<LocalDate,
	                        List<AttendanceProcessVO>>
	                        recordsByDay =
	                        new LinkedHashMap<>();


	                for (
	                        AttendanceProcessVO rec
	                        : empRecords
	                ) {

	                    LocalDate workDate =
	                            rec.getCheckInDate();


	                    /*
	                     * IMPORTANT:
	                     *
	                     * DO NOT move OUT to next day here.
	                     *
	                     * We will decide that only while pairing
	                     * IN -> OUT.
	                     */


	                    recordsByDay
	                            .computeIfAbsent(
	                                    workDate,
	                                    k -> new ArrayList<>()
	                            )
	                            .add(rec);
	                }


	                /* =================================================
	                   11. CREATE ATTENDANCE DAILY PER DAY
	                   ================================================= */

	                for (
	                        Map.Entry<
	                                LocalDate,
	                                List<AttendanceProcessVO>
	                                > entry
	                        : recordsByDay.entrySet()
	                ) {

	                    LocalDate workDate =
	                            entry.getKey();

	                    List<AttendanceProcessVO>
	                            dayRecords =
	                            entry.getValue();


	                    dayRecords.sort(
	                            Comparator
	                                    .comparing(
	                                            AttendanceProcessVO
	                                                    ::getCheckInDate
	                                    )
	                                    .thenComparing(
	                                            AttendanceProcessVO
	                                                    ::getEntryTime
	                                    )
	                    );


	                    /* =================================================
	                       IN QUEUE
	                       ================================================= */

	                    Deque<AttendanceProcessVO>
	                            inQueue =
	                            new ArrayDeque<>();


	                    LocalDateTime mergedIn =
	                            null;

	                    LocalDateTime mergedOut =
	                            null;

	                    int effectiveHours = 0;


	                    /* =================================================
	                       12. PAIR IN -> OUT
	                       ================================================= */

	                    for (
	                            AttendanceProcessVO rec
	                            : dayRecords
	                    ) {


	                        /* =============================================
	                           IN
	                           ============================================= */

	                        if (
	                                "In".equalsIgnoreCase(
	                                        rec.getStatus()
	                                )
	                        ) {

	                            inQueue.addLast(
	                                    rec
	                            );


	                            if (mergedIn == null) {

	                                mergedIn =
	                                        LocalDateTime.of(
	                                                rec.getCheckInDate(),
	                                                rec.getEntryTime()
	                                        );
	                            }


	                            continue;
	                        }


	                        /* =============================================
	                           OUT
	                           ============================================= */

	                        if (
	                                "Out".equalsIgnoreCase(
	                                        rec.getStatus()
	                                )
	                                && !inQueue.isEmpty()
	                        ) {

	                            /*
	                             * Get the oldest unmatched IN.
	                             */

	                            AttendanceProcessVO firstIn =
	                                    inQueue.removeFirst();


	                            LocalDateTime inDT =
	                                    LocalDateTime.of(
	                                            firstIn.getCheckInDate(),
	                                            firstIn.getEntryTime()
	                                    );


	                            LocalDateTime outDT =
	                                    LocalDateTime.of(
	                                            rec.getCheckInDate(),
	                                            rec.getEntryTime()
	                                    );


	                            /* =========================================
	                               IMPORTANT FIX

	                               DO NOT blindly do:

	                               outDT.plusDays(1)

	                               when OUT < IN.

	                               Only allow overnight when the actual
	                               OUT record belongs to the next date.
	                               ========================================= */

	                            if (
	                                    outDT.isBefore(inDT)
	                            ) {

	                                /*
	                                 * If OUT is earlier than IN on the
	                                 * same date, this is not a valid pair.
	                                 *
	                                 * Do NOT convert it into next-day
	                                 * attendance automatically.
	                                 */

	                                continue;
	                            }


	                            /* =========================================
	                               MERGED IN
	                               ========================================= */

	                            if (
	                                    mergedIn == null
	                                    || inDT.isBefore(
	                                            mergedIn
	                                    )
	                            ) {

	                                mergedIn =
	                                        inDT;
	                            }


	                            /* =========================================
	                               MERGED OUT
	                               ========================================= */

	                            if (
	                                    mergedOut == null
	                                    || outDT.isAfter(
	                                            mergedOut
	                                    )
	                            ) {

	                                mergedOut =
	                                        outDT;
	                            }


	                            /* =========================================
	                               EFFECTIVE HOURS
	                               ========================================= */

	                            effectiveHours +=
	                                    (int) Duration
	                                            .between(
	                                                    inDT,
	                                                    outDT
	                                            )
	                                            .toHours();
	                        }
	                    }


	                    /* =================================================
	                       13. CREATE ATTENDANCE DAILY
	                       ================================================= */

	                    if (
	                            mergedIn != null
	                            && mergedOut != null
	                    ) {

	                        AttendanceProcessVO first =
	                                dayRecords.get(0);


	                        AttendanceDailyVO ad =
	                                new AttendanceDailyVO();


	                        ad.setEmpCode(
	                                first.getEmpCode()
	                        );

	                        ad.setEmpName(
	                                first.getEmpName()
	                        );

	                        ad.setOrgId(
	                                first.getOrgId()
	                        );

	                        ad.setBranch(
	                                first.getBranch()
	                        );

	                        ad.setBranchCode(
	                                first.getBranchCode()
	                        );


	                        /* =============================================
	                           CHECK-IN DATE
	                           ============================================= */

	                        ad.setCheckInDate(
	                                mergedIn.toLocalDate()
	                        );


	                        /* =============================================
	                           CHECK-OUT DATE
	                           ============================================= */

	                        ad.setCheckOutDate(
	                                mergedOut.toLocalDate()
	                        );


	                        ad.setFinyear(
	                                first.getFinyear()
	                        );

	                        ad.setAttendanceMode(
	                                "BIOMETRIC"
	                        );


	                        ad.setInTime(
	                                mergedIn.toLocalTime()
	                        );

	                        ad.setOutTime(
	                                mergedOut.toLocalTime()
	                        );


	                        /* =============================================
	                           GROSS HOURS
	                           ============================================= */

	                        ad.setGrossHours(
	                                (int) Duration
	                                        .between(
	                                                mergedIn,
	                                                mergedOut
	                                        )
	                                        .toHours()
	                        );


	                        /* =============================================
	                           EFFECTIVE HOURS
	                           ============================================= */

	                        ad.setEffectiveHours(
	                                effectiveHours
	                        );


	                        ad.setCreatedBy(
	                                createdBy
	                        );


	                        dailyListQueue.add(
	                                ad
	                        );
	                    }
	                }
	            });


	    /* ============================================================
	       14. SAVE ATTENDANCE DAILY
	       ============================================================ */

	    if (!dailyListQueue.isEmpty()) {

	        attendanceDailyRepo.saveAll(
	                dailyListQueue
	        );
	    }


	    /* ============================================================
	       15. RESPONSE
	       ============================================================ */

	    result.put(
	            "successCount",
	            successCount.get()
	    );

	    result.put(
	            "message",
	            "Attendance processed successfully."
	    );


	    return result;
	}
	
	
//	---------------------------------------
	// Run every day at 12 PM
////	@Scheduled(cron = "0 0 12 * * ?")
//	@Scheduled(cron = "0 0/10 * * * ?")   // every 10 minutes
//	public void fetchAttendanceLogsAtNoon() {
//	    try {
//	        // Yesterday's date
//	        LocalDate yesterday = LocalDate.now()
//	                .minusDays(1);
//	        
//            Long orgId= 1000000001L;
//            String createdBy="AUTO SCHEDULAR";
//            String branch="HOSUR";
//            String branchCode="HOS";
//	        
//	        // Call your method with yesterday as both from and to date
//	        createCheckInOutBiometricDeviceSchedular(orgId,createdBy,yesterday, yesterday,branch,branchCode);
//
//	        System.out.println("Scheduler executed for date: " + yesterday);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Map<String, Object> createCheckInOutBiometricDeviceSchedular(Long orgId,
	                                                           String createdBy,
	                                                           LocalDate fromDate,
	                                                           LocalDate toDate,
	                                                           String branch,
	                                                           String branchCode) throws Exception {

	    Map<String, Object> result = new LinkedHashMap<>();
	    Queue<CheckInOutBiometricVO> biometricQueue = new ConcurrentLinkedQueue<>();
	    Queue<AttendanceDailyVO> dailyListQueue = new ConcurrentLinkedQueue<>();
	    AtomicInteger successCount = new AtomicInteger(0);

	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	    // 1️⃣ Fetch logs
	    List<AttendanceLogVO> logs = attendanceLogRepo.findByAttendanceDateBetween(fromDate, toDate);
	    if (logs.isEmpty()) {
	        throw new RuntimeException("No attendance logs found.");
	    }

	    // 2️⃣ Fetch existing biometric records once (to skip duplicates)
	    Set<String> existingKeys = checkInOutBiometricRepo
	            .findKeysByDateRange(fromDate, toDate, orgId, branchCode)
	            .stream()
	            .map(r -> r[0] + "|" + r[1] + "|" + r[2]) // r[0]=empCode, r[1]=checkInDate, r[2]=entryTime
	            .collect(Collectors.toSet());


	    // 3️⃣ Process logs in parallel
	    logs.parallelStream().forEach(log -> {
	        try {
	            LocalDate inDate = null;
	            LocalTime inTime = null;
	            LocalDate outDate = null;
	            LocalTime outTime = null;

	            // Parse IN
	            if (log.getInTime() != null && !log.getInTime().isEmpty() && !log.getInTime().equals("00:00")) {
	                try {
	                    LocalDateTime dt = LocalDateTime.parse(log.getInTime(), dateTimeFormatter);
	                    inDate = dt.toLocalDate();
	                    inTime = dt.toLocalTime();
	                } catch (Exception e) {
	                    inDate = LocalDate.parse(log.getAttendanceDate(), dateFormatter);
	                    inTime = LocalTime.parse(log.getInTime(), timeFormatter);
	                }
	            }

	            // Parse OUT
	            if (log.getOutTime() != null && !log.getOutTime().isEmpty() && !log.getOutTime().equals("00:00")) {
	                try {
	                    LocalDateTime dt = LocalDateTime.parse(log.getOutTime(), dateTimeFormatter);
	                    outDate = dt.toLocalDate();
	                    outTime = dt.toLocalTime();
	                } catch (Exception e) {
	                    outDate = LocalDate.parse(log.getAttendanceDate(), dateFormatter);
	                    outTime = LocalTime.parse(log.getOutTime(), timeFormatter);
	                }
	            }

	            // Skip rows without valid IN/OUT
	            if (inTime == null && outTime == null) return;

	            // Add IN (if not duplicate)
	            if (inTime != null) {
	                String key = log.getEmployeeCode() + "|" + inDate + "|" + inTime;
	                if (!existingKeys.contains(key)) {
	                    CheckInOutBiometricVO inVo = new CheckInOutBiometricVO();
	                    inVo.setAttendanceMode("BIOMETRIC");
	                    inVo.setCheckInDate(inDate);
	                    inVo.setCreatedBy(createdBy);
	                    inVo.setBranch(branch);
	                    inVo.setBranchCode(branchCode);
	                    inVo.setEmpCode(log.getEmployeeCode());
	                    inVo.setEmpName(log.getEmployeeName());
	                    inVo.setEntryTime(inTime);
	                    inVo.setFinyear(String.valueOf(inDate.getYear()));
	                    inVo.setOrgId(orgId);
	                    inVo.setScreenCode("CIOB");
	                    inVo.setScreenName("CHECKINOUTBIOMETRIC");
	                    inVo.setStatus("In");
	                    biometricQueue.add(inVo);
	                    successCount.incrementAndGet();
	                }
	            }

	            // Add OUT (if not duplicate)
	            if (outTime != null) {
	                String key = log.getEmployeeCode() + "|" + outDate + "|" + outTime;
	                if (!existingKeys.contains(key)) {
	                    CheckInOutBiometricVO outVo = new CheckInOutBiometricVO();
	                    outVo.setAttendanceMode("BIOMETRIC");
	                    outVo.setCheckInDate(outDate);
	                    outVo.setCreatedBy(createdBy);
	                    outVo.setBranch(branch);
	                    outVo.setBranchCode(branchCode);
	                    outVo.setEmpCode(log.getEmployeeCode());
	                    outVo.setEmpName(log.getEmployeeName());
	                    outVo.setEntryTime(outTime);
	                    outVo.setFinyear(String.valueOf(outDate.getYear()));
	                    outVo.setOrgId(orgId);
	                    outVo.setScreenCode("CIOB");
	                    outVo.setScreenName("CHECKINOUTBIOMETRIC");
	                    outVo.setStatus("Out");
	                    biometricQueue.add(outVo);
	                    successCount.incrementAndGet();
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });

	    if (biometricQueue.isEmpty()) {
	        throw new RuntimeException("No valid biometric records found in the logs.");
	    }

	    // 4️⃣ Save biometrics in batch
	    List<CheckInOutBiometricVO> savedBiometric = checkInOutBiometricRepo.saveAll(biometricQueue);
	    List<Long> currentIds = savedBiometric.stream()
	            .map(CheckInOutBiometricVO::getId)
	            .collect(Collectors.toList());

	    if (currentIds.isEmpty()) {
	        throw new RuntimeException("No biometric records saved.");
	    }

	    // 5️⃣ Insert AttendanceProcess in batch
	    BigInteger currentSeq = (BigInteger) entityManager.createNativeQuery(
	            "SELECT next_val FROM attendanceprocessseq").getSingleResult();

	    entityManager.createNativeQuery(
	            "INSERT INTO attendanceprocess (" +
	                    "attendanceprocessid, empcode, empname, orgid, branchcode, branch, finyear, " +
	                    "checkindate, entrytime, status, sourceid, attendancemode, createdby, createdon, modifiedon, screencode, screenname" +
	                    ") " +
	                    "SELECT (:seq + ROW_NUMBER() OVER (ORDER BY checkinoutbiometricid)) AS new_id, " +
	                    "empcode, empname, orgid, branchcode, branch, finyear, " +
	                    "checkindate, entrytime, status, checkinoutbiometricid, attendancemode, createdby, NOW(), NOW(), 'AM', 'ATTENDANCE MODE' " +
	                    "FROM checkinoutbiometric WHERE checkinoutbiometricid IN (:ids)")
	            .setParameter("seq", currentSeq)
	            .setParameter("ids", currentIds)
	            .executeUpdate();

	    entityManager.createNativeQuery(
	            "UPDATE attendanceprocessseq SET next_val = next_val + " +
	                    "(SELECT COUNT(*) FROM checkinoutbiometric WHERE checkinoutbiometricid IN (:ids))")
	            .setParameter("ids", currentIds)
	            .executeUpdate();

	    // 6️⃣ Build AttendanceDaily (parallel per employee)
	    List<AttendanceProcessVO> attendanceList = attendanceProcessRepo.findBySourceIdIn(currentIds);
	    Map<String, List<AttendanceProcessVO>> groupedByEmp = attendanceList.stream()
	            .collect(Collectors.groupingBy(a -> a.getEmpCode() + "_" + a.getOrgId() + "_" + a.getBranchCode()));

	    groupedByEmp.values().parallelStream().forEach(empRecords -> {
	        empRecords.sort(Comparator.comparing(AttendanceProcessVO::getCheckInDate)
	                .thenComparing(AttendanceProcessVO::getEntryTime));

	        Map<LocalDate, List<AttendanceProcessVO>> recordsByDay = new LinkedHashMap<>();

	        for (int i = 0; i < empRecords.size(); i++) {
	            AttendanceProcessVO rec = empRecords.get(i);
	            LocalDate workDate = rec.getCheckInDate();

	            // Night shift OUT adjustment
	            if ("Out".equalsIgnoreCase(rec.getStatus()) && i > 0) {
	                AttendanceProcessVO prev = empRecords.get(i - 1);
	                if ("In".equalsIgnoreCase(prev.getStatus()) &&
	                        rec.getEntryTime().isBefore(prev.getEntryTime())) {
	                    workDate = prev.getCheckInDate();
	                }
	            }

	            recordsByDay.computeIfAbsent(workDate, k -> new ArrayList<>()).add(rec);
	        }

	        // Create AttendanceDaily per day
	        for (Map.Entry<LocalDate, List<AttendanceProcessVO>> entry : recordsByDay.entrySet()) {
	            LocalDate workDate = entry.getKey();
	            List<AttendanceProcessVO> dayRecords = entry.getValue();
	            dayRecords.sort(Comparator.comparing(AttendanceProcessVO::getCheckInDate)
	                    .thenComparing(AttendanceProcessVO::getEntryTime));

	            Deque<AttendanceProcessVO> inQueue = new ArrayDeque<>();
	            LocalDateTime mergedIn = null;
	            LocalDateTime mergedOut = null;
	            int effectiveHours = 0;

	            for (AttendanceProcessVO rec : dayRecords) {
	                if ("In".equalsIgnoreCase(rec.getStatus())) {
	                    inQueue.add(rec);
	                    if (mergedIn == null)
	                        mergedIn = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
	                } else if ("Out".equalsIgnoreCase(rec.getStatus()) && !inQueue.isEmpty()) {
	                    AttendanceProcessVO firstIn = inQueue.removeFirst();
	                    LocalDateTime inDT = LocalDateTime.of(firstIn.getCheckInDate(), firstIn.getEntryTime());
	                    LocalDateTime outDT = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
	                    if (outDT.isBefore(inDT)) outDT = outDT.plusDays(1);

	                    if (mergedOut == null || outDT.isAfter(mergedOut))
	                        mergedOut = outDT;

	                    effectiveHours += (int) Duration.between(inDT, outDT).toHours();
	                }
	            }

	            if (mergedIn != null && mergedOut != null) {
	                AttendanceProcessVO first = dayRecords.get(0);
	                AttendanceDailyVO ad = new AttendanceDailyVO();
	                ad.setEmpCode(first.getEmpCode());
	                ad.setEmpName(first.getEmpName());
	                ad.setOrgId(first.getOrgId());
	                ad.setBranch(first.getBranch());
	                ad.setBranchCode(first.getBranchCode());
	                ad.setCheckInDate(mergedIn.toLocalDate());
	                ad.setCheckOutDate(mergedOut.toLocalDate());
	                ad.setFinyear(first.getFinyear());
	                ad.setAttendanceMode("BIOMETRIC");
	                ad.setInTime(mergedIn.toLocalTime());
	                ad.setOutTime(mergedOut.toLocalTime());
	                ad.setGrossHours((int) Duration.between(mergedIn, mergedOut).toHours());
	                ad.setEffectiveHours(effectiveHours);
	                ad.setCreatedBy(createdBy);

	                dailyListQueue.add(ad);
	            }
	        }
	    });

	    // 7️⃣ Save AttendanceDaily in batch
	    attendanceDailyRepo.saveAll(dailyListQueue);

	    result.put("successCount", successCount.get());
	    result.put("message", "Attendance processed successfully (optimized with parallel processing).");
	    return result;
	}
	
	
	

//before sep 9	
//	@Transactional(rollbackOn = Exception.class)
//	@Override
//	public String checkInOutUploadExcel(MultipartFile file, Long orgId, String createdBy) throws Exception {
//
//	    Queue<CheckInOutUploadVO> validList = new ConcurrentLinkedQueue<>();
//	    Queue<Map<String, Object>> failures = new ConcurrentLinkedQueue<>();
//	    AtomicInteger successCount = new AtomicInteger(0);
//
//	    try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
//
//	        Sheet sheet = workbook.getSheetAt(0);
//	        DataFormatter formatter = new DataFormatter();
//	        int totalRows = sheet.getLastRowNum();
//	        if (totalRows < 1)
//	            throw new IllegalArgumentException("No data rows found in Excel.");
//
//	        // 1️⃣ Collect employee codes
//	        Set<String> empCodes = new HashSet<>();
//	        for (int i = 1; i <= totalRows; i++) {
//	            Row row = sheet.getRow(i);
//	            if (row == null) continue;
//	            String empCode = formatter.formatCellValue(row.getCell(0));
//	            if (empCode != null && !empCode.trim().isEmpty())
//	                empCodes.add(empCode.trim());
//	        }
//
//	        // 2️⃣ Fetch employees in batch
//	        Map<String, EmployeeVO> employeeMap = employeeRepo.findByEmployeeCodeInAndOrgId(empCodes, orgId)
//	                .stream().collect(Collectors.toMap(EmployeeVO::getEmployeeCode, e -> e));
//
//	        // 3️⃣ Parse rows (multi-threaded)
//	        int numThreads = (totalRows <= 100) ? 10 : Math.min(20, totalRows / 100);
//	        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
//	        int chunkSize = (int) Math.ceil((double) totalRows / numThreads);
//	        List<Future<?>> futures = new ArrayList<>();
//
//	        for (int start = 1; start <= totalRows; start += chunkSize) {
//	            final int finalStart = start;
//	            final int finalEnd = Math.min(start + chunkSize - 1, totalRows);
//	            futures.add(executor.submit(() -> {
//	                for (int i = finalStart; i <= finalEnd; i++) {
//	                    Row row = sheet.getRow(i);
//	                    if (row == null) continue;
//	                    try {
//	                        String empCode = formatter.formatCellValue(row.getCell(0));
//	                        if (empCode == null || empCode.trim().isEmpty())
//	                            throw new IllegalArgumentException("Employee Code is missing");
//
//	                        EmployeeVO employeeVO = employeeMap.get(empCode);
//	                        if (employeeVO == null)
//	                            throw new IllegalArgumentException("Employee not found: " + empCode);
//
//	                        String empName = employeeVO.getEmployeeName();
//	                        String branch = employeeVO.getBranch();
//	                        String branchCode = employeeVO.getBranchCode();
//
//	                        LocalDate checkInDate = getDateCell(row, 2, formatter);
//	                        LocalTime inTime = getTimeCell(row, 4, formatter);
//	                        LocalTime outTime = getTimeCell(row, 5, formatter);
//
//	                        List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo.findApplicableShifts(empCode,
//	                                checkInDate, orgId);
//	                        ShiftAssignDetailsVO latestShift = shifts.stream()
//	                                .max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom))
//	                                .orElse(null);
//	                        boolean isNightShift = latestShift != null
//	                                && "NIGHT".equalsIgnoreCase(latestShift.getShiftType());
//
//	                        // IN
//	                        if (inTime != null) {
//	                            CheckInOutUploadVO vo = new CheckInOutUploadVO();
//	                            vo.setEmpcode(empCode);
//	                            vo.setEmpname(empName);
//	                            vo.setOrgId(orgId);
//	                            vo.setCheckInDate(checkInDate);
//	                            vo.setBranch(branch);
//	                            vo.setBranchCode(branchCode);
//	                            vo.setEntryTime(inTime);
//	                            vo.setStatus("In");
//	                            vo.setCreatedBy(createdBy);
//	                            vo.setFinYear(String.valueOf(checkInDate.getYear()));
//	                            validList.add(vo);
//	                            successCount.incrementAndGet();
//	                        }
//
//	                        // OUT
//	                        if (outTime != null) {
//	                            LocalDate outDate = checkInDate;
//	                            if (isNightShift && inTime != null && outTime.isBefore(inTime))
//	                                outDate = checkInDate.plusDays(1);
//
//	                            CheckInOutUploadVO vo = new CheckInOutUploadVO();
//	                            vo.setEmpcode(empCode);
//	                            vo.setEmpname(empName);
//	                            vo.setOrgId(orgId);
//	                            vo.setCheckInDate(outDate);
//	                            vo.setBranch(branch);
//	                            vo.setBranchCode(branchCode);
//	                            vo.setEntryTime(outTime);
//	                            vo.setStatus("Out");
//	                            vo.setCreatedBy(createdBy);
//	                            vo.setFinYear(String.valueOf(outDate.getYear()));
//	                            validList.add(vo);
//	                            successCount.incrementAndGet();
//	                        }
//
//	                    } catch (Exception e) {
//	                        Map<String, Object> error = new HashMap<>();
//	                        error.put("row", i + 1);
//	                        String empCode = formatter.formatCellValue(row.getCell(0));
//	                        EmployeeVO emp = (empCode != null) ? employeeMap.get(empCode) : null;
//	                        error.put("empName", (emp != null) ? emp.getEmployeeName() : "");
//	                        error.put("error", e.getMessage());
//	                        failures.add(error);
//	                    }
//	                }
//	            }));
//	        }
//
//	        for (Future<?> f : futures) f.get();
//	        executor.shutdown();
//
//	        Map<String, Object> result = new LinkedHashMap<>();
//	        result.put("successCount", successCount.get());
//	        result.put("failedCount", failures.size());
//	        result.put("failures", failures);
//	        if (!failures.isEmpty()) {
//	            result.put("message", "Upload failed due to errors. No records saved.");
//	            return new ObjectMapper().writeValueAsString(result);
//	        }
//
//	        // ✅ Save CheckInOutUpload in bulk
//	        List<CheckInOutUploadVO> savedUploads = checkInOutUploadRepo.saveAll(validList);
//
//	        // ✅ Collect IDs for this upload
//	        List<Long> currentUploadIds = savedUploads.stream()
//	                .map(CheckInOutUploadVO::getId)
//	                .collect(Collectors.toList());
//
//	        // Step 1: Fetch current sequence value
//	        BigInteger currentSeq = (BigInteger) entityManager.createNativeQuery(
//	                "SELECT next_val FROM attendanceprocessseq"
//	        ).getSingleResult();
//
//	        // Step 2: Insert into attendanceprocess for current upload
//	        entityManager.createNativeQuery(
//	                "INSERT INTO attendanceprocess (" +
//	                        "attendanceprocessid, empcode, empname, orgid, branchcode, branch, finyear, " +
//	                        "checkindate, entrytime, status, sourceid, attendancemode, createdby,createdon,modifiedon,screencode, screenname" +
//	                ") " +
//	                "SELECT " +
//	                        "(?1 + ROW_NUMBER() OVER (ORDER BY checkinoutuploadid)) AS new_id, " +
//	                        "empcode, empname, orgid, branchcode, branch, finyear, " +
//	                        "checkindate, entrytime, status, checkinoutuploadid, attendancemode,createdby,createdon,modifiedon, 'AM', 'ATTENDANCE MODE' " +
//	                "FROM checkinoutupload " +
//	                "WHERE checkinoutuploadid IN (?2)"
//	        )
//	        .setParameter(1, currentSeq)
//	        .setParameter(2, currentUploadIds)
//	        .executeUpdate();
//
//	        // Step 3: Update sequence table
//	        entityManager.createNativeQuery(
//	                "UPDATE attendanceprocessseq " +
//	                "SET next_val = next_val + (" +
//	                    "SELECT COUNT(*) FROM checkinoutupload WHERE checkinoutuploadid IN (:ids)" +
//	                ")"
//	        )
//	        .setParameter("ids", currentUploadIds)
//	        .executeUpdate();
//
//	        // ✅ Calculate daily attendance
//	     // ✅ Fetch attendance rows inserted for this upload
//	        List<AttendanceProcessVO> attendanceList = attendanceProcessRepo.findBySourceIdIn(currentUploadIds);
//
//	        // Group only by employee (do NOT include checkInDate here)
//	        Map<String, List<AttendanceProcessVO>> groupedByEmp = attendanceList.stream()
//	                .collect(Collectors.groupingBy(a -> a.getEmpCode() + "_" + a.getOrgId() + "_" + a.getBranchCode()));
//
//	        Queue<AttendanceDailyVO> dailyListQueue = new ConcurrentLinkedQueue<>();
//
//	        // Keep same thread-count as earlier
//	        ExecutorService dailyExecutor = Executors.newFixedThreadPool(numThreads);
//	        List<Future<?>> dailyFutures = new ArrayList<>();
//
//	        for (List<AttendanceProcessVO> empRecords : groupedByEmp.values()) {
//	            dailyFutures.add(dailyExecutor.submit(() -> {
//	                // Sort chronologically by checkInDate then entryTime
//	                List<AttendanceProcessVO> recs = empRecords.stream()
//	                        .sorted(Comparator.comparing(AttendanceProcessVO::getCheckInDate)
//	                                .thenComparing(AttendanceProcessVO::getEntryTime))
//	                        .collect(Collectors.toList());
//
//	                // FIFO queue for unmatched INs
//	                Deque<AttendanceProcessVO> inQueue = new ArrayDeque<>();
//
//	                for (AttendanceProcessVO rec : recs) {
//	                    String status = rec.getStatus() == null ? "" : rec.getStatus().trim();
//
//	                    if ("In".equalsIgnoreCase(status)) {
//	                        // Add to IN queue to be paired with a future OUT
//	                        inQueue.addLast(rec);
//	                        continue;
//	                    }
//
//	                    if ("Out".equalsIgnoreCase(status)) {
//	                        // Pair with earliest unmatched IN (FIFO)
//	                        if (inQueue.isEmpty()) {
//	                            // No unmatched IN found. Skip this OUT (or optionally log it to failures).
//	                            continue;
//	                        }
//
//	                        AttendanceProcessVO firstIn = inQueue.removeFirst();
//	                        AttendanceProcessVO lastOut = rec;
//
//	                        // Build full datetimes
//	                        LocalDateTime inDT = LocalDateTime.of(firstIn.getCheckInDate(), firstIn.getEntryTime());
//	                        LocalDateTime outDT = LocalDateTime.of(lastOut.getCheckInDate(), lastOut.getEntryTime());
//
//	                        // If OUT is before IN -> night shift => move only the OUT datetime forward by 1 day
//	                        if (outDT.isBefore(inDT)) {
//	                            outDT = outDT.plusDays(1);
//	                        }
//
//	                        long grossSeconds = Duration.between(inDT, outDT).getSeconds();
//	                        int hours = (int) (grossSeconds / 3600);
//	                        int minutes = (int) ((grossSeconds % 3600) / 60);
//
//	                        AttendanceDailyVO ad = new AttendanceDailyVO();
//	                        ad.setEmpCode(firstIn.getEmpCode());
//	                        ad.setEmpName(firstIn.getEmpName());
//	                        ad.setOrgId(firstIn.getOrgId());
//	                        ad.setBranch(firstIn.getBranch());
//	                        ad.setBranchCode(firstIn.getBranchCode());
//
//	                        // ✅ Attendance day ALWAYS = IN date (firstIn)
//	                        ad.setCheckInDate(firstIn.getCheckInDate());
//
//	                        // ✅ Checkout date = outDT date (may be +1 day)
//	                        ad.setCheckOutDate(outDT.toLocalDate());
//
//	                        ad.setFinyear(firstIn.getFinyear());
//	                        ad.setAttendanceMode("FILES");
//
//	                        ad.setInTime(inDT.toLocalTime());
//	                        ad.setOutTime(outDT.toLocalTime());
//
//	                        // If you want to keep hours as integer hours (same as before):
//	                        ad.setEffectiveHours(hours);
//	                        ad.setGrossHours(hours);
//
//	                        // If you prefer fractional hours, compute: double fractional = hours + minutes/60.0
//	                        // and store appropriately (requires VO fields capable of double).
//
//	                        ad.setCreatedBy(createdBy);
//	                        dailyListQueue.add(ad);
//
//	                    }}}));
//	        }
//
//	        for (Future<?> f : dailyFutures) f.get();
//	        dailyExecutor.shutdown();
//
//	        attendanceDailyRepo.saveAll(dailyListQueue);
//
//	        result.put("message", "Attendance Uploaded successfully");
//	        return new ObjectMapper().writeValueAsString(result);
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        throw e;
//	    }
//	}

	// SEP 10 CORRECT
//	@Transactional(rollbackOn = Exception.class)
//	@Override
//	public String checkInOutUploadExcel(MultipartFile file, Long orgId, String createdBy) throws Exception {
//
//		Queue<CheckInOutUploadVO> validList = new ConcurrentLinkedQueue<>();
//		Queue<Map<String, Object>> failures = new ConcurrentLinkedQueue<>();
//		AtomicInteger successCount = new AtomicInteger(0);
//
//		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
//
//			Sheet sheet = workbook.getSheetAt(0);
//			DataFormatter formatter = new DataFormatter();
//			int totalRows = sheet.getLastRowNum();
//			if (totalRows < 1)
//				throw new IllegalArgumentException("No data rows found in Excel.");
//
//			// 1️⃣ Collect employee codes
//			Set<String> empCodes = new HashSet<>();
//			for (int i = 1; i <= totalRows; i++) {
//				Row row = sheet.getRow(i);
//				if (row == null)
//					continue;
//				String empCode = formatter.formatCellValue(row.getCell(0));
//				if (empCode != null && !empCode.trim().isEmpty())
//					empCodes.add(empCode.trim());
//			}
//
//			// 2️⃣ Fetch employees in batch
//			Map<String, EmployeeVO> employeeMap = employeeRepo.findByEmployeeCodeInAndOrgId(empCodes, orgId).stream()
//					.collect(Collectors.toMap(EmployeeVO::getEmployeeCode, e -> e));
//
//			// 3️⃣ Parse rows (multi-threaded)
//			int numThreads = (totalRows <= 100) ? 10 : Math.min(20, totalRows / 100);
//			ExecutorService executor = Executors.newFixedThreadPool(numThreads);
//			int chunkSize = (int) Math.ceil((double) totalRows / numThreads);
//			List<Future<?>> futures = new ArrayList<>();
//
//			for (int start = 1; start <= totalRows; start += chunkSize) {
//				final int finalStart = start;
//				final int finalEnd = Math.min(start + chunkSize - 1, totalRows);
//				futures.add(executor.submit(() -> {
//					for (int i = finalStart; i <= finalEnd; i++) {
//						Row row = sheet.getRow(i);
//						if (row == null)
//							continue;
//						try {
//							String empCode = formatter.formatCellValue(row.getCell(0));
//							if (empCode == null || empCode.trim().isEmpty())
//								throw new IllegalArgumentException("Employee Code is missing");
//
//							EmployeeVO employeeVO = employeeMap.get(empCode);
//							if (employeeVO == null)
//								throw new IllegalArgumentException("Employee not found: " + empCode);
//
//							String empName = employeeVO.getEmployeeName();
//							String branch = employeeVO.getBranch();
//							String branchCode = employeeVO.getBranchCode();
//
//							LocalDate checkInDate = getDateCell(row, 2, formatter);
//							LocalTime inTime = getTimeCell(row, 4, formatter);
//							LocalTime outTime = getTimeCell(row, 5, formatter);
//
//							List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo.findApplicableShifts(empCode,
//									checkInDate, orgId);
//							ShiftAssignDetailsVO latestShift = shifts.stream()
//									.max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom)).orElse(null);
//							boolean isNightShift = latestShift != null
//									&& "NIGHT".equalsIgnoreCase(latestShift.getShiftType());
//
//							boolean isOpenShift = latestShift != null
//									&& "OPEN".equalsIgnoreCase(latestShift.getShiftType());
//
//							// IN
//							if (inTime != null) {
//								CheckInOutUploadVO vo = new CheckInOutUploadVO();
//								vo.setEmpcode(empCode);
//								vo.setEmpname(empName);
//								vo.setOrgId(orgId);
//								vo.setCheckInDate(checkInDate);
//								vo.setBranch(branch);
//								vo.setBranchCode(branchCode);
//								vo.setEntryTime(inTime);
//								vo.setStatus("In");
//								vo.setCreatedBy(createdBy);
//								vo.setFinYear(String.valueOf(checkInDate.getYear()));
//								validList.add(vo);
//								successCount.incrementAndGet();
//							}
//
//							// OUT
//							if (outTime != null) {
//								LocalDate outDate = checkInDate;
//
//								if (isOpenShift && inTime != null) {
//									// 🔹 OpenShift employee → Decide night/day dynamically
//									if (inTime.isAfter(outTime)) {
//										// IN > OUT → treat as night shift → next day
//										outDate = checkInDate.plusDays(1);
//									}
//								} else if (isNightShift && inTime != null && outTime.isBefore(inTime)) {
//									// 🔹 Fixed Night shift
//									outDate = checkInDate.plusDays(1);
//								}
//
//								CheckInOutUploadVO vo = new CheckInOutUploadVO();
//								vo.setEmpcode(empCode);
//								vo.setEmpname(empName);
//								vo.setOrgId(orgId);
//								vo.setCheckInDate(outDate);
//								vo.setBranch(branch);
//								vo.setBranchCode(branchCode);
//								vo.setEntryTime(outTime);
//								vo.setStatus("Out");
//								vo.setCreatedBy(createdBy);
//								vo.setFinYear(String.valueOf(outDate.getYear()));
//								validList.add(vo);
//								successCount.incrementAndGet();
//							}
//
//						} catch (Exception e) {
//							Map<String, Object> error = new HashMap<>();
//							error.put("row", i + 1);
//							String empCode = formatter.formatCellValue(row.getCell(0));
//							EmployeeVO emp = (empCode != null) ? employeeMap.get(empCode) : null;
//							error.put("empName", (emp != null) ? emp.getEmployeeName() : "");
//							error.put("error", e.getMessage());
//							failures.add(error);
//						}
//					}
//				}));
//			}
//
//			for (Future<?> f : futures)
//				f.get();
//			executor.shutdown();
//
//			Map<String, Object> result = new LinkedHashMap<>();
//			result.put("successCount", successCount.get());
//			result.put("failedCount", failures.size());
//			result.put("failures", failures);
//			if (!failures.isEmpty()) {
//				result.put("message", "Upload failed due to errors. No records saved.");
//				return new ObjectMapper().writeValueAsString(result);
//			}
//
//			// ✅ Save CheckInOutUpload in bulk
//			List<CheckInOutUploadVO> savedUploads = checkInOutUploadRepo.saveAll(validList);
//
//			// ✅ Collect IDs for this upload
//			List<Long> currentUploadIds = savedUploads.stream().map(CheckInOutUploadVO::getId)
//					.collect(Collectors.toList());
//
//			// Step 1: Fetch current sequence value
//			BigInteger currentSeq = (BigInteger) entityManager
//					.createNativeQuery("SELECT next_val FROM attendanceprocessseq").getSingleResult();
//
//			// Step 2: Insert into attendanceprocess for current upload
//			entityManager.createNativeQuery("INSERT INTO attendanceprocess ("
//					+ "attendanceprocessid, empcode, empname, orgid, branchcode, branch, finyear, "
//					+ "checkindate, entrytime, status, sourceid, attendancemode, createdby,createdon,modifiedon,screencode, screenname"
//					+ ") " + "SELECT " + "(?1 + ROW_NUMBER() OVER (ORDER BY checkinoutuploadid)) AS new_id, "
//					+ "empcode, empname, orgid, branchcode, branch, finyear, "
//					+ "checkindate, entrytime, status, checkinoutuploadid, attendancemode,createdby,createdon,modifiedon, 'AM', 'ATTENDANCE MODE' "
//					+ "FROM checkinoutupload " + "WHERE checkinoutuploadid IN (?2)").setParameter(1, currentSeq)
//					.setParameter(2, currentUploadIds).executeUpdate();
//
//			// Step 3: Update sequence table
//			entityManager
//					.createNativeQuery("UPDATE attendanceprocessseq " + "SET next_val = next_val + ("
//							+ "SELECT COUNT(*) FROM checkinoutupload WHERE checkinoutuploadid IN (:ids)" + ")")
//					.setParameter("ids", currentUploadIds).executeUpdate();
//
//			// ✅ Calculate daily attendance
//			// ✅ Fetch attendance rows inserted for this upload
//			List<AttendanceProcessVO> attendanceList = attendanceProcessRepo.findBySourceIdIn(currentUploadIds);
//
//			// Group only by employee (do NOT include checkInDate here)
//			Map<String, List<AttendanceProcessVO>> groupedByEmp = attendanceList.stream()
//					.collect(Collectors.groupingBy(a -> a.getEmpCode() + "_" + a.getOrgId() + "_" + a.getBranchCode()));
//
//			Queue<AttendanceDailyVO> dailyListQueue = new ConcurrentLinkedQueue<>();
//
//			// Keep same thread-count as earlier
//			ExecutorService dailyExecutor = Executors.newFixedThreadPool(numThreads);
//			List<Future<?>> dailyFutures = new ArrayList<>();
//
//			for (List<AttendanceProcessVO> empRecords : groupedByEmp.values()) {
//				dailyFutures.add(dailyExecutor.submit(() -> {
//					// Sort chronologically by checkInDate then entryTime
//					List<AttendanceProcessVO> recs = empRecords.stream()
//							.sorted(Comparator.comparing(AttendanceProcessVO::getCheckInDate)
//									.thenComparing(AttendanceProcessVO::getEntryTime))
//							.collect(Collectors.toList());
//
//					// FIFO queue for unmatched INs
//					Deque<AttendanceProcessVO> inQueue = new ArrayDeque<>();
//
//					for (AttendanceProcessVO rec : recs) {
//						String status = rec.getStatus() == null ? "" : rec.getStatus().trim();
//
//						if ("In".equalsIgnoreCase(status)) {
//							// Add to IN queue to be paired with a future OUT
//							inQueue.addLast(rec);
//							continue;
//						}
//
//						if ("Out".equalsIgnoreCase(status)) {
//							// Pair with earliest unmatched IN (FIFO)
//							if (inQueue.isEmpty()) {
//								// No unmatched IN found. Skip this OUT (or optionally log it to failures).
//								continue;
//							}
//
//							AttendanceProcessVO firstIn = inQueue.removeFirst();
//							AttendanceProcessVO lastOut = rec;
//
//							// Build full datetimes
//							LocalDateTime inDT = LocalDateTime.of(firstIn.getCheckInDate(), firstIn.getEntryTime());
//							LocalDateTime outDT = LocalDateTime.of(lastOut.getCheckInDate(), lastOut.getEntryTime());
//
//							// If OUT is before IN -> night shift => move only the OUT datetime forward by 1
//							// day
//							if (outDT.isBefore(inDT)) {
//								outDT = outDT.plusDays(1);
//							}
//
//							long grossSeconds = Duration.between(inDT, outDT).getSeconds();
//							int hours = (int) (grossSeconds / 3600);
//							int minutes = (int) ((grossSeconds % 3600) / 60);
//
//							AttendanceDailyVO ad = new AttendanceDailyVO();
//							ad.setEmpCode(firstIn.getEmpCode());
//							ad.setEmpName(firstIn.getEmpName());
//							ad.setOrgId(firstIn.getOrgId());
//							ad.setBranch(firstIn.getBranch());
//							ad.setBranchCode(firstIn.getBranchCode());
//
//							// ✅ Attendance day ALWAYS = IN date (firstIn)
//							ad.setCheckInDate(firstIn.getCheckInDate());
//
//							// ✅ Checkout date = outDT date (may be +1 day)
//							ad.setCheckOutDate(outDT.toLocalDate());
//
//							ad.setFinyear(firstIn.getFinyear());
//							ad.setAttendanceMode("FILES");
//
//							ad.setInTime(inDT.toLocalTime());
//							ad.setOutTime(outDT.toLocalTime());
//
//							// If you want to keep hours as integer hours (same as before):
//							ad.setEffectiveHours(hours);
//							ad.setGrossHours(hours);
//
//							// If you prefer fractional hours, compute: double fractional = hours +
//							// minutes/60.0
//							// and store appropriately (requires VO fields capable of double).
//
//							ad.setCreatedBy(createdBy);
//							dailyListQueue.add(ad);
//
//						}
//					}
//				}));
//			}
//
//			for (Future<?> f : dailyFutures)
//				f.get();
//			dailyExecutor.shutdown();
//
//			attendanceDailyRepo.saveAll(dailyListQueue);
//
//			result.put("message", "Attendance Uploaded successfully");
//			return new ObjectMapper().writeValueAsString(result);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public String checkInOutUploadExcel(MultipartFile file, Long orgId, String createdBy) throws Exception {

		Queue<CheckInOutUploadVO> validList = new ConcurrentLinkedQueue<>();
		Queue<Map<String, Object>> failures = new ConcurrentLinkedQueue<>();
		AtomicInteger successCount = new AtomicInteger(0);

		try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {

			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int totalRows = sheet.getLastRowNum();
			if (totalRows < 1)
				throw new IllegalArgumentException("No data rows found in Excel.");

			// 1️⃣ Collect employee codes
			Set<String> empCodes = new HashSet<>();
			for (int i = 1; i <= totalRows; i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;
				String empCode = formatter.formatCellValue(row.getCell(0));
				if (empCode != null && !empCode.trim().isEmpty())
					empCodes.add(empCode.trim());
			}

			// 2️⃣ Fetch employees in batch
			Map<String, EmployeeVO> employeeMap = employeeRepo.findByEmployeeCodeInAndOrgId(empCodes, orgId).stream()
					.collect(Collectors.toMap(EmployeeVO::getEmployeeCode, e -> e));

			// 3️⃣ Parse rows (multi-threaded)
			int numThreads = (totalRows <= 100) ? 10 : Math.min(20, totalRows / 100);
			ExecutorService executor = Executors.newFixedThreadPool(numThreads);
			int chunkSize = (int) Math.ceil((double) totalRows / numThreads);
			List<Future<?>> futures = new ArrayList<>();

			for (int start = 1; start <= totalRows; start += chunkSize) {
				final int finalStart = start;
				final int finalEnd = Math.min(start + chunkSize - 1, totalRows);
				futures.add(executor.submit(() -> {
					for (int i = finalStart; i <= finalEnd; i++) {
						Row row = sheet.getRow(i);
						if (row == null)
							continue;
						try {
							String empCode = formatter.formatCellValue(row.getCell(0));
							if (empCode == null || empCode.trim().isEmpty())
								throw new IllegalArgumentException("Employee Code is missing");

							EmployeeVO employeeVO = employeeMap.get(empCode);
							if (employeeVO == null)
								throw new IllegalArgumentException("Employee not found: " + empCode);

							String empName = employeeVO.getEmployeeName();
							String branch = employeeVO.getBranch();
							String branchCode = employeeVO.getBranchCode();

							LocalDate checkInDate = getDateCell(row, 2, formatter);
							LocalTime inTime = getTimeCell(row, 4, formatter);
							LocalTime outTime = getTimeCell(row, 5, formatter);

							List<ShiftAssignDetailsVO> shifts = shiftAssignDetailsRepo.findApplicableShifts(empCode,
									checkInDate, orgId);
							ShiftAssignDetailsVO latestShift = shifts.stream()
									.max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom)).orElse(null);
							boolean isNightShift = latestShift != null
									&& "NIGHT".equalsIgnoreCase(latestShift.getShiftType());

							boolean isOpenShift = latestShift != null
									&& "OPEN".equalsIgnoreCase(latestShift.getShiftType());

							// Dynamic night shift detection
							boolean dynamicNight = (inTime != null && outTime != null && inTime.isAfter(outTime));

							// IN
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
								successCount.incrementAndGet();
							}

							// OUT
							if (outTime != null) {
								LocalDate outDate = checkInDate;

								if (isOpenShift && inTime != null) {
									if (inTime.isAfter(outTime)) {
										outDate = checkInDate.plusDays(1);
									}
								} else if (isNightShift && inTime != null && outTime.isBefore(inTime)) {
									outDate = checkInDate.plusDays(1);
								} else if (dynamicNight) {
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
								successCount.incrementAndGet();
							}

						} catch (Exception e) {
							Map<String, Object> error = new HashMap<>();
							error.put("row", i + 1);
							String empCode = formatter.formatCellValue(row.getCell(0));
							EmployeeVO emp = (empCode != null) ? employeeMap.get(empCode) : null;
							error.put("empName", (emp != null) ? emp.getEmployeeName() : "");
							error.put("error", e.getMessage());
							failures.add(error);
						}
					}
				}));
			}

			for (Future<?> f : futures)
				f.get();
			executor.shutdown();

			Map<String, Object> result = new LinkedHashMap<>();
			result.put("successCount", successCount.get());
			result.put("failedCount", failures.size());
			result.put("failures", failures);
			if (!failures.isEmpty()) {
				result.put("message", "Upload failed due to errors. No records saved.");
				return new ObjectMapper().writeValueAsString(result);
			}

			// Save CheckInOutUpload in bulk
			List<CheckInOutUploadVO> savedUploads = checkInOutUploadRepo.saveAll(validList);

			List<Long> currentUploadIds = savedUploads.stream().map(CheckInOutUploadVO::getId)
					.collect(Collectors.toList());

			// Fetch current sequence value
			BigInteger currentSeq = (BigInteger) entityManager
					.createNativeQuery("SELECT next_val FROM attendanceprocessseq").getSingleResult();

			// Insert into attendanceprocess
			entityManager.createNativeQuery("INSERT INTO attendanceprocess ("
					+ "attendanceprocessid, empcode, empname, orgid, branchcode, branch, finyear, "
					+ "checkindate, entrytime, status, sourceid, attendancemode, createdby,createdon,modifiedon,screencode, screenname"
					+ ") " + "SELECT (?1 + ROW_NUMBER() OVER (ORDER BY checkinoutuploadid)) AS new_id, "
					+ "empcode, empname, orgid, branchcode, branch, finyear, "
					+ "checkindate, entrytime, status, checkinoutuploadid, attendancemode,createdby,createdon,modifiedon, 'AM', 'ATTENDANCE MODE' "
					+ "FROM checkinoutupload WHERE checkinoutuploadid IN (?2)").setParameter(1, currentSeq)
					.setParameter(2, currentUploadIds).executeUpdate();

			// Update sequence
			entityManager
					.createNativeQuery("UPDATE attendanceprocessseq SET next_val = next_val + ("
							+ "SELECT COUNT(*) FROM checkinoutupload WHERE checkinoutuploadid IN (:ids))")
					.setParameter("ids", currentUploadIds).executeUpdate();

			// Calculate daily attendance (merged IN/OUT)
			// ------------------ Calculate daily attendance (merged IN/OUT)
			// ------------------
			// Fetch AttendanceProcess records for current uploads
			// ------------------ build daily attendance (per employee, per work-day)
			// ------------------
			List<AttendanceProcessVO> attendanceList = attendanceProcessRepo.findBySourceIdIn(currentUploadIds);

			// Group by employee + org + branch
			Map<String, List<AttendanceProcessVO>> groupedByEmp = attendanceList.stream()
					.collect(Collectors.groupingBy(a -> a.getEmpCode() + "_" + a.getOrgId() + "_" + a.getBranchCode()));

			Queue<AttendanceDailyVO> dailyListQueue = new ConcurrentLinkedQueue<>();
			ExecutorService dailyExecutor = Executors.newFixedThreadPool(numThreads);
			List<Future<?>> dailyFutures = new ArrayList<>();

			for (List<AttendanceProcessVO> empRecords : groupedByEmp.values()) {
				dailyFutures.add(dailyExecutor.submit(() -> {
					// Sort all records for this employee by date & time
					List<AttendanceProcessVO> recs = empRecords.stream()
							.sorted(Comparator.comparing(AttendanceProcessVO::getCheckInDate)
									.thenComparing(AttendanceProcessVO::getEntryTime))
							.collect(Collectors.toList());

					// Group records by "work day" so night-shift OUTs map to the IN's date
					Map<LocalDate, List<AttendanceProcessVO>> recordsByDay = new LinkedHashMap<>();
					for (int i = 0; i < recs.size(); i++) {
						AttendanceProcessVO rec = recs.get(i);
						LocalDate workDate = rec.getCheckInDate();

						// If this is an OUT and looks like a night-out (time before previous IN),
						// assign it to previous IN date
						if ("Out".equalsIgnoreCase(rec.getStatus()) && i > 0) {
							AttendanceProcessVO prev = recs.get(i - 1);
							if ("In".equalsIgnoreCase(prev.getStatus())
									&& rec.getEntryTime().isBefore(prev.getEntryTime())) {
								workDate = prev.getCheckInDate();
							}
						}

						recordsByDay.computeIfAbsent(workDate, k -> new ArrayList<>()).add(rec);
					}

					// Process each day's records
					for (Map.Entry<LocalDate, List<AttendanceProcessVO>> entry : recordsByDay.entrySet()) {
						LocalDate workDate = entry.getKey();
						List<AttendanceProcessVO> dayRecords = entry.getValue();
						dayRecords.sort(Comparator.comparing(AttendanceProcessVO::getCheckInDate)
								.thenComparing(AttendanceProcessVO::getEntryTime));

						Deque<AttendanceProcessVO> inQueue = new ArrayDeque<>();
						LocalDateTime mergedIn = null;
						LocalDateTime mergedOut = null;
						int sumSessionHours = 0; // effective hours (sum of each IN->OUT)
						// iterate and pair IN/OUT
						for (AttendanceProcessVO rec : dayRecords) {
							String status = rec.getStatus() == null ? "" : rec.getStatus().trim();

							if ("In".equalsIgnoreCase(status)) {
								if (mergedIn == null) {
									mergedIn = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
								}
								inQueue.addLast(rec);
								continue;
							}

							if ("Out".equalsIgnoreCase(status) && !inQueue.isEmpty()) {
								AttendanceProcessVO firstIn = inQueue.removeFirst();
								LocalDateTime inDT = LocalDateTime.of(firstIn.getCheckInDate(), firstIn.getEntryTime());
								LocalDateTime outDT = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());

								// if out is before in, it's next day -> add 1 day
								if (outDT.isBefore(inDT))
									outDT = outDT.plusDays(1);

								// extend merged span
								if (mergedIn == null || inDT.isBefore(mergedIn))
									mergedIn = inDT;
								if (mergedOut == null || outDT.isAfter(mergedOut))
									mergedOut = outDT;

								// accumulate effective (session) hours
								sumSessionHours += (int) Duration.between(inDT, outDT).toHours();
							}
						}

						// only create a daily row if we got at least one IN/OUT pair
						if (mergedIn != null && mergedOut != null) {
							AttendanceDailyVO ad = new AttendanceDailyVO();
							AttendanceProcessVO first = dayRecords.get(0);

							// gross = span from first IN to last OUT (this is the change you requested)
							int grossSpanHours = (int) Duration.between(mergedIn, mergedOut).toHours();

							ad.setEmpCode(first.getEmpCode());
							ad.setEmpName(first.getEmpName());
							ad.setOrgId(first.getOrgId());
							ad.setBranch(first.getBranch());
							ad.setBranchCode(first.getBranchCode());
							ad.setCheckInDate(mergedIn.toLocalDate());
							ad.setCheckOutDate(mergedOut.toLocalDate());
							ad.setFinyear(first.getFinyear());
							ad.setAttendanceMode("FILES");
							ad.setInTime(mergedIn.toLocalTime());
							ad.setOutTime(mergedOut.toLocalTime());

							// IMPORTANT:
							// - grossHours = first IN -> last OUT (span)
							// - effectiveHours = sum of actual session hours (IN->OUT pairs)
							ad.setGrossHours(grossSpanHours);
							ad.setEffectiveHours(sumSessionHours);

							ad.setCreatedBy(createdBy);

							dailyListQueue.add(ad);
						}
					}
				}));
			}

			// wait for threads and save
			for (Future<?> f : dailyFutures)
				f.get();
			dailyExecutor.shutdown();

			attendanceDailyRepo.saveAll(dailyListQueue);

			result.put("message", "Attendance Uploaded successfully");
			return new ObjectMapper().writeValueAsString(result);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

//	/* ✅ Helpers rewritten with DataFormatter */
	private LocalDate getDateCell(Row row, int colIndex, DataFormatter formatter) {
		Cell cell = row.getCell(colIndex);
		if (cell == null)
			throw new IllegalArgumentException("Check-in date is required");

		if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}

		String raw = formatter.formatCellValue(cell).trim();
		if (raw.isEmpty())
			throw new IllegalArgumentException("Check-in date is empty");

		try {
			return LocalDate.parse(raw, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (Exception ignored) {
		}
		try {
			return LocalDate.parse(raw, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (Exception ignored) {
		}
		throw new IllegalArgumentException("Invalid date format: " + raw);
	}

	private LocalTime getTimeCell(Row row, int colIndex, DataFormatter formatter) {
		Cell cell = row.getCell(colIndex);
		if (cell == null)
			return null;

		if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			return cell.getLocalDateTimeCellValue().toLocalTime();
		}

		String raw = formatter.formatCellValue(cell).trim();
		if (raw.isEmpty())
			return null;

		try {
			return LocalTime.parse(raw, DateTimeFormatter.ofPattern("HH:mm"));
		} catch (Exception ignored) {
		}
		try {
			return LocalTime.parse(raw, DateTimeFormatter.ofPattern("HH:mm:ss"));
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
			map.put("bankOtAmounts", record[16] != null ? record[16].toString() : "00:00");
			map.put("cashOtAmounts", record[17] != null ? record[17].toString() : "00:00");
			map.put("sunday", record[18] != null ? record[18].toString() : "0");

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

//	@Override
//	public List<OtCalculationVO> generateOtAndSave(Long orgId) {
//		List<Object[]> rows = otCalculationRepo.getFinalOtRecords(orgId);
//		List<OtCalculationVO> resultList = new ArrayList<>();
//		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//		for (Object[] row : rows) {
//			try {
//				String empcode = (String) row[0];
//				String empname = (String) row[1];
//				LocalDate checkindate = ((Date) row[2]).toLocalDate();
//				LocalTime intime = LocalTime.parse((String) row[3], timeFormatter);
//				LocalTime outtime = LocalTime.parse((String) row[4], timeFormatter);
//				Integer othours = row[5] != null ? Integer.parseInt(row[5].toString().trim()) : 0;
//				BigDecimal otamount = safeBigDecimal(row[6]);
//				BigDecimal rate = safeBigDecimal(row[7]);
//				String ottype = row[8] != null ? row[8].toString().trim() : null;
//				String otcategory = row[9] != null ? row[9].toString().trim() : null;
//				String companyOtPolicy = row[10] != null ? row[10].toString().trim() : null;
//
//				// Try exact match with empcode + checkindate + intime + outtime
//				Optional<OtCalculationVO> exactMatchOpt = otCalculationRepo
//						.findByEmpcodeAndCheckindateAndIntimeAndOuttime(empcode, checkindate, intime, outtime);
//
//				OtCalculationVO vo;
//
//				if (exactMatchOpt.isPresent()) {
//					vo = exactMatchOpt.get();
//					if (!isDifferent(vo, othours, otamount, rate, ottype, otcategory, companyOtPolicy)) {
//						continue; // All values same, skip
//					}
//					// Else update the fields
//				} else {
//					// Check if record exists with empcode + checkindate only (even if in/out
//					// changed)
//					Optional<OtCalculationVO> partialMatch = otCalculationRepo.findByEmpcodeAndCheckindate(empcode,
//							checkindate);
//					if (partialMatch.isPresent()) {
//						vo = partialMatch.get(); // Update existing
//					} else {
//						vo = new OtCalculationVO(); // Create new
//						vo.setCreatedon(LocalDateTime.now());
//					}
//				}
//
//				// Create or update values
//				vo.setEmpcode(empcode);
//				vo.setEmpname(empname);
//				vo.setCheckindate(checkindate);
//				vo.setIntime(intime);
//				vo.setOuttime(outtime);
//				vo.setOthours(othours);
//				vo.setOtamount(otamount);
//				vo.setRate(rate);
//				vo.setOttype(ottype);
//				vo.setOtcategory(otcategory);
//				vo.setCompanyOtPolicy(companyOtPolicy);
//				vo.setOrgId(orgId);
//				vo.setStatus("PENDING");
//
//				resultList.add(vo);
//
//			} catch (Exception e) {
//				System.err.println("Error processing OT row: " + Arrays.toString(row));
//				e.printStackTrace();
//			}
//		}
//
//		return otCalculationRepo.saveAll(resultList);
//	}
//
//	private boolean isDifferent(OtCalculationVO vo, Integer othours, BigDecimal otamount, BigDecimal rate,
//			String ottype, String otcategory, String companyOtPolicy) {
//
//		return !Objects.equals(vo.getOthours(), othours) || !Objects.equals(vo.getOtamount(), otamount)
//				|| !Objects.equals(vo.getRate(), rate) || !Objects.equals(vo.getOttype(), ottype)
//				|| !Objects.equals(vo.getOtcategory(), otcategory)
//				|| !Objects.equals(vo.getCompanyOtPolicy(), companyOtPolicy);
//	}
//
//	private BigDecimal safeBigDecimal(Object obj) {
//		try {
//			return obj != null ? new BigDecimal(obj.toString().trim()) : BigDecimal.ZERO;
//		} catch (NumberFormatException e) {
//			System.err.println("Invalid BigDecimal input: " + obj);
//			return BigDecimal.ZERO;
//		}
//	}
	
	
	
	@Override
	public List<OtCalculationVO> generateOtAndSave(Long orgId) {
	    List<Object[]> rows = otCalculationRepo.getFinalOtRecords(orgId); // SQL must return bankOtAmount and cashOtAmount separately
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

	            // Correctly map bank and cash OT amounts
	            BigDecimal bankOtAmount = safeBigDecimal(row[6]);
	            BigDecimal cashOtAmount = safeBigDecimal(row[7]);

	            BigDecimal rate = safeBigDecimal(row[8]);
	            String ottype = row[9] != null ? row[9].toString().trim() : null;
	            String otcategory = row[10] != null ? row[10].toString().trim() : null;
	            String companyOtPolicy = row[11] != null ? row[11].toString().trim() : null;

	            Optional<OtCalculationVO> exactMatchOpt = otCalculationRepo
	                    .findByEmpcodeAndCheckindateAndIntimeAndOuttime(empcode, checkindate, intime, outtime);

	            OtCalculationVO vo;

	            if (exactMatchOpt.isPresent()) {
	                vo = exactMatchOpt.get();
	                if (!isDifferent(vo, othours, bankOtAmount, cashOtAmount, rate, ottype, otcategory, companyOtPolicy)) {
	                    continue; // No change, skip
	                }
	            } else {
	                Optional<OtCalculationVO> partialMatch = otCalculationRepo
	                        .findByEmpcodeAndCheckindate(empcode, checkindate);
	                vo = partialMatch.orElseGet(() -> {
	                    OtCalculationVO newVo = new OtCalculationVO();
	                    newVo.setCreatedon(LocalDateTime.now());
	                    return newVo;
	                });
	            }

	            // Set all fields
	            vo.setEmpcode(empcode);
	            vo.setEmpname(empname);
	            vo.setCheckindate(checkindate);
	            vo.setIntime(intime);
	            vo.setOuttime(outtime);
	            vo.setOthours(othours);
	            vo.setBankOtAmount(bankOtAmount);
	            vo.setCashOtAmount(cashOtAmount);
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

	private boolean isDifferent(OtCalculationVO vo, Integer othours, BigDecimal bankOtAmount, BigDecimal cashOtAmount,
	        BigDecimal rate, String ottype, String otcategory, String companyOtPolicy) {

	    return !Objects.equals(vo.getOthours(), othours)
	            || !Objects.equals(vo.getBankOtAmount(), bankOtAmount)
	            || !Objects.equals(vo.getCashOtAmount(), cashOtAmount)
	            || !Objects.equals(vo.getRate(), rate)
	            || !Objects.equals(vo.getOttype(), ottype)
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
			orderedMap.put("workStatus", row.get("work_status"));
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
		vo.setBankOtAmount(dto.getBankOtAmount());
		vo.setCashOtAmount(dto.getCashOtAmount());
		vo.setSunday(dto.getSunday());
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

	@Override
	public Map<String, Object> createApprovalOtCalculation(Long orgId, List<Long> ids, String action, String actionBy)
			throws ApplicationException {
		List<OtCalculationVO> updatedList = new ArrayList<>();
		String message = "";

		for (Long id : ids) {
			OtCalculationVO otCalculationVO = otCalculationRepo.findById(id)
					.orElseThrow(() -> new ApplicationException("Invalid otCalculation ID: " + id));

			String currentStatus = otCalculationVO.getStatus();

			if (currentStatus == null
					|| (!currentStatus.equalsIgnoreCase("APPROVED") && !currentStatus.equalsIgnoreCase("REJECTED"))) {

				if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {
					otCalculationVO.setStatus(action.toUpperCase());
					otCalculationVO.setApproveBy(actionBy);

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
					otCalculationVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

					updatedList.add(otCalculationVO);
				}
			} else if ("APPROVED".equalsIgnoreCase(currentStatus)) {
				throw new ApplicationException("OtCalculation already approved for employee: ");
			} else if ("REJECTED".equalsIgnoreCase(currentStatus)) {
				throw new ApplicationException("OtCalculation already rejected for employee: ");
			}
		}

		otCalculationRepo.saveAll(updatedList);

		if ("APPROVED".equalsIgnoreCase(action)) {
			message = "OtCalculation Approved Successfully";
		} else if ("REJECTED".equalsIgnoreCase(action)) {
			message = "OtCalculation Rejected Successfully";
		}

		Map<String, Object> response = new HashMap<>();
		response.put("otCalculationVO", updatedList);
		response.put("message", message);
		return response;
	}

	@Override
	public List<Map<String, Object>> getEmployeeNameForApprovalOtProcess(Long orgId, String branch, String department,
			String type, String contractor) {
		List<Object[]> rawList = otMasterRepo.getEmployeeNameForApprovalOtProcess(orgId, branch, department, type,
				contractor); // change to Object[]
		return mapLeaveDetails(rawList);
	}

	private List<Map<String, Object>> mapLeaveDetails(List<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeName", record[0] != null ? record[0].toString() : "");
			map.put("employeeCode", record[1] != null ? record[1].toString() : "");
			detailsList.add(map);
		}
		return detailsList;
	}

	@Override
	public List<Map<String, Object>> getEmployeeShiftHoursForMonthlyReport(String empCode, Integer month,
			String finYear, Long orgId, String branchCode) {
		List<Object[]> rawList = shiftMasterRepo.getEmployeeShiftHoursForMonthlyReport(empCode, month, finYear, orgId,
				branchCode); // change to Object[]
		return getEmployeeShiftHoursForMonthlyReport(rawList);
	}

	private List<Map<String, Object>> getEmployeeShiftHoursForMonthlyReport(List<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeCode", record[0] != null ? record[0].toString() : "");
			map.put("employeeName", record[1] != null ? record[1].toString() : "");
			map.put("shift", record[2] != null ? record[2].toString() : "");
			map.put("hours", record[3] != null ? record[3].toString() : "");

			detailsList.add(map);
		}
		return detailsList;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public String processDeviceLogs(Long orgId, String createdBy) throws Exception {
	    // 1️⃣ Fetch device logs
	    List<DeviceLogVO> deviceLogs = deviceLogRepo.findAll(); // filter by date if needed

	    if (deviceLogs.isEmpty()) {
	        return "{\"message\":\"No device logs found\"}";
	    }

	    List<CheckInOutBiometricVO> validList = new ArrayList<>();
	    List<Map<String, Object>> failures = new ArrayList<>();
	    AtomicInteger successCount = new AtomicInteger();

	    // 2️⃣ Build employee map for validation
	    Set<String> empCodes = deviceLogs.stream()
	            .map(DeviceLogVO::getEmployeeCode)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toSet());

	    Map<String, EmployeeVO> employeeMap = employeeRepo.findByEmployeeCodeInAndOrgId(empCodes, orgId)
	            .stream().collect(Collectors.toMap(EmployeeVO::getEmployeeCode, e -> e));

	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
	    DateTimeFormatter timeFormatterWithSeconds = DateTimeFormatter.ofPattern("HH:mm:ss");
	    DateTimeFormatter timeFormatterNoSeconds = DateTimeFormatter.ofPattern("HH:mm");

	    // 3️⃣ Transform DeviceLog -> CheckInOutBiometricVO
	    for (DeviceLogVO log : deviceLogs) {
	        try {
	            EmployeeVO employeeVO = employeeMap.get(log.getEmployeeCode());
	            if (employeeVO == null) continue;

	            LocalDate checkDate = LocalDate.parse(log.getAttendanceDate(), dateFormatter);

	            // Parse time
	            LocalTime entryTime;
	            try {
	                entryTime = LocalTime.parse(log.getAttendanceTime(), timeFormatterWithSeconds);
	            } catch (DateTimeParseException e) {
	                entryTime = LocalTime.parse(log.getAttendanceTime(), timeFormatterNoSeconds);
	            }

	            CheckInOutBiometricVO bio = new CheckInOutBiometricVO();
	            bio.setEmpCode(log.getEmployeeCode());
	            bio.setEmpName(employeeVO.getEmployeeName());
	            bio.setOrgId(orgId);
	            bio.setBranch(employeeVO.getBranch());
	            bio.setBranchCode(employeeVO.getBranchCode());
	            bio.setCheckInDate(checkDate);
	            bio.setEntryTime(entryTime);

	            // ✅ Assign IN/OUT based on direction, default to "In" if only Out exists
	            String direction = log.getDirection();
	            if (!"ADMIN".equalsIgnoreCase(log.getDeviceName())) {
	                // Non-admin device: use log direction
	                bio.setStatus(log.getDirection().equalsIgnoreCase("in") ? "In" : "Out");
	            } else {
	                // Admin device: alternate In/Out
	                String empKey = log.getEmployeeCode() + "_" + checkDate;

	                List<CheckInOutBiometricVO> dailyLogs = validList.stream()
	                        .filter(v -> (v.getEmpCode() + "_" + v.getCheckInDate()).equals(empKey))
	                        .sorted(Comparator.comparing(CheckInOutBiometricVO::getEntryTime))
	                        .collect(Collectors.toList()); // ✅ fixed

	                // First punch = In, second = Out, third = In, etc.
	                bio.setStatus(dailyLogs.size() % 2 == 0 ? "In" : "Out");
	            }

	            bio.setCreatedBy(createdBy);
	            validList.add(bio);

	            successCount.incrementAndGet();
	        } catch (Exception ex) {
	            Map<String, Object> error = new HashMap<>();
	            error.put("logId", log.getDeviceLogId());
	            error.put("empCode", log.getEmployeeCode());
	            error.put("error", ex.getMessage());
	            failures.add(error);
	        }
	    }

	    if (!failures.isEmpty()) {
	        Map<String, Object> result = new HashMap<>();
	        result.put("successCount", successCount.get());
	        result.put("failedCount", failures.size());
	        result.put("failures", failures);
	        return new ObjectMapper().writeValueAsString(result);
	    }

	    // 4️⃣ Save to CheckInOutBiometric
	    List<CheckInOutBiometricVO> savedBiometric = checkInOutBiometricRepo.saveAll(validList);
	    List<Long> currentIds = savedBiometric.stream()
	            .map(CheckInOutBiometricVO::getId)
	            .collect(Collectors.toList());

	    // 5️⃣ Save to AttendanceProcess
	    BigInteger currentSeq = (BigInteger) entityManager
	            .createNativeQuery("SELECT next_val FROM attendanceprocessseq")
	            .getSingleResult();

	    entityManager.createNativeQuery(
	            "INSERT INTO attendanceprocess (" +
	                    "attendanceprocessid, empcode, empname, orgid, branchcode, branch, finyear, " +
	                    "checkindate, entrytime, status, sourceid, attendancemode, createdby, screencode, screenname) " +
	                    "SELECT (?1 + ROW_NUMBER() OVER (ORDER BY checkinoutbiometricid)) AS new_id, " +
	                    "empcode, empname, orgid, branchcode, branch, finyear, " +
	                    "checkindate, entrytime, status, checkinoutbiometricid, 'DEVICE', createdby, 'AM', 'ATTENDANCE MODE' " +
	                    "FROM checkinoutbiometric WHERE checkinoutbiometricid IN (?2)"
	    ).setParameter(1, currentSeq)
	     .setParameter(2, currentIds)
	     .executeUpdate();

	    // Update sequence
	    entityManager.createNativeQuery(
	            "UPDATE attendanceprocessseq SET next_val = next_val + (" +
	                    "SELECT COUNT(*) FROM checkinoutbiometric WHERE checkinoutbiometricid IN (:ids))"
	    ).setParameter("ids", currentIds)
	     .executeUpdate();

	    // 6️⃣ Build AttendanceDaily (merge IN/OUT with all old validations)
	    List<AttendanceProcessVO> attendanceList = attendanceProcessRepo.findBySourceIdIn(currentIds);

	    Map<String, List<AttendanceProcessVO>> groupedByEmp = attendanceList.stream()
	            .collect(Collectors.groupingBy(a -> a.getEmpCode() + "_" + a.getCheckInDate()));

	    List<AttendanceDailyVO> dailyList = new ArrayList<>();

	    for (List<AttendanceProcessVO> recs : groupedByEmp.values()) {
	        recs.sort(Comparator.comparing(AttendanceProcessVO::getEntryTime));

	        LocalDateTime in = null;
	        LocalDateTime out = null;
	        int effectiveHours = 0;

	        // ✅ Keep ADMIN device rule
	        boolean isAdminDevice = recs.stream().anyMatch(r -> {
	            DeviceLogVO log = deviceLogRepo.findById(String.valueOf(r.getSourceId())).orElse(null);
	            return log != null && "ADMIN".equalsIgnoreCase(log.getDeviceName());
	        });

	        if (isAdminDevice && recs.size() % 2 != 0) {
	            continue; // skip odd punches for ADMIN
	        }

	        // Normal IN/OUT processing (handles Out-only logs)
	        for (int i = 0; i < recs.size(); i += 2) {
	            AttendanceProcessVO recIn = recs.get(i);
	            in = LocalDateTime.of(recIn.getCheckInDate(), recIn.getEntryTime());

	            if (i + 1 < recs.size()) {
	                AttendanceProcessVO recOut = recs.get(i + 1);
	                out = LocalDateTime.of(recOut.getCheckInDate(), recOut.getEntryTime());
	                if (out.isBefore(in)) out = out.plusDays(1);
	            } else {
	                // Skip this IN if OUT is missing
	                continue;
	            }

	            effectiveHours += (int) Duration.between(in, out).toHours();

	            AttendanceDailyVO ad = new AttendanceDailyVO();
	            ad.setEmpCode(recIn.getEmpCode());
	            ad.setEmpName(recIn.getEmpName());
	            ad.setOrgId(recIn.getOrgId());
	            ad.setBranch(recIn.getBranch());
	            ad.setBranchCode(recIn.getBranchCode());
	            ad.setCheckInDate(in.toLocalDate());
	            ad.setInTime(in.toLocalTime());
	            ad.setOutTime(out.toLocalTime());
	            ad.setGrossHours((int) Duration.between(in, out).toHours());
	            ad.setEffectiveHours(effectiveHours);
	            ad.setAttendanceMode("DEVICE");
	            ad.setCreatedBy(createdBy);
	            ad.setFinyear(String.valueOf(in.getYear()));

	            dailyList.add(ad);
	        }
	    }

	    attendanceDailyRepo.saveAll(dailyList);

	    Map<String, Object> result = new HashMap<>();
	    result.put("successCount", successCount.get());
	    result.put("message", "Device logs processed successfully");

	    return new ObjectMapper().writeValueAsString(result);
	}
	
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public String uploadAdvanceExcel(MultipartFile file, Long orgId, String createdBy, String branch, String branchCode,Long month,Long year) throws Exception {
	    List<Map<String, Object>> failures = new ArrayList<>();
	    AtomicInteger successCount = new AtomicInteger(0);

	    try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {

	        Sheet sheet = workbook.getSheetAt(0);
	        DataFormatter formatter = new DataFormatter();
	        int totalRows = sheet.getLastRowNum();
	        if (totalRows < 1) {
	            throw new IllegalArgumentException("No data rows found in Excel.");
	        }

	        for (int i = 1; i <= totalRows; i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            try {
	                // Read and validate Excel values
	                String empCode = formatter.formatCellValue(row.getCell(0)).trim();
	                String empName = formatter.formatCellValue(row.getCell(1)).trim();
	                String bankStr = formatter.formatCellValue(row.getCell(2)).trim();
	                String cashStr = formatter.formatCellValue(row.getCell(3)).trim();
//	                String monthStr = formatter.formatCellValue(row.getCell(4)).trim();
//	                String yearStr = formatter.formatCellValue(row.getCell(5)).trim();

	                if (empCode.isEmpty()) throw new IllegalArgumentException("Employee code is empty");
	                if (empName.isEmpty()) throw new IllegalArgumentException("Employee name is empty");

	                BigDecimal bank = bankStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(bankStr);
	                BigDecimal cash = cashStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(cashStr);
//	                Long month = monthStr.isEmpty() ? null : Long.parseLong(monthStr);
//	                Long year = yearStr.isEmpty() ? null : Long.parseLong(yearStr);

	                if (month == null || year == null) throw new IllegalArgumentException("Month or Year is missing");

	                // Check if employee exists
	                EmployeeVO employeeVO = employeeRepo.findByEmployeeCodeAndOrgIdAndBranchCode(empCode, orgId, branchCode);
	                if (employeeVO == null) {
	                    throw new ApplicationException("Employee not Found: " + empCode);
	                }

	                // Check if record already exists for this employee, month & year
	                Optional<AdvanceUploadVO> existingOpt = advanceUploadRepo
	                        .findByEmployeeCodeAndMonthAndYearAndOrgId(empCode, month, year, orgId);

	                AdvanceUploadVO advance;
	                if (existingOpt.isPresent()) {
	                    // Update existing record
	                    advance = existingOpt.get();
	                    advance.setBank(bank);
	                    advance.setCash(cash);
	                    advance.setBranch(branch);
	                    advance.setBranchCode(branchCode);
	                    advance.setCreatedBy(createdBy); // track who updated
	                } else {
	                    // Create new record
	                    advance = new AdvanceUploadVO();
	                    advance.setEmployeeCode(empCode);
	                    advance.setEmployeeName(empName);
	                    advance.setBank(bank);
	                    advance.setCash(cash);
	                    advance.setMonth(month);
	                    advance.setYear(year);
	                    advance.setBranch(branch);
	                    advance.setBranchCode(branchCode);
	                    advance.setOrgId(orgId);
	                    advance.setCreatedBy(createdBy);
	                    advance.setActive(true);
	                }

	                advanceUploadRepo.save(advance);
	                successCount.incrementAndGet();

	            } catch (Exception e) {
	                Map<String, Object> error = new HashMap<>();
	                error.put("row", i + 1);
	                error.put("error", e.getMessage());
	                failures.add(error);
	            }
	        }

	        // Build response JSON
	        Map<String, Object> result = new HashMap<>();
	        result.put("successCount", successCount.get());
	        result.put("failedCount", failures.size());
	        result.put("failures", failures);
	        result.put("message", failures.isEmpty() ? 
	                "Advance data uploaded successfully" : 
	                "Advance data uploaded with some errors");

	        return new ObjectMapper().writeValueAsString(result);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}
	
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public String uploadOtherPaymentsExcel(MultipartFile file, Long orgId, String createdBy, String branch, String branchCode,Long month,Long year) throws Exception {
	    List<Map<String, Object>> failures = new ArrayList<>();
	    AtomicInteger successCount = new AtomicInteger(0);

	    try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {

	        Sheet sheet = workbook.getSheetAt(0);
	        DataFormatter formatter = new DataFormatter();
	        int totalRows = sheet.getLastRowNum();
	        if (totalRows < 1) {
	            throw new IllegalArgumentException("No data rows found in Excel.");
	        }

	        for (int i = 1; i <= totalRows; i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            try {
	                // Read and validate Excel values
	                String empCode = formatter.formatCellValue(row.getCell(0)).trim();
	                String empName = formatter.formatCellValue(row.getCell(1)).trim();
	                String amountStr = formatter.formatCellValue(row.getCell(2)).trim();
	                String allowanceStr = formatter.formatCellValue(row.getCell(3)).trim();


	                if (empCode.isEmpty()) throw new IllegalArgumentException("Employee code is empty");
	                if (empName.isEmpty()) throw new IllegalArgumentException("Employee name is empty");

	                BigDecimal amount = amountStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(amountStr);
	                BigDecimal allowance = amountStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(allowanceStr);
	                
	                if (month == null || year == null) throw new IllegalArgumentException("Month or Year is missing");

	                // Check if employee exists
	                EmployeeVO employeeVO = employeeRepo.findByEmployeeCodeAndOrgIdAndBranchCode(empCode, orgId, branchCode);
	                if (employeeVO == null) {
	                    throw new ApplicationException("Employee not Found: " + empCode);
	                }

	                // Create new record
	                OtherPaymentsVO otherPaymentsVO = new OtherPaymentsVO();
	                otherPaymentsVO.setEmployeeCode(empCode);
	                otherPaymentsVO.setEmployeeName(empName);
	                otherPaymentsVO.setAmount(amount);
	                otherPaymentsVO.setAllowance(allowance);
	                otherPaymentsVO.setBranch(branch);
	                otherPaymentsVO.setBranchCode(branchCode);
	                otherPaymentsVO.setOrgId(orgId);
	                otherPaymentsVO.setCreatedBy(createdBy);
	                otherPaymentsVO.setActive(true);
	                otherPaymentsVO.setMonth(month);   // <- add this
	                otherPaymentsVO.setYear(year);
	                
	                otherPaymentsRepo.save(otherPaymentsVO);
	                successCount.incrementAndGet();

	            } catch (Exception e) {
	                Map<String, Object> error = new HashMap<>();
	                error.put("row", i + 1);
	                error.put("error", e.getMessage());
	                failures.add(error);
	            }
	        }

	        // Build response JSON
	        Map<String, Object> result = new HashMap<>();
	        result.put("successCount", successCount.get());
	        result.put("failedCount", failures.size());
	        result.put("failures", failures);
	        result.put("message", failures.isEmpty() ? 
	                "OtherPayments data uploaded successfully" : 
	                "OtherPayments data uploaded with some errors");

	        return new ObjectMapper().writeValueAsString(result);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}


	
	@Override
	@Transactional
	public Map<String, Object> createUpdateAdvanceExcel(AdvanceUploadDTO dto) throws ApplicationException {
	    Map<String, Object> response = new LinkedHashMap<>();
	    AdvanceUploadVO advance;

	    // Validate mandatory fields
	    if (dto.getEmployeeCode() == null || dto.getEmployeeCode().isEmpty())
	        throw new IllegalArgumentException("Employee code is empty");
	    if (dto.getEmployeeName() == null || dto.getEmployeeName().isEmpty())
	        throw new IllegalArgumentException("Employee name is empty");
	    if (dto.getMonth() == null || dto.getYear() == null)
	        throw new ApplicationException("Month or Year is missing");

	    // Validate employee exists
	    EmployeeVO employeeVO = employeeRepo.findByEmployeeCodeAndOrgIdAndBranchCode(
	            dto.getEmployeeCode(), dto.getOrgId(), dto.getBranchCode());
	    if (employeeVO == null) {
	        throw new ApplicationException("Employee not Found: " + dto.getEmployeeCode());
	    }

	    // 1️⃣ If ID is provided → update by ID
	    if (dto.getId() != null) {
	        advance = advanceUploadRepo.findById(dto.getId())
	                .orElseThrow(() -> new ApplicationException("Error: AdvanceUpload ID " + dto.getId() + " not found!"));
	        response.put("message", "AdvanceUpload Updated Successfully");
	    } else {
	        // 2️⃣ Else → check existing by employeeCode + month + year + orgId
	        Optional<AdvanceUploadVO> existingOpt = advanceUploadRepo
	                .findByEmployeeCodeAndMonthAndYearAndOrgId(
	                        dto.getEmployeeCode(), dto.getMonth(), dto.getYear(), dto.getOrgId());

	        if (existingOpt.isPresent()) {
	            advance = existingOpt.get();
	            response.put("message", "AdvanceUpload Updated Successfully");
	        } else {
	            // New record
	            advance = new AdvanceUploadVO();
	            advance.setCreatedBy(dto.getCreatedBy());
	            advance.setActive(true);
	            response.put("message", "AdvanceUpload Created Successfully");
	        }
	    }

	    // Set/update common fields
	    advance.setEmployeeCode(dto.getEmployeeCode());
	    advance.setEmployeeName(dto.getEmployeeName());
	    advance.setBank(dto.getBank());
	    advance.setCash(dto.getCash());
	    advance.setMonth(dto.getMonth());
	    advance.setYear(dto.getYear());
	    advance.setBranch(dto.getBranch());
	    advance.setBranchCode(dto.getBranchCode());
	    advance.setOrgId(dto.getOrgId());
	    advance.setUpdatedBy(dto.getCreatedBy());

	    // Save record
	    AdvanceUploadVO savedAdvance = advanceUploadRepo.save(advance);

	    // Build response
	    Map<String, Object> paramObjectsMap = new LinkedHashMap<>();
	    paramObjectsMap.put("advanceUploadVO", savedAdvance);
	    response.put("paramObjectsMap", paramObjectsMap);

	    return response;
	}


	
	
	@Override
	@Transactional
	public Map<String, Object> createUpdateOtherPayments(OtherPaymentsDTO dto) throws ApplicationException {
	    Map<String, Object> response = new LinkedHashMap<>();
	    OtherPaymentsVO otherPaymentVO;

	    // Validate mandatory fields
	    if (dto.getEmployeeCode() == null || dto.getEmployeeCode().isEmpty())
	        throw new IllegalArgumentException("Employee code is empty");
	    if (dto.getEmployeeName() == null || dto.getEmployeeName().isEmpty())
	        throw new IllegalArgumentException("Employee name is empty");
	    if (dto.getMonth() == null || dto.getYear() == null)
	        throw new ApplicationException("Month or Year is missing");

	    // Validate employee exists
	    EmployeeVO employeeVO = employeeRepo.findByEmployeeCodeAndOrgIdAndBranchCode(
	            dto.getEmployeeCode(), dto.getOrgId(), dto.getBranchCode());
	    if (employeeVO == null) {
	        throw new ApplicationException("Employee not Found: " + dto.getEmployeeCode());
	    }

	    // 1️⃣ If ID is provided → update by ID
	    if (dto.getId() != null) {
	    	otherPaymentVO = otherPaymentsRepo.findById(dto.getId())
	                .orElseThrow(() -> new ApplicationException("Error: OtherPayment ID " + dto.getId() + " not found!"));
	    	otherPaymentVO.setUpdatedBy(dto.getCreatedBy());
	        response.put("message", "OtherPayment Updated Successfully");
	    } else {
	            // New record
	    	otherPaymentVO = new OtherPaymentsVO();
	    	otherPaymentVO.setCreatedBy(dto.getCreatedBy());
	    	otherPaymentVO.setActive(true);
	            response.put("message", "AdvanceUpload Created Successfully");
	        
	    }

	    // Set/update common fields
	    otherPaymentVO.setEmployeeCode(dto.getEmployeeCode());
	    otherPaymentVO.setEmployeeName(dto.getEmployeeName());
	    otherPaymentVO.setAmount(dto.getAmount());
	    otherPaymentVO.setMonth(dto.getMonth());
	    otherPaymentVO.setYear(dto.getYear());
	    otherPaymentVO.setBranch(dto.getBranch());
	    otherPaymentVO.setBranchCode(dto.getBranchCode());
	    otherPaymentVO.setOrgId(dto.getOrgId());
	    otherPaymentVO.setAllowance(dto.getAllowance());

	    // Save record
	    OtherPaymentsVO savedAdvance = otherPaymentsRepo.save(otherPaymentVO);

	    // Build response
	    Map<String, Object> paramObjectsMap = new LinkedHashMap<>();
	    paramObjectsMap.put("otherPaymentVO", savedAdvance);
	    response.put("paramObjectsMap", paramObjectsMap);

	    return response;
	}
	
	
	@Override
	public List<AdvanceUploadVO> getAllAdvanceUploadByOrgId(Long orgId,Long month,Long year) {
		return advanceUploadRepo.getAllAdvanceUploadByOrgId(orgId,month,year);
	}

	@Override
	public List<OtherPaymentsVO> getAllOtherPaymentsByOrgId(Long orgId,Long month,Long year) {
		return otherPaymentsRepo.getAllOtherPaymentsByOrgId(orgId,month,year);
	}



}
