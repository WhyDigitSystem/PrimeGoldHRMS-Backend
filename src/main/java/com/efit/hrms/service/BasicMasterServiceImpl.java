package com.efit.hrms.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.AnnouncementDTO;
import com.efit.hrms.dto.CalendarDTO;
import com.efit.hrms.dto.CheckInOutAdjustmentDTO;
import com.efit.hrms.dto.CheckinRequestDTO;
import com.efit.hrms.dto.CircularDTO;
import com.efit.hrms.dto.EmployeeCodeConfigDTO;
import com.efit.hrms.dto.EmployeeDTOnew;
import com.efit.hrms.dto.HolidayDTO;
import com.efit.hrms.dto.PollDetailsDTO;
import com.efit.hrms.dto.PollVoteDTO;
import com.efit.hrms.dto.PollsDTO;
import com.efit.hrms.dto.PraiseDTO;
import com.efit.hrms.dto.TaskDTO;
import com.efit.hrms.dto.UserNameDTO;
import com.efit.hrms.entity.AnnouncementVO;
import com.efit.hrms.entity.AttendanceDailyVO;
import com.efit.hrms.entity.AttendanceProcessVO;
import com.efit.hrms.entity.BranchVO;
import com.efit.hrms.entity.CalendarVO;
import com.efit.hrms.entity.CheckInOutAdjustmentVO;
import com.efit.hrms.entity.CheckInStatusVO;
import com.efit.hrms.entity.CheckInVO;
import com.efit.hrms.entity.CircularVO;
import com.efit.hrms.entity.CompanyVO;
import com.efit.hrms.entity.DepartmentVO;
import com.efit.hrms.entity.EmployeeCodeConfigVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.HolidayVO;
import com.efit.hrms.entity.LocationUtils;
import com.efit.hrms.entity.PollDetailsVO;
import com.efit.hrms.entity.PollVoteVO;
import com.efit.hrms.entity.PollsVO;
import com.efit.hrms.entity.PraiseVO;
import com.efit.hrms.entity.SalaryProcessVO;
import com.efit.hrms.entity.ShiftAssignDetailsVO;
import com.efit.hrms.entity.TaskVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.AnnouncementRepo;
import com.efit.hrms.repo.AttendanceDailyRepo;
import com.efit.hrms.repo.AttendanceProcessRepo;
import com.efit.hrms.repo.BranchRepo;
import com.efit.hrms.repo.CalendarRepo;
import com.efit.hrms.repo.CheckInOutAdjustmentRepo;
import com.efit.hrms.repo.CheckInRepo;
import com.efit.hrms.repo.CheckInStatusRepo;
import com.efit.hrms.repo.CircularRepo;
import com.efit.hrms.repo.CompanyRepo;
import com.efit.hrms.repo.DepartmentRepo;
import com.efit.hrms.repo.EmployeeCodeConfigRepo;
import com.efit.hrms.repo.EmployeeRepo;
import com.efit.hrms.repo.HolidayRepo;
import com.efit.hrms.repo.PollDetailsRepo;
import com.efit.hrms.repo.PollVoteRepo;
import com.efit.hrms.repo.PollsRepo;
import com.efit.hrms.repo.PraiseRepo;
import com.efit.hrms.repo.SalaryProcessRepo;
import com.efit.hrms.repo.ShiftAssignDetailsRepo;
import com.efit.hrms.repo.TaskRepo;

@Service
public class BasicMasterServiceImpl implements BasicMasterService {

	@Autowired
	CheckInRepo checkInRepo;

	@Autowired
	CheckInStatusRepo checkInStatusRepo;

	@Autowired
	HolidayRepo holidayRepo;

	@Autowired
	DepartmentRepo departmentRepo;

	@Autowired
	BranchRepo branchRepo;

	@Autowired
	CircularRepo circularRepo;

	@Autowired
	PraiseRepo praiseRepo;

	@Autowired
	TaskRepo taskRepo;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	SalaryProcessRepo salaryProcessRepo;

	@Autowired
	AnnouncementRepo announcementRepo;

	@Autowired
	PollsRepo pollsRepo;

	@Autowired
	PollVoteRepo pollVoteRepo;

	@Autowired
	PollDetailsRepo pollDetailsRepo;

	@Autowired
	CalendarRepo calendarRepo;

	@Autowired
	CheckInOutAdjustmentRepo checkInOutAdjustmentRepo;

	@Autowired
	CompanyRepo companyRepo;

	@Autowired
	EmployeeCodeConfigRepo employeeCodeConfigRepo;

	@Autowired
	AttendanceProcessRepo attendanceProcessRepo;

	@Autowired
	ShiftAssignDetailsRepo shiftAssignDetailsRepo;

	@Autowired
	AttendanceDailyRepo attendanceDailyRepo;

	//DAY,GENERAL,NIGHT SHIFT
//	@Override
//	@Transactional
//	public Map<String, Object> createCheckInOut(UserNameDTO userNameDTO) throws ApplicationException {
//	    Map<String, Object> response = new HashMap<>();
//	    LocalDate today = LocalDate.now();
//	    LocalTime now = LocalTime.now();
//	    boolean allowedTodayCheckin = true;
//	    CheckInVO todayCheck = null;
//
//	    List<CompanyVO> companyList = companyRepo.findByCompany(userNameDTO.getOrgId());
//	    boolean hybridStatus;
//
//	    if (!companyList.isEmpty()) {
//	        CompanyVO company = companyList.get(0);
//	        hybridStatus = company.isHybrid();
//	    } else {
//	        throw new ApplicationException("Company not found for id: " + userNameDTO.getOrgId());
//	    }
//
//	    if (userNameDTO.getLatitude() == null || userNameDTO.getLongitude() == null) {
//	        throw new ApplicationException("Latitude or Longitude cannot be null");
//	    }
//
//	    String workFromHome = hybridStatus ? userNameDTO.getWorkFromHome() : "NO";
//
//	    if ("NO".equals(workFromHome)) {
//	        if (!isWithinCompanyLocation(userNameDTO.getLatitude(), userNameDTO.getLongitude(), userNameDTO.getOrgId())) {
//	            throw new ApplicationException("You are not within the allowed office location. Check-in/out denied");
//	        }
//	    }
//
//	    Optional<CheckInStatusVO> latestStatusOpt = checkInStatusRepo.findTopByEmpcodeAndOrgIdAndBranchOrderByIdDesc(
//	            userNameDTO.getEmpcode(), userNameDTO.getOrgId(), userNameDTO.getBranch());
//
//	    List<ShiftAssignDetailsVO> shiftList = shiftAssignDetailsRepo
//	            .findByEmployeeCodeAndShiftAssignVO_OrgId(userNameDTO.getEmpcode(), userNameDTO.getOrgId());
//
//	    if (shiftList == null || shiftList.isEmpty()) {
//	        throw new ApplicationException("This employee has no assigned shift.");
//	    }
//
//	    ShiftAssignDetailsVO latestShift = shiftList.stream()
//	            .max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom))
//	            .orElseThrow(() -> new ApplicationException("No effective shift assignment found."));
//
//	    String shiftType = latestShift.getShiftType() != null ? latestShift.getShiftType().trim().toUpperCase() : "DAY";
//	    boolean isNightShift = "NIGHT".equals(shiftType);
//	    boolean isOpenShift = "OPEN".equals(shiftType);
//
//	    if (!isNightShift && latestStatusOpt.isPresent()) {
//	        CheckInStatusVO latestStatus = latestStatusOpt.get();
//	        if ("In".equalsIgnoreCase(latestStatus.getStatus())) {
//	            Optional<CheckInVO> lastCheckInOpt = checkInRepo.findTopByEmpCodeAndOrgIdAndBranchOrderByIdDesc(
//	                    userNameDTO.getEmpcode(), userNameDTO.getOrgId(), userNameDTO.getBranch());
//	            if (lastCheckInOpt.isPresent()) {
//	                CheckInVO lastCheckIn = lastCheckInOpt.get();
//	                if (!lastCheckIn.getCheckInDate().isEqual(today)) {
//	                    CheckInVO autoCheckout = new CheckInVO();
//	                    autoCheckout.setEmpCode(userNameDTO.getEmpcode());
//	                    autoCheckout.setEmpName(userNameDTO.getEmpName());
//	                    autoCheckout.setBranch(userNameDTO.getBranch());
//	                    autoCheckout.setBranchCode(userNameDTO.getBranchCode());
//	                    autoCheckout.setCheckInDate(lastCheckIn.getCheckInDate());
//	                    autoCheckout.setNotify(userNameDTO.getNotify());
//	                    autoCheckout.setNotifyCode(userNameDTO.getNotifyCode());
//	                    autoCheckout.setNotifyEmail(userNameDTO.getNotifyEmail());
//	                    autoCheckout.setEmail(userNameDTO.getEmail());
//	                    autoCheckout.setLatitude(userNameDTO.getLatitude());
//	                    autoCheckout.setLongitude(userNameDTO.getLongitude());
//	                    autoCheckout.setWorkFromHome(userNameDTO.getWorkFromHome());
//	                    autoCheckout.setLocationAddress(userNameDTO.getLocationAddress());
//	                    autoCheckout.setEntryTime(LocalTime.MIDNIGHT);
//	                    autoCheckout.setOrgId(userNameDTO.getOrgId());
//	                    autoCheckout.setAttendanceMode("SYSTEM");
//	                    autoCheckout.setCreatedOn(LocalDateTime.now());
//	                    autoCheckout.setStatus("Out");
//
//	                    checkInRepo.save(autoCheckout);
//
//	                    CheckInStatusVO autoCheckoutStatus = new CheckInStatusVO();
//	                    autoCheckoutStatus.setEmpcode(userNameDTO.getEmpcode());
//	                    autoCheckoutStatus.setEmpName(userNameDTO.getEmpName());
//	                    autoCheckoutStatus.setStatus("Out");
//	                    autoCheckoutStatus.setOrgId(userNameDTO.getOrgId());
//	                    autoCheckoutStatus.setBranch(userNameDTO.getBranch());
//	                    allowedTodayCheckin = false;
//	                    checkInStatusRepo.save(autoCheckoutStatus);
//
//	                    AttendanceProcessVO attendanceProcessVO = new AttendanceProcessVO();
//	                    attendanceProcessVO.setEmpName(userNameDTO.getEmpName());
//	                    attendanceProcessVO.setEmpCode(userNameDTO.getEmpcode());
//	                    attendanceProcessVO.setBranch(userNameDTO.getBranch());
//	                    attendanceProcessVO.setBranchCode(userNameDTO.getBranchCode());
//	                    attendanceProcessVO.setFinyear(String.valueOf(lastCheckIn.getCheckInDate().getYear()));
//	                    attendanceProcessVO.setCheckInDate(lastCheckIn.getCheckInDate());
//	                    attendanceProcessVO.setEntryTime(LocalTime.MIDNIGHT);
//	                    attendanceProcessVO.setSourceId(autoCheckout.getId());
//	                    attendanceProcessVO.setAttendanceMode("SYSTEM");
//	                    attendanceProcessVO.setOrgId(userNameDTO.getOrgId());
//
//	                    attendanceProcessRepo.save(attendanceProcessVO);
//	                }
//	            }
//	        }
//	    }
//
//	    if (Boolean.TRUE.equals(allowedTodayCheckin) || isNightShift) {
//	        todayCheck = new CheckInVO();
//	        todayCheck.setEmpCode(userNameDTO.getEmpcode());
//	        todayCheck.setEmpName(userNameDTO.getEmpName());
//	        todayCheck.setBranch(userNameDTO.getBranch());
//	        todayCheck.setBranchCode(userNameDTO.getBranchCode());
//	        todayCheck.setCheckInDate(today);
//	        todayCheck.setEntryTime(now);
//	        todayCheck.setOrgId(userNameDTO.getOrgId());
//	        todayCheck.setStatus(userNameDTO.isStatus() ? "In" : "Out");
//	        todayCheck.setCreatedOn(LocalDateTime.now());
//	        todayCheck.setNotify(userNameDTO.getNotify());
//	        todayCheck.setNotifyCode(userNameDTO.getNotifyCode());
//	        todayCheck.setNotifyEmail(userNameDTO.getNotifyEmail());
//	        todayCheck.setEmail(userNameDTO.getEmail());
//	        todayCheck.setLatitude(userNameDTO.getLatitude());
//	        todayCheck.setLongitude(userNameDTO.getLongitude());
//	        todayCheck.setWorkFromHome(userNameDTO.getWorkFromHome());
//	        todayCheck.setLocationAddress(userNameDTO.getLocationAddress());
//	        todayCheck.setFinyear(String.valueOf(today.getYear()));
//	        todayCheck.setAttendanceMode("SYSTEM");
//
//	        checkInRepo.save(todayCheck);
//
//	        CheckInStatusVO statusUpdate = new CheckInStatusVO();
//	        statusUpdate.setEmpcode(userNameDTO.getEmpcode());
//	        statusUpdate.setEmpName(userNameDTO.getEmpName());
//	        statusUpdate.setStatus(todayCheck.getStatus());
//	        statusUpdate.setOrgId(userNameDTO.getOrgId());
//	        statusUpdate.setBranch(userNameDTO.getBranch());
//	        checkInStatusRepo.save(statusUpdate);
//
//	        AttendanceProcessVO attendanceProcessVO = new AttendanceProcessVO();
//	        attendanceProcessVO.setEmpName(userNameDTO.getEmpName());
//	        attendanceProcessVO.setEmpCode(userNameDTO.getEmpcode());
//	        attendanceProcessVO.setBranch(userNameDTO.getBranch());
//	        attendanceProcessVO.setBranchCode(userNameDTO.getBranchCode());
//	        attendanceProcessVO.setFinyear(String.valueOf(today.getYear()));
//	        attendanceProcessVO.setCheckInDate(today);
//	        attendanceProcessVO.setEntryTime(now);
//	        attendanceProcessVO.setStatus(userNameDTO.isStatus() ? "In" : "Out");
//	        attendanceProcessVO.setSourceId(todayCheck.getId());
//	        attendanceProcessVO.setAttendanceMode("SYSTEM");
//	        attendanceProcessVO.setOrgId(userNameDTO.getOrgId());
//
//	        attendanceProcessRepo.save(attendanceProcessVO);
//
//	        // Final dynamic logic to post attendance daily summary based on shift timing
//	        LocalTime shiftOutTime = LocalTime.parse(latestShift.getOutTime());
//	        LocalDate baseDate;
//	        if (isNightShift) {
//	            if (userNameDTO.isStatus()) {
//	                baseDate = now.isBefore(shiftOutTime) ? today.minusDays(1) : today;
//	            } else {
//	                Optional<AttendanceProcessVO> lastIn = attendanceProcessRepo
//	                        .findTopByEmpCodeAndStatusAndOrgIdAndBranchAndCheckInDateLessThanEqualOrderByCheckInDateDescEntryTimeDesc(
//	                                userNameDTO.getEmpcode(), "In", userNameDTO.getOrgId(), userNameDTO.getBranch(), today);
//	                baseDate = lastIn.map(AttendanceProcessVO::getCheckInDate)
//	                        .orElse(now.isBefore(shiftOutTime) ? today.minusDays(1) : today);
//	            }
//	        } else {
//	            baseDate = today;
//	        }
//
//	        List<AttendanceProcessVO> records = attendanceProcessRepo.findByEmpCodeAndDateRange(
//	                userNameDTO.getEmpcode(), baseDate, baseDate.plusDays(1), userNameDTO.getOrgId(), userNameDTO.getBranch());
//
//	        List<LocalDateTime> inList = new ArrayList<>();
//	        List<LocalDateTime> outList = new ArrayList<>();
//
//	        for (AttendanceProcessVO rec : records) {
//	            LocalDateTime dt = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
//	            if ("In".equalsIgnoreCase(rec.getStatus())) inList.add(dt);
//	            else if ("Out".equalsIgnoreCase(rec.getStatus())) outList.add(dt);
//	        }
//
//	        Collections.sort(inList);
//	        Collections.sort(outList);
//
//	        long effectiveSeconds = 0;
//	        int outIndex = 0;
//	        for (LocalDateTime inTime : inList) {
//	            while (outIndex < outList.size() && outList.get(outIndex).isBefore(inTime)) outIndex++;
//	            if (outIndex < outList.size()) {
//	                LocalDateTime outTime = outList.get(outIndex);
//	                if (!outTime.isBefore(inTime)) {
//	                    effectiveSeconds += Duration.between(inTime, outTime).getSeconds();
//	                    outIndex++;
//	                }
//	            }
//	        }
//
//	        LocalDateTime firstIn = inList.stream().min(LocalDateTime::compareTo).orElse(null);
//	        LocalDateTime lastOut = outList.stream().max(LocalDateTime::compareTo).orElse(null);
//
//	        long grossSeconds = (firstIn != null && lastOut != null && lastOut.isAfter(firstIn)) ?
//	                Duration.between(firstIn, lastOut).getSeconds() : 0;
//
//	        AttendanceDailyVO dailyVO = attendanceDailyRepo.findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
//	                userNameDTO.getEmpcode(), baseDate, userNameDTO.getOrgId(), userNameDTO.getBranch());
//
//	        if (dailyVO == null) {
//	            dailyVO = new AttendanceDailyVO();
//	            dailyVO.setEmpCode(userNameDTO.getEmpcode());
//	            dailyVO.setEmpName(userNameDTO.getEmpName());
//	            dailyVO.setBranch(userNameDTO.getBranch());
//	            dailyVO.setBranchCode(userNameDTO.getBranchCode());
//	            dailyVO.setOrgId(userNameDTO.getOrgId());
//	            dailyVO.setCheckInDate(baseDate);
//	            dailyVO.setFinyear(String.valueOf(baseDate.getYear()));
//	            dailyVO.setAttendanceMode("System");
//	        }
//
//	        if (firstIn != null) dailyVO.setInTime(firstIn.toLocalTime());
//	        if (lastOut != null) {
//	            dailyVO.setOutTime(lastOut.toLocalTime());
//	            dailyVO.setCheckOutDate(lastOut.toLocalDate());
//	        }
//
//	        dailyVO.setEffectiveHours((int)(effectiveSeconds / 3600));
//	        dailyVO.setGrossHours((int)(grossSeconds / 3600));
//
//	        attendanceDailyRepo.save(dailyVO);
//	    }
//
//	    response.put("message", userNameDTO.isStatus() ? "Check-in created successfully" : "Check-out created successfully");
//	    response.put("checkInVO", todayCheck);
//	    return response;
//	}
//
//	public boolean isWithinCompanyLocation(double empLat, double empLng, long orgId) throws ApplicationException {
//	    List<CompanyVO> companyVOList = companyRepo.findByCompany(orgId);
//	    if (companyVOList.isEmpty()) throw new ApplicationException("Company not found for id: " + orgId);
//
//	    CompanyVO companyVO = companyVOList.get(0);
//	    double allowedRadius = 500;
//	    double distance = LocationUtils.distanceInMeters(empLat, empLng, companyVO.getLatitude(), companyVO.getLongitude());
//	    return distance <= allowedRadius;
//	}
	
	
	
	
	@Override
	@Transactional
	public Map<String, Object> createCheckInOut(UserNameDTO userNameDTO) throws ApplicationException {
	    Map<String, Object> response = new HashMap<>();
	    LocalDate today = LocalDate.now();
	    LocalTime now = LocalTime.now();
	    boolean allowedTodayCheckin = true;
	    CheckInVO todayCheck = null;

	    // Get company and hybrid status
	    List<CompanyVO> companyList = companyRepo.findByCompany(userNameDTO.getOrgId());
	    if (companyList.isEmpty()) throw new ApplicationException("Company not found for id: " + userNameDTO.getOrgId());
	    boolean hybridStatus = companyList.get(0).isHybrid();

	    if (userNameDTO.getLatitude() == null || userNameDTO.getLongitude() == null)
	        throw new ApplicationException("Latitude or Longitude cannot be null");

	    String workFromHome = hybridStatus ? userNameDTO.getWorkFromHome() : "NO";

	    if ("NO".equalsIgnoreCase(workFromHome)) {
	        if (!isWithinCompanyLocation(userNameDTO.getLatitude(), userNameDTO.getLongitude(), userNameDTO.getOrgId())) {
	            throw new ApplicationException("You are not within the allowed office location. Check-in/out denied");
	        }
	    }

	    // Get last check-in/out status
	    Optional<CheckInStatusVO> latestStatusOpt = checkInStatusRepo.findTopByEmpcodeAndOrgIdAndBranchOrderByIdDesc(
	            userNameDTO.getEmpcode(), userNameDTO.getOrgId(), userNameDTO.getBranch());

	    // Get employee's shift assignment
	    List<ShiftAssignDetailsVO> shiftList = shiftAssignDetailsRepo
	            .findByEmployeeCodeAndShiftAssignVO_OrgId(userNameDTO.getEmpcode(), userNameDTO.getOrgId());

	    if (shiftList == null || shiftList.isEmpty()) throw new ApplicationException("This employee has no assigned shift.");

	    ShiftAssignDetailsVO latestShift = shiftList.stream()
	            .max(Comparator.comparing(ShiftAssignDetailsVO::getEffectiveFrom))
	            .orElseThrow(() -> new ApplicationException("No effective shift assignment found."));

	    String shiftType = latestShift.getShiftType() != null ? latestShift.getShiftType().trim().toUpperCase() : "DAY";
	    boolean isNightShift = "NIGHT".equals(shiftType);
	    boolean isOpenShift = "OPEN".equals(shiftType);

	    // Auto checkout for Day shift if last status is IN
	    if (!isNightShift && !isOpenShift && latestStatusOpt.isPresent()) {
	        CheckInStatusVO latestStatus = latestStatusOpt.get();
	        if ("In".equalsIgnoreCase(latestStatus.getStatus())) {
	            Optional<CheckInVO> lastCheckInOpt = checkInRepo.findTopByEmpCodeAndOrgIdAndBranchOrderByIdDesc(
	                    userNameDTO.getEmpcode(), userNameDTO.getOrgId(), userNameDTO.getBranch());
	            if (lastCheckInOpt.isPresent()) {
	                CheckInVO lastCheckIn = lastCheckInOpt.get();
	                if (!lastCheckIn.getCheckInDate().isEqual(today)) {
	                    // Auto checkout previous day
	                    CheckInVO autoCheckout = new CheckInVO();
	                    autoCheckout.setEmpCode(userNameDTO.getEmpcode());
	                    autoCheckout.setEmpName(userNameDTO.getEmpName());
	                    autoCheckout.setBranch(userNameDTO.getBranch());
	                    autoCheckout.setBranchCode(userNameDTO.getBranchCode());
	                    autoCheckout.setCheckInDate(lastCheckIn.getCheckInDate());
	                    autoCheckout.setNotify(userNameDTO.getNotify());
	                    autoCheckout.setNotifyCode(userNameDTO.getNotifyCode());
	                    autoCheckout.setNotifyEmail(userNameDTO.getNotifyEmail());
	                    autoCheckout.setEmail(userNameDTO.getEmail());
	                    autoCheckout.setLatitude(userNameDTO.getLatitude());
	                    autoCheckout.setLongitude(userNameDTO.getLongitude());
	                    autoCheckout.setWorkFromHome(userNameDTO.getWorkFromHome());
	                    autoCheckout.setLocationAddress(userNameDTO.getLocationAddress());
	                    autoCheckout.setEntryTime(LocalTime.MIDNIGHT);
	                    autoCheckout.setOrgId(userNameDTO.getOrgId());
	                    autoCheckout.setAttendanceMode("SYSTEM");
	                    autoCheckout.setCreatedOn(LocalDateTime.now());
	                    autoCheckout.setStatus("Out");
	                    checkInRepo.save(autoCheckout);

	                    CheckInStatusVO autoCheckoutStatus = new CheckInStatusVO();
	                    autoCheckoutStatus.setEmpcode(userNameDTO.getEmpcode());
	                    autoCheckoutStatus.setEmpName(userNameDTO.getEmpName());
	                    autoCheckoutStatus.setStatus("Out");
	                    autoCheckoutStatus.setOrgId(userNameDTO.getOrgId());
	                    autoCheckoutStatus.setBranch(userNameDTO.getBranch());
	                    allowedTodayCheckin = false;
	                    checkInStatusRepo.save(autoCheckoutStatus);

	                    AttendanceProcessVO attendanceProcessVO = new AttendanceProcessVO();
	                    attendanceProcessVO.setEmpName(userNameDTO.getEmpName());
	                    attendanceProcessVO.setEmpCode(userNameDTO.getEmpcode());
	                    attendanceProcessVO.setBranch(userNameDTO.getBranch());
	                    attendanceProcessVO.setBranchCode(userNameDTO.getBranchCode());
	                    attendanceProcessVO.setFinyear(String.valueOf(lastCheckIn.getCheckInDate().getYear()));
	                    attendanceProcessVO.setCheckInDate(lastCheckIn.getCheckInDate());
	                    attendanceProcessVO.setEntryTime(LocalTime.MIDNIGHT);
	                    attendanceProcessVO.setSourceId(autoCheckout.getId());
	                    attendanceProcessVO.setAttendanceMode("SYSTEM");
	                    attendanceProcessVO.setOrgId(userNameDTO.getOrgId());
	                    attendanceProcessRepo.save(attendanceProcessVO);
	                }
	            }
	        }
	    }

	    // Proceed with today's check-in/out
	    if (Boolean.TRUE.equals(allowedTodayCheckin) || isNightShift || isOpenShift) {
	        todayCheck = new CheckInVO();
	        todayCheck.setEmpCode(userNameDTO.getEmpcode());
	        todayCheck.setEmpName(userNameDTO.getEmpName());
	        todayCheck.setBranch(userNameDTO.getBranch());
	        todayCheck.setBranchCode(userNameDTO.getBranchCode());
	        todayCheck.setCheckInDate(today);
	        todayCheck.setEntryTime(now);
	        todayCheck.setOrgId(userNameDTO.getOrgId());
	        todayCheck.setStatus(userNameDTO.isStatus() ? "In" : "Out");
	        todayCheck.setCreatedOn(LocalDateTime.now());
	        todayCheck.setNotify(userNameDTO.getNotify());
	        todayCheck.setNotifyCode(userNameDTO.getNotifyCode());
	        todayCheck.setNotifyEmail(userNameDTO.getNotifyEmail());
	        todayCheck.setEmail(userNameDTO.getEmail());
	        todayCheck.setLatitude(userNameDTO.getLatitude());
	        todayCheck.setLongitude(userNameDTO.getLongitude());
	        todayCheck.setWorkFromHome(userNameDTO.getWorkFromHome());
	        todayCheck.setLocationAddress(userNameDTO.getLocationAddress());
	        todayCheck.setFinyear(String.valueOf(today.getYear()));
	        todayCheck.setAttendanceMode("SYSTEM");

	        checkInRepo.save(todayCheck);

	        // Update check-in status
	        CheckInStatusVO statusUpdate = new CheckInStatusVO();
	        statusUpdate.setEmpcode(userNameDTO.getEmpcode());
	        statusUpdate.setEmpName(userNameDTO.getEmpName());
	        statusUpdate.setStatus(todayCheck.getStatus());
	        statusUpdate.setOrgId(userNameDTO.getOrgId());
	        statusUpdate.setBranch(userNameDTO.getBranch());
	        checkInStatusRepo.save(statusUpdate);

	        // Save to attendance process
	        AttendanceProcessVO attendanceProcessVO = new AttendanceProcessVO();
	        attendanceProcessVO.setEmpName(userNameDTO.getEmpName());
	        attendanceProcessVO.setEmpCode(userNameDTO.getEmpcode());
	        attendanceProcessVO.setBranch(userNameDTO.getBranch());
	        attendanceProcessVO.setBranchCode(userNameDTO.getBranchCode());
	        attendanceProcessVO.setFinyear(String.valueOf(today.getYear()));
	        attendanceProcessVO.setCheckInDate(today);
	        attendanceProcessVO.setEntryTime(now);
	        attendanceProcessVO.setStatus(userNameDTO.isStatus() ? "In" : "Out");
	        attendanceProcessVO.setSourceId(todayCheck.getId());
	        attendanceProcessVO.setAttendanceMode("SYSTEM");
	        attendanceProcessVO.setOrgId(userNameDTO.getOrgId());
	        attendanceProcessRepo.save(attendanceProcessVO);

	        // --- Calculate base date for pairing ---
	        LocalDate baseDate;
	        if (isNightShift) {
	            LocalTime shiftOutTime = LocalTime.parse(latestShift.getOutTime());
	            if (userNameDTO.isStatus()) {
	                baseDate = now.isBefore(shiftOutTime) ? today.minusDays(1) : today;
	            } else {
	                Optional<AttendanceProcessVO> lastIn = attendanceProcessRepo
	                        .findTopByEmpCodeAndStatusAndOrgIdAndBranchAndCheckInDateLessThanEqualOrderByCheckInDateDescEntryTimeDesc(
	                                userNameDTO.getEmpcode(), "In", userNameDTO.getOrgId(), userNameDTO.getBranch(), today);
	                baseDate = lastIn.map(AttendanceProcessVO::getCheckInDate)
	                        .orElse(now.isBefore(shiftOutTime) ? today.minusDays(1) : today);
	            }
	        } else {
	            // For Day and Open shift, baseDate = today
	            baseDate = today;
	        }

	        // --- Get all IN/OUT for the base date ---
	        List<AttendanceProcessVO> records = attendanceProcessRepo.findByEmpCodeAndDateRange(
	                userNameDTO.getEmpcode(), baseDate, baseDate.plusDays(1),
	                userNameDTO.getOrgId(), userNameDTO.getBranch()
	        );

	        List<LocalDateTime> inList = new ArrayList<>();
	        List<LocalDateTime> outList = new ArrayList<>();
	        for (AttendanceProcessVO rec : records) {
	            LocalDateTime dt = LocalDateTime.of(rec.getCheckInDate(), rec.getEntryTime());
	            if ("In".equalsIgnoreCase(rec.getStatus())) inList.add(dt);
	            else if ("Out".equalsIgnoreCase(rec.getStatus())) outList.add(dt);
	        }

	        Collections.sort(inList);
	        Collections.sort(outList);

	        long effectiveSeconds = 0;
	        int outIndex = 0;
	        for (LocalDateTime inTime : inList) {
	            while (outIndex < outList.size() && outList.get(outIndex).isBefore(inTime)) outIndex++;
	            if (outIndex < outList.size()) {
	                LocalDateTime outTime = outList.get(outIndex);
	                if (!outTime.isBefore(inTime)) {
	                    effectiveSeconds += Duration.between(inTime, outTime).getSeconds();
	                    outIndex++;
	                }
	            }
	        }

	        LocalDateTime firstIn = inList.stream().min(LocalDateTime::compareTo).orElse(null);
	        LocalDateTime lastOut = outList.stream().max(LocalDateTime::compareTo).orElse(null);

	        long grossSeconds = (firstIn != null && lastOut != null && lastOut.isAfter(firstIn)) ?
	                Duration.between(firstIn, lastOut).getSeconds() : 0;

	        // --- Save AttendanceDailyVO ---
	        AttendanceDailyVO dailyVO = attendanceDailyRepo.findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
	                userNameDTO.getEmpcode(), baseDate, userNameDTO.getOrgId(), userNameDTO.getBranch()
	        );

	        if (dailyVO == null) {
	            dailyVO = new AttendanceDailyVO();
	            dailyVO.setEmpCode(userNameDTO.getEmpcode());
	            dailyVO.setEmpName(userNameDTO.getEmpName());
	            dailyVO.setBranch(userNameDTO.getBranch());
	            dailyVO.setBranchCode(userNameDTO.getBranchCode());
	            dailyVO.setOrgId(userNameDTO.getOrgId());
	            dailyVO.setCheckInDate(baseDate);
	            dailyVO.setFinyear(String.valueOf(baseDate.getYear()));
	            dailyVO.setAttendanceMode("System");
	        }

	        if (firstIn != null) dailyVO.setInTime(firstIn.toLocalTime());
	        if (lastOut != null) {
	            dailyVO.setOutTime(lastOut.toLocalTime());
	            dailyVO.setCheckOutDate(lastOut.toLocalDate());
	        }

	        dailyVO.setEffectiveHours((int) (effectiveSeconds / 3600));
	        dailyVO.setGrossHours((int) (grossSeconds / 3600));

	        attendanceDailyRepo.save(dailyVO);
	    }

	    response.put("message", userNameDTO.isStatus() ? "Check-in created successfully" : "Check-out created successfully");
	    response.put("checkInVO", todayCheck);
	    return response;
	}
	
	public boolean isWithinCompanyLocation(double empLat, double empLng, long orgId) throws ApplicationException {
    List<CompanyVO> companyVOList = companyRepo.findByCompany(orgId);
    if (companyVOList.isEmpty()) throw new ApplicationException("Company not found for id: " + orgId);

    CompanyVO companyVO = companyVOList.get(0);
    double allowedRadius = 500;
    double distance = LocationUtils.distanceInMeters(empLat, empLng, companyVO.getLatitude(), companyVO.getLongitude());
    return distance <= allowedRadius;
}


//	@Override
//	public Map<String, Object> createApprovalCheckOut(Long orgId, String employeeCode, String action, String actionBy,
//			LocalDate localCheckInDate, String notifyCode, String notify, String screenName)
//			throws ApplicationException {
//
//		CheckInVO checkInVO = checkInRepo.findTopByOrgIdAndEmpCodeAndCheckInDateAndStatusOrderByCreatedOnDesc(orgId,
//				employeeCode, localCheckInDate, "OUT");
//		String message = "";
//
//		if (checkInVO.getApprovalStatus() == null || (!checkInVO.getApprovalStatus().equalsIgnoreCase("Approved"))
//				&& (!checkInVO.getApprovalStatus().equalsIgnoreCase("Rejected"))) {
//
//			if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {
//
//				if ("REJECTED".equalsIgnoreCase(action)) {
//					// Type casting from String to LocalTime
//					LocalTime checkOutTimeCast = LocalTime.parse("00:00:00");
//
//					// Set entry time
//					checkInVO.setEntryTime(checkOutTimeCast);
//				}
//
//				checkInVO.setApprovalStatus(action);
//				checkInVO.setApproveBy(actionBy);
//
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
//				checkInVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());
//
//				checkInRepo.save(checkInVO);
//
//				if (checkInVO.getApprovalStatus().equalsIgnoreCase("Approved")) {
//					message = "Approved Successfully";
//				} else if (checkInVO.getApprovalStatus().equalsIgnoreCase("Rejected")) {
//					message = "Rejected Successfully";
//				}
//			}
//
//		} else if (checkInVO.getApprovalStatus().equalsIgnoreCase("Approved")) {
//			throw new ApplicationException("This CheckOut Already Approved");
//		} else if (checkInVO.getApprovalStatus().equalsIgnoreCase("Rejected")) {
//			throw new ApplicationException("This CheckOut Already Rejected");
//		}
//
//		Map<String, Object> response = new HashMap<>();
//		response.put("checkInVO", checkInVO);
//		response.put("message", message);
//		return response;
//	}
	
	@Override
	public Map<String, Object> createApprovalCheckOut(Long orgId, String employeeCode, String action, String actionBy,
	        LocalDate localCheckInDate, String notifyCode, String notify, String screenName)
	        throws ApplicationException {

	    CheckInVO checkOutVO = checkInRepo.findTopByOrgIdAndEmpCodeAndCheckInDateAndStatusOrderByCreatedOnDesc(
	            orgId, employeeCode, localCheckInDate, "OUT");

	    String message = "";

	    if (checkOutVO == null) {
	        throw new ApplicationException("No CheckOut record found.");
	    }

	    if (checkOutVO.getApprovalStatus() == null ||
	        (!"Approved".equalsIgnoreCase(checkOutVO.getApprovalStatus()) &&
	         !"Rejected".equalsIgnoreCase(checkOutVO.getApprovalStatus()))) {

	        if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {

	            if ("REJECTED".equalsIgnoreCase(action)) {
	                // Set entry time to 00:00:00 for rejected check-out
	                checkOutVO.setEntryTime(LocalTime.parse("00:00:00"));
	            }

	            checkOutVO.setApprovalStatus(action);
	            checkOutVO.setApproveBy(actionBy);
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
	            checkOutVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

	            checkInRepo.save(checkOutVO);

	            if ("APPROVED".equalsIgnoreCase(action)) {
	                // ✅ Find matching IN record
	                CheckInVO checkInVO = checkInRepo.findTopByOrgIdAndEmpCodeAndCheckInDateAndStatusOrderByCreatedOnDesc(
	                        orgId, employeeCode, localCheckInDate, "IN");

	                if (checkInVO != null && checkInVO.getEntryTime() != null && checkOutVO.getEntryTime() != null) {
	                    // Compute duration
	                    LocalDateTime fullIn = LocalDateTime.of(checkInVO.getCheckInDate(), checkInVO.getEntryTime());
	                    LocalDateTime fullOut = LocalDateTime.of(checkOutVO.getCheckInDate(), checkOutVO.getEntryTime());

	                    long seconds = Duration.between(fullIn, fullOut).getSeconds();
	                    int hours = (int) (seconds / 3600);

	                    // Check existing AttendanceDaily record
	                    AttendanceDailyVO existing = attendanceDailyRepo
	                            .findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
	                                    employeeCode, localCheckInDate, orgId, checkOutVO.getBranch());

	                    boolean isDuplicate = existing != null &&
	                            checkInVO.getEntryTime().equals(existing.getInTime()) &&
	                            checkOutVO.getEntryTime().equals(existing.getOutTime());

	                    if (!isDuplicate) {
	                        AttendanceDailyVO daily = (existing != null) ? existing : new AttendanceDailyVO();
	                        daily.setEmpCode(employeeCode);
	                        daily.setEmpName(checkOutVO.getEmpName());
	                        daily.setBranch(checkOutVO.getBranch());
	                        daily.setBranchCode(checkOutVO.getBranchCode());
	                        daily.setOrgId(orgId);
	                        daily.setCheckInDate(localCheckInDate);
	                        daily.setCheckOutDate(checkOutVO.getCheckInDate());
	                        daily.setFinyear(String.valueOf(localCheckInDate.getYear()));
	                        daily.setAttendanceMode("System");

	                        daily.setInTime(checkInVO.getEntryTime());
	                        daily.setOutTime(checkOutVO.getEntryTime());
	                        daily.setEffectiveHours(hours);
	                        daily.setGrossHours(hours);

	                        attendanceDailyRepo.save(daily);
	                    }
	                }
	                message = "Approved Successfully";
	            } else if ("REJECTED".equalsIgnoreCase(action)) {
	                message = "Rejected Successfully";
	            }
	        }

	    } else if ("Approved".equalsIgnoreCase(checkOutVO.getApprovalStatus())) {
	        throw new ApplicationException("This CheckOut Already Approved");
	    } else if ("Rejected".equalsIgnoreCase(checkOutVO.getApprovalStatus())) {
	        throw new ApplicationException("This CheckOut Already Rejected");
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("checkInVO", checkOutVO);
	    response.put("message", message);
	    return response;
	}


	@Override
	public Map<String, Object> createRequestCheckOut(CheckinRequestDTO checkinRequestDTO) throws ApplicationException {

		// Fetch existing CheckInVO by ID
		Optional<CheckInVO> optionalCheckInVO = checkInRepo.findBycheckInDateAndEmpCodeAndOrgIdAndBranchCode(
				checkinRequestDTO.getDate(), checkinRequestDTO.getEmpCode(), checkinRequestDTO.getOrgId(),
				checkinRequestDTO.getBranch());

		CheckInVO checkInVO;

		if (optionalCheckInVO.isPresent()) {
			checkInVO = optionalCheckInVO.get();
			updateCheckInVOFromDTO(checkInVO, checkinRequestDTO);
		} else {
			checkInVO = new CheckInVO();
			updateCheckInVOFromDTO(checkInVO, checkinRequestDTO);
			checkInVO.setBranch(checkinRequestDTO.getBranch());
			checkInVO.setEmpCode(checkinRequestDTO.getEmpCode());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate checkInDate = LocalDate.parse(checkinRequestDTO.getDate(), formatter);
			checkInVO.setCheckInDate(checkInDate);

			checkInVO.setOrgId(checkinRequestDTO.getOrgId());
			checkInVO.setStatus("OUT");
			checkInVO.setEmpName(checkinRequestDTO.getEmpName());
			checkInVO.setNotify(checkinRequestDTO.getNotify());
			checkInVO.setNotifyCode(checkinRequestDTO.getNotifyCode());
			checkInVO.setNotifyEmail(checkinRequestDTO.getNotifyEmail());

			checkInVO.setCreatedOn(LocalDateTime.now());

			CheckInStatusVO autoCheckoutStatus = new CheckInStatusVO();
			autoCheckoutStatus.setEmpcode(checkinRequestDTO.getEmpCode());
			autoCheckoutStatus.setEmpName(checkinRequestDTO.getEmpName());
			autoCheckoutStatus.setStatus("Out");
			autoCheckoutStatus.setOrgId(checkinRequestDTO.getOrgId());
			autoCheckoutStatus.setBranch(checkinRequestDTO.getBranch());
			checkInStatusRepo.save(autoCheckoutStatus);

		}

		// Save updated entity
		checkInRepo.save(checkInVO);

		// Prepare response
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Check-out request submitted successfully.");
		response.put("checkInVO", checkInVO);
		return response;
	}

	// Helper method to update the check-out time
	private void updateCheckInVOFromDTO(CheckInVO checkInVO, CheckinRequestDTO checkinRequestDTO) {
		checkInVO.setApprovalStatus("PENDING");

		// Set check-out time (assumes checkOutTime field exists in CheckInVO)
		// Convert String to LocalTime
		String entryTimeStr = checkinRequestDTO.getEntryTime(); // e.g., "09:30"

		if (entryTimeStr != null && !entryTimeStr.isEmpty()) {
			// Optional: specify format if needed
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // or "HH:mm:ss"
			LocalTime entryTime = LocalTime.parse(entryTimeStr, formatter);

			checkInVO.setEntryTime(entryTime);
			
			
			AttendanceProcessVO attendanceProcessVO = attendanceProcessRepo.findBySourceId(checkInVO.getId());
			if (attendanceProcessVO != null) {
			    attendanceProcessVO.setEntryTime(entryTime);
			    attendanceProcessVO.setStatus("Out");	
			    attendanceProcessRepo.save(attendanceProcessVO); // Don't forget to save changes
			    
			}

		} else {
			throw new IllegalArgumentException("Entry time is missing or invalid.");
		}
		
	}

//adjustmentcheckinout

	@Override
	public Map<String, Object> createCheckInOutAdjustment(CheckInOutAdjustmentDTO dto) throws ApplicationException {
	    Map<String, Object> response = new HashMap<>();
	    List<CheckInOutAdjustmentVO> savedEntries = new ArrayList<>();

	    String dateStr = dto.getDate();
	    LocalDate localDate = LocalDate.parse(dateStr); // e.g., "2025-06-29"
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

	    LocalTime inTime = null;
	    LocalTime outTime = null;

	    if (dto.getEntryIn() != null && !dto.getEntryIn().isEmpty()) {
	        inTime = LocalTime.parse(dto.getEntryIn(), timeFormatter);
	    }
	    if (dto.getEntryOut() != null && !dto.getEntryOut().isEmpty()) {
	        outTime = LocalTime.parse(dto.getEntryOut(), timeFormatter);
	    }

	    // Detect night shift based on time comparison
	    boolean isNightShift = (inTime != null && outTime != null && inTime.isAfter(outTime));
	  
	    System.out.println();
	    // Save IN entry
	    if (inTime != null) {
	        CheckInOutAdjustmentVO checkIn = new CheckInOutAdjustmentVO();
	        checkIn.setCheckInDate(localDate);
	        checkIn.setEmpCode(dto.getEmpCode());
	        checkIn.setEmpName(dto.getEmpName());
	        checkIn.setBranch(dto.getBranch());
	        checkIn.setBranchCode(dto.getBranchCode());
	        checkIn.setOrgId(dto.getOrgId());
	        checkIn.setNotify(dto.getNotify());
	        checkIn.setNotifyCode(dto.getNotifyCode());
	        checkIn.setNotifyEmail(dto.getNotifyEmail());
	        checkIn.setEmail(dto.getEmail());

	        checkIn.setCreatedOn(LocalDateTime.of(localDate, inTime));
	        checkIn.setApprovalStatus("PENDING");
	        checkIn.setStatus("IN");
	        checkIn.setEntryTime(inTime);

	        checkInOutAdjustmentRepo.save(checkIn);
	        savedEntries.add(checkIn);

	        AttendanceProcessVO attendanceIn = new AttendanceProcessVO();
	        attendanceIn.setEmpName(dto.getEmpName());
	        attendanceIn.setEmpCode(dto.getEmpCode());
	        attendanceIn.setBranch(dto.getBranch());
	        attendanceIn.setBranchCode(dto.getBranchCode());
	        attendanceIn.setCheckInDate(localDate);
	        attendanceIn.setEntryTime(inTime);
	        attendanceIn.setStatus("IN");
	        attendanceIn.setOrgId(dto.getOrgId());
	        attendanceIn.setAttendanceMode("SYSTEM");
	        attendanceIn.setSourceId(checkIn.getId());
	        attendanceIn.setFinyear(String.valueOf(localDate.getYear()));

	        attendanceProcessRepo.save(attendanceIn);
	    }

	    // Save OUT entry
	    if (outTime != null) {
	        CheckInOutAdjustmentVO checkOut = new CheckInOutAdjustmentVO();
	        checkOut.setCheckInDate(localDate); // base date
	        checkOut.setEmpCode(dto.getEmpCode());
	        checkOut.setEmpName(dto.getEmpName());
	        checkOut.setBranch(dto.getBranch());
	        checkOut.setBranchCode(dto.getBranchCode());
	        checkOut.setOrgId(dto.getOrgId());
	        checkOut.setNotify(dto.getNotify());
	        checkOut.setNotifyCode(dto.getNotifyCode());
	        checkOut.setNotifyEmail(dto.getNotifyEmail());
	        checkOut.setEmail(dto.getEmail());

	        LocalDate checkOutDate = isNightShift ? localDate.plusDays(1) : localDate;
	        checkOut.setCheckInDate(checkOutDate); // base date
	        checkOut.setCreatedOn(LocalDateTime.of(checkOutDate, outTime));
	        checkOut.setApprovalStatus("PENDING");
	        checkOut.setStatus("OUT");
	        checkOut.setEntryTime(outTime);

	        checkInOutAdjustmentRepo.save(checkOut);
	        savedEntries.add(checkOut);

	        AttendanceProcessVO attendanceOut = new AttendanceProcessVO();
	        attendanceOut.setEmpName(dto.getEmpName());
	        attendanceOut.setEmpCode(dto.getEmpCode());
	        attendanceOut.setBranch(dto.getBranch());
	        attendanceOut.setBranchCode(dto.getBranchCode());
	        attendanceOut.setCheckInDate(checkOutDate); // ✅ for night shift
	        attendanceOut.setEntryTime(outTime);
	        attendanceOut.setStatus("OUT");
	        attendanceOut.setOrgId(dto.getOrgId());
	        attendanceOut.setAttendanceMode("SYSTEM");
	        attendanceOut.setSourceId(checkOut.getId());
	        attendanceOut.setFinyear(String.valueOf(checkOutDate.getYear()));

	        attendanceProcessRepo.save(attendanceOut);
	    }

	    response.put("message", "Check-in/out created successfully.");
	    response.put("entries", savedEntries);
	    return response;
	}



	// approval checkinout
//	@Override
//	public Map<String, Object> createApprovalCheckInOutAdjustment(Long orgId, String employeeCode, String action,
//			String actionBy, LocalDate localCheckInDate, String notifyCode, String notify, String screenName)
//			throws ApplicationException {
//		// method body updated accordingly
//
//		// Fetch both IN and OUT records for the given employee and date
//		List<CheckInOutAdjustmentVO> checkInOutAdjustments = checkInOutAdjustmentRepo
//				.findByOrgIdAndEmpCodeAndCheckInDate(orgId, employeeCode, localCheckInDate);
//
//		if (checkInOutAdjustments == null || checkInOutAdjustments.isEmpty()) {
//			throw new ApplicationException(
//					"No CheckIn/Out adjustment records found for this employee on the given date.");
//		}
//
//		// Check if all records are already approved
//		boolean allApproved = checkInOutAdjustments.stream()
//				.allMatch(adjustment -> "APPROVED".equalsIgnoreCase(adjustment.getApprovalStatus()));
//		boolean allRejected = checkInOutAdjustments.stream()
//				.allMatch(adjustment -> "REJECTED".equalsIgnoreCase(adjustment.getApprovalStatus()));
//
//		if (allApproved) {
//			throw new ApplicationException("CheckIn/Out already approved.");
//		} else if (allRejected) {
//			throw new ApplicationException("CheckIn/Out already rejected.");
//		}
//
//		// Format approval time
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
//		String formattedDateTime = LocalDateTime.now().format(formatter).toUpperCase();
//
//		// Update all records with the action
//		for (CheckInOutAdjustmentVO adjustment : checkInOutAdjustments) {
//			if (!"APPROVED".equalsIgnoreCase(adjustment.getApprovalStatus())) {
//				adjustment.setApprovalStatus(action.toUpperCase());
//				adjustment.setApproveBy(actionBy);
//				adjustment.setApproveOn(formattedDateTime);
//			}
//		}
//
//		// Save all updated records
//		checkInOutAdjustmentRepo.saveAll(checkInOutAdjustments);
//
//		Map<String, Object> response = new HashMap<>();
//		response.put("checkInOutAdjustmentList", checkInOutAdjustments);
//
//		if ("APPROVED".equalsIgnoreCase(action)) {
//			response.put("message", "Approved Successfully");
//		} else if ("REJECTED".equalsIgnoreCase(action)) {
//			response.put("message", "Rejected Successfully");
//		}
//		return response;
//	}
	
	
	@Override
	public Map<String, Object> createApprovalCheckInOutAdjustment(Long orgId, String employeeCode, String action,
	        String actionBy, LocalDate localCheckInDate, String notifyCode, String notify, String screenName)
	        throws ApplicationException {

	    List<CheckInOutAdjustmentVO> allAdjustments = checkInOutAdjustmentRepo
	            .findByOrgIdAndEmpCodeAndCheckInDateBetween(
	                    orgId, employeeCode, localCheckInDate, localCheckInDate.plusDays(1));

	    if (allAdjustments == null || allAdjustments.isEmpty()) {
	        throw new ApplicationException("No CheckIn/Out adjustment records found.");
	    }

	    List<CheckInOutAdjustmentVO> pendingAdjustments = allAdjustments.stream()
	            .filter(adj -> "PENDING".equalsIgnoreCase(adj.getApprovalStatus()))
	            .collect(Collectors.toList());

	    List<CheckInOutAdjustmentVO> alreadyApproved = allAdjustments.stream()
	            .filter(adj -> "APPROVED".equalsIgnoreCase(adj.getApprovalStatus()))
	            .collect(Collectors.toList());

	    List<CheckInOutAdjustmentVO> alreadyRejected = allAdjustments.stream()
	            .filter(adj -> "REJECTED".equalsIgnoreCase(adj.getApprovalStatus()))
	            .collect(Collectors.toList());

	    Map<String, Object> response = new HashMap<>();

	    if (pendingAdjustments.isEmpty()) {
	        if (!alreadyApproved.isEmpty() && alreadyRejected.isEmpty()) {
	            throw new ApplicationException("This CheckIn/Out is already Approved.");
	        } else if (alreadyApproved.isEmpty() && !alreadyRejected.isEmpty()) {
	            throw new ApplicationException("This CheckIn/Out is already Rejected.");
	        } else {
	            throw new ApplicationException("All adjustments are already Approved or Rejected.");
	        }
	    }

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
	    String approvedOn = LocalDateTime.now().format(formatter).toUpperCase();

	    List<CheckInOutAdjustmentVO> approvedAdjustments = new ArrayList<>();

	    List<CheckInOutAdjustmentVO> inList = pendingAdjustments.stream()
	            .filter(e -> "IN".equalsIgnoreCase(e.getStatus()))
	            .sorted(Comparator.comparing(CheckInOutAdjustmentVO::getCheckInDate)
	                    .thenComparing(CheckInOutAdjustmentVO::getEntryTime))
	            .collect(Collectors.toList());

	    List<CheckInOutAdjustmentVO> outList = pendingAdjustments.stream()
	            .filter(e -> "OUT".equalsIgnoreCase(e.getStatus()))
	            .sorted(Comparator.comparing(CheckInOutAdjustmentVO::getCheckInDate)
	                    .thenComparing(CheckInOutAdjustmentVO::getEntryTime))
	            .collect(Collectors.toList());

	    Set<Long> usedOutIds = new HashSet<>();

	    for (CheckInOutAdjustmentVO in : inList) {
	        for (CheckInOutAdjustmentVO out : outList) {
	            if (usedOutIds.contains(out.getId())) continue;

	            boolean isValidPair = out.getCheckInDate().isAfter(in.getCheckInDate()) ||
	                    (out.getCheckInDate().isEqual(in.getCheckInDate()) &&
	                            out.getEntryTime().isAfter(in.getEntryTime()));

	            if (isValidPair) {
	                in.setApprovalStatus("APPROVED");
	                in.setApproveBy(actionBy);
	                in.setApproveOn(approvedOn);

	                out.setApprovalStatus("APPROVED");
	                out.setApproveBy(actionBy);
	                out.setApproveOn(approvedOn);

	                approvedAdjustments.add(in);
	                approvedAdjustments.add(out);
	                usedOutIds.add(out.getId());

	                LocalDateTime fullIn = LocalDateTime.of(in.getCheckInDate(), in.getEntryTime());
	                LocalDateTime fullOut = LocalDateTime.of(out.getCheckInDate(), out.getEntryTime());
	                long seconds = Duration.between(fullIn, fullOut).getSeconds();

	                AttendanceDailyVO existing = attendanceDailyRepo
	                        .findByEmpCodeAndCheckInDateAndOrgIdAndBranch(
	                                in.getEmpCode(), in.getCheckInDate(), orgId, in.getBranch());

	                boolean isDuplicate = existing != null &&
	                        in.getEntryTime().equals(existing.getInTime()) &&
	                        out.getEntryTime().equals(existing.getOutTime());

	                if (isDuplicate) break;

	                AttendanceDailyVO daily = (existing != null) ? existing : new AttendanceDailyVO();
	                daily.setEmpCode(in.getEmpCode());
	                daily.setEmpName(in.getEmpName());
	                daily.setBranch(in.getBranch());
	                daily.setBranchCode(in.getBranchCode());
	                daily.setOrgId(in.getOrgId());
	                daily.setCheckInDate(in.getCheckInDate());
	                daily.setFinyear(String.valueOf(in.getCheckInDate().getYear()));
	                daily.setAttendanceMode("System");

	                daily.setInTime(in.getEntryTime());
	                daily.setOutTime(out.getEntryTime());
	                daily.setCheckOutDate(out.getCheckInDate());
	                daily.setEffectiveHours((int) (seconds / 3600));
	                daily.setGrossHours((int) (seconds / 3600));

	                attendanceDailyRepo.save(daily);
	                break;
	            }
	        }
	    }

	    if (!approvedAdjustments.isEmpty()) {
	        checkInOutAdjustmentRepo.saveAll(approvedAdjustments);
	        response.put("checkInOutAdjustmentList", approvedAdjustments);
	        response.put("message", "Approved Successfully");
	    } else {
	        throw new ApplicationException("No valid pending IN/OUT pairs found to approve.");
	    }

	    return response;
	}



	@Override
	public List<Map<String, Object>> getRequestCheckInOutByOrgId(Long orgId, String branch,
			String reportingPersonCode) {
		// TODO Auto-generated method stub
		Set<Object[]> checkInOutAdjustmentVO = checkInOutAdjustmentRepo.getRequestCheckInOutByOrgId(orgId, branch,
				reportingPersonCode);
		return checkInOutVODetails(checkInOutAdjustmentVO);
	}

	private List<Map<String, Object>> checkInOutVODetails(Set<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("branch", record[0] != null ? record[0].toString() : "");
			map.put("checkInDate", record[1] != null ? record[1].toString() : "");
			map.put("employeeCode", record[2] != null ? record[2].toString() : " ");
			map.put("entryTime", record[3] != null ? record[3].toString() : " ");
			map.put("orgId", record[4] != null ? record[4].toString() : "");
			map.put("employeeName", record[5] != null ? record[5].toString() : " ");
			map.put("screenName", record[6] != null ? record[6].toString() : " ");
			map.put("screenCode", record[7] != null ? record[7].toString() : "");
			map.put("employeeEmail", record[8] != null ? record[8].toString() : "");

			detailsList.add(map);
		}
		return detailsList;
	}

//	    CheckInVO checkInVO = checkInRepo.findTopByOrgIdAndEmpCodeAndCheckInDateAndStatusOrderByCreatedOnDesc(orgId, employeeCode, localCheckInDate,  "OUT");
//
//	    if (checkInVO.getApprovalStatus() == null
//	            || (!checkInVO.getApprovalStatus().equalsIgnoreCase("Approved"))) {
//
//	            checkInVO.setApprovalStatus("PENDING");
//	            LocalTime checkOutTimeCast = LocalTime.parse(checkOutTime);
//
//                // Set entry time
//                checkInVO.setEntryTime(checkOutTimeCast);
//
//	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
//	            checkInVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());
//
//	            checkInRepo.save(checkInVO);
//	    }
//
//	     else if (checkInVO.getApprovalStatus().equalsIgnoreCase("Approved")) {
//	        throw new ApplicationException("This PermissionRequest Already Approved");
//	    } 
//
//
//	    return checkInVO;
//	}

	@Override
	public List<Map<String, Object>> getRequestCheckOutByOrgId(Long orgId, String branch, String reportingPersonCode) {
		// TODO Auto-generated method stub
		Set<Object[]> checkInVO = checkInRepo.getRequestCheckOutByOrgId(orgId, branch, reportingPersonCode);
		return checkInVODetails(checkInVO);
	}

	private List<Map<String, Object>> checkInVODetails(Set<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("branch", record[0] != null ? record[0].toString() : "");
			map.put("checkInDate", record[1] != null ? record[1].toString() : "");
			map.put("employeeCode", record[2] != null ? record[2].toString() : " ");
			map.put("entryTime", record[3] != null ? record[3].toString() : " ");
			map.put("orgId", record[4] != null ? record[4].toString() : "");
			map.put("employeeName", record[5] != null ? record[5].toString() : " ");
			map.put("screenName", record[6] != null ? record[6].toString() : " ");
			map.put("screenCode", record[7] != null ? record[7].toString() : "");
			map.put("employeeEmail", record[8] != null ? record[8].toString() : "");

			detailsList.add(map);
		}
		return detailsList;
	}

	@Override
	public Map<String, Object> createUpdateHolidays(HolidayDTO holidayDTO) throws ApplicationException {

		HolidayVO holidayVO;
		String message = null;

		if (ObjectUtils.isEmpty(holidayDTO.getId())) {

			holidayVO = new HolidayVO();
			holidayVO.setCreatedBy(holidayDTO.getCreatedBy());
			holidayVO.setUpdatedBy(holidayDTO.getCreatedBy());
			message = "Holiday Creation SuccessFully";
		} else {
			// Update existing branch
			holidayVO = holidayRepo.findById(holidayDTO.getId())
					.orElseThrow(() -> new ApplicationException("Holiday not found with id: " + holidayDTO.getId()));
			holidayVO.setUpdatedBy(holidayDTO.getCreatedBy());

			message = "Holiday Update Successfully";
		}

		getHolidayVOFromHolidayDTO(holidayVO, holidayDTO);
		holidayRepo.save(holidayVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("holidayVO", holidayVO);
		return response;

	}

	private void getHolidayVOFromHolidayDTO(HolidayVO holidayVO, HolidayDTO holidayDTO) {
		holidayVO.setOrgId(holidayDTO.getOrgId());
//		holidayVO.setBranchId(holidayDTO.getBranchId());
		holidayVO.setBranchCode(holidayDTO.getBranchCode());
		holidayVO.setBranchName(holidayDTO.getBranchName());
		holidayVO.setDepartment(holidayDTO.getDepartment());
		holidayVO.setHolidayDate(holidayDTO.getHolidayDate());
		holidayVO.setDay(holidayDTO.getDay());
		holidayVO.setFestival(holidayDTO.getFestival());
	}

	@Override
	public List<HolidayVO> getAllHolidayByOrgId(Long orgId) {
		List<HolidayVO> holidayVO = new ArrayList<>();
		holidayVO = holidayRepo.getAllHolidayByOrgId(orgId);

		return holidayVO;
	}

	@Override
	public HolidayVO getHolidayById(Long id) {
		HolidayVO holidayVO = new HolidayVO();

		holidayVO = holidayRepo.getHolidayById(id);

		return holidayVO;
	}

	@Override
	public HolidayVO uploadHolidayImageInBloob(MultipartFile file, Long id) throws IOException, java.io.IOException {
		HolidayVO holidayVO = holidayRepo.findById(id).get();
		holidayVO.setHolidaysImage(file.getBytes());
		return holidayRepo.save(holidayVO);
	}

	// empstatus

//	@Override
//	public CheckInStatusVO getStatusByEmpcode(String empcode) {
//
//		return checkInStatusRepo.findById(empcode);
//	}

//	@Override
//	public Optional<CheckInStatusVO> getStatusByEmpcode(String empcode) {
//
//		return checkInStatusRepo.findByempcode(empcode);
//	}

	@Override
	public List<Map<String, Object>> getStatusByEmpcode(String empcode) {
		List<Object[]> checkinStatusVO = checkInStatusRepo.findByempcode(empcode);
		return convertResultToMap(checkinStatusVO);
	}

	private List<Map<String, Object>> convertResultToMap(List<Object[]> resultList) {
		List<Map<String, Object>> mappedList = new ArrayList<>();
		for (Object[] ch : resultList) {
			Map<String, Object> map = new HashMap<>();
			map.put("checkInStatusId", ch[0] != null ? ch[0].toString() : "");
			map.put("branch", ch[1] != null ? ch[1].toString() : "");
			map.put("empName", ch[2] != null ? ch[2].toString() : "");
			map.put("empCode", ch[3] != null ? ch[3].toString() : "");
			map.put("orgId", ch[4] != null ? ch[4].toString() : "");
			map.put("status", ch[5] != null ? ch[5].toString() : "");
			map.put("latestIn", ch[6] != null ? ch[6].toString() : "");
			map.put("latestOut", ch[7] != null ? ch[7].toString() : "");

			mappedList.add(map);
		}
		return mappedList;
	}

	public List<Map<String, Object>> getAttendanceByEmpcode(String empcode, int month, String orgId,
			String branchCode) {
		return checkInStatusRepo.findByEmpcode(empcode, month, orgId, branchCode);
	}

	// holiday excel upload

	private int totalRows = 0;
	private int successfulUploads = 0;

	@Override
	public void excelUploadForHolidays(MultipartFile[] files, String createdBy, Long orgId)
			throws EncryptedDocumentException, ApplicationException, IOException {
		totalRows = 0;
		successfulUploads = 0;
		for (MultipartFile file : files) {
			try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
				Sheet sheet = workbook.getSheetAt(0); // Assuming only one sheet
				List<String> errorMessages = new ArrayList<>();
				System.out.println("Processing file: " + file.getOriginalFilename()); // Debug statement
				Row headerRow = sheet.getRow(0);
//				if (!isHeaderValidChargeType(headerRow)) {
//					throw new ApplicationException("Invalid Excel format. Please refer to the sample file.");
//				}
				// Check all rows for validity first
				for (Row row : sheet) {
					if (row.getRowNum() == 0 || isRowEmpty1(row)) {
						continue; // Skip header row and empty rows
					}
					totalRows++; // Increment totalRows
					try {
						// Retrieve cell values based on the provided order
						String department = getStringCellValue1(row.getCell(0));
						LocalDate holidayDate = getDateCellValue(row.getCell(1));
						String day = getStringCellValue1(row.getCell(2));
						String branchCode = getStringCellValue1(row.getCell(3));
						String branchName = getStringCellValue1(row.getCell(4));
						String festival = getStringCellValue1(row.getCell(5));
						// String createdBy
						String activeString = getStringCellValue1(row.getCell(6));

						// Convert activeString to integer and handle the conditions
						boolean active;
						if ("1".equals(activeString)) {
							active = true; // If the value is '1', set active to true
						} else if ("0".equals(activeString)) {
							active = false; // If the value is '0', set active to false
						} else {
							throw new ApplicationException(
									"Invalid value for 'active' field. Expected '1' or '0', but got: " + activeString);
						}
						HolidayVO holidayVO = new HolidayVO();

						if (holidayRepo.existsByOrgIdAndHolidayDate(orgId, holidayDate)) {
							throw new ApplicationException("The given holidayDate already exists.");
						}
						// Create CoaVO and add to appropriate list
						holidayVO.setDepartment(department.toUpperCase());
						holidayVO.setHolidayDate(holidayDate);
						holidayVO.setDay(day.toUpperCase());
						holidayVO.setBranchCode(branchCode.toUpperCase());
						holidayVO.setBranchName(branchName.toUpperCase());
						holidayVO.setFestival(festival.toUpperCase());
						holidayVO.setActive(active);
						holidayVO.setOrgId(orgId);

						holidayRepo.save(holidayVO);
						successfulUploads++; // Increment successfulUploads
					} catch (Exception e) {
						errorMessages.add("Error processing row " + (row.getRowNum() + 1) + ": " + e.getMessage());
					}

				}
				if (!errorMessages.isEmpty()) {
					throw new ApplicationException(
							"Excel upload validation failed. Errors: " + String.join(", ", errorMessages));
				}
			} catch (IOException e) {
				throw new ApplicationException(
						"Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());

			}
		}
	}

	private boolean isHeaderValid1(Row headerRow) {
		if (headerRow == null) {
			return false;
		}
		// Adjust based on the actual header names in your Excel
		return "Department".equalsIgnoreCase(getStringCellValue1(headerRow.getCell(0)))
				&& "HolidayDate".equals(getStringCellValue1(headerRow.getCell(1)))
				&& "Day".equalsIgnoreCase(getStringCellValue1(headerRow.getCell(2)))
				&& "BranchCode".equalsIgnoreCase(getStringCellValue1(headerRow.getCell(3)))
				&& "BranchName".equalsIgnoreCase(getStringCellValue1(headerRow.getCell(4)))
				&& "Festival".equalsIgnoreCase(getStringCellValue1(headerRow.getCell(5)))
				&& "Active".equalsIgnoreCase(getStringCellValue1(headerRow.getCell(6)))
				&& "OrgId".equals(getStringCellValue1(headerRow.getCell(7)));

	}

	private String getStringCellValue1(Cell cell) {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue());
			} else {
				double numericValue = cell.getNumericCellValue();
				if (numericValue == (int) numericValue) {
					return String.valueOf((int) numericValue);
				} else {
					return BigDecimal.valueOf(numericValue).toPlainString();
				}
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		default:
			return "";
		}
	}

	private LocalDate getDateCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getLocalDateTimeCellValue().toLocalDate(); // ✅ Directly return LocalDate
			}
			break;
		case FORMULA:
			if (cell.getCachedFormulaResultType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
				return cell.getLocalDateTimeCellValue().toLocalDate(); // ✅ Directly return LocalDate
			}
			break;
		default:
			break;
		}

		return null; // Return null if the cell does not contain a valid date
	}

	private boolean isRowEmpty1(Row row) {
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

	private String getStringCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue());
			} else {
				double numericValue = cell.getNumericCellValue();
				if (numericValue == (int) numericValue) {
					return String.valueOf((int) numericValue);
				} else {
					return BigDecimal.valueOf(numericValue).toPlainString();
				}
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		default:
			return "";
		}
	}

	private boolean isRowEmpty(Row row) {
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getTotalRows() {
		return totalRows;
	}

	@Override
	public int getSuccessfulUploads() {
		return successfulUploads;
	}

//Circular

	@Override
	public Map<String, Object> createUpdatecircular(CircularDTO circularDTO) throws ApplicationException {
		CircularVO circularVO;
		String message = null;

		if (ObjectUtils.isEmpty(circularDTO.getId())) {

			circularVO = new CircularVO();
			circularVO.setCreatedBy(circularDTO.getCreatedBy());
			circularVO.setUpdatedBy(circularDTO.getCreatedBy());
			message = "Circular Creation SuccessFully";
		} else {
			// Update existing branch
			circularVO = circularRepo.findById(circularDTO.getId())
					.orElseThrow(() -> new ApplicationException("Circular not found with id: " + circularDTO.getId()));
			circularVO.setUpdatedBy(circularDTO.getCreatedBy());

			message = "Circular Update Successfully";
		}

		getCircularVOFromCircularDTO(circularVO, circularDTO);
		circularRepo.save(circularVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("circularVO", circularVO);
		return response;

	}

	private void getCircularVOFromCircularDTO(CircularVO circularVO, CircularDTO circularDTO) {
		circularVO.setOrgId(circularDTO.getOrgId());
//	holidayVO.setBranchId(holidayDTO.getBranchId());
		circularVO.setBranchCode(circularDTO.getBranchCode());
		circularVO.setBranchName(circularDTO.getBranchName());
		if (circularDTO.getDepartment() != null) {
			circularVO.setDepartment(circularDTO.getDepartment());
		} else {
			circularVO.setDepartment("ALL");
		}
		circularVO.setCircularcontent(circularDTO.getCircularcontent());
		circularVO.setCircularTopic(circularDTO.getCircularTopic());
//		circularVO.setFinYear(circularDTO.getFinYear());
		circularVO.setExpiresDate(circularDTO.getExpiresDate());
		circularVO.setType(circularDTO.getType());
	}

	@Override
	public List<CircularVO> getAllCircularByOrgId(Long orgId, String branchCode, String department, String type) {
		List<CircularVO> circularVO = new ArrayList<>();
		circularVO = circularRepo.getAllCircularsByOrgId(orgId, branchCode, department, type);

		return circularVO;
	}

	@Override
	public CircularVO getCircularById(Long id) {
		CircularVO circularVO = new CircularVO();

		circularVO = circularRepo.getCircularsById(id);

		return circularVO;
	}

//Polls//

	public Map<String, Object> createUpdatepolls(PollsDTO pollsDTO) throws ApplicationException {
		PollsVO pollsVO;
		String message;

		// Check if PollsDTO ID is present for updating or creating
		if (pollsDTO.getId() == null) {
			pollsVO = new PollsVO();
			pollsVO.setCreatedBy(pollsDTO.getCreatedBy());
			pollsVO.setUpdatedBy(pollsDTO.getCreatedBy());
			message = "Poll Creation Success";
		} else {
			pollsVO = pollsRepo.findById(pollsDTO.getId())
					.orElseThrow(() -> new ApplicationException("Poll Not Found with ID: " + pollsDTO.getId()));
			pollsVO.setUpdatedBy(pollsDTO.getCreatedBy());
			message = "Poll Update Success";
		}

		// Set data in PollsVO from PollsDTO
		pollsVO = getPollsVOFromPollsDTO(pollsVO, pollsDTO);
		pollsRepo.save(pollsVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("pollsVO", pollsVO);
		return response;
	}

	private PollsVO getPollsVOFromPollsDTO(PollsVO pollsVO, @Valid PollsDTO pollsDTO) {
		// Set basic Poll data
		if (pollsDTO.getDepartment() != null) {
			pollsVO.setDepartment(pollsDTO.getDepartment());
		} else {
			pollsVO.setDepartment("ALL");
		}
		pollsVO.setBranchCode(pollsDTO.getBranchCode());
		pollsVO.setBranchName(pollsDTO.getBranchName());
		pollsVO.setOrgId(pollsDTO.getOrgId());
		pollsVO.setMaxSelection(pollsDTO.getMaxSelection());
		pollsVO.setMultiSelect(pollsDTO.getMultiSelect());
		pollsVO.setQuestion(pollsDTO.getQuestion());
		pollsVO.setExpiresDate(pollsDTO.getExpiresDate());
		pollsVO.setType(pollsDTO.getType());

		// If updating, remove old poll details
		if (pollsDTO.getId() != null) {
			List<PollDetailsVO> pollDetailsVOs = pollDetailsRepo.findByPollsVO(pollsVO);
			pollDetailsRepo.deleteAll(pollDetailsVOs);
		}

		// Set Poll Details from PollDetailsDTO
		List<PollDetailsVO> pollDetailsVOs = new ArrayList<>();
		for (PollDetailsDTO pollDetailsDTO : pollsDTO.getPollDetailsDTO()) {
			PollDetailsVO pollDetailsVO = new PollDetailsVO();
			pollDetailsVO.setOptions(pollDetailsDTO.getOptions());
			pollDetailsVO.setPollsVO(pollsVO); // Set parent reference in child
			pollDetailsVOs.add(pollDetailsVO);
		}
		pollsVO.setPollDetailsVO(pollDetailsVOs);
		return pollsVO;
	}

	@Override
	public List<PollsVO> getAllPollsByOrgId(Long orgId, String branchCode, String department, String type) {
		return pollsRepo.getAllPollsByOrgId(orgId, branchCode, department, type);
	}

	@Override
	public PollsVO getPollById(Long id) {
		PollsVO pollsVO = new PollsVO();

		pollsVO = pollsRepo.getPollsById(id);

		return pollsVO;
	}

//@Override
//public Map<String, Object> createUpdatepollVote(PollVoteDTO pollVoteDTO) throws ApplicationException {
//	// TODO Auto-generated method stub
//	return null;
//}

//	@Override
//	public Map<String, Object> createUpdatepollVote(List<PollVoteDTO> pollVoteDTO1) throws ApplicationException {
//
//		List<PollVoteVO> pollVoteVO1 = new ArrayList<>();
//		String message = null;
//
//		for (PollVoteDTO pollVoteDTO : pollVoteDTO1) {
//			
//			List<PollVoteVO> vo = pollVoteRepo.findByOrgIdAndPollIdAndUserNameIgnoreCase(pollVoteDTO.getOrgId(),
//					pollVoteDTO.getPollId(), pollVoteDTO.getUserName());
//			if (!vo.isEmpty()) {
//			    pollVoteRepo.deleteAll(vo);
//			}
//		
//			PollVoteVO pollVoteVO = new PollVoteVO();
//			pollVoteVO.setDepartment(pollVoteDTO.getDepartment());
//			pollVoteVO.setBranchCode(pollVoteDTO.getBranchCode());
//			pollVoteVO.setOrgId(pollVoteDTO.getOrgId());
//			pollVoteVO.setBranchName(pollVoteDTO.getBranchName());
//			pollVoteVO.setOptions(pollVoteDTO.getOptions());
//			pollVoteVO.setPollId(pollVoteDTO.getPollId());
//			pollVoteVO.setQuestion(pollVoteDTO.getQuestion());
//			pollVoteVO.setUserName(pollVoteDTO.getUserName());
//			pollVoteVO1.add(pollVoteVO);
//		}
//		pollVoteRepo.saveAll(pollVoteVO1);
//		message="Successfully Voted";
//		Map<String, Object> response = new HashMap<>();
//		response.put("message", message);
//		response.put("pollVoteVO1", pollVoteVO1);
//		return response;
//
//	}

	@Override
	public Map<String, Object> createUpdatepollVote(List<PollVoteDTO> pollVoteDTO1) throws ApplicationException {
		List<PollVoteVO> pollVoteVO1 = new ArrayList<>();
		String message = "Successfully Voted";

		for (PollVoteDTO pollVoteDTO : pollVoteDTO1) {
			// Check if poll is active
			Optional<PollsVO> poll = pollsRepo.findById(pollVoteDTO.getPollId());

			if (!poll.isPresent() || Boolean.FALSE.equals(poll.get().getActive())) {
				throw new ApplicationException(
						"Poll with ID " + pollVoteDTO.getPollId() + " is inactive or does not exist.");
			}

			// Fetch existing votes
			List<PollVoteVO> vo = pollVoteRepo.findByOrgIdAndPollIdAndUserNameIgnoreCase(pollVoteDTO.getOrgId(),
					pollVoteDTO.getPollId(), pollVoteDTO.getUserName());

			// Delete existing votes if any
			if (vo != null && !vo.isEmpty()) {
				pollVoteRepo.deleteAll(vo);
				pollVoteRepo.flush(); // Ensure deletion is committed
			}

			// Create new vote entry
			PollVoteVO pollVoteVO = new PollVoteVO();
			pollVoteVO.setDepartment(pollVoteDTO.getDepartment());
			pollVoteVO.setBranchCode(pollVoteDTO.getBranchCode());
			pollVoteVO.setOrgId(pollVoteDTO.getOrgId());
			pollVoteVO.setBranchName(pollVoteDTO.getBranchName());
			pollVoteVO.setOptions(pollVoteDTO.getOptions());
			pollVoteVO.setPollId(pollVoteDTO.getPollId());
			pollVoteVO.setQuestion(pollVoteDTO.getQuestion());
			pollVoteVO.setUserName(pollVoteDTO.getUserName());

			pollVoteVO1.add(pollVoteVO);
		}

		// Batch insert new votes
		pollVoteRepo.saveAll(pollVoteVO1);

		// Response
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("pollVoteVO1", pollVoteVO1);
		return response;
	}

	@Override
	public List<Map<String, Object>> getPollResultForUser(Long orgId, String userName, Long pollId) {

		Set<Object[]> pollVoteVO = pollVoteRepo.findPollResultForUser(orgId, userName, pollId);
		return getPollResultForUser(pollVoteVO);
	}

	private List<Map<String, Object>> getPollResultForUser(Set<Object[]> pollVoteVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : pollVoteVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("branchcode", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("department", ch[1] != null ? ch[1].toString() : "");
			map.put("options", ch[2] != null ? ch[2].toString() : "");
			map.put("orgid", ch[3] != null ? ch[3].toString() : "");
			map.put("pollid", ch[4] != null ? ch[4].toString() : "");
			map.put("question", ch[5] != null ? ch[5].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getPollResultForHR(Long orgId, Long pollId) {
		Set<Object[]> pollVoteVO = pollVoteRepo.findPollResultForHR(orgId, pollId);
		return getPollResultForHR(pollVoteVO);
	}

	private List<Map<String, Object>> getPollResultForHR(Set<Object[]> pollVoteVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : pollVoteVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("options", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("Count", ch[1] != null ? ch[1].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public Map<String, Object> createUpdateAnnouncement(AnnouncementDTO announcementDTO) throws ApplicationException {
		AnnouncementVO announcementVO;
		String message = null;

		if (ObjectUtils.isEmpty(announcementDTO.getId())) {

			announcementVO = new AnnouncementVO();
			announcementVO.setCreatedBy(announcementDTO.getCreatedBy());
			announcementVO.setUpdatedBy(announcementDTO.getCreatedBy());
			message = "Announcement Creation SuccessFully";
		} else {
			// Update existing branch
			announcementVO = announcementRepo.findById(announcementDTO.getId()).orElseThrow(
					() -> new ApplicationException("Holiday not found with id: " + announcementDTO.getId()));
			announcementVO.setUpdatedBy(announcementDTO.getCreatedBy());

			message = "Announcement Update Successfully";
		}

		getAnnouncementVOFromAnnouncementDTO(announcementVO, announcementDTO);
		announcementRepo.save(announcementVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("announcementVO", announcementVO);
		return response;

	}

	private void getAnnouncementVOFromAnnouncementDTO(AnnouncementVO announcementVO, AnnouncementDTO announcementDTO) {
		announcementVO.setOrgId(announcementDTO.getOrgId());
//		holidayVO.setBranchId(holidayDTO.getBranchId());
		announcementVO.setBranchCode(announcementDTO.getBranchCode());
		announcementVO.setBranchName(announcementDTO.getBranchName());
		announcementVO.setDepartment(announcementDTO.getDepartment());
		announcementVO.setAnnouncement(announcementDTO.getAnnouncement());
		announcementVO.setTopic(announcementDTO.getTopic());
		announcementVO.setExpiresDate(announcementDTO.getExpiresDate());

	}

	@Override
	public List<AnnouncementVO> GetAnnouncementByOrgId(Long orgId, String branchCode) {
		List<AnnouncementVO> announcementVO = new ArrayList<>();
		announcementVO = announcementRepo.GetAnnouncementsByOrgId(orgId, branchCode);

		return announcementVO;
	}

	@Override
	public AnnouncementVO GetAnnouncementById(Long id) {
		AnnouncementVO announcementVO = new AnnouncementVO();
		announcementVO = announcementRepo.getAnnouncementById(id);
		return announcementVO;

	}

	public CircularVO uploadPostImageInBloob(MultipartFile file, Long id) throws IOException {
		CircularVO circularVO = circularRepo.findById(id).orElseThrow(() -> new RuntimeException("Circular not found"));
		circularVO.setPostImage(file.getBytes()); // Store image as byte array
		return circularRepo.save(circularVO);
	}

	// Praise//
	@Override
	public Map<String, Object> createUpdatePraise(PraiseDTO praiseDTO) throws ApplicationException {
		PraiseVO praiseVO;
		String message = null;

		if (ObjectUtils.isEmpty(praiseDTO.getId())) {

			praiseVO = new PraiseVO();
			message = "Praise Creation SuccessFully";
		} else {
			// Update existing branch
			praiseVO = praiseRepo.findById(praiseDTO.getId())
					.orElseThrow(() -> new ApplicationException("Holiday not found with id: " + praiseDTO.getId()));

			message = "Announcement Update Successfully";
		}

		getPraiseVOFromPraiseDTO(praiseVO, praiseDTO);
		praiseRepo.save(praiseVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("praiseVO", praiseVO);
		return response;

	}

	private void getPraiseVOFromPraiseDTO(PraiseVO praiseVO, PraiseDTO praiseDTO) {

		praiseVO.setDepartment(praiseDTO.getDepartment());
		praiseVO.setBranchCode(praiseDTO.getBranchCode());
		praiseVO.setOrgId(praiseDTO.getOrgId());
		praiseVO.setBranchName(praiseDTO.getBranchName());
		praiseVO.setCircularId(praiseDTO.getCircularId());
		praiseVO.setLiked(praiseDTO.getLiked());
		praiseVO.setUserName(praiseDTO.getUserName());

	}

	@Override
	public List<Map<String, Object>> GetCountOfPraiseByOrgIdAndCircularId(Long circularId, Long Orgid) {
		Set<Object[]> praiseVO = praiseRepo.findCountOfPraiseByOrgIdAndCircularId(circularId, Orgid);
		return GetCountOfPraiseByOrgIdAndCircularId(praiseVO);
	}

	private List<Map<String, Object>> GetCountOfPraiseByOrgIdAndCircularId(Set<Object[]> praiseVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : praiseVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("Count", ch[0] != null ? ch[0].toString() : ""); // Empty string if null

			List1.add(map);
		}
		return List1;

	}
//		@Override
//		public List<PraiseVO> GetCountOfPraiseByOrgIdAndCircularId(Long circularId, Long Orgid) {
//			List<PraiseVO> praiseVO = new ArrayList<>();
//			praiseVO = praiseRepo.findCountOfPraiseByOrgIdAndCircularId(circularId,Orgid);
//
//			return praiseVO;
//		}

	@Override
	public Map<String, Object> createUpdateTask(TaskDTO taskDTO) throws ApplicationException {
		TaskVO taskVO;
		String message = null;

		if (ObjectUtils.isEmpty(taskDTO.getId())) {

			taskVO = new TaskVO();
			message = "Task Creation SuccessFully";
		} else {
			// Update existing branch
			taskVO = taskRepo.findById(taskDTO.getId())
					.orElseThrow(() -> new ApplicationException("Task not found with id: " + taskDTO.getId()));

			message = "Task Update Successfully";
		}

		getTaskVOFromTaskDTO(taskVO, taskDTO);
		taskRepo.save(taskVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("taskVO", taskVO);
		return response;

	}

	private void getTaskVOFromTaskDTO(TaskVO taskVO, TaskDTO taskDTO) {

		taskVO.setDepartment(taskDTO.getDepartment());
		taskVO.setBranchCode(taskDTO.getBranchCode());
		taskVO.setOrgId(taskDTO.getOrgId());
		taskVO.setBranchName(taskDTO.getBranchName());
		taskVO.setTaskTitle(taskDTO.getTaskTitle());
		taskVO.setTaskDescription(taskDTO.getTaskDescription());
		taskVO.setCreatedBy(taskDTO.getCreatedBy());
		taskVO.setDueDate(taskDTO.getDueDate());
		taskVO.setAssignedBy(taskDTO.getAssignedBy());
		taskVO.setAssignedTo(taskDTO.getAssignedTo());
		taskVO.setCategory(taskDTO.getCategory());
		taskVO.setPriority(taskDTO.getPriority());
		taskVO.setStatus(taskDTO.getStatus());
		taskVO.setUpdatedBy(taskDTO.getUpdatedBy());
		taskVO.setRemarks(taskDTO.getRemarks());
		taskVO.setFinYear(taskDTO.getFinYear());
		taskVO.setUserName(taskDTO.getUserName());

	}

	@Override
	public List<Map<String, Object>> GetNewTask(String Assignedto, Long Orgid) {
		Set<Object[]> taskVO = taskRepo.findNewTaskByOrgIdAndAssignedto(Assignedto, Orgid);
		return GetNewTask(taskVO);
	}

	private List<Map<String, Object>> GetNewTask(Set<Object[]> taskVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : taskVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("taskid", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("assignedby", ch[1] != null ? ch[1].toString() : "");
			map.put("assignedto", ch[2] != null ? ch[2].toString() : "");
			map.put("category", ch[3] != null ? ch[3].toString() : "");
			map.put("department", ch[4] != null ? ch[4].toString() : "");
			map.put("duedate", ch[5] != null ? ch[5].toString() : "");
			map.put("priority", ch[6] != null ? ch[6].toString() : "");
			map.put("status", ch[7] != null ? ch[7].toString() : "");
			map.put("taskdescription", ch[8] != null ? ch[8].toString() : "");
			map.put("tasktitle", ch[9] != null ? ch[9].toString() : "");
			map.put("remarks", ch[10] != null ? ch[10].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetPendingTask(String Assignedto, Long Orgid) {
		Set<Object[]> taskVO = taskRepo.findPendingTaskByOrgIdAndAssignedto(Assignedto, Orgid);
		return GetNewTask(taskVO);
	}

	private List<Map<String, Object>> GetPendingTask(Set<Object[]> taskVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : taskVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("taskid", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("assignedby", ch[1] != null ? ch[1].toString() : "");
			map.put("assignedto", ch[2] != null ? ch[2].toString() : "");
			map.put("category", ch[3] != null ? ch[3].toString() : "");
			map.put("department", ch[4] != null ? ch[4].toString() : "");
			map.put("duedate", ch[5] != null ? ch[5].toString() : "");
			map.put("priority", ch[6] != null ? ch[6].toString() : "");
			map.put("status", ch[7] != null ? ch[7].toString() : "");
			map.put("taskdescription", ch[8] != null ? ch[8].toString() : "");
			map.put("tasktitle", ch[9] != null ? ch[9].toString() : "");
			map.put("remarks", ch[10] != null ? ch[10].toString() : "");

			// taskid, assignedby, assignedto, category, department, duedate, orgid,
			// priority, status, taskdescription, tasktitle

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetCountofPendingTask(String Assignedto, Long Orgid) {
		Set<Object[]> taskVO = taskRepo.findCountofPendingTaskByOrgIdAndAssignedto(Assignedto, Orgid);
		return GetCountofPendingTask(taskVO);
	}

	private List<Map<String, Object>> GetCountofPendingTask(Set<Object[]> taskVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : taskVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("count", ch[0] != null ? ch[0].toString() : ""); //

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetCountofNewTask(String Assignedto, Long Orgid) {
		Set<Object[]> taskVO = taskRepo.findCountNewTaskByOrgIdAndAssignedto(Assignedto, Orgid);
		return GetCountofNewTask(taskVO);
	}

	private List<Map<String, Object>> GetCountofNewTask(Set<Object[]> taskVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : taskVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("count", ch[0] != null ? ch[0].toString() : ""); // Empty string if null

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetNewAssignedTask(String Assignedby, Long Orgid) {
		Set<Object[]> taskVO = taskRepo.findNewTaskByOrgIdAndAssignedby(Assignedby, Orgid);
		return GetNewAssignedTask(taskVO);
	}

	private List<Map<String, Object>> GetNewAssignedTask(Set<Object[]> taskVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : taskVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("taskid", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("assignedby", ch[1] != null ? ch[1].toString() : "");
			map.put("assignedto", ch[2] != null ? ch[2].toString() : "");
			map.put("category", ch[3] != null ? ch[3].toString() : "");
			map.put("department", ch[4] != null ? ch[4].toString() : "");
			map.put("duedate", ch[5] != null ? ch[5].toString() : "");
			map.put("priority", ch[6] != null ? ch[6].toString() : "");
			map.put("status", ch[7] != null ? ch[7].toString() : "");
			map.put("taskdescription", ch[8] != null ? ch[8].toString() : "");
			map.put("tasktitle", ch[9] != null ? ch[9].toString() : "");
			map.put("remarks", ch[10] != null ? ch[10].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetPendingAssignedTask(String Assignedby, Long Orgid) {
		Set<Object[]> taskVO = taskRepo.findPendingTaskByOrgIdAndAssignedby(Assignedby, Orgid);
		return GetPendingAssignedTask(taskVO);
	}

	private List<Map<String, Object>> GetPendingAssignedTask(Set<Object[]> taskVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : taskVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("taskid", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("assignedby", ch[1] != null ? ch[1].toString() : "");
			map.put("assignedto", ch[2] != null ? ch[2].toString() : "");
			map.put("category", ch[3] != null ? ch[3].toString() : "");
			map.put("department", ch[4] != null ? ch[4].toString() : "");
			map.put("duedate", ch[5] != null ? ch[5].toString() : "");
			map.put("priority", ch[6] != null ? ch[6].toString() : "");
			map.put("status", ch[7] != null ? ch[7].toString() : "");
			map.put("taskdescription", ch[8] != null ? ch[8].toString() : "");
			map.put("tasktitle", ch[9] != null ? ch[9].toString() : "");
			map.put("remarks", ch[10] != null ? ch[10].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetCountofPendingAssignedTask(String Assignedby, Long Orgid) {
		Set<Object[]> taskVO = taskRepo.findCountofPendingTaskByOrgIdAndAssignedby(Assignedby, Orgid);
		return GetCountofPendingAssignedTask(taskVO);
	}

	private List<Map<String, Object>> GetCountofPendingAssignedTask(Set<Object[]> taskVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : taskVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("count", ch[0] != null ? ch[0].toString() : ""); //

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetCountofNewAssignedTask(Long Orgid, String Assignedby) {
		Set<Object[]> taskVO = taskRepo.findCountofNewTaskByOrgIdAndAssignedby(Orgid, Assignedby);
		return GetCountofNewAssignedTask(taskVO);
	}

	private List<Map<String, Object>> GetCountofNewAssignedTask(Set<Object[]> taskVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : taskVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("count", ch[0] != null ? ch[0].toString() : ""); //

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getEmpDob(Long orgId) {
		Set<Object[]> chType = employeeRepo.getEmpDob(orgId);
		return getDob(chType);
	}

	private List<Map<String, Object>> getDob(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("empName", ch[0] != null ? ch[0].toString() : null);
			map.put("empCode", ch[1] != null ? ch[1].toString() : null);
			map.put("dob", ch[2] != null ? ch[2].toString() : null);
			if (ch[3] != null && ch[3] instanceof byte[]) {
				byte[] imageBytes = (byte[]) ch[3];
				String base64Image = Base64.getEncoder().encodeToString(imageBytes);
				map.put("profileImage", base64Image);
			} else {
				map.put("profileImage", null);
			}

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetworkAniversary(Long Orgid) {
		Set<Object[]> employeeVO = employeeRepo.findWorkaniversaryByOrgId(Orgid);
		return GetworkAniversary(employeeVO);
	}

	private List<Map<String, Object>> GetworkAniversary(Set<Object[]> employeeVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : employeeVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeid", ch[0] != null ? ch[0].toString() : "");
			map.put("department", ch[1] != null ? ch[1].toString() : "");
			map.put("designation", ch[2] != null ? ch[2].toString() : "");
			map.put("employeecode", ch[3] != null ? ch[3].toString() : "");
			map.put("employee", ch[4] != null ? ch[4].toString() : "");
			map.put("gender", ch[5] != null ? ch[5].toString() : "");
			map.put("orgid", ch[6] != null ? ch[6].toString() : "");
			map.put("noofyears", ch[7] != null ? ch[7].toString() : "");
			if (ch[8] != null && ch[8] instanceof byte[]) {
				byte[] imageBytes = (byte[]) ch[8];
				String base64Image = Base64.getEncoder().encodeToString(imageBytes);
				map.put("profileImage", base64Image);
			} else {
				map.put("profileImage", null);
			}

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetnewJoineDetails(Long Orgid) {
		Set<Object[]> employeeVO = employeeRepo.findNewJoinieDtailsByOrgId(Orgid);
		return GetnewJoineDetails(employeeVO);
	}

	private List<Map<String, Object>> GetnewJoineDetails(Set<Object[]> employeeVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : employeeVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeid", ch[0] != null ? ch[0].toString() : "");
			map.put("department", ch[1] != null ? ch[1].toString() : "");
			map.put("designation", ch[2] != null ? ch[2].toString() : "");
			map.put("employeecode", ch[3] != null ? ch[3].toString() : "");
			map.put("employee", ch[4] != null ? ch[4].toString() : "");
			map.put("gender", ch[5] != null ? ch[5].toString() : "");
			map.put("orgid", ch[6] != null ? ch[6].toString() : "");
			if (ch[7] != null && ch[7] instanceof byte[]) {
				byte[] imageBytes = (byte[]) ch[7];
				String base64Image = Base64.getEncoder().encodeToString(imageBytes);
				map.put("profileImage", base64Image);
			} else {
				map.put("profileImage", null);
			}
			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getpayslipemployeedetails(Long orgId, String employeeCode) {
		// Fetch the raw data (salary process details)
		Set<Object[]> salaryProcessVO = salaryProcessRepo.findpayslipemployeeandearningsdetails(orgId, employeeCode);

		// Process the fetched data
		return getPayslipEmployeeDetails(salaryProcessVO);
	}

	private List<Map<String, Object>> getPayslipEmployeeDetails(Set<Object[]> salaryProcessVO) {
		List<Map<String, Object>> list = new ArrayList<>();

		for (Object[] ch : salaryProcessVO) {
			// Assuming ch[11] holds the effective working days and ch[4] holds salary
			// (possibly a String)

			Map<String, Object> map = new HashMap<>();

			// Safely put values into the map, avoiding nulls and casting errors
			map.put("employee", ch[0] != null ? ch[0].toString() : ""); // Employee Name or ID
			map.put("orgid", ch[1] != null ? ch[1].toString() : ""); // Organization ID
			map.put("employeecode", ch[2] != null ? ch[2].toString() : ""); // Employee Code
			map.put("joiningdate", ch[3] != null ? ch[3].toString() : ""); // Joining Date
			map.put("designation", ch[4] != null ? ch[4].toString() : ""); // Designation
			map.put("department", ch[5] != null ? ch[5].toString() : ""); // Department
			map.put("branch", ch[6] != null ? ch[6].toString() : ""); // Branch
			map.put("branchcode", ch[7] != null ? ch[7].toString() : ""); // Branch Code
			map.put("accountno", ch[8] != null ? ch[8].toString() : ""); // Account Number
			map.put("panno", ch[9] != null ? ch[9].toString() : ""); // PAN Number
			map.put("uanno", ch[10] != null ? ch[10].toString() : ""); // UAN Number
			map.put("effectiveworkingdays", ch[11] != null ? ch[11].toString() : ""); // Effective Working Days
			map.put("monthDays", ch[12] != null ? ch[12].toString() : ""); 
			map.put("bankName", ch[13] != null ? ch[13].toString() : ""); // Bank Name
			map.put("lop", ch[14] != null ? ch[14].toString() : ""); 
			map.put("otHours", ch[15] != null ? ch[15].toString() : ""); 



			// Add the map to the result list
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getpayslipearningdetails(Long orgId, String employeeCode, Long month, Long year) {

		// 🔍 Check if salary process exists for the selected month and year
		List<SalaryProcessVO> salaryProcessCheck = salaryProcessRepo.findByOrgIdAndEmployeeCodeAndMonthAndYear(orgId,
				employeeCode, month, String.valueOf(year) // convert Long to String because year is a String in DB
		);

		if (salaryProcessCheck == null || salaryProcessCheck.isEmpty()) {
			throw new RuntimeException("Payslip not generated for the selected month.");
		}

		// ✅ Proceed to fetch payslip earnings details
		List<Object[]> salaryprocessVO = salaryProcessRepo.findpayslipearningsdetails(orgId, employeeCode, month, year);

		return getpayslipearningdetails(salaryprocessVO, employeeCode, month, year);
	}

	private List<Map<String, Object>> getpayslipearningdetails(List<Object[]> salaryprocessVO, String employeeCode,
			Long month, Long year) {
		List<Map<String, Object>> list = new ArrayList<>();

		EmployeeVO employeeVO = employeeRepo.findByEmployeeCode(employeeCode);
		if (employeeVO == null) {
			throw new RuntimeException("Employee not found for code: " + employeeCode);
		}

		LocalDate paySlipEffectiveDate = employeeVO.getPayslipEffectiveDate();
		LocalDate payslipDate = LocalDate.of(year.intValue(), month.intValue(), 1);
		
		LocalDate joiningDate = employeeVO.getJoiningDate();

//			    System.out.println(joiningDate);
//			    System.out.println(payslipDate);

		if (payslipDate.isBefore(paySlipEffectiveDate.withDayOfMonth(1))) {
			throw new RuntimeException("Payslip date is before the employee's PaySlipEffectiveDate date.");
		}

		for (Object[] ch : salaryprocessVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("employee", ch[0] != null ? ch[0].toString() : "");
			map.put("orgid", ch[1] != null ? ch[1].toString() : "");
			map.put("employeecode", ch[2] != null ? ch[2].toString() : "");
			map.put("heading", ch[3] != null ? ch[3].toString() : "");
			map.put("amount", ch[4] != null ? ch[4].toString() : "");
			map.put("totalCompanyWorkingDays", ch[5] != null ? ch[5].toString() : "");
			map.put("empSalaryDays", ch[6] != null ? ch[6].toString() : "");
			map.put("actuals", ch[7] != null ? ch[7].toString() : "");
			map.put("joiningDate", joiningDate != null ? joiningDate.toString() : "");
			list.add(map);
		}

		return list;
	}

	@Override
	public List<Map<String, Object>> getpayslipdeductiondetails(Long orgId, String employeeCode, Long month, Long year) {
	    // Use List instead of Set to preserve duplicates
	    List<Object[]> salaryprocessVO = salaryProcessRepo.findpayslipdeductionsdetails(orgId, employeeCode, month, year);
	    return mapPayslipDeductionDetails(salaryprocessVO);
	}

	private List<Map<String, Object>> mapPayslipDeductionDetails(List<Object[]> salaryprocessVO) {
	    List<Map<String, Object>> list = new ArrayList<>();
	    for (Object[] row : salaryprocessVO) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("employee", row[0] != null ? row[0].toString() : "");
	        map.put("orgid", row[1] != null ? row[1].toString() : "");
	        map.put("employeecode", row[2] != null ? row[2].toString() : "");
	        map.put("heading", row[3] != null ? row[3].toString() : "");
	        map.put("amount", row[4] != null ? new BigDecimal(row[4].toString()) : BigDecimal.ZERO);
	        list.add(map);
	    }
	    return list;
	}


	@Override
	public List<Map<String, Object>> getpayslipCompanydetails(Long orgId) {
		Set<Object[]> salaryprocessVO = salaryProcessRepo.findpayslipcompanydetails(orgId);
		return getpayslipCompanydetails(salaryprocessVO);
	}

	private List<Map<String, Object>> getpayslipCompanydetails(Set<Object[]> salaryprocessVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : salaryprocessVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("companyid", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("companycode", ch[1] != null ? ch[1].toString() : "");
			map.put("companyname", ch[2] != null ? ch[2].toString() : "");
			map.put("address", ch[3] != null ? ch[3].toString() : "");
			map.put("city", ch[4] != null ? ch[4].toString() : "");
			map.put("state", ch[5] != null ? ch[5].toString() : "");
			map.put("zipcode", ch[6] != null ? ch[6].toString() : "");
			map.put("companylogo", ch[7] != null ? (byte[]) ch[7] : null);

			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getpayslipPayOnHandAmount(Long orgId, String employeeCode, Long month, String year) {
	    Set<BigDecimal> raw = salaryProcessRepo.getpayslipPayOnHandAmount(orgId, employeeCode, month, year);
	    List<Map<String, Object>> result = new ArrayList<>();

	    for (BigDecimal payOnHand : raw) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("payOnHand", payOnHand != null ? payOnHand : BigDecimal.ZERO);
	        result.add(map);
	    }
	    return result;
	}


	
	@Override
	public List<Map<String, Object>> getpaysliphandsondetails(Long orgId, String Employeecode, Long Month, Long year) {
		Set<Object[]> raw = salaryProcessRepo.findpayslipshandsondetails(orgId, Employeecode, Month, year);
		return getpaysliphandsondetails(raw);
	}

	private List<Map<String, Object>> getpaysliphandsondetails(Set<Object[]> raw) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : raw) {
			Map<String, Object> map = new HashMap<>();
			map.put("employee", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("orgid", ch[1] != null ? ch[1].toString() : "");
			map.put("employeecode", ch[2] != null ? ch[2].toString() : "");
			map.put("netpay", ch[3] != null ? ch[3].toString() : "");

			List1.add(map);
		}
		return List1;
	}
	
	@Override
	public List<Map<String, Object>> getTodayAttendanceReportByOrgId(Long orgId, String branch, String date) {
		return checkInStatusRepo.getTodayAttendanceReportByOrgId(orgId, branch, date);
	}

	// Calendar

	@Override
	public Map<String, Object> createUpdateCalendar(CalendarDTO calendarDTO) throws ApplicationException {

		CalendarVO calendarVO;
		String message = null;

		if (ObjectUtils.isEmpty(calendarDTO.getId())) {

			calendarVO = new CalendarVO();
			calendarVO.setCreatedBy(calendarDTO.getCreatedBy());
			calendarVO.setUpdatedBy(calendarDTO.getCreatedBy());
			message = "calendar Creation SuccessFully";
		} else {
			// Update existing branch
			calendarVO = calendarRepo.findById(calendarDTO.getId())
					.orElseThrow(() -> new ApplicationException("calendar not found with id: " + calendarDTO.getId()));
			calendarVO.setUpdatedBy(calendarDTO.getCreatedBy());

			message = "calendar Update Successfully";
		}

		getCalendarVOFromCalendarDTO(calendarVO, calendarDTO);
		calendarRepo.save(calendarVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("calendarVO", calendarVO);
		return response;

	}

	private void getCalendarVOFromCalendarDTO(CalendarVO calendarVO, CalendarDTO calendarDTO) {
		calendarVO.setOrgId(calendarDTO.getOrgId());
		calendarVO.setBranchCode(calendarDTO.getBranchCode());
		calendarVO.setBranchName(calendarDTO.getBranchName());
		calendarVO.setDepartment(calendarDTO.getDepartment());
		calendarVO.setDate(calendarDTO.getDate());
		calendarVO.setEventTitle(calendarDTO.getEventTitle());
		calendarVO.setEventType(calendarDTO.getEventType());
		calendarVO.setDescription(calendarDTO.getDescription());
		calendarVO.setEmpName(calendarDTO.getEmpName());
		calendarVO.setEmpCode(calendarDTO.getEmpCode());
		calendarVO.setFromTime(calendarDTO.getFromTime());
		calendarVO.setToTime(calendarDTO.getToTime());

	}

	@Override
	public List<CalendarVO> getAllCalendarByOrgId(Long orgId, String branchCode, String empCode) {
		List<CalendarVO> calendarVO = new ArrayList<>();
		calendarVO = calendarRepo.getAllCalendarByOrgId(orgId, branchCode, empCode);

		return calendarVO;
	}

	@Override
	public CalendarVO getCalendarById(Long id) {
		CalendarVO calendarVO = new CalendarVO();

		calendarVO = calendarRepo.getCalendarById(id);

		return calendarVO;
	}

	@Override
	public List<Map<String, Object>> getApprovalPendingCountForDashBoard(Long orgId, String reportingPersonCode,
			String branchCode) {
		Set<Object[]> result = checkInRepo.getApprovalPendingCountForDashBoard(orgId, reportingPersonCode, branchCode);
		return getApprovalPendingCountForDashBoard(result);
	}

	private List<Map<String, Object>> getApprovalPendingCountForDashBoard(Set<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("count", record[0] != null ? record[0].toString() : "");

			detailsList.add(map);
		}
		return detailsList;
	}

	// CalendarNotification

	@Override
	public List<CalendarVO> getCalendarNotificationByOrgId(Long orgId, String branchCode, String empCode) {
		List<CalendarVO> calendarVO = new ArrayList<>();
		calendarVO = calendarRepo.getCalendarNotificationByOrgId(orgId, branchCode, empCode);

		return calendarVO;
	}

	@Override
	public EmployeeCodeConfigVO createEmployeeCodeConfig(EmployeeCodeConfigDTO employeeCodeConfigDTO) {

		EmployeeCodeConfigVO employeeCodeConfigVO = new EmployeeCodeConfigVO();
		employeeCodeConfigVO.setOrgId(employeeCodeConfigDTO.getOrgId());
		employeeCodeConfigVO.setCompany(employeeCodeConfigDTO.getCompany());
		employeeCodeConfigVO.setCompanyCode(employeeCodeConfigDTO.getCompanyCode());
		employeeCodeConfigVO.setBranchCode(employeeCodeConfigDTO.getBranchCode());
		employeeCodeConfigVO.setDepartmentCode(employeeCodeConfigDTO.getDepartmentCode());
		employeeCodeConfigVO.setYear(employeeCodeConfigDTO.getYear());
		employeeCodeConfigVO.setSeq(employeeCodeConfigDTO.getSeq());
		employeeCodeConfigVO.setSeqDigit(employeeCodeConfigDTO.getSeqDigit());
		employeeCodeConfigVO.setCodePattern(employeeCodeConfigDTO.getCodePattern());
		employeeCodeConfigRepo.save(employeeCodeConfigVO);
		return employeeCodeConfigVO;
	}

	@Override
	@Transactional
	public String generateEmployeeCodeByOrgId(EmployeeDTOnew employeeDTO) {
		// Step 1: Fetch config for this org
		EmployeeCodeConfigVO config = employeeCodeConfigRepo.findByOrgId(employeeDTO.getOrgId())
				.orElseThrow(() -> new RuntimeException("No code config found for orgId: " + employeeDTO.getOrgId()));

		// Step 2: Increment seq and persist
		int nextSeq = config.getLastSeq() + 1;
		config.setLastSeq(nextSeq);
		employeeCodeConfigRepo.save(config);
		CompanyVO companyVO = companyRepo.findById(employeeDTO.getOrgId()).get();
		DepartmentVO departmentVO = departmentRepo.findByOrgIdAndDepartmentName(employeeDTO.getOrgId(),
				employeeDTO.getDepartment());
		BranchVO branchVO = branchRepo.findByOrgIdAndBranch(employeeDTO.getOrgId(), employeeDTO.getBranch());
		int digitCount = config.getSeqDigit();

		// Step 3: Prepare value map
		Map<String, Object> values = new HashMap<>();
		values.put("companyCode", companyVO.getCompanyCode());
		values.put("departmentCode", departmentVO.getDepartmentCode());
		values.put("branchCode", branchVO.getBranchCode());
		values.put("year", Year.now().getValue());
		String paddedSeq = String.format("%0" + digitCount + "d", nextSeq);
		values.put("seq", paddedSeq);

		// Step 4: Replace pattern dynamically
//		String code = resolvePatternWithSmartSkipping(config.getCodePattern(), values);
		String code =null;
		System.out.println("EmployeeCode: " + code);
		return code;
	}

//	private String resolvePatternWithSmartSkipping(String pattern, Map<String, Object> values) {
//		Pattern regex = Pattern.compile("\\$\\{(.*?)}");
//		Matcher matcher = regex.matcher(pattern);
//
//		StringBuilder result = new StringBuilder();
//		int lastIndex = 0;
//		while (matcher.find()) {
//			String placeholder = matcher.group(1); // e.g., companyCode
//			Object value = values.get(placeholder);
//
//			// Extract separator text before placeholder
//			String separator = pattern.substring(lastIndex, matcher.start());
//
//			// Include only if value is not zero
//			if (value != null && !(value instanceof Integer && (Integer) value == 0)) {
//				result.append(separator).append(value);
//			}
//
//			lastIndex = matcher.end();
//		}
//
//		// Append trailing part after last placeholder
//		result.append(pattern.substring(lastIndex));
//
//		// Optional cleanup
//		return result.toString().replaceAll("[-_/\\.]{2,}", "-") // prevent multiple symbols
//				.replaceAll("^[-_/\\.]+|[-_/\\.]+$", ""); // trim ends
//	}

}