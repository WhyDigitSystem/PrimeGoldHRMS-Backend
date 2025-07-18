package com.efit.hrms.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.CompensatoryOffDTO;
import com.efit.hrms.dto.CompoffNotifyDTO;
import com.efit.hrms.dto.LeaveProcessDTO;
import com.efit.hrms.dto.LeaveRequestDTO;
import com.efit.hrms.dto.LeaveRequestNotifyDTO;
import com.efit.hrms.dto.LeaveTypeDTO;
import com.efit.hrms.dto.TravelRequestDTO;
import com.efit.hrms.dto.WorkFromHomeDTO;
import com.efit.hrms.entity.ApprovalLeavesVO;
import com.efit.hrms.entity.CheckInOutAdjustmentVO;
import com.efit.hrms.entity.CheckInVO;
import com.efit.hrms.entity.CompanyVO;
import com.efit.hrms.entity.CompensatoryOffVO;
import com.efit.hrms.entity.CompoffNotifyVO;
import com.efit.hrms.entity.EmployeeLeaveVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.HolidayVO;
import com.efit.hrms.entity.LeaveBalanceVO;
import com.efit.hrms.entity.LeaveProcessVO;
import com.efit.hrms.entity.LeaveRequestNotifyVO;
import com.efit.hrms.entity.LeaveRequestVO;
import com.efit.hrms.entity.LeaveTypeVO;
import com.efit.hrms.entity.TravelRequestVO;
import com.efit.hrms.entity.WorkFromHomeVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.ApprovalLeavesRepo;
import com.efit.hrms.repo.CheckInOutAdjustmentRepo;
import com.efit.hrms.repo.CheckInRepo;
import com.efit.hrms.repo.CompanyRepo;
import com.efit.hrms.repo.CompanyWeekOffRepo;
import com.efit.hrms.repo.CompensatoryOffRepo;
import com.efit.hrms.repo.CompoffNotifyRepo;
import com.efit.hrms.repo.EmployeeLeaveRepo;
import com.efit.hrms.repo.EmployeeRepo;
import com.efit.hrms.repo.HolidayRepo;
import com.efit.hrms.repo.LeaveBalanceRepo;
import com.efit.hrms.repo.LeaveProcessRepo;
import com.efit.hrms.repo.LeaveRequestNotifyRepo;
import com.efit.hrms.repo.LeaveRequestRepo;
import com.efit.hrms.repo.LeaveTypeRepo;
import com.efit.hrms.repo.TravelRequestRepo;
import com.efit.hrms.repo.WorkFromHomeRepo;

@Service
@Component
public class LeaveProcessServiceImpl implements LeaveProcessService {

	public static final Logger LOGGER = LoggerFactory.getLogger(LeaveProcessServiceImpl.class);

	@Autowired
	LeaveTypeRepo leaveTypeRepo;

	@Autowired
	LeaveRequestRepo leaveRequestRepo;

	@Autowired
	ApprovalLeavesRepo approvalLeavesRepo;

	@Autowired
	LeaveBalanceRepo leaveBalanceRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	EmployeeLeaveRepo employeeLeaveRepo;

	@Autowired
	CompanyRepo companyRepo;

	@Autowired
	CompanyWeekOffRepo companyWeekOffRepo;

	@Autowired
	LeaveProcessRepo leaveProcessRepo;

	@Autowired
	HolidayRepo holidayRepo;

    @Autowired
    CompensatoryOffRepo compensatoryOffRepo;
    
    @Autowired
    CompoffNotifyRepo compoffNotifyRepo;
    
    @Autowired
    LeaveRequestNotifyRepo leaveRequestNotifyRepo;
	
    @Autowired
    CheckInRepo checkInRepo;
    
    @Autowired
    CheckInOutAdjustmentRepo checkInOutAdjustmentRepo;
    
    @Autowired
    TravelRequestRepo travelRequestRepo;

    @Autowired
    WorkFromHomeRepo workFromHomeRepo;
    
	// LeaveType
	@Override
	public Map<String, Object> createUpdateLeaveType(LeaveTypeDTO leaveTypeDTO) throws ApplicationException {

		LeaveTypeVO leaveTypeVO = new LeaveTypeVO();
		String message;
		String screenCode = "DEPT";
		if (ObjectUtils.isNotEmpty(leaveTypeDTO.getId())) {
			leaveTypeVO = leaveTypeRepo.findById(leaveTypeDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid Leave Type details"));

			leaveTypeVO.setUpdatedBy(leaveTypeDTO.getCreatedBy());
			if (!leaveTypeVO.getLeaveType().equalsIgnoreCase(leaveTypeDTO.getLeaveType())) {
				if (leaveTypeRepo.existsByLeaveTypeAndOrgId(leaveTypeDTO.getLeaveType(), leaveTypeDTO.getOrgId())) {
					String errorMessage = String.format("The Leave Type: %s already exists in This Organization.",
							leaveTypeDTO.getLeaveType());
					throw new ApplicationException(errorMessage);
				}
				leaveTypeVO.setLeaveType(leaveTypeDTO.getLeaveType().toUpperCase());
			}

			if (!leaveTypeVO.getLeaveCode().equalsIgnoreCase(leaveTypeDTO.getLeaveCode())) {
				if (leaveTypeRepo.existsByLeaveCodeAndOrgId(leaveTypeDTO.getLeaveCode(), leaveTypeDTO.getOrgId())) {
					String errorMessage = String.format("The Leave Code: %s already exists in This Organization.",
							leaveTypeDTO.getLeaveType());
					throw new ApplicationException(errorMessage);
				}
				leaveTypeVO.setLeaveCode(leaveTypeDTO.getLeaveCode().toUpperCase());
			}

			message = "LeaveType Updated Successfully";
		} else {

			if (leaveTypeRepo.existsByLeaveTypeAndOrgId(leaveTypeDTO.getLeaveType(), leaveTypeDTO.getOrgId())) {
				String errorMessage = String.format("The LeaveType : %s already exists in This Organization.",
						leaveTypeDTO.getLeaveType());
				throw new ApplicationException(errorMessage);
			}
			if (leaveTypeRepo.existsByLeaveCodeAndOrgId(leaveTypeDTO.getLeaveCode(), leaveTypeDTO.getOrgId())) {
				String errorMessage = String.format("The LeaveCode : %s already exists in This Organization.",
						leaveTypeDTO.getLeaveCode());
				throw new ApplicationException(errorMessage);
			}

			leaveTypeVO.setCreatedBy(leaveTypeDTO.getCreatedBy());
			leaveTypeVO.setUpdatedBy(leaveTypeDTO.getCreatedBy());
			message = "LeaveType Created Successfully";
		}

		createUpdateLeaveTypeVOByLeaveTypeDTO(leaveTypeDTO, leaveTypeVO);
		leaveTypeRepo.save(leaveTypeVO);
		Map<String, Object> response = new HashMap<>();
		response.put("leaveTypeVO", leaveTypeVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateLeaveTypeVOByLeaveTypeDTO(LeaveTypeDTO leaveTypeDTO, LeaveTypeVO leaveTypeVO) {
		leaveTypeVO.setLeaveType(leaveTypeDTO.getLeaveType().toUpperCase());
		leaveTypeVO.setOrgId(leaveTypeDTO.getOrgId());
		leaveTypeVO.setActive(leaveTypeDTO.isActive());
		leaveTypeVO.setLeaveCode(leaveTypeDTO.getLeaveCode().toUpperCase());
		leaveTypeVO.setLeaveApplicable(leaveTypeDTO.getLeaveApplicable());
		leaveTypeVO.setBranch(leaveTypeDTO.getBranch());
		leaveTypeVO.setBranchCode(leaveTypeDTO.getBranchCode());
		leaveTypeVO.setFinYear(leaveTypeDTO.getFinYear());
		leaveTypeVO.setActive(leaveTypeDTO.isActive());
		leaveTypeVO.setSalaryDeduction(leaveTypeDTO.getSalaryDeduction());
		leaveTypeVO.setCarryForward(leaveTypeDTO.isCarryForward());

	}

	@Override
	public LeaveTypeVO getLeaveTypeById(Long id) {

		return leaveTypeRepo.getLeaveTypeById(id);
	}

	@Override
	public List<LeaveTypeVO> getLeaveTypeByOrgId(Long orgId) {
		// TODO Auto-generated method stub
		return leaveTypeRepo.getAllLeaveTypeByOrgId(orgId);
	}

	@Override
	public Map<String, Object> createUpdateLeaveRequest(LeaveRequestDTO leaveRequestDTO) throws ApplicationException {

		LeaveRequestVO leaveRequestVO = new LeaveRequestVO();
		String message;
		String screenCode = "LRQ";
		if (ObjectUtils.isNotEmpty(leaveRequestDTO.getId())) {
			leaveRequestVO = leaveRequestRepo.findById(leaveRequestDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid Leave Request details"));

			leaveRequestVO.setUpdatedBy(leaveRequestDTO.getCreatedBy());

			message = "Leave request Updated  Successfully";
		} else {

			leaveRequestVO.setCreatedBy(leaveRequestDTO.getCreatedBy());
			leaveRequestVO.setUpdatedBy(leaveRequestDTO.getCreatedBy());
			message = "Leave Request Created Successfully";
		}

		createUpdateLeaveRequestVOByLeaveRequestDTO(leaveRequestDTO, leaveRequestVO);
		leaveRequestRepo.save(leaveRequestVO);
		Map<String, Object> response = new HashMap<>();
		response.put("leaveRequestVO", leaveRequestVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateLeaveRequestVOByLeaveRequestDTO(LeaveRequestDTO leaveRequestDTO,
			LeaveRequestVO leaveRequestVO) {
		leaveRequestVO.setLeaveType(leaveRequestDTO.getLeaveType().toUpperCase());
		leaveRequestVO.setLeaveCode(leaveRequestDTO.getLeaveCode().toUpperCase());
		leaveRequestVO.setOrgId(leaveRequestDTO.getOrgId());
//		leaveRequestVO.setId(leaveRequestDTO.getId());
		leaveRequestVO.setFromDate(leaveRequestDTO.getFromDate());
		leaveRequestVO.setToDate(leaveRequestDTO.getToDate());
		leaveRequestVO.setSelectLeave(leaveRequestDTO.getSelectLeave());
		leaveRequestVO.setTotalDays(leaveRequestDTO.getTotalDays());
		leaveRequestVO.setEmployeeName(leaveRequestDTO.getEmployeeName());
		leaveRequestVO.setEmployeeCode(leaveRequestDTO.getEmployeeCode());
//		leaveRequestVO.setFinYear(leaveRequestDTO.getFinYear());

		leaveRequestVO.setBranch(leaveRequestDTO.getBranch());
		leaveRequestVO.setBranchCode(leaveRequestDTO.getBranchCode());
		leaveRequestVO.setDepartment(leaveRequestDTO.getDepartment());
		leaveRequestVO.setDesignation(leaveRequestDTO.getDesignation());
		leaveRequestVO.setNotes(leaveRequestDTO.getNotes());
		leaveRequestVO.setNotify(leaveRequestDTO.getNotify());
		leaveRequestVO.setNotifyCode(leaveRequestDTO.getNotifyCode());
		leaveRequestVO.setNotifyEmail(leaveRequestDTO.getNotifyEmail());
		leaveRequestVO.setCompOffDate(leaveRequestDTO.getCompOffDate());

	    if (leaveRequestDTO.getId() != null) {
            List<LeaveRequestNotifyVO> leaveRequestNotifyVO = leaveRequestNotifyRepo.findByLeaveRequestVO(leaveRequestVO);
            leaveRequestNotifyRepo.deleteAll(leaveRequestNotifyVO);
        }

        // Set Poll Details from PollDetailsDTO
        List<LeaveRequestNotifyVO> leaveRequestNotifyVOs = new ArrayList<>();
        for (LeaveRequestNotifyDTO leaveRequestNotifyDTO : leaveRequestDTO.getLeaveRequestNotifyDTO()) {
        	LeaveRequestNotifyVO leaveRequestNotifyVO = new LeaveRequestNotifyVO();
        	leaveRequestNotifyVO.setNotify2(leaveRequestNotifyDTO.getNotify2());
        	leaveRequestNotifyVO.setNotify2Code(leaveRequestNotifyDTO.getNotify2Code());
        	leaveRequestNotifyVO.setNotify2Email(leaveRequestNotifyDTO.getNotify2Email());

        	leaveRequestNotifyVO.setLeaveRequestVO(leaveRequestVO);  // Set parent reference in child
        	leaveRequestNotifyVOs.add(leaveRequestNotifyVO);
        }
        leaveRequestVO.setLeaveRequestNotifyVO(leaveRequestNotifyVOs);
        
		leaveRequestVO.setApproveStatus("PENDING");

		leaveRequestVO.setBranch(leaveRequestDTO.getBranch());
		leaveRequestVO.setBranchCode(leaveRequestDTO.getBranchCode());
		// leaveRequestVO.setFinYear(leaveRequestDTO.getFinYear());
		// leaveRequestVO.setActive(leaveRequestDTO.isActive());

	}

	@Override
	public LeaveRequestVO getLeaverequestById(Long id) {
		return leaveRequestRepo.getLeaveRequestById(id);

	}

	@Override
	public List<LeaveRequestVO> getLeaveRequestByOrgId(Long orgId, String employeeCode) {
		// TODO Auto-generated method stub
		return leaveRequestRepo.getLeaveRequestByOrgId(orgId, employeeCode);
	}

	// LeaveRequestTOTALDAYS
	
	public Map<String, Object> calculateLeavedays(Long orgId, LocalDate fromDate, LocalDate toDate, String selectLeave)
	        throws ApplicationException {

	    // Get company
	    CompanyVO company = companyRepo.findById(orgId)
	            .orElseThrow(() -> new ApplicationException("Company not found for orgId: " + orgId));
	    String policyType = company.getLeavePolicy();
	    Long companyId = company.getId();

	    // Fetch week-off pattern (e.g., [("SUNDAY", -1), ("SATURDAY", 1)])
	    List<Object[]> weekOffPatterns = companyWeekOffRepo.findWeekOffOccurrencesByCompanyId(companyId);

	    // Fetch holiday dates
	    List<HolidayVO> holidayVOList = holidayRepo.findByOrgId(orgId);
	    List<LocalDate> holidaysDate = holidayVOList.stream()
	            .map(HolidayVO::getHolidayDate)
	            .collect(Collectors.toList());

	    BigDecimal totalDays = BigDecimal.ZERO;
	    List<String> workingDates = new ArrayList<>();

	    // Single day case
	    if (fromDate.equals(toDate)) {
	        if ("ALL DAY".equalsIgnoreCase(selectLeave)) {
	            totalDays = BigDecimal.ONE;
	        } else if ("HALF DAY".equalsIgnoreCase(selectLeave)) {
	            totalDays = BigDecimal.valueOf(0.5);
	        }
	        workingDates.add(fromDate.toString());

	    } else {
	        LocalDate current = fromDate;
	        while (!current.isAfter(toDate)) {
	            boolean isWeekOff = isWeekOffDay(current, weekOffPatterns);
	            boolean isHoliday = holidaysDate.contains(current);

	            if ("REGULAR".equalsIgnoreCase(policyType)) {
	                if (!isWeekOff && !isHoliday) {
	                    totalDays = totalDays.add(BigDecimal.ONE);
	                    workingDates.add(current.toString());
	                }
	            } else { // SANDWICH: count all days
	                totalDays = totalDays.add(BigDecimal.ONE);
	                workingDates.add(current.toString());
	            }

	            current = current.plusDays(1);
	        }
	    }

	    // Final result map
	    Map<String, Object> result = new HashMap<>();
	    result.put("workingDays", totalDays);
	    result.put("workingDates", workingDates);
	    return result;
	}

	
	public boolean isWeekOffDay(LocalDate date, List<Object[]> weekOffPatterns) {
	    DayOfWeek currentDay = date.getDayOfWeek(); // e.g., MONDAY
	    int weekOfMonth = (date.getDayOfMonth() - 1) / 7 + 1;

	    for (Object[] pattern : weekOffPatterns) {
	        String day = pattern[0].toString().toUpperCase();
	        int week = Integer.parseInt(pattern[1].toString());
	        DayOfWeek patternDay = DayOfWeek.valueOf(day);

	        if (currentDay == patternDay && (week == -1 || week == weekOfMonth)) {
	            return true;
	        }
	    }
	    return false;
	}



	@Override
	public List<Map<String, Object>> getAllLeaveTypeFromLeaveMaster(Long orgId, String employeeCode) {
		Set<Object[]> leaveTpe = leaveRequestRepo.getAllLeaveTypeFromLeaveMaster(orgId, employeeCode);
		return getLeaveTpe(leaveTpe); // Returning a list of Map<String, Object>
	}

	private List<Map<String, Object>> getLeaveTpe(Set<Object[]> leaveTpe) {
		List<Map<String, Object>> leaveTpeList = new ArrayList<>(); // Correct variable name

		for (Object[] leaveTypes : leaveTpe) { // Iterating over getFullGridCurrency
			Map<String, Object> leaveTypesMap = new HashMap<>();
			leaveTypesMap.put("id", leaveTypes[0] != null ? Integer.parseInt(leaveTypes[0].toString()) : 0);
			leaveTypesMap.put("leaveType", leaveTypes[1] != null ? leaveTypes[1].toString() : "");
			leaveTypesMap.put("leaveTypeCode", leaveTypes[2] != null ? leaveTypes[2].toString() : "");
			leaveTypesMap.put("effectiveFrom", leaveTypes[3] != null ? leaveTypes[3].toString() : "");
			leaveTypesMap.put("leaveDays", leaveTypes[4] != null ? leaveTypes[4].toString() : "");

			leaveTpeList.add(leaveTypesMap); // Add the Map to the list
		}
		return leaveTpeList;
	}

//	@Override
//	public List<Map<String, Object>> getTotalLeaveFromLeaveBalance(Long orgId, String employeeCode,String leaveType) {
//		Set<Object[]> totalLeave = leaveRequestRepo.getTotalLeaveFromLeaveBalance(orgId, employeeCode,leaveType);
//		return getTotalLeave(totalLeave); // Returning a list of Map<String, Object>
//	}
//
//	private List<Map<String, Object>> getTotalLeave(Set<Object[]> totalLeave) {
//		List<Map<String, Object>> totalLeaveList = new ArrayList<>(); // Correct variable name
//
//		for (Object[] leaveTypes : totalLeave) { // Iterating over getFullGridCurrency
//			Map<String, Object> leaveMap = new HashMap<>();
//			leaveMap.put("id", leaveTypes[0] != null ? Integer.parseInt(leaveTypes[0].toString()) : 0);
//			leaveMap.put("totalLeave", leaveTypes[1] != null ? leaveTypes[1].toString() : "");
//			totalLeaveList.add(leaveMap); // Add the Map to the list
//		}
//		return totalLeaveList;
//	}

//	@Override
//	public LeaveRequestVO createApprovalLeave(Long orgId, Long id, String employeeCode, String action, String actionBy)
//			throws ApplicationException {
//
//		LeaveRequestVO leaveRequestVO = leaveRequestRepo.findByOrgIdAndIdAndEmployeeCode(orgId, id, employeeCode);
//
//		if (leaveRequestVO.getApproveStatus() == null
//				|| (!leaveRequestVO.getApproveStatus().equalsIgnoreCase("Approved")
//						&& !leaveRequestVO.getApproveStatus().equalsIgnoreCase("Rejected"))) {
//
//			if ("APPROVED".equalsIgnoreCase(action)) {
//				LocalDate fromDate = leaveRequestVO.getFromDate();
//				LocalDate toDate = leaveRequestVO.getToDate();
//
//				CompanyVO company = companyRepo.findById(orgId)
//						.orElseThrow(() -> new ApplicationException("Company not found for orgId: " + orgId));
//
//				String policyType = company.getLeavePolicy();
//
//				// 🔹 Fetch week-off days
//				List<CompanyWeekOffVO> weekOffList = companyWeekOffRepo.findByCompanyId(company.getId());
//				Set<String> weekOffDays = weekOffList.stream().map(weekOff -> weekOff.getWeekOffDays().toUpperCase())
//						.collect(Collectors.toSet());
//
//				List<ApprovalLeavesVO> approvalLeavesList = new ArrayList<>();
//				BigDecimal totalWorkingDays = BigDecimal.ZERO;
//				BigDecimal totalDays = BigDecimal.ZERO;
//
//				List<HolidayVO> holidayVOList = holidayRepo.findByOrgId(orgId);
//				List<LocalDate> holidaysDate = new ArrayList<>();
//
//				if (holidayVOList != null) {
//					for (HolidayVO holiday : holidayVOList) {
//						LocalDate localDate = holiday.getHolidayDate();
//						holidaysDate.add(localDate);
//					}
//				}
//
//				while (!fromDate.isAfter(toDate)) {
//					String currentDay = fromDate.getDayOfWeek().name(); // Convert to "MONDAY", "TUESDAY", etc.
//
//					if ("REGULAR".equalsIgnoreCase(policyType) 
//					        && (weekOffDays.contains(currentDay) || holidaysDate.contains(fromDate))) {
//						fromDate = fromDate.plusDays(1);
//						// Count only working days
//						continue; // Skip week-off days in Regular policy
//					} else {
//						// If SANDWICH policy, count all days
//						totalDays = totalDays.add(BigDecimal.ONE);
//
//					}
//
//					// 🔹 Only post working dates
//					ApprovalLeavesVO approvalLeavesVO = new ApprovalLeavesVO();
//					approvalLeavesVO.setEmployeeName(leaveRequestVO.getEmployeeName());
//					approvalLeavesVO.setEmployeeCode(leaveRequestVO.getEmployeeCode());
//					approvalLeavesVO.setLeaveType(leaveRequestVO.getLeaveType());
//					approvalLeavesVO.setLeaveDate(fromDate);
//					if (leaveRequestVO.getSelectLeave() == null || leaveRequestVO.getSelectLeave().trim().isEmpty()) {
//						approvalLeavesVO.setTotalLeave(BigDecimal.ONE);
//					} else if ("HALF DAY".equalsIgnoreCase(leaveRequestVO.getSelectLeave().trim())) {
//						approvalLeavesVO.setTotalLeave(BigDecimal.valueOf(0.5));
//					} else if ("ALL DAY".equalsIgnoreCase(leaveRequestVO.getSelectLeave().trim())) {
//						approvalLeavesVO.setTotalLeave(BigDecimal.ONE);
//					}
//
//					approvalLeavesVO.setApproveStatus(action);
//					approvalLeavesVO.setApproveBy(actionBy);
//					approvalLeavesVO.setOrgId(leaveRequestVO.getOrgId());
//					approvalLeavesVO.setBranch(leaveRequestVO.getBranch());
//					approvalLeavesVO.setBranchCode(leaveRequestVO.getBranchCode());
////					approvalLeavesVO.setFinYear(leaveRequestVO.getFinYear());
////					approvalLeavesVO.setLpstatus("PENDING");
//
//					approvalLeavesList.add(approvalLeavesVO);
//
//					totalWorkingDays = totalWorkingDays.add(BigDecimal.ONE);
//					fromDate = fromDate.plusDays(1);
//				}
//
//				if (approvalLeavesList.isEmpty()) {
//					throw new ApplicationException("No working days found in the selected range.");
//				}
//
//				// Save all records in batch
//				approvalLeavesRepo.saveAll(approvalLeavesList);
//				// Update leave request approval details
//
//				List<LeaveBalanceVO> leaveBalanceVOs = new ArrayList<LeaveBalanceVO>();
//
//				LeaveBalanceVO leaveBalanceVO = new LeaveBalanceVO();
//				leaveBalanceVO.setLeaveCode(leaveRequestVO.getLeaveCode());
//				leaveBalanceVO.setLeaveType(leaveRequestVO.getLeaveType());
//				
//				if (leaveRequestVO.getSelectLeave() != null) {
//					String leaveType = leaveRequestVO.getSelectLeave().trim();
//
//					if ("HALF DAY".equalsIgnoreCase(leaveType)) {
//						leaveBalanceVO.setTotalLeave(BigDecimal.valueOf(-0.5)); // Set -0.5 for half-day leave
//					} else if ("ALL DAY".equalsIgnoreCase(leaveType)) {
//						leaveBalanceVO.setTotalLeave(BigDecimal.valueOf(-1)); // Set -1 for full-day leave
//					} else {
//						leaveBalanceVO.setTotalLeave(totalDays.negate()); // Negate totalDays for any other case
//					}
//				} else {
//					leaveBalanceVO.setTotalLeave(totalDays.negate()); // Handle null selectLeave
//				}
//
//				leaveBalanceVO.setEmployeeName(leaveRequestVO.getEmployeeName());
//				leaveBalanceVO.setEmployeeCode(leaveRequestVO.getEmployeeCode());
//				leaveBalanceVO.setOrgId(leaveRequestVO.getOrgId());
//				leaveBalanceVO.setBranch(leaveRequestVO.getBranch());
//				leaveBalanceVO.setBranchCode(leaveRequestVO.getBranchCode());
////				leaveBalanceVO.setFinYear(leaveRequestVO.getFinYear());
//
//				leaveBalanceVO.setLeaveStatus("Leave");
//				leaveBalanceVOs.add(leaveBalanceVO);
//
//				leaveBalanceRepo.saveAll(leaveBalanceVOs);
////	            leaveRequestRepo.save(leaveRequestVO);
//			}
//		leaveRequestVO.setApproveStatus(action);
//			System.out.println(action);
//			leaveRequestVO.setApproveBy(actionBy);
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
//			leaveRequestVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());
//			leaveRequestRepo.save(leaveRequestVO);
//
//		} else if (leaveRequestVO.getApproveStatus().equalsIgnoreCase("Approved")) {
//			throw new ApplicationException("This Invoice Already Approved,");
//		} else if (leaveRequestVO.getApproveStatus().equalsIgnoreCase("rejected")) {
//			throw new ApplicationException("This Invoice Already Rejected");
//		}
//
//		return leaveRequestVO; // Single return statement at the end
//	}

	
	@Override
	public Map<String, Object> createApprovalLeave(Long orgId, Long id, String employeeCode, String action, String actionBy,String notifyCode,String notify, String screenName)
			throws ApplicationException {

		LeaveRequestVO leaveRequestVO = leaveRequestRepo.findByOrgIdAndIdAndEmployeeCode(orgId, id, employeeCode);
	    String message = "";
		if (leaveRequestVO.getApproveStatus() == null
				|| (!leaveRequestVO.getApproveStatus().equalsIgnoreCase("Approved")
						&& !leaveRequestVO.getApproveStatus().equalsIgnoreCase("Rejected"))) {

			if ("APPROVED".equalsIgnoreCase(action)) {
				LocalDate fromDate = leaveRequestVO.getFromDate();
				LocalDate toDate = leaveRequestVO.getToDate();

				CompanyVO company = companyRepo.findById(orgId)
						.orElseThrow(() -> new ApplicationException("Company not found for orgId: " + orgId));

				String policyType = company.getLeavePolicy();

				// 🔹 Fetch week-off days
				List<Object[]> weekOffPatterns = companyWeekOffRepo.findWeekOffOccurrencesByCompanyId(company.getId());

				List<ApprovalLeavesVO> approvalLeavesList = new ArrayList<>();
				BigDecimal totalWorkingDays = BigDecimal.ZERO;
				BigDecimal totalDays = BigDecimal.ZERO;

				List<HolidayVO> holidayVOList = holidayRepo.findByOrgId(orgId);
				List<LocalDate> holidaysDate = new ArrayList<>();

				if (holidayVOList != null) {
					for (HolidayVO holiday : holidayVOList) {
						LocalDate localDate = holiday.getHolidayDate();
						holidaysDate.add(localDate);
					}
				}

				while (!fromDate.isAfter(toDate)) {
					String currentDay = fromDate.getDayOfWeek().name(); // Convert to "MONDAY", "TUESDAY", etc.

					boolean isWeekOff = isWeekOffDay(fromDate, weekOffPatterns);
					boolean isHoliday = holidaysDate.contains(fromDate);

					if ("REGULAR".equalsIgnoreCase(policyType) && (isWeekOff || isHoliday)) {
					    fromDate = fromDate.plusDays(1);
					    continue;
					}
					else {
						// If SANDWICH policy, count all days
						totalDays = totalDays.add(BigDecimal.ONE);

					}

					// 🔹 Only post working dates
					ApprovalLeavesVO approvalLeavesVO = new ApprovalLeavesVO();
					approvalLeavesVO.setEmployeeName(leaveRequestVO.getEmployeeName());
					approvalLeavesVO.setEmployeeCode(leaveRequestVO.getEmployeeCode());
					approvalLeavesVO.setLeaveType(leaveRequestVO.getLeaveType());
					approvalLeavesVO.setLeaveDate(fromDate);
					if (leaveRequestVO.getSelectLeave() == null || leaveRequestVO.getSelectLeave().trim().isEmpty()) {
						approvalLeavesVO.setTotalLeave(BigDecimal.ONE);
					} else if ("HALF DAY".equalsIgnoreCase(leaveRequestVO.getSelectLeave().trim())) {
						approvalLeavesVO.setTotalLeave(BigDecimal.valueOf(0.5));
					} else if ("ALL DAY".equalsIgnoreCase(leaveRequestVO.getSelectLeave().trim())) {
						approvalLeavesVO.setTotalLeave(BigDecimal.ONE);
					}

					approvalLeavesVO.setApproveStatus(action);
					approvalLeavesVO.setApproveBy(actionBy);
					approvalLeavesVO.setOrgId(leaveRequestVO.getOrgId());
					approvalLeavesVO.setBranch(leaveRequestVO.getBranch());
					approvalLeavesVO.setBranchCode(leaveRequestVO.getBranchCode());
//					approvalLeavesVO.setFinYear(leaveRequestVO.getFinYear());
//					approvalLeavesVO.setLpstatus("PENDING");

					approvalLeavesList.add(approvalLeavesVO);

					totalWorkingDays = totalWorkingDays.add(BigDecimal.ONE);
					fromDate = fromDate.plusDays(1);
				}

				if (approvalLeavesList.isEmpty()) {
					throw new ApplicationException("No working days found in the selected range.");
				}

				// Save all records in batch
				approvalLeavesRepo.saveAll(approvalLeavesList);
				// Update leave request approval details

				List<LeaveBalanceVO> leaveBalanceVOs = new ArrayList<LeaveBalanceVO>();

				LeaveBalanceVO leaveBalanceVO = new LeaveBalanceVO();
				leaveBalanceVO.setLeaveCode(leaveRequestVO.getLeaveCode());
				leaveBalanceVO.setLeaveType(leaveRequestVO.getLeaveType());
				
				if (leaveRequestVO.getSelectLeave() != null) {
					String leaveType = leaveRequestVO.getSelectLeave().trim();

					if ("HALF DAY".equalsIgnoreCase(leaveType)) {
						leaveBalanceVO.setTotalLeave(BigDecimal.valueOf(-0.5)); // Set -0.5 for half-day leave
					} else if ("ALL DAY".equalsIgnoreCase(leaveType)) {
						leaveBalanceVO.setTotalLeave(BigDecimal.valueOf(-1)); // Set -1 for full-day leave
					} else {
						leaveBalanceVO.setTotalLeave(totalDays.negate()); // Negate totalDays for any other case
					}
				} else {
					leaveBalanceVO.setTotalLeave(totalDays.negate()); // Handle null selectLeave
				}

				leaveBalanceVO.setEmployeeName(leaveRequestVO.getEmployeeName());
				leaveBalanceVO.setEmployeeCode(leaveRequestVO.getEmployeeCode());
				leaveBalanceVO.setOrgId(leaveRequestVO.getOrgId());
				leaveBalanceVO.setBranch(leaveRequestVO.getBranch());
				leaveBalanceVO.setBranchCode(leaveRequestVO.getBranchCode());
//				leaveBalanceVO.setFinYear(leaveRequestVO.getFinYear());

				leaveBalanceVO.setLeaveStatus("Leave");
				leaveBalanceVOs.add(leaveBalanceVO);

				leaveBalanceRepo.saveAll(leaveBalanceVOs);
//	            leaveRequestRepo.save(leaveRequestVO);
				
			}
		leaveRequestVO.setApproveStatus(action);
			System.out.println(action);
			leaveRequestVO.setApproveBy(actionBy);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
			leaveRequestVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());
			leaveRequestRepo.save(leaveRequestVO);
			
			if (leaveRequestVO.getApproveStatus().equalsIgnoreCase("Approved")) {
			    message = "Approved Successfully";
			} else if (leaveRequestVO.getApproveStatus().equalsIgnoreCase("Rejected")) {
			    message = "Rejected Successfully";
			}
			
			
			
		} else if (leaveRequestVO.getApproveStatus().equalsIgnoreCase("Approved")) {
			throw new ApplicationException("This LeaveRequest Already Approved,");
		} else if (leaveRequestVO.getApproveStatus().equalsIgnoreCase("rejected")) {
			throw new ApplicationException("This LeaveRequest Already Rejected");
		}
		Map<String, Object> response = new HashMap<>();
	    response.put("leaveRequestVO", leaveRequestVO);
	    response.put("message", message);
	    return response;
		
		
	}

	
	
	// AUTO LEAVE CREDIT DAYS

//	@Scheduled(fixedRate = 86400000) // Runs every 24 hours
	@Scheduled(fixedRate = 60000) // Runs every 1 minute (for testing)
	@Transactional
	public void checkAndAssignLeaveCredits() {
		try {
			System.out.println("Run																																																			ning Leave Credit Scheduler...");

			// 🔹 Fetch all company records
			List<Object[]> policies = entityManager
					.createNativeQuery("SELECT companyId, leavecreditcontrol, autoCreditDate FROM Company")
					.getResultList();

			LocalDate today = LocalDate.now();

			for (Object[] policyData : policies) {
				Long companyId = ((Number) policyData[0]).longValue(); // 🔹 Extract companyId
				String leavecreditcontrol = (policyData[1] != null) ? policyData[1].toString() : null;
				java.sql.Date sqlDate = (java.sql.Date) policyData[2];
				LocalDate nextCreditDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

				// 🔹 Skip invalid records
				if (leavecreditcontrol == null || nextCreditDate == null) {
					continue;
				}

				// 🔹 Skip if today is not the auto credit date
				if (!today.equals(nextCreditDate)) {
					continue;
				}

				// 🔹 Fetch week-off days
				List<LeaveTypeVO> leaveTypeVOList = leaveTypeRepo.findByOrgId(companyId);
				Set<String> leaveType = leaveTypeVOList.stream()
						.map(leaveTypes -> leaveTypes.getLeaveType().toUpperCase()).collect(Collectors.toSet());

				// 🔹 Fetch leave balances for this specific company
				List<Object[]> results = leaveBalanceRepo.findBalanceLeaveRawByOrgId(companyId);

				// 🔹 Convert query results to LeaveBalanceVO objects
				List<LeaveBalanceVO> updatedLeaveBalances = results.stream().map(obj -> {
					LeaveBalanceVO leaveBalance = new LeaveBalanceVO();

					String leaveTypeStr = (obj[3] != null) ? obj[3].toString().trim() : "";
					String employeeCode = (obj[0] != null) ? obj[0].toString().trim() : "";

					// Skip if leaveType already exists
					if (leaveType.contains(leaveTypeStr.toUpperCase())) {
						return null; // Skip this entry
					}

					leaveBalance.setEmployeeCode(employeeCode); // Employee Code
					leaveBalance.setEmployeeName((String) obj[1]); // Employee Name
					leaveBalance.setOrgId(((Number) obj[2]).longValue()); // Org ID
					leaveBalance.setLeaveType(leaveTypeStr); // Leave Type
					leaveBalance.setLeaveCode((String) obj[4]); // Leave Code

					// Handle null and negative cases correctly
					BigDecimal totalLeaveValue = (obj[5] != null) ? BigDecimal.valueOf(((Number) obj[5]).doubleValue())
							: BigDecimal.ZERO;

					if (totalLeaveValue.compareTo(BigDecimal.ZERO) == 0) {
						return null; // Skip rows where totalLeave is 0
					}

					System.out.println(totalLeaveValue);
					leaveBalance.setTotalLeave(totalLeaveValue.abs().negate());

					leaveBalance.setBranch((String) obj[6]); // Branch
					leaveBalance.setBranchCode((String) obj[7]); // Branch Code
					leaveBalance.setLeaveStatus("ASSIGNED LEAVE"); // Custom Status

					return leaveBalance;
				}).filter(Objects::nonNull).collect(Collectors.toList()); // Filter out null values

				// 🔹 Save only valid leave balances
				if (!updatedLeaveBalances.isEmpty()) {
					leaveBalanceRepo.saveAll(updatedLeaveBalances);
				}

				// 🔹 Save updated leave balances
//	            leaveBalanceRepo.saveAll(updatedLeaveBalances);

				// 🔹 Fetch employees for this company
				List<EmployeeVO> employees = employeeRepo.findByOrgId(companyId);
				List<LeaveBalanceVO> leaveBalances = new ArrayList<>();

				for (EmployeeVO employee : employees) {
					List<EmployeeLeaveVO> employeeLeaves = employeeLeaveRepo.findByEmployeeVO(employee);
					for (EmployeeLeaveVO employeeLeave : employeeLeaves) {
						LeaveBalanceVO leaveBalance = new LeaveBalanceVO();
						leaveBalance.setEmployeeCode(employee.getEmployeeCode());
						leaveBalance.setEmployeeName(employee.getEmployeeName());
						leaveBalance.setOrgId(companyId); // 🔹 Use dynamic orgId
						leaveBalance.setBranch(employee.getBranch());
						leaveBalance.setBranchCode(employee.getBranchCode());
						leaveBalance.setLeaveCode(employeeLeave.getLeaveCode());
						leaveBalance.setLeaveType(employeeLeave.getLeaveType());
						leaveBalance.setTotalLeave(employeeLeave.getTotalLeave());
						leaveBalance.setLeaveStatus("CREDIT LEAVE");

						leaveBalances.add(leaveBalance);
					}
				}

				// 🔹 Save all leave balances
				leaveBalanceRepo.saveAll(leaveBalances);

				// 🔹 Update autoCreditDate for the next cycle
				LocalDate newCreditDate = calculateNextCreditDate(leavecreditcontrol, nextCreditDate);
				entityManager
						.createNativeQuery(
								"UPDATE Company SET autoCreditDate = :newCreditDate WHERE companyId = :companyId")
						.setParameter("newCreditDate", java.sql.Date.valueOf(newCreditDate))
						.setParameter("companyId", companyId).executeUpdate();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LocalDate calculateNextCreditDate(String leavecreditcontrol, LocalDate currentCreditDate) {
		switch (leavecreditcontrol.toUpperCase()) {
		case "MONTHLY":
			return currentCreditDate.plusMonths(1);
		case "QUARTERLY":
			return currentCreditDate.plusMonths(3);
		case "HALF-YEARLY":
			return currentCreditDate.plusMonths(6);
		case "YEARLY":
			return currentCreditDate.plusYears(1);
		default:
			return currentCreditDate.plusMonths(1);
		}
	}

	// leaveprocess

	@Override
	public Map<String, Object> createUpdateLeaveProcess(@Valid List<LeaveProcessDTO> leaveProcessDTOs)
			throws ApplicationException {

		List<LeaveProcessVO> leaveProcessVOList = new ArrayList<>();
		String message = null;
		for (LeaveProcessDTO leaveProcessDTO : leaveProcessDTOs) {
			LeaveProcessVO leaveProcessVO;

			if (ObjectUtils.isNotEmpty(leaveProcessDTO.getId())) {
				leaveProcessVO = leaveProcessRepo.findById(leaveProcessDTO.getId())
						.orElseThrow(() -> new ApplicationException("Invalid Leave Process details"));
				leaveProcessVO.setUpdatedBy(leaveProcessDTO.getCreatedBy());
				message = "Leave Process updated Successfully";

			} else {
				leaveProcessVO = new LeaveProcessVO();
				leaveProcessVO.setCreatedBy(leaveProcessDTO.getCreatedBy());
				leaveProcessVO.setUpdatedBy(leaveProcessDTO.getCreatedBy());
				message = "Leave Process Created Successfully";

			}

			createUpdateLeaveProcessVOByLeaveProcessDTO(leaveProcessDTO, leaveProcessVO);
			leaveProcessVOList.add(leaveProcessVO);
		}

		leaveProcessRepo.saveAll(leaveProcessVOList); // Save all processed records

		Map<String, Object> response = new HashMap<>();
		response.put("leaveProcessVO", leaveProcessVOList);
		response.put("message", message);
		return response;
	}

	private void createUpdateLeaveProcessVOByLeaveProcessDTO(LeaveProcessDTO leaveProcessDTO,
			LeaveProcessVO leaveProcessVO) {
		leaveProcessVO.setEmployeeCode(leaveProcessDTO.getEmployeeCode());
		leaveProcessVO.setEmployeeName(leaveProcessDTO.getEmployeeName());
		leaveProcessVO.setOrgId(leaveProcessDTO.getOrgId());
		leaveProcessVO.setTotalCompanyWorkingDays(leaveProcessDTO.getTotalCompanyWorkingDays());
		leaveProcessVO.setMonth(leaveProcessDTO.getMonth());
		leaveProcessVO.setYear(leaveProcessDTO.getYear());
		leaveProcessVO.setTotalLeave(leaveProcessDTO.getTotalLeave());
		leaveProcessVO.setLopLeave(leaveProcessDTO.getLopLeave());
		leaveProcessVO.setEmpTotalWorkingDays(leaveProcessDTO.getEmpTotalWorkingDays());
		leaveProcessVO.setEmpSalaryDays(leaveProcessDTO.getEmpSalaryDays());
		leaveProcessVO.setApprovedStatus("PENDING");
		leaveProcessVO.setBranch(leaveProcessDTO.getBranch());
		leaveProcessVO.setBranchCode(leaveProcessDTO.getBranchCode());

//		 List<ApprovalLeavesVO> approvalLeavesVOList = approvalLeavesRepo.findByEmployeeCodeAndOrgIdAndMonthAndYear(leaveProcessDTO.getEmployeeCode(),leaveProcessDTO.getOrgId(),leaveProcessDTO.getMonth(),salaryProcessDTO.getYear());
//
//         for (LeaveProcessVO leaveProcessVO : leaveProcessVOList) {
//             leaveProcessVO.setApprovedStatus("APPROVED");
//         }
//         leaveProcessRepo.saveAll(leaveProcessVOList);

	}

	@Override
	public List<LeaveProcessVO> getLeaveProcessByOrgId(Long orgId) {
		// TODO Auto-generated method stub
		return leaveProcessRepo.getLeaveProcessByOrgId(orgId);
	}

	@Override
	public List<Map<String, Object>> getLeaveDetailsForLeaveProcess(String fromDate, String toDate, Long orgId, String department, String branch) {

//	    CompanyVO companyVO = companyRepo.findByCompanyId(orgId);
//
//	    String attendanceMode = companyVO.getAttendanceMode();  // e.g., "SYSTEM,MANUAL"
//	    String selectedMode = null;
//
//	    if (attendanceMode != null && !attendanceMode.trim().isEmpty()) {
//	        String[] modes = attendanceMode.split(",");
//	        selectedMode = modes[0].trim();  // Take only the first mode
//	    }

	    Set<Object[]> result = leaveProcessRepo.getLeaveDetailsForLeaveProcess(fromDate, toDate, orgId, department, branch);
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
			map.put("totalCompanyWorkingDays", record[4] != null ? record[4].toString() : "0");
			map.put("month", record[5] != null ? record[5].toString() : "0");
			map.put("year", record[6] != null ? record[6].toString() : "0");
			map.put("totalLeave", record[7] != null ? record[7].toString() : "0");
			map.put("lopLeave", record[8] != null ? record[8].toString() : "0");
			map.put("empSalaryDays", record[9] != null ? record[9].toString() : "0");
			map.put("empTotalWorkingDays", record[10] != null ? record[10].toString() : "0");

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
//	public List<Map<String, Object>> getCheckInAndOutDaysForLeaveProcess(@RequestParam String fromDate,
//			@RequestParam String toDate, @RequestParam Long orgId,@RequestParam String branchCode,@RequestParam String empCode) {
//		Set<Object[]> result = checkInRepo.getCheckInAndOutDaysForLeaveProcess(fromDate,
//				 toDate, orgId, branchCode, empCode);
//		return getCheckInAndOutDaysForLeaveProcess(result,fromDate,toDate,orgId,branchCode,empCode);
//	}
//
//	private List<Map<String, Object>> getCheckInAndOutDaysForLeaveProcess(Set<Object[]> result,fromDate,toDate,orgId,branchCode,empCode) {
//		List<Map<String, Object>> detailsList = new ArrayList<>();
//		
//		List<HolidayVO> holidayVO = new ArrayList();
//		int count=0;
//		 holidayVO =holidayRepo.findByFromDateAndToDateAndOrgIdAndBranchCodeAndEmpCode(  fromDate,
//				  toDate,   orgId,  branchCode,  empCode);
//		for(holidayVO.hasNext()) {
//			count++;
//		}
//
//		for (Object[] record : result) {
//			Map<String, Object> map = new HashMap<>();
//			map.put("totalEmpWorkingDays", record[0] != null ? record[0].toString() : "");
//			map.put("holidaysCount", count != null ? count.toInteger() : "");
//
//			detailsList.add(map);
//		}
//		return detailsList;
//	}

	
	@Override
	public List<Map<String, Object>> getCheckInAndOutDaysForLeaveProcess(
	        @RequestParam String fromDate,
	        @RequestParam String toDate,
	        @RequestParam Long orgId,
	        @RequestParam String branchCode,
	        @RequestParam String empCode) {

	    Set<Object[]> result = checkInRepo.getCheckInAndOutDaysForLeaveProcess(fromDate, toDate, orgId, branchCode, empCode);
	    return processCheckInAndOutDays(result, fromDate, toDate, orgId, branchCode, empCode);
	}

	private List<Map<String, Object>> processCheckInAndOutDays(
	        Set<Object[]> result,
	        String fromDate,
	        String toDate,
	        Long orgId,
	        String branchCode,
	        String empCode) {

	    List<Map<String, Object>> detailsList = new ArrayList<>();

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate from = LocalDate.parse(fromDate, formatter);
	    LocalDate to = LocalDate.parse(toDate, formatter);

	    long totalCompanyWorkingDays = ChronoUnit.DAYS.between(from, to) + 1;

	    List<HolidayVO> holidayList = holidayRepo.findByFromDateAndToDateAndOrgIdAndBranchCode(
	            fromDate, toDate, orgId, branchCode);

	    List<ApprovalLeavesVO> approvalLeavesList = approvalLeavesRepo.findByFromDateAndToDateAndOrgIdAndBranchCodeAndEmployeeCode(
	            fromDate, toDate, orgId, branchCode, empCode);

	    int empLeaveCount = approvalLeavesList != null ? approvalLeavesList.size() : 0;
	    int holidayCount = holidayList != null ? holidayList.size() : 0;

	    List<Object[]> weekOffPattern = companyWeekOffRepo.findWeekOffDaysAndWeeks(orgId, branchCode);
	    int weekOffCount = countWeekOffsBetweenDates(from, to, weekOffPattern);

	    List<String> missingDates = checkInRepo.findByFromDateAndToDateAndEmpCodeAndOrgId(fromDate, toDate, empCode, orgId);

	    for (Object[] record : result) {
	        Map<String, Object> map = new HashMap<>();

	        // ✅ FIX: Remove (int) cast to preserve decimal part (e.g. 29.5)
	        double workingDays = record[0] != null ? Double.parseDouble(record[0].toString()) : 0.0;

	        double totalLeavedays1 = workingDays + holidayCount + empLeaveCount + weekOffCount;
	        long totalLeavedays2 = totalCompanyWorkingDays - empLeaveCount;

	        map.put("empCode", empCode);
	        map.put("EmpcheckInOutDays", workingDays); // ✅ Now it shows 29.5 correctly
	        map.put("holidaysCount", holidayCount);
	        map.put("empLeaveCount", empLeaveCount);
	        map.put("weekOffCount", weekOffCount);
	        map.put("totalCheckInOutLeave", totalLeavedays1);
	        map.put("totalCompanyWorkingDays", totalCompanyWorkingDays);
	        map.put("totalApprovedLeave", totalLeavedays2);
	        map.put("missingDates", missingDates);

	        map.put("status", (missingDates == null || missingDates.isEmpty()) ? "MATCHED" : "MISMATCHED");

	        detailsList.add(map);
	    }

	    return detailsList;
	}

	public int countWeekOffsBetweenDates(LocalDate from, LocalDate to, List<Object[]> weekOffPattern) {
	    int count = 0;
	    Map<DayOfWeek, Set<Integer>> weekMap = new HashMap<>();

	    for (Object[] row : weekOffPattern) {
	        String day = row[0].toString().toUpperCase();
	        Integer week = Integer.parseInt(row[1].toString());
	        DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);
	        weekMap.computeIfAbsent(dayOfWeek, k -> new HashSet<>()).add(week);
	    }

	    LocalDate current = from;
	    while (!current.isAfter(to)) {
	        DayOfWeek currentDay = current.getDayOfWeek();
	        int weekOfMonth = (current.getDayOfMonth() - 1) / 7 + 1;

	        if (weekMap.containsKey(currentDay)) {
	            Set<Integer> weeks = weekMap.get(currentDay);
	            if (weeks.contains(-1) || weeks.contains(weekOfMonth)) {
	                count++;
	            }
	        }
	        current = current.plusDays(1);
	    }
	    return count;
	}




	// InboxLeaveRequest Approval

	@Override
	public List<Map<String, Object>> getLeaveRequestForDashBoard(Long orgId, String reportingPersonCode,String branchCode) {
		Set<Object[]> result = leaveRequestRepo.getLeaveRequestForDashBoard(orgId, reportingPersonCode,branchCode);
		return getLeaveRequestForDashBoard(result);
	}

	private List<Map<String, Object>> getLeaveRequestForDashBoard(Set<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeName", record[0] != null ? record[0].toString() : "");
			map.put("employeeCode", record[1] != null ? record[1].toString() : "");
			map.put("leaveType", record[2] != null ? record[2].toString() : " ");
			map.put("startDate", record[3] != null ? record[3].toString() : " ");
			map.put("endDate", record[4] != null ? record[4].toString() : " ");
			map.put("totalDays", record[5] != null ? record[5].toString() : "0");
			map.put("reason", record[6] != null ? record[6].toString() : " ");
			map.put("id", record[7] != null ? record[7].toString() : " ");
			map.put("employeeEmail", record[8] != null ? record[8].toString() : "");
			map.put("screenName", record[9] != null ? record[9].toString() : "");


			detailsList.add(map);
		}
		return detailsList;
	}
	
	@Override
	public List<Map<String, Object>> getAllApprovedLeaveForTeam(Long orgId, String reportingPersonCode,String branchCode) {
		Set<Object[]> result = leaveRequestRepo.getAllApprovedLeaveForTeam(orgId, reportingPersonCode,branchCode);
		return getAllApprovedLeaveForTeam(result);
	}

	private List<Map<String, Object>> getAllApprovedLeaveForTeam(Set<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeName", record[0] != null ? record[0].toString() : "");
			map.put("employeeCode", record[1] != null ? record[1].toString() : "");
			map.put("leaveType", record[2] != null ? record[2].toString() : " ");
			map.put("startDate", record[3] != null ? record[3].toString() : " ");
			map.put("endDate", record[4] != null ? record[4].toString() : " ");
			map.put("totalDays", record[5] != null ? record[5].toString() : "0");
			map.put("reason", record[6] != null ? record[6].toString() : " ");
			map.put("id", record[7] != null ? record[7].toString() : " ");
			map.put("employeeEmail", record[8] != null ? record[8].toString() : "");
			map.put("screenName", record[9] != null ? record[9].toString() : "");


			detailsList.add(map);
		}
		return detailsList;
	}

	// uploadleaveprocess

	@Override
	@Transactional
	public void uploadLeaveData(MultipartFile file, Long orgId, String createdBy) throws Exception {
		List<LeaveProcessDTO> leaveProcessList = new ArrayList<>();

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = WorkbookFactory.create(inputStream)) {

			processLeaveSheet(workbook.getSheetAt(0), leaveProcessList, orgId, createdBy);

			for (LeaveProcessDTO leaveData : leaveProcessList) {
				saveLeaveData(leaveData);
			}
		}
	}

	private void processLeaveSheet(Sheet sheet, List<LeaveProcessDTO> leaveProcessList, Long orgId, String createdBy) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue; // Skip header row

			LeaveProcessDTO leaveData = new LeaveProcessDTO();
			leaveData.setEmployeeName(getStringCellValue(row.getCell(0)));
			leaveData.setEmployeeCode(getStringCellValue(row.getCell(1)));
			leaveData.setBranch(getStringCellValue(row.getCell(2)));
			leaveData.setBranchCode(getStringCellValue(row.getCell(3)));
			leaveData.setActive(getBooleanCellValue(row.getCell(4)));
			leaveData.setTotalCompanyWorkingDays(getBigDecimalCellValue(row.getCell(5)));
			leaveData.setTotalLeave(getBigDecimalCellValue(row.getCell(6)));
			leaveData.setLopLeave(getBigDecimalCellValue(row.getCell(7)));
			leaveData.setEmpTotalWorkingDays(getBigDecimalCellValue(row.getCell(8)));
			leaveData.setEmpSalaryDays(getBigDecimalCellValue(row.getCell(9)));
			leaveData.setMonth(getLongCellValue(row.getCell(10)));
			leaveData.setYear(getStringCellValue(row.getCell(11)));
			leaveData.setOrgId(orgId);
			leaveData.setCreatedBy(createdBy);

			leaveProcessList.add(leaveData);

		}
	}

	private void saveLeaveData(LeaveProcessDTO leaveData) {
		LeaveProcessVO leaveProcessVO = new LeaveProcessVO();
		BeanUtils.copyProperties(leaveData, leaveProcessVO);
		leaveProcessVO.setApprovedStatus("PENDING");
		leaveProcessVO.setUpdatedBy(leaveData.getCreatedBy());

		leaveProcessRepo.save(leaveProcessVO);

	}

	private BigDecimal getBigDecimalCellValue(Cell cell) {
		if (cell == null || cell.getCellType() != CellType.NUMERIC) {
			return BigDecimal.ZERO; // Default value if the cell is null or not numeric
		}
		return BigDecimal.valueOf(cell.getNumericCellValue());
	}

	private String getStringCellValue(Cell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			double numericValue = cell.getNumericCellValue();
			if (numericValue == Math.floor(numericValue)) {
				return String.valueOf((long) numericValue);
			}
			return String.valueOf(numericValue);
		default:
			return "";
		}
	}

	private Long getLongCellValue(Cell cell) {
		return (cell == null || cell.getCellType() != CellType.NUMERIC) ? null : (long) cell.getNumericCellValue();
	}

	private Boolean getBooleanCellValue(Cell cell) {
		if (cell == null)
			return false;

		switch (cell.getCellType()) {
		case BOOLEAN:
			return cell.getBooleanCellValue(); // Directly returns true/false
		case NUMERIC:
			return cell.getNumericCellValue() == 1; // Treat 1 as true, 0 as false
		case STRING:
			String value = cell.getStringCellValue().trim().toLowerCase();
			return value.equals("true") || value.equals("yes") || value.equals("1");
		default:
			return false; // Default to false if the cell is empty or unknown
		}
	}

	@Override
	public CompensatoryOffVO getCompensatoryOffVOById(Long id) {
		return compensatoryOffRepo.getCompensatoryOffVOById(id);

	}

	@Override
	public List<CompensatoryOffVO> getCompensatoryOffByOrgId(Long orgId,String empCode) {
		// TODO Auto-generated method stub
		return compensatoryOffRepo.getCompensatoryOffByOrgId(orgId,empCode);
	}
	
	
	@Override
	public Map<String, Object> createUpdateCompOff(@Valid List<CompensatoryOffDTO> compensatoryOffDTOList)
	        throws ApplicationException {

	    List<CompensatoryOffVO> compensatoryOffVOList = new ArrayList<>();
	    String message = "";

	    // ✅ Step 1: Check duplicate inside incoming request list
	    Set<String> duplicateCheckSet = new HashSet<>();

	    for (CompensatoryOffDTO dto : compensatoryOffDTOList) {
	        // Unique key based on duplicate rule (Branch + Emp + Org + LeaveCode + Date)
	        String uniqueKey = dto.getBranch().trim().toUpperCase() + "|"
	                + dto.getEmployeeCode().trim().toUpperCase() + "|"
	                + dto.getOrgId() + "|"
	                + dto.getLeaveCode().trim().toUpperCase() + "|"
	                + dto.getCompOffDate();

	        if (!duplicateCheckSet.add(uniqueKey)) {
	            throw new ApplicationException("Duplicate CompensatoryOff found in request for Date: "
	                    + dto.getCompOffDate() + " Employee: " + dto.getEmployeeCode());
	        }
	    }
	    
	    for (CompensatoryOffDTO dto : compensatoryOffDTOList) {
	        CompensatoryOffVO vo;

	        if (ObjectUtils.isNotEmpty(dto.getId())) {
	            vo = compensatoryOffRepo.findById(dto.getId())
	                    .orElseThrow(() -> new ApplicationException("Invalid compensatoryOff details"));
	            vo.setUpdatedBy(dto.getCreatedBy());
	            message = "Compensatory Off updated successfully";
	        } else {
	            vo = new CompensatoryOffVO();
	            
	            List<CompensatoryOffVO> existingList = compensatoryOffRepo.findByBranchAndEmployeeCodeAndOrgIdAndLeaveCodeAndCompOffDate(
	            	    dto.getBranch(), dto.getEmployeeCode(), dto.getOrgId(), dto.getLeaveCode(), dto.getCompOffDate());

	            	if (!existingList.isEmpty()) {
	            	    throw new ApplicationException("This Date: " + dto.getCompOffDate() + " CompensatoryOff Already Applied ");
	            	}

	            vo.setCreatedBy(dto.getCreatedBy());
	            vo.setUpdatedBy(dto.getCreatedBy());
	            message = "Compensatory Off created successfully";
	        }

	        mapDtoToVo(dto, vo);
	        compensatoryOffVOList.add(vo);
	    }

	    compensatoryOffRepo.saveAll(compensatoryOffVOList); // Save all records

	    Map<String, Object> response = new HashMap<>();
	    response.put("compensatoryOffVO", compensatoryOffVOList);
	    response.put("message", message);
	    return response;
	}

	private void mapDtoToVo(CompensatoryOffDTO dto, CompensatoryOffVO vo) {
	    vo.setEmployeeCode(dto.getEmployeeCode());
	    vo.setEmployeeName(dto.getEmployeeName());
	    vo.setOrgId(dto.getOrgId());
	    vo.setDepartment(dto.getDepartment());
	    vo.setDesignation(dto.getDesignation());

	    vo.setLeaveType(dto.getLeaveType());
	    vo.setLeaveCode(dto.getLeaveCode());
	    vo.setCompOffDate(dto.getCompOffDate());
	    vo.setCompOffDay(dto.getCompOffDay());
	    vo.setTotalDays(dto.getTotalDays());
	    vo.setNotes(dto.getNotes());
	    vo.setNotify(dto.getNotify());
	    vo.setAssignedBy(dto.getAssignedBy());
	    vo.setNotifyCode(dto.getNotifyCode());
	    vo.setNotifyEmail(dto.getNotifyEmail());
	    
	      if (dto.getId() != null) {
	            List<CompoffNotifyVO> compoff = compoffNotifyRepo.findByCompensatoryOffVO(vo);
	            compoffNotifyRepo.deleteAll(compoff);
	        }

	        // Set Poll Details from PollDetailsDTO
	        List<CompoffNotifyVO> compoffNotifyVOs = new ArrayList<>();
	        for (CompoffNotifyDTO compoffNotifyDTO : dto.getCompoffNotifyDTO()) {
	            CompoffNotifyVO compoffNotifyVO = new CompoffNotifyVO();
	            compoffNotifyVO.setNotify2(compoffNotifyDTO.getNotify2());
	            compoffNotifyVO.setNotify2Code(compoffNotifyDTO.getNotify2Code());
	            compoffNotifyVO.setNotify2Email(compoffNotifyDTO.getNotify2Email());

	            compoffNotifyVO.setCompensatoryOffVO(vo);  // Set parent reference in child
	            compoffNotifyVOs.add(compoffNotifyVO);
	        }
	        vo.setCompoffNotifyVO(compoffNotifyVOs);
	    
	    vo.setApprovalStatus("PENDING");

	    vo.setBranch(dto.getBranch());
	    vo.setBranchCode(dto.getBranchCode());
	}
	
	private String joinList(List<String> list) {
	    return (list == null || list.isEmpty()) ? null : String.join(",", list);
	}


	
	@Override
	public Map<String, Object> createApprovalCompOff(Long orgId, Long id, String employeeCode, String action, String actionBy,String notifyCode, String notify,String screenName)
	        throws ApplicationException {

	    CompensatoryOffVO compensatoryOffVO = compensatoryOffRepo.findByOrgIdAndIdAndEmployeeCode(orgId, id, employeeCode);
	    String message = "";
	    
	    if (compensatoryOffVO.getApprovalStatus() == null
	            || (!compensatoryOffVO.getApprovalStatus().equalsIgnoreCase("Approved")
	            && !compensatoryOffVO.getApprovalStatus().equalsIgnoreCase("Rejected"))) {

	        if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {
	        	
	        	if("APPROVED".equalsIgnoreCase(action)) {
	        	List<LeaveBalanceVO> leaveBalanceVOs = new ArrayList<LeaveBalanceVO>();

	        	 LeaveBalanceVO leaveBalanceVO = new LeaveBalanceVO();
	 	        leaveBalanceVO.setLeaveCode(compensatoryOffVO.getLeaveCode());
	 	        leaveBalanceVO.setLeaveType(compensatoryOffVO.getLeaveType());
	 	        leaveBalanceVO.setTotalLeave(compensatoryOffVO.getTotalDays());
	 	        leaveBalanceVO.setEmployeeName(compensatoryOffVO.getEmployeeName());
	 	        leaveBalanceVO.setEmployeeCode(compensatoryOffVO.getEmployeeCode());
	 	        leaveBalanceVO.setOrgId(compensatoryOffVO.getOrgId());
	 	        leaveBalanceVO.setBranch(compensatoryOffVO.getBranch());
	 	        leaveBalanceVO.setBranchCode(compensatoryOffVO.getBranchCode());
	 	        leaveBalanceVO.setLeaveStatus("Assigned");
	 	        leaveBalanceVOs.add(leaveBalanceVO);

				leaveBalanceRepo.saveAll(leaveBalanceVOs);
			}
	        	
	        	compensatoryOffVO.setApprovalStatus(action);
	        	compensatoryOffVO.setApproveBy(actionBy);

	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
	            compensatoryOffVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

	            compensatoryOffRepo.save(compensatoryOffVO);
	            
	        	if (compensatoryOffVO.getApprovalStatus().equalsIgnoreCase("Approved")) {
				    message = "Approved Successfully";
				} else if (compensatoryOffVO.getApprovalStatus().equalsIgnoreCase("Rejected")) {
				    message = "Rejected Successfully";
				}
	        }
	    }
	     else if (compensatoryOffVO.getApprovalStatus().equalsIgnoreCase("Approved")) {
	        throw new ApplicationException("This CompensatoryOff Already Approved");
	    } else if (compensatoryOffVO.getApprovalStatus().equalsIgnoreCase("Rejected")) {
	        throw new ApplicationException("This CompensatoryOff Already Rejected");
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("compensatoryOffVO", compensatoryOffVO);
	    response.put("message", message);
	    return response;	
	    }
	
	@Override
	public List<Map<String, Object>> getCompoffRequestForDashBoard(Long orgId, String reportingPersonCode,String branchCode) {
		Set<Object[]> result = compensatoryOffRepo.getCompoffRequestForDashBoard(orgId, reportingPersonCode,branchCode);
		return getCompoffRequestForDashBoard(result);
	}

	private List<Map<String, Object>> getCompoffRequestForDashBoard(Set<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeName", record[0] != null ? record[0].toString() : "");
			map.put("employeeCode", record[1] != null ? record[1].toString() : "");
			map.put("leaveType", record[2] != null ? record[2].toString() : " ");
			map.put("compOffDate", record[3] != null ? record[3].toString() : " ");
			map.put("totalDays", record[4] != null ? record[4].toString() : "0");
			map.put("reason", record[5] != null ? record[5].toString() : " ");
			map.put("id", record[6] != null ? record[6].toString() : " ");
			map.put("employeeEmail", record[7] != null ? record[7].toString() : "");
			map.put("screenName", record[8] != null ? record[8].toString() : "");

			detailsList.add(map);
		}
		return detailsList;
	}
	
	//uploadcheckin
	
	@Override
	public Map<String, Object> uploadExcelData(MultipartFile files, Long orgId) {
	    Map<String, Object> response = new HashMap<>();
	    List<CheckInVO> checkIns = new ArrayList<>();

	    try (Workbook workbook = WorkbookFactory.create(files.getInputStream())) {
	        Sheet sheet = workbook.getSheetAt(0);
	        boolean isFirstRow = true;

	        for (Row row : sheet) {
	            if (isFirstRow) {
	                isFirstRow = false;
	                continue;
	            }

	            String branch = getCellValueAsString(row.getCell(0));
	            String empCode = getCellValueAsString(row.getCell(1));
	            String empName = getCellValueAsString(row.getCell(2));
	            LocalDate checkInDate = getCellValueAsDate(row.getCell(3));
	            LocalTime entryTime = getCellValueAsTime(row.getCell(4));
	            String status = getCellValueAsString(row.getCell(5));

	            // Skip if required fields are missing
	            if (empCode == null || checkInDate == null || entryTime == null || status == null) {
	                continue;
	            }

	            CheckInVO checkIn = new CheckInVO();
	            checkIn.setBranch(branch);
	            checkIn.setEmpCode(empCode);
	            checkIn.setEmpName(empName);
	            checkIn.setCheckInDate(checkInDate);
	            checkIn.setEntryTime(entryTime);
	            checkIn.setStatus(status);
	            checkIn.setOrgId(orgId);
	            checkIn.setCreatedOn(LocalDateTime.of(checkInDate, LocalTime.now()));

	            checkIns.add(checkIn);
	        }

	        checkInRepo.saveAll(checkIns);
	        response.put("message", "CheckInOut data uploaded successfully");
	        return response;

	    } catch (Exception e) {
	        response.put("message", "Failed to process Excel file: " + e.getMessage());
	        return response;
	    }
	}

	private String getCellValueAsString(Cell cell) {
	    if (cell == null) return null;

	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue().trim();
	        case NUMERIC:
	            return String.valueOf((long) cell.getNumericCellValue());
	        case BLANK:
	            return null;
	        default:
	            return null;
	    }
	}

	private LocalDate getCellValueAsDate(Cell cell) {
	    if (cell == null) return null;

	    try {
	        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
	            return cell.getLocalDateTimeCellValue().toLocalDate();
	        } else if (cell.getCellType() == CellType.STRING) {
	            String dateStr = cell.getStringCellValue().trim();
	            if (dateStr.isEmpty()) return null;

	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // ✅ FIXED
	            return LocalDate.parse(dateStr, formatter);
	        }
	    } catch (Exception e) {
	        System.err.println("Error parsing date cell: " + e.getMessage());
	    }
	    return null;
	}

	private LocalTime getCellValueAsTime(Cell cell) {
	    if (cell == null) return null;

	    try {
	        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
	            return cell.getLocalDateTimeCellValue().toLocalTime();
	        } else if (cell.getCellType() == CellType.STRING) {
	            String timeStr = cell.getStringCellValue().trim();
	            if (timeStr.isEmpty()) return null;
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	            return LocalTime.parse(timeStr, formatter);
	        }
	    } catch (Exception e) {
	        System.err.println("Error parsing time cell: " + e.getMessage());
	    }
	    return null;
	}

	    //AttandanceReport
	    
		@Override
		public List<LeaveProcessVO> getAttandanceReport( Long orgId,
				 String employeeCode, String year, String month) {
			// TODO Auto-generated method stub
			return leaveProcessRepo.getAttandanceReport(orgId,employeeCode,year,month);
		}

		//CheckInOutReport
		
		@Override
		public List<Map<String, Object>>  getCheckInOutReport( Long orgId,
				 String employeeCode, String fromDate, String toDate,String branch) {
			Set<Object[]> result = checkInRepo.getCheckInOutReport(orgId,employeeCode,fromDate,toDate,branch);
			return CheckInOutReport(result);
		}

		private List<Map<String, Object>> CheckInOutReport(Set<Object[]> result) {
			List<Map<String, Object>> detailsList = new ArrayList<>();

			for (Object[] record : result) {
				Map<String, Object> map = new HashMap<>();
				map.put("employeeCode", record[0] != null ? record[0].toString() : "");
				map.put("employeeName", record[1] != null ? record[1].toString() : "");
				map.put("entryDate", record[2] != null ? record[2].toString() : " ");
				map.put("checkInTime", record[3] != null ? record[3].toString() : " ");
				map.put("checkOutTime", record[4] != null ? record[4].toString() : "0");
				map.put("grossHours", record[5] != null ? record[5].toString() : " ");
				map.put("effectiveHours", record[6] != null ? record[6].toString() : " ");
				map.put("otHours", 0);


				detailsList.add(map);
			}
			return detailsList;
		}
		
		
		//ApprovalALL
		
		@Override
		public String createUnifiedApprovalAllTypes(Long orgId, String action, String actionBy, String notifyCode, String notify)
		        throws ApplicationException {

		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
		    String formattedDateTime = LocalDateTime.now().format(formatter).toUpperCase();
		    String finalMessage = "";

		    // ------------------- 1. Leave Request Approval -------------------
		    List<LeaveRequestVO> leaveRequests = leaveRequestRepo.findByOrgIdAndNotifyCode(orgId, notifyCode);
		    for (LeaveRequestVO leaveRequestVO : leaveRequests) {
		        if (leaveRequestVO.getApproveStatus() == null ||
		            (!leaveRequestVO.getApproveStatus().equalsIgnoreCase("Approved") &&
		             !leaveRequestVO.getApproveStatus().equalsIgnoreCase("Rejected"))) {

		            LocalDate fromDate = leaveRequestVO.getFromDate();
		            LocalDate toDate = leaveRequestVO.getToDate();
		            CompanyVO company = companyRepo.findById(orgId)
		                .orElseThrow(() -> new ApplicationException("Company not found"));

		            String policyType = company.getLeavePolicy();
		            List<Object[]> weekOffPatterns = companyWeekOffRepo.findWeekOffOccurrencesByCompanyId(company.getId());
		            List<LocalDate> holidays = holidayRepo.findByOrgId(orgId).stream()
		                .map(HolidayVO::getHolidayDate).collect(Collectors.toList());

		            List<ApprovalLeavesVO> approvalLeavesList = new ArrayList<>();
		            BigDecimal totalDays = BigDecimal.ZERO;

		            while (!fromDate.isAfter(toDate)) {
		                boolean isWeekOff = isWeekOffDay(fromDate, weekOffPatterns);
		                boolean isHoliday = holidays.contains(fromDate);

		                if ("REGULAR".equalsIgnoreCase(policyType) && (isWeekOff || isHoliday)) {
		                    fromDate = fromDate.plusDays(1);
		                    continue;
		                }

		                ApprovalLeavesVO approvalLeavesVO = new ApprovalLeavesVO();
		                approvalLeavesVO.setEmployeeCode(leaveRequestVO.getEmployeeCode());
		                approvalLeavesVO.setEmployeeName(leaveRequestVO.getEmployeeName());
		                approvalLeavesVO.setLeaveType(leaveRequestVO.getLeaveType());
		                approvalLeavesVO.setLeaveDate(fromDate);
		                approvalLeavesVO.setOrgId(orgId);
		                approvalLeavesVO.setBranch(leaveRequestVO.getBranch());
		                approvalLeavesVO.setBranchCode(leaveRequestVO.getBranchCode());
		                approvalLeavesVO.setApproveStatus(action);
		                approvalLeavesVO.setApproveBy(actionBy);

		                if ("HALF DAY".equalsIgnoreCase(leaveRequestVO.getSelectLeave())) {
		                    approvalLeavesVO.setTotalLeave(BigDecimal.valueOf(0.5));
		                    totalDays = totalDays.add(BigDecimal.valueOf(0.5));
		                } else {
		                    approvalLeavesVO.setTotalLeave(BigDecimal.ONE);
		                    totalDays = totalDays.add(BigDecimal.ONE);
		                }

		                approvalLeavesList.add(approvalLeavesVO);
		                fromDate = fromDate.plusDays(1);
		            }

		            approvalLeavesRepo.saveAll(approvalLeavesList);

		            LeaveBalanceVO leaveBalanceVO = new LeaveBalanceVO();
		            leaveBalanceVO.setLeaveCode(leaveRequestVO.getLeaveCode());
		            leaveBalanceVO.setLeaveType(leaveRequestVO.getLeaveType());
		            leaveBalanceVO.setEmployeeName(leaveRequestVO.getEmployeeName());
		            leaveBalanceVO.setEmployeeCode(leaveRequestVO.getEmployeeCode());
		            leaveBalanceVO.setOrgId(orgId);
		            leaveBalanceVO.setBranch(leaveRequestVO.getBranch());
		            leaveBalanceVO.setBranchCode(leaveRequestVO.getBranchCode());
		            leaveBalanceVO.setLeaveStatus("Leave");
		            leaveBalanceVO.setTotalLeave(totalDays.negate());

		            leaveBalanceRepo.save(leaveBalanceVO);

		            leaveRequestVO.setApproveStatus(action);
		            leaveRequestVO.setApproveBy(actionBy);
		            leaveRequestVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());		            
		            leaveRequestRepo.save(leaveRequestVO);
		        }
		    }

		    // ------------------- 2. Compensatory Off Approval -------------------
		    List<CompensatoryOffVO> compensatoryOffList = compensatoryOffRepo.findByOrgIdAndNotifyCode(orgId, notifyCode);
		    List<LeaveBalanceVO> leaveBalanceList = new ArrayList<>();

		    for (CompensatoryOffVO compOff : compensatoryOffList) {
		        if (compOff.getApprovalStatus() == null ||
		            (!compOff.getApprovalStatus().equalsIgnoreCase("Approved") &&
		             !compOff.getApprovalStatus().equalsIgnoreCase("Rejected"))) {

		            if ("APPROVED".equalsIgnoreCase(action)) {
		                LeaveBalanceVO leaveBalanceVO = new LeaveBalanceVO();
		                leaveBalanceVO.setLeaveCode(compOff.getLeaveCode());
		                leaveBalanceVO.setLeaveType(compOff.getLeaveType());
		                leaveBalanceVO.setTotalLeave(compOff.getTotalDays());
		                leaveBalanceVO.setEmployeeName(compOff.getEmployeeName());
		                leaveBalanceVO.setEmployeeCode(compOff.getEmployeeCode());
		                leaveBalanceVO.setOrgId(compOff.getOrgId());
		                leaveBalanceVO.setBranch(compOff.getBranch());
		                leaveBalanceVO.setBranchCode(compOff.getBranchCode());
		                leaveBalanceVO.setLeaveStatus("Assigned");

		                leaveBalanceList.add(leaveBalanceVO);
		            }

		            compOff.setApprovalStatus(action);
		            compOff.setApproveBy(actionBy);
		          
		            compOff.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());		            
		            
		            compensatoryOffRepo.save(compOff);
		        }
		    }

		    if (!leaveBalanceList.isEmpty()) {
		        leaveBalanceRepo.saveAll(leaveBalanceList);
		    }

		    // ------------------- 3. CheckInOutAdjustment Approval -------------------
		    List<CheckInOutAdjustmentVO> adjustments = checkInOutAdjustmentRepo.findByOrgIdAndNotifyCode(orgId, notifyCode);
		    for (CheckInOutAdjustmentVO adj : adjustments) {
		        if (!"APPROVED".equalsIgnoreCase(adj.getApprovalStatus()) && !"REJECTED".equalsIgnoreCase(adj.getApprovalStatus())) {
		            adj.setApprovalStatus(action.toUpperCase());
		            adj.setApproveBy(actionBy);
		            adj.setApproveOn(formattedDateTime);
		        }
		    }
		    checkInOutAdjustmentRepo.saveAll(adjustments);

		    // ------------------- 4. CheckOut Approval -------------------
		    List<CheckInVO> checkOutList = checkInRepo.findByOrgIdAndNotifyCodeAndStatus(orgId, notifyCode, "OUT");
		    for (CheckInVO checkOutVO : checkOutList) {
		        if (!"APPROVED".equalsIgnoreCase(checkOutVO.getApprovalStatus()) &&
		            !"REJECTED".equalsIgnoreCase(checkOutVO.getApprovalStatus())) {

		            if ("REJECTED".equalsIgnoreCase(action)) {
		                checkOutVO.setEntryTime(LocalTime.parse("00:00:00"));
		            }

		            checkOutVO.setApprovalStatus(action.toUpperCase());
		            checkOutVO.setApproveBy(actionBy);
		            checkOutVO.setApproveOn(formattedDateTime);
		        }
		    }
		    checkInRepo.saveAll(checkOutList);

		    // Final return message
		    return action.equalsIgnoreCase("APPROVED") ?
		            "All types approved successfully" : "All types rejected successfully";
		}
		
		//travelrequest
		
		@Override
		public Map<String, Object> createUpdateTravelRequest(TravelRequestDTO travelRequestDTO) throws ApplicationException {

			TravelRequestVO travelRequestVO = new TravelRequestVO();
			String message;
			String screenCode = "TR";
			if (ObjectUtils.isNotEmpty(travelRequestDTO.getId())) {
				travelRequestVO = travelRequestRepo.findById(travelRequestDTO.getId())
						.orElseThrow(() -> new ApplicationException("Invalid travelRequest details"));

				travelRequestVO.setUpdatedBy(travelRequestDTO.getCreatedBy());
				message = "TravelRequest Updated Successfully";
			} else {


				travelRequestVO.setCreatedBy(travelRequestDTO.getCreatedBy());
				travelRequestVO.setUpdatedBy(travelRequestDTO.getCreatedBy());
				message = "TravelRequest Created Successfully";
			}

			createUpdateTravelRequestVOByTravelRequestDTO(travelRequestDTO, travelRequestVO);
			travelRequestRepo.save(travelRequestVO);
			Map<String, Object> response = new HashMap<>();
			response.put("travelRequestVO", travelRequestVO);
			response.put("message", message);
			return response;
		}

		private void createUpdateTravelRequestVOByTravelRequestDTO(TravelRequestDTO travelRequestDTO, TravelRequestVO travelRequestVO) {
			travelRequestVO.setFromDate(travelRequestDTO.getFromDate());
			travelRequestVO.setToDate(travelRequestDTO.getToDate());
			travelRequestVO.setTravelReason(travelRequestDTO.getTravelReason());
			travelRequestVO.setModeOfTravel(travelRequestDTO.getModeOfTravel());
			travelRequestVO.setApprovingAuthorities(travelRequestDTO.getApprovingAuthorities());
			travelRequestVO.setApprovingAuthoritiesCode(travelRequestDTO.getApprovingAuthoritiesCode());
			travelRequestVO.setApprovingAuthoritiesEmail(travelRequestDTO.getApprovingAuthoritiesEmail());

			travelRequestVO.setBranch(travelRequestDTO.getBranch());
			travelRequestVO.setBranchCode(travelRequestDTO.getBranchCode());
			travelRequestVO.setFinYear(travelRequestDTO.getFinYear());
			travelRequestVO.setEmployeeCode(travelRequestDTO.getEmployeeCode());
			travelRequestVO.setEmployeeName(travelRequestDTO.getEmployeeName());
			travelRequestVO.setOrgId(travelRequestDTO.getOrgId());
			travelRequestVO.setApproveStatus("PENDING");


		}
		
		
		@Override
		public TravelRequestVO getTravelRequestById(Long id) {

			return travelRequestRepo.getTravelRequestById(id);
		}
		
		@Override
		public List<TravelRequestVO> getTravelRequestByOrgId(Long orgId) {
			// TODO Auto-generated method stub
			return travelRequestRepo.getTravelRequestByOrgId(orgId);
		}
		
		//WORKFROMHOME
		
		@Override
		public Map<String, Object> createUpdateWorkFromHome(WorkFromHomeDTO workFromHomeDTO) throws ApplicationException {

			WorkFromHomeVO workFromHomeVO = new WorkFromHomeVO();
			String message;
			String screenCode = "WFH";
			if (ObjectUtils.isNotEmpty(workFromHomeDTO.getId())) {
				workFromHomeVO = workFromHomeRepo.findById(workFromHomeDTO.getId())
						.orElseThrow(() -> new ApplicationException("Invalid WorkFromHome details"));

				workFromHomeVO.setUpdatedBy(workFromHomeDTO.getCreatedBy());
				message = "WorkFromHome Updated Successfully";
			} else {
				workFromHomeVO.setCreatedBy(workFromHomeDTO.getCreatedBy());
				workFromHomeVO.setUpdatedBy(workFromHomeDTO.getCreatedBy());
				message = "WorkFromHome Created Successfully";
			}

			createUpdateWorkFromHomeVOByWorkFromHomeDTO(workFromHomeDTO, workFromHomeVO);
			workFromHomeRepo.save(workFromHomeVO);
			Map<String, Object> response = new HashMap<>();
			response.put("workFromHomeVO", workFromHomeVO);
			response.put("message", message);
			return response;
		}

		private void createUpdateWorkFromHomeVOByWorkFromHomeDTO(WorkFromHomeDTO workFromHomeDTO, WorkFromHomeVO workFromHomeVO) {
			workFromHomeVO.setWfhDate(workFromHomeDTO.getWfhDate());
			workFromHomeVO.setReason(workFromHomeDTO.getReason());
			workFromHomeVO.setWorkAccomplished(workFromHomeDTO.getWorkAccomplished());
			workFromHomeVO.setReportingManager(workFromHomeDTO.getReportingManager());
			workFromHomeVO.setReportingManagerCode(workFromHomeDTO.getReportingManagerCode());
			workFromHomeVO.setReportingManagerEmail(workFromHomeDTO.getReportingManagerEmail());
			workFromHomeVO.setDepartmentHead(workFromHomeDTO.getDepartmentHead());
			workFromHomeVO.setDepartmentHeadCode(workFromHomeDTO.getDepartmentHeadCode());
			workFromHomeVO.setDepartmentHeadEmail(workFromHomeDTO.getDepartmentHeadEmail());
			workFromHomeVO.setBranch(workFromHomeDTO.getBranch());
			workFromHomeVO.setBranchCode(workFromHomeDTO.getBranchCode());
			workFromHomeVO.setFinYear(workFromHomeDTO.getFinYear());
			workFromHomeVO.setEmployeeCode(workFromHomeDTO.getEmployeeCode());
			workFromHomeVO.setEmployeeName(workFromHomeDTO.getEmployeeName());
			workFromHomeVO.setOrgId(workFromHomeDTO.getOrgId());
			workFromHomeVO.setEmployeeEmail(workFromHomeDTO.getEmployeeEmail());
			workFromHomeVO.setApproveStatus("PENDING");


		}
		
		@Override
		public WorkFromHomeVO getWorkFromHomeById(Long id) {

			return workFromHomeRepo.getWorkFromHomeById(id);
		}
		

		@Override
		public List<WorkFromHomeVO> getWorkFromHomeByOrgId(Long orgId) {
			// TODO Auto-generated method stub
			return workFromHomeRepo.getWorkFromHomeByOrgId(orgId);
		}
		
		
		@Override
		public Map<String, Object> createApprovalTravelRequest(Long orgId, String employeeCode, String action, String actionBy,
				Long id, String notifyCode, String notify,String screenName) throws ApplicationException {

			TravelRequestVO ticketRequestVO = travelRequestRepo.findByOrgIdAndEmployeeCodeAndId(orgId,
					employeeCode, id);
			String message = "";

			if (ticketRequestVO.getApproveStatus() == null || (!ticketRequestVO.getApproveStatus().equalsIgnoreCase("Approved"))  && (!ticketRequestVO.getApproveStatus().equalsIgnoreCase("Rejected") )) {

				if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {

					if ("REJECTED".equalsIgnoreCase(action)) {
						// Type casting from String to LocalTime

						// Set entry time
						ticketRequestVO.setApproveStatus(action);
					}

					ticketRequestVO.setApproveStatus(action);
					ticketRequestVO.setApproveBy(actionBy);

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
					ticketRequestVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

					travelRequestRepo.save(ticketRequestVO);
					
					if (ticketRequestVO.getApproveStatus().equalsIgnoreCase("Approved")) {
					    message = "Approved Successfully";
					} else if (ticketRequestVO.getApproveStatus().equalsIgnoreCase("Rejected")) {
					    message = "Rejected Successfully";
					}
				}

			} else if (ticketRequestVO.getApproveStatus().equalsIgnoreCase("Approved")) {
				throw new ApplicationException("This TicketRequest Already Approved");
			} else if (ticketRequestVO.getApproveStatus().equalsIgnoreCase("Rejected")) {
				throw new ApplicationException("This TicketRequest Already Rejected");
			}

			Map<String, Object> response = new HashMap<>();
			response.put("ticketRequestVO", ticketRequestVO);
			response.put("message", message);
			return response;
		}
		

		@Override
		public Map<String, Object> createApprovalWorkFromHome(Long orgId, String employeeCode, String action, String actionBy,
				Long id, String notifyCode, String notify,String screenName) throws ApplicationException {

			WorkFromHomeVO workFromHomeVO = workFromHomeRepo.findByOrgIdAndEmployeeCodeAndId(orgId,
					employeeCode, id);
			String message = "";

			if (workFromHomeVO.getApproveStatus() == null || (!workFromHomeVO.getApproveStatus().equalsIgnoreCase("Approved"))  && (!workFromHomeVO.getApproveStatus().equalsIgnoreCase("Rejected") )) {

				if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {

					if ("REJECTED".equalsIgnoreCase(action)) {
						// Type casting from String to LocalTime

						// Set entry time
						workFromHomeVO.setApproveStatus(action);
					}

					workFromHomeVO.setApproveStatus(action);
					workFromHomeVO.setApproveBy(actionBy);

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
					workFromHomeVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

					workFromHomeRepo.save(workFromHomeVO);
					
					if (workFromHomeVO.getApproveStatus().equalsIgnoreCase("Approved")) {
					    message = "Approved Successfully";
					} else if (workFromHomeVO.getApproveStatus().equalsIgnoreCase("Rejected")) {
					    message = "Rejected Successfully";
					}
				}

			} else if (workFromHomeVO.getApproveStatus().equalsIgnoreCase("Approved")) {
				throw new ApplicationException("This WorkFromHome Already Approved");
			} else if (workFromHomeVO.getApproveStatus().equalsIgnoreCase("Rejected")) {
				throw new ApplicationException("This WorkFromHome Already Rejected");
			}

			Map<String, Object> response = new HashMap<>();
			response.put("workFromHomeVO", workFromHomeVO);
			response.put("message", message);
			return response;
		}
		
		
		@Override
		public List<Map<String, Object>> getPendingWorkFromHomeForDashBoard(Long orgId, String reportingPersonCode,String branchCode) {
			Set<Object[]> result = workFromHomeRepo.getPendingWorkFromHomeForDashBoard(orgId, reportingPersonCode,branchCode);
			return getPendingWorkFromHomeForDashBoard(result);
		}

		private List<Map<String, Object>> getPendingWorkFromHomeForDashBoard(Set<Object[]> result) {
			List<Map<String, Object>> detailsList = new ArrayList<>();

			for (Object[] record : result) {
				Map<String, Object> map = new HashMap<>();
				map.put("employeeName", record[0] != null ? record[0].toString() : "");
				map.put("employeeCode", record[1] != null ? record[1].toString() : "");
				map.put("wfhDate", record[2] != null ? record[2].toString() : " ");
				map.put("workAccomplished", record[3] != null ? record[3].toString() : " ");
				map.put("departmentHead", record[4] != null ? record[4].toString() : " ");
				map.put("departmentHeadCode", record[5] != null ? record[5].toString() : " ");
				map.put("departmentHeadEmail", record[6] != null ? record[6].toString() : " ");
				map.put("screenName", record[7] != null ? record[7].toString() : " ");
				map.put("reportingManager", record[8] != null ? record[8].toString() : "");
				map.put("reportingManagerCode", record[9] != null ? record[9].toString() : "");
				map.put("reportingManagerEmail", record[10] != null ? record[10].toString() : "");
				map.put("reason", record[11] != null ? record[11].toString() : "");
				map.put("finYear", record[12] != null ? record[12].toString() : "");
				map.put("orgId", record[13] != null ? record[13].toString() : "");
				map.put("employeeEmail", record[14] != null ? record[14].toString() : "");
				map.put("id", record[15] != null ? record[15].toString() : "");

				detailsList.add(map);
			}
			return detailsList;
		}
		
		

		@Override
		public List<Map<String, Object>> getPendingTravelRequestForDashBoard(Long orgId, String reportingPersonCode,String branchCode) {
			Set<Object[]> result = travelRequestRepo.getPendingTravelRequestForDashBoard(orgId, reportingPersonCode,branchCode);
			return getPendingTravelRequestForDashBoard(result);
		}

		private List<Map<String, Object>> getPendingTravelRequestForDashBoard(Set<Object[]> result) {
			List<Map<String, Object>> detailsList = new ArrayList<>();
			
			for (Object[] record : result) {
				Map<String, Object> map = new HashMap<>();
				map.put("employeeName", record[0] != null ? record[0].toString() : "");
				map.put("employeeCode", record[1] != null ? record[1].toString() : "");
				map.put("approvingAuthorities", record[2] != null ? record[2].toString() : " ");
				map.put("fromDate", record[3] != null ? record[3].toString() : " ");
				map.put("modeOfTravel", record[4] != null ? record[4].toString() : " ");
				map.put("toDate", record[5] != null ? record[5].toString() : " ");
				map.put("travelReason", record[6] != null ? record[6].toString() : " ");
				map.put("screenName", record[7] != null ? record[7].toString() : " ");
				map.put("screenCode", record[8] != null ? record[8].toString() : "");
				map.put("approvingAuthoritiesCode", record[9] != null ? record[9].toString() : "");
				map.put("approvingAuthoritiesEmail", record[10] != null ? record[10].toString() : "");
				map.put("finYear", record[11] != null ? record[11].toString() : "");
				map.put("orgId", record[12] != null ? record[12].toString() : "");
				map.put("employeeEmail", record[13] != null ? record[13].toString() : "");
				map.put("id", record[14] != null ? record[14].toString() : "");


				detailsList.add(map);
			}
			return detailsList;
		}
		

}		           

