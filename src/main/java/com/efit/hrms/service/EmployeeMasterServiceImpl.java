package com.efit.hrms.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efit.hrms.dto.PermissionRequestDTO;
import com.efit.hrms.dto.PermissionRequestNotifyDTO;
import com.efit.hrms.dto.PfEsiAmountDTO;
import com.efit.hrms.dto.SalaryDetectionDetailsDTO;
import com.efit.hrms.dto.SalaryEarningDetailsDTO;
import com.efit.hrms.dto.SalaryHeadsDTO;
import com.efit.hrms.dto.SalaryProcessDTO;
import com.efit.hrms.dto.SalaryStructureDTO;
import com.efit.hrms.entity.AttendanceSummaryVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.LeaveProcessVO;
import com.efit.hrms.entity.PermissionRequestNotifyVO;
import com.efit.hrms.entity.PermissionRequestVO;
import com.efit.hrms.entity.SalaryDetectionDetailsVO;
import com.efit.hrms.entity.SalaryEarningDetailsVO;
import com.efit.hrms.entity.SalaryHeadsVO;
import com.efit.hrms.entity.SalaryProcessVO;
import com.efit.hrms.entity.SalaryStructureVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.EmployeeRepo;
import com.efit.hrms.repo.LeaveProcessRepo;
import com.efit.hrms.repo.PermissionRequestNotifyRepo;
import com.efit.hrms.repo.PermissionRequestRepo;
import com.efit.hrms.repo.SalaryDetectionDetailsRepo;
import com.efit.hrms.repo.SalaryEarningDetailsRepo;
import com.efit.hrms.repo.SalaryHeadsRepo;
import com.efit.hrms.repo.SalaryProcessRepo;
import com.efit.hrms.repo.SalaryStructureRepo;

@Service
public class EmployeeMasterServiceImpl implements EmployeeMasterService {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeMasterServiceImpl.class);

	@Autowired
	SalaryHeadsRepo salaryHeadsRepo;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	SalaryStructureRepo salaryStructureRepo;

	@Autowired
	SalaryDetectionDetailsRepo salaryDetectionDetailsRepo;

	@Autowired
	PermissionRequestRepo permissionRequestRepo;

	@Autowired
	SalaryEarningDetailsRepo salaryEarningDetailsRepo;

	@Autowired
	SalaryProcessRepo salaryProcessRepo;

	@Autowired
	LeaveProcessRepo leaveProcessRepo;

	@Autowired
	PermissionRequestNotifyRepo  permissionRequestNotifyRepo;
	

	@Override
	@Transactional
	public Map<String, Object> createUpdateSalaryHeads(SalaryHeadsDTO salaryHeadsDTO) throws ApplicationException {
		final SalaryHeadsVO salaryHeadsVO; // Declare final reference
		Map<String, Object> response = new LinkedHashMap<>(); // Preserve order

		if (salaryHeadsDTO.getId() != null) {
			// If ID is provided, check if it exists
			salaryHeadsVO = salaryHeadsRepo.findById(salaryHeadsDTO.getId()).orElseThrow(
					() -> new ApplicationException("Error: SalaryHeads ID " + salaryHeadsDTO.getId() + " not found!"));

			// Updating existing record

			if (!salaryHeadsVO.getHeading().equalsIgnoreCase(salaryHeadsDTO.getHeading())) {
				if (salaryHeadsRepo.existsByHeadingAndOrgId(salaryHeadsDTO.getHeading(), salaryHeadsDTO.getOrgId())) {
					String errorMessage = String.format("The Heading: %s already exists in This Organization.",
							salaryHeadsDTO.getHeading());
					throw new ApplicationException(errorMessage);
				}
				salaryHeadsVO.setHeading(salaryHeadsDTO.getHeading().toUpperCase());
			}

			if (!salaryHeadsVO.getCode().equalsIgnoreCase(salaryHeadsDTO.getCode())) {
				if (salaryHeadsRepo.existsByCodeAndOrgId(salaryHeadsDTO.getCode(), salaryHeadsDTO.getOrgId())) {
					String errorMessage = String.format("The Code: %s already exists in This Organization.",
							salaryHeadsDTO.getCode());
					throw new ApplicationException(errorMessage);
				}
				salaryHeadsVO.setCode(salaryHeadsDTO.getCode().toUpperCase());
			}
			salaryHeadsVO.setUpdatedBy(salaryHeadsDTO.getCreatedBy());
			response.put("message", "Salary Heads Updated Successfully");
		} else {
			// Creating new record
			salaryHeadsVO = new SalaryHeadsVO();

			if (salaryHeadsRepo.existsByHeadingAndOrgId(salaryHeadsDTO.getHeading(), salaryHeadsDTO.getOrgId())) {
				String errorMessage = String.format("The Heading: %s already exists in This Organization.",
						salaryHeadsDTO.getHeading());
				throw new ApplicationException(errorMessage);
			}

			if (salaryHeadsRepo.existsByCodeAndOrgId(salaryHeadsDTO.getCode(), salaryHeadsDTO.getOrgId())) {
				String errorMessage = String.format("The Code: %s already exists in This Organization.",
						salaryHeadsDTO.getCode());
				throw new ApplicationException(errorMessage);
			}

			salaryHeadsVO.setCreatedBy(salaryHeadsDTO.getCreatedBy());
			salaryHeadsVO.setUpdatedBy(salaryHeadsDTO.getCreatedBy());
			response.put("message", "Salary Heads Created Successfully");
		}

		// Set other fields
		salaryHeadsVO.setHeading(salaryHeadsDTO.getHeading());
		salaryHeadsVO.setCode(salaryHeadsDTO.getCode());
		salaryHeadsVO.setCategory(salaryHeadsDTO.getCategory());
		salaryHeadsVO.setType(salaryHeadsDTO.getType());
		salaryHeadsVO.setActive(salaryHeadsDTO.isActive());
		salaryHeadsVO.setOrgId(salaryHeadsDTO.getOrgId());
		salaryHeadsVO.setBranch(salaryHeadsDTO.getBranch());
		salaryHeadsVO.setBranchCode(salaryHeadsDTO.getBranchCode());
//		salaryHeadsVO.setFinYear(salaryHeadsDTO.getFinYear());

		// Save Parent Record
		final SalaryHeadsVO savedSalaryHeadsVO = salaryHeadsRepo.save(salaryHeadsVO); // Make final

		// Attach parent details
		Map<String, Object> paramObjectsMap = new LinkedHashMap<>();
		paramObjectsMap.put("salaryHeadsVO", savedSalaryHeadsVO);
		response.put("paramObjectsMap", paramObjectsMap);

		return response;
	}

	@Override
	public SalaryHeadsVO getSalaryHeadsById(Long id) {
		return Optional.ofNullable(salaryHeadsRepo.getSalaryHeadsById(id))
				.orElseThrow(() -> new RuntimeException("SalaryHeads not found for ID: " + id));
	}

	@Override
	public List<SalaryHeadsVO> getAllSalaryHeadsByOrgId(Long orgId) {
		return Optional.ofNullable(salaryHeadsRepo.getAllSalaryHeadsByOrgId(orgId)).orElseGet(Collections::emptyList);
	}

	@Override
	public List<SalaryStructureVO> getAllSalaryStructureByOrgId(Long orgId) {
		return Optional.ofNullable(salaryStructureRepo.getAllSalaryStructureByOrgId(orgId))
				.orElseGet(Collections::emptyList);
	}

	@Override
	public SalaryStructureVO getSalaryStructureById(Long id) {
		return Optional.ofNullable(salaryStructureRepo.getSalaryStructureById(id))
				.orElseThrow(() -> new RuntimeException("salaryStructure not found for ID: " + id));
	}

	@Override
	public List<PfEsiAmountDTO> getPfAmountAndEsiAmountByEmployee(Long orgId, String employeeCode, String branch, BigDecimal sumOfEarnings) {
	    EmployeeVO employeeVO = Optional.ofNullable(
	            employeeRepo.getPfAmountAndEsiAmountByEmployee(orgId, employeeCode, branch))
	        .orElseThrow(() -> new RuntimeException("Employee details not found for orgId: " + orgId));

	    BigDecimal pfPercentage = employeeVO.getPfPercentage();
	    BigDecimal esiPercentage = employeeVO.getEsiPercentage();

	    BigDecimal pfAmount = sumOfEarnings.multiply(pfPercentage).divide(BigDecimal.valueOf(100));
	    BigDecimal esiAmount = sumOfEarnings.multiply(esiPercentage).divide(BigDecimal.valueOf(100));

	    List<PfEsiAmountDTO> list = new ArrayList<>();

	    PfEsiAmountDTO pfDto = new PfEsiAmountDTO();
	    pfDto.setHeading("Professional Tax");
	    pfDto.setAmount(pfAmount);

	    PfEsiAmountDTO esiDto = new PfEsiAmountDTO();
	    esiDto.setHeading("ESI Tax");
	    esiDto.setAmount(esiAmount);

	    list.add(pfDto);
	    list.add(esiDto);

	    return list;
	}


	@Override
	@Transactional
	public Map<String, Object> createUpdateSalaryStructure(SalaryStructureDTO salaryStructureDTO)
	        throws ApplicationException {
	    final SalaryStructureVO salaryStructureVO; // Declare final reference
	    Map<String, Object> response = new LinkedHashMap<>(); // Preserve order

	    if (salaryStructureDTO.getId() != null) {
	        // If ID is provided, check if it exists
	        salaryStructureVO = salaryStructureRepo.findById(salaryStructureDTO.getId())
	                .orElseThrow(() -> new ApplicationException(
	                        "Error: Salary Structure ID " + salaryStructureDTO.getId() + " not found!"));
	        salaryStructureVO.setUpdatedBy(salaryStructureDTO.getCreatedBy());
	        response.put("message", "Salary Structure Updated Successfully");
	    } else {
	        // Creating new record
	        salaryStructureVO = new SalaryStructureVO();
	        salaryStructureVO.setCreatedBy(salaryStructureDTO.getCreatedBy());
	        salaryStructureVO.setUpdatedBy(salaryStructureDTO.getCreatedBy());
	        response.put("message", "Salary Structure Created Successfully");
	    }

	    // Set other fields
	    salaryStructureVO.setEmployeeName(salaryStructureDTO.getEmployeeName());
	    salaryStructureVO.setEmployeeCode(salaryStructureDTO.getEmployeeCode());
	    salaryStructureVO.setDateOfBirth(salaryStructureDTO.getDateOfBirth());
	    salaryStructureVO.setGrade(salaryStructureDTO.getGrade());
	    salaryStructureVO.setDepartment(salaryStructureDTO.getDepartment());
	    salaryStructureVO.setPanNo(salaryStructureDTO.getPanNo());
	    salaryStructureVO.setBankAccountNo(salaryStructureDTO.getBankAccountNo());
	    salaryStructureVO.setDateOfJoining(salaryStructureDTO.getDateOfJoining());
	    salaryStructureVO.setOrgId(salaryStructureDTO.getOrgId());
	    salaryStructureVO.setBranch(salaryStructureDTO.getBranch());
	    salaryStructureVO.setBranchCode(salaryStructureDTO.getBranchCode());
	    salaryStructureVO.setDesignation(salaryStructureDTO.getDesignation());

	    // Save Parent Record
	    final SalaryStructureVO savedSalaryStructureVO = salaryStructureRepo.save(salaryStructureVO);

	    if (salaryStructureDTO.getId() != null) {
	    	salaryEarningDetailsRepo.deleteBySalaryStructureVO(savedSalaryStructureVO);
	        salaryDetectionDetailsRepo.deleteBySalaryStructureVO(savedSalaryStructureVO);
	    }

	    // Calculate total earnings
	    BigDecimal totalEarnings = salaryStructureDTO.getSalaryEarningDetailsDTO().stream()
	            .map(SalaryEarningDetailsDTO::getAmount)
	            .filter(Objects::nonNull)
	            .reduce(BigDecimal.ZERO, BigDecimal::add);

	    // Calculate total deductions
	    BigDecimal totalDeductions = salaryStructureDTO.getSalaryDetectionDetailsDTO().stream()
	            .map(SalaryDetectionDetailsDTO::getAmount)
	            .filter(Objects::nonNull)
	            .reduce(BigDecimal.ZERO, BigDecimal::add);

	    // Set total earnings and deductions in header
	    savedSalaryStructureVO.setSumOfEarning(totalEarnings);
	    savedSalaryStructureVO.setSumOfDetection(totalDeductions);
	    savedSalaryStructureVO.setAmount(totalEarnings.subtract(totalDeductions));
	    salaryStructureRepo.save(savedSalaryStructureVO);

	    // Validate duplicate earnings headings
	    Set<String> earningHeadings = new HashSet<>();
	    for (SalaryEarningDetailsDTO dto : salaryStructureDTO.getSalaryEarningDetailsDTO()) {
	        if (!earningHeadings.add(dto.getHeading().toUpperCase())) {
	            throw new ApplicationException("Duplicate heading found in salary earning details: " + dto.getHeading());
	        }
	    }

	    // Validate duplicate deductions headings
	    Set<String> deductionHeadings = new HashSet<>();
	    for (SalaryDetectionDetailsDTO dto : salaryStructureDTO.getSalaryDetectionDetailsDTO()) {
	        if (!deductionHeadings.add(dto.getHeading().toUpperCase())) {
	            throw new ApplicationException("Duplicate heading found in salary deduction details: " + dto.getHeading());
	        }
	    }

	    // Save Salary Earning Details
	    List<SalaryEarningDetailsVO> salaryEarningDetailsVOs = salaryStructureDTO.getSalaryEarningDetailsDTO().stream()
	            .map(dto -> {
	                SalaryEarningDetailsVO vo = new SalaryEarningDetailsVO();
	                vo.setHeading(dto.getHeading());
	                vo.setAmount(dto.getAmount());
	                vo.setSalaryStructureVO(savedSalaryStructureVO);
	                return vo;
	            }).collect(Collectors.toList());
	    salaryEarningDetailsRepo.saveAll(salaryEarningDetailsVOs);

	    // Save Salary Deduction Details
	    List<SalaryDetectionDetailsVO> salaryDetectionDetailsVOs = salaryStructureDTO.getSalaryDetectionDetailsDTO().stream()
	            .map(dto -> {
	                SalaryDetectionDetailsVO vo = new SalaryDetectionDetailsVO();
	                vo.setHeading(dto.getHeading());
	                vo.setAmount(dto.getAmount());
	                vo.setSalaryStructureVO(savedSalaryStructureVO);
	                return vo;
	            }).collect(Collectors.toList());
	    salaryDetectionDetailsRepo.saveAll(salaryDetectionDetailsVOs);

	    // Attach Parent and Child Data in Response
	    savedSalaryStructureVO.setSalaryEarningDetailsVO(salaryEarningDetailsVOs);
	    savedSalaryStructureVO.setSalaryDetectionDetailsVO(salaryDetectionDetailsVOs);
	    response.put("salaryStructure", savedSalaryStructureVO);

	    return response;
	}

	@Override
	public List<EmployeeVO> getAllEmployeeByActive(Long orgId) {
		return Optional.ofNullable(employeeRepo.getAllEmployeeByActive(orgId))
				.orElseGet(Collections::emptyList);
	}

	// Permission Request
	@Override
	@Transactional
	public Map<String, Object> createUpdatePermissionRequest(PermissionRequestDTO permissionRequestDTO)
			throws ApplicationException {
		final PermissionRequestVO permissionRequestVO; // Declare final reference
		Map<String, Object> response = new LinkedHashMap<>(); // Preserve order

		if (permissionRequestDTO.getId() != null) {
			// If ID is provided, check if it exists
			permissionRequestVO = permissionRequestRepo.findById(permissionRequestDTO.getId())
					.orElseThrow(() -> new ApplicationException(
							"Error: Permission Request ID " + permissionRequestDTO.getId() + " not found!"));

			// Updating existing record
			permissionRequestVO.setUpdatedBy(permissionRequestDTO.getCreatedBy());
			response.put("message", "Permission Request Updated Successfully");
		} else {
			// Creating new record
			permissionRequestVO = new PermissionRequestVO();
			permissionRequestVO.setCreatedBy(permissionRequestDTO.getCreatedBy());
			permissionRequestVO.setUpdatedBy(permissionRequestDTO.getCreatedBy());
			response.put("message", "Permission Request Created Successfully");
		}

		// Set fields
		permissionRequestVO.setDate(permissionRequestDTO.getDate());
		permissionRequestVO.setFromTime(permissionRequestDTO.getFromTime());
		permissionRequestVO.setToTime(permissionRequestDTO.getToTime());
		permissionRequestVO.setTotalHours(permissionRequestDTO.getTotalHours());
		permissionRequestVO.setNotes(permissionRequestDTO.getNotes());
		permissionRequestVO.setOrgId(permissionRequestDTO.getOrgId());
		permissionRequestVO.setNotify(permissionRequestDTO.getNotify());
		permissionRequestVO.setNotifyCode(permissionRequestDTO.getNotifyCode());
		permissionRequestVO.setBranch(permissionRequestDTO.getBranch());
		permissionRequestVO.setBranchCode(permissionRequestDTO.getBranchCode());
		permissionRequestVO.setEmployeeName(permissionRequestDTO.getEmployeeName());
		permissionRequestVO.setEmployeeCode(permissionRequestDTO.getEmployeeCode());
		permissionRequestVO.setEmployeeEmail(permissionRequestDTO.getEmployeeEmail());

		if (permissionRequestDTO.getId() != null) {
            List<PermissionRequestNotifyVO> permissionRequestNotifyVO = permissionRequestNotifyRepo.findByPermissionRequestVO(permissionRequestDTO);
            permissionRequestNotifyRepo.deleteAll(permissionRequestNotifyVO);
        }

        // Set Poll Details from PollDetailsDTO
        List<PermissionRequestNotifyVO> permissionRequestNotifyVOs = new ArrayList<>();
        for (PermissionRequestNotifyDTO permissionRequestNotifyDTO : permissionRequestDTO.getPermissionRequestNotifyDTO()) {
        	PermissionRequestNotifyVO permissionRequestNotifyVO = new PermissionRequestNotifyVO();
        	permissionRequestNotifyVO.setNotify2(permissionRequestNotifyDTO.getNotify2());
        	permissionRequestNotifyVO.setNotify2Code(permissionRequestNotifyDTO.getNotify2Code());
        	permissionRequestNotifyVO.setNotify2Email(permissionRequestNotifyDTO.getNotify2Email());

        	permissionRequestNotifyVO.setPermissionRequestVO(permissionRequestVO);  // Set parent reference in child
        	permissionRequestNotifyVOs.add(permissionRequestNotifyVO);
        }
        permissionRequestVO.setPermissionRequestNotifyVO(permissionRequestNotifyVOs);

		permissionRequestVO.setApproveStatus("PENDING");


		// Save the entity
		final PermissionRequestVO savedPermissionRequestVO = permissionRequestRepo.save(permissionRequestVO);

		// Attach response
		Map<String, Object> paramObjectsMap = new LinkedHashMap<>();
		paramObjectsMap.put("permissionRequestVO", savedPermissionRequestVO);
		response.put("paramObjectsMap", paramObjectsMap);

		return response;
	}

	@Override
	public PermissionRequestVO getPermissionRequestById(Long id) {
		return permissionRequestRepo.getPermissionRequestById(id);
	}

	@Override
	public List<PermissionRequestVO> getAllPermissionRequestByOrgId(Long orgId, String branchCode) {
		return Optional.ofNullable(permissionRequestRepo.getAllPermissionRequestByOrgId(orgId, branchCode))
				.orElseGet(Collections::emptyList);
	}

	@Override
	public List<Map<String, Object>> getReportingPerson(Long orgId, String employeeCode) {
		Set<Object[]> chType = permissionRequestRepo.getReportingPerson(orgId, employeeCode);
		return ReportingPersonDetails(chType);
	}

	private List<Map<String, Object>> ReportingPersonDetails(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
//				map.put("leaveType", ch[0] != null ? ch[0].toString() : "");
			map.put("reportingPerson", (ch != null && ch.length > 0 && ch[0] != null) ? ch[0].toString() : "");
			map.put("email", (ch != null && ch.length > 1 && ch[1] != null) ? ch[1].toString() : "");
			map.put("reportingPersonCode", (ch != null && ch.length > 2 && ch[2] != null) ? ch[2].toString() : "");
			List1.add(map);
		}
		return List1;

	}

//	@Override
//	@Transactional
//	public Map<String, Object> createUpdateSalaryProcess(List<SalaryProcessDTO> salaryProcessDTOList)
//			throws ApplicationException {
//		List<SalaryProcessVO> savedSalaryProcessVOs = new ArrayList<>();
//		Map<String, Object> response = new LinkedHashMap<>(); // Preserve order
//		String message = null;
//		for (SalaryProcessDTO salaryProcessDTO : salaryProcessDTOList) {
//			final SalaryProcessVO salaryProcessVO; // Declare final reference
//
//			if (salaryProcessDTO.getId() != null) {
//				// If ID is provided, check if it exists
//				salaryProcessVO = salaryProcessRepo.findById(salaryProcessDTO.getId())
//						.orElseThrow(() -> new ApplicationException(
//								"Error: SalaryProcess ID " + salaryProcessDTO.getId() + " not found!"));
//
//				// Updating existing record
//				salaryProcessVO.setUpdatedBy(salaryProcessDTO.getCreatedBy());
//				message = "Salary Process updated Successfully";
//			} else {
//				// Creating new record
//				salaryProcessVO = new SalaryProcessVO();
//				salaryProcessVO.setCreatedBy(salaryProcessDTO.getCreatedBy());
//				salaryProcessVO.setUpdatedBy(salaryProcessDTO.getCreatedBy());
//				message = "Salary Process Created Successfully";
//
//			}
//
//			// Set other fields
//			if ("APPROVED".equalsIgnoreCase(salaryProcessDTO.getApprovedStatus())) {
//				salaryProcessVO.setMonth(salaryProcessDTO.getMonth());
//				salaryProcessVO.setYear(salaryProcessDTO.getYear());
//				salaryProcessVO.setEmployeeName(salaryProcessDTO.getEmployeeName());
//				salaryProcessVO.setEmployeeCode(salaryProcessDTO.getEmployeeCode());
//				salaryProcessVO.setTotalCompanyWorkingDays(salaryProcessDTO.getTotalCompanyWorkingDays());
//				salaryProcessVO.setTotalLeave(salaryProcessDTO.getTotalLeave());
//				salaryProcessVO.setLopLeave(salaryProcessDTO.getLopLeave());
//				salaryProcessVO.setEmpTotalWorkingDays(salaryProcessDTO.getEmpTotalWorkingDays());
//				salaryProcessVO.setEmpSalaryDays(salaryProcessDTO.getEmpSalaryDays());
//				salaryProcessVO.setApprovedStatus(salaryProcessDTO.getApprovedStatus());
//
//				salaryProcessVO.setGrossPay(salaryProcessDTO.getGrossPay());
//				salaryProcessVO.setNetPay(salaryProcessDTO.getNetPay());
//				salaryProcessVO.setPayOnHand(salaryProcessDTO.getPayOnHand());
//				salaryProcessVO.setOtHours(salaryProcessDTO.getOtHours());
//				salaryProcessVO.setOtAmount(salaryProcessDTO.getOtAmount());
//				salaryProcessVO.setOrgId(salaryProcessDTO.getOrgId());
//				salaryProcessVO.setBranch(salaryProcessDTO.getBranch());
//				salaryProcessVO.setBranchCode(salaryProcessDTO.getBranchCode());
//
//				List<LeaveProcessVO> leaveProcessVOList = leaveProcessRepo.findByEmployeeCodeAndOrgIdAndMonthAndYear(
//						salaryProcessDTO.getEmployeeCode(), salaryProcessDTO.getOrgId(), salaryProcessDTO.getMonth(),
//						salaryProcessDTO.getYear());
//
//				for (LeaveProcessVO leaveProcessVO : leaveProcessVOList) {
//					leaveProcessVO.setApprovedStatus("APPROVED");
//				}
//				leaveProcessRepo.saveAll(leaveProcessVOList);
//
//				// Save Parent Record
//				SalaryProcessVO savedSalaryProcessVO = salaryProcessRepo.save(salaryProcessVO);
//				savedSalaryProcessVOs.add(savedSalaryProcessVO);
//			}
//		}
//
//		// Response map
//		response.put("message", message);
//		response.put("salaryProcessVOs", savedSalaryProcessVOs);
////	    response.put("approvedSalaryProcessVOs", approvedSalaryProcessVOs);
//
//		return response;
//	}

	
	@Override
	@Transactional
	public Map<String, Object> createUpdateSalaryProcess(List<SalaryProcessDTO> salaryProcessDTOList)
			throws ApplicationException {
		List<SalaryProcessVO> savedSalaryProcessVOs = new ArrayList<>();
		Map<String, Object> response = new LinkedHashMap<>(); // Preserve order
		String message = null;
		for (SalaryProcessDTO salaryProcessDTO : salaryProcessDTOList) {
			final SalaryProcessVO salaryProcessVO; // Declare final reference

			if (salaryProcessDTO.getId() != null) {
				// If ID is provided, check if it exists
				salaryProcessVO = salaryProcessRepo.findById(salaryProcessDTO.getId())
						.orElseThrow(() -> new ApplicationException(
								"Error: SalaryProcess ID " + salaryProcessDTO.getId() + " not found!"));

				// Updating existing record
				salaryProcessVO.setUpdatedBy(salaryProcessDTO.getCreatedBy());
				message = "Salary Process updated Successfully";
			} else {
				// Creating new record
				salaryProcessVO = new SalaryProcessVO();
				salaryProcessVO.setCreatedBy(salaryProcessDTO.getCreatedBy());
				salaryProcessVO.setUpdatedBy(salaryProcessDTO.getCreatedBy());
				message = "Salary Process Created Successfully";

			}

			// Set other fields
				salaryProcessVO.setMonth(salaryProcessDTO.getMonth());
				salaryProcessVO.setYear(salaryProcessDTO.getYear());
				salaryProcessVO.setEmployeeName(salaryProcessDTO.getEmployeeName());
				salaryProcessVO.setEmployeeCode(salaryProcessDTO.getEmployeeCode());
				salaryProcessVO.setTotalCompanyWorkingDays(salaryProcessDTO.getTotalCompanyWorkingDays());
				salaryProcessVO.setTotalLeave(salaryProcessDTO.getTotalLeave());
				salaryProcessVO.setLopLeave(salaryProcessDTO.getLopLeave());
				salaryProcessVO.setEmpTotalWorkingDays(salaryProcessDTO.getEmpTotalWorkingDays());
				salaryProcessVO.setEmpSalaryDays(salaryProcessDTO.getEmpSalaryDays());
				salaryProcessVO.setApprovedStatus("PENDING");

				salaryProcessVO.setGrossPay(salaryProcessDTO.getGrossPay());
				salaryProcessVO.setNetPay(salaryProcessDTO.getNetPay());
				salaryProcessVO.setPayOnHand(salaryProcessDTO.getPayOnHand());
				salaryProcessVO.setOtHours(salaryProcessDTO.getOtHours());
				salaryProcessVO.setOtAmount(salaryProcessDTO.getOtAmount());
				salaryProcessVO.setOrgId(salaryProcessDTO.getOrgId());
				salaryProcessVO.setBranch(salaryProcessDTO.getBranch());
				salaryProcessVO.setBranchCode(salaryProcessDTO.getBranchCode());

//				List<LeaveProcessVO> leaveProcessVOList = leaveProcessRepo.findByEmployeeCodeAndOrgIdAndMonthAndYear(
//						salaryProcessDTO.getEmployeeCode(), salaryProcessDTO.getOrgId(), salaryProcessDTO.getMonth(),
//						salaryProcessDTO.getYear());
//
//				for (LeaveProcessVO leaveProcessVO : leaveProcessVOList) {
//					leaveProcessVO.setApprovedStatus("APPROVED");
//				}
//				leaveProcessRepo.saveAll(leaveProcessVOList);

				// Save Parent Record
				SalaryProcessVO savedSalaryProcessVO = salaryProcessRepo.save(salaryProcessVO);
				savedSalaryProcessVOs.add(savedSalaryProcessVO);
			
		}

		// Response map
		response.put("message", message);
		response.put("salaryProcessVOs", savedSalaryProcessVOs);
//	    response.put("approvedSalaryProcessVOs", approvedSalaryProcessVOs);

		return response;
	}

	
	@Override
	public Map<String, Object> createApprovalSalaryProcess(Long orgId, List<Long> ids, String action,
			String actionBy) throws ApplicationException {
		List<SalaryProcessVO> updatedList = new ArrayList<>();
		String message = "";

		for (Long id : ids) {
			SalaryProcessVO salaryProcessVO = salaryProcessRepo.findById(id)
					.orElseThrow(() -> new ApplicationException("Invalid SalaryProcess ID: " + id));

			String currentStatus = salaryProcessVO.getApprovedStatus();

			if (currentStatus == null
					|| (!currentStatus.equalsIgnoreCase("APPROVED") && !currentStatus.equalsIgnoreCase("REJECTED"))) {

				if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {
					salaryProcessVO.setApprovedStatus(action.toUpperCase());
					salaryProcessVO.setApproveBy(actionBy);

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
					salaryProcessVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

					List<LeaveProcessVO> leaveProcessVOList = leaveProcessRepo.findByEmployeeCodeAndOrgIdAndMonthAndYear(
							salaryProcessVO.getEmployeeCode(), salaryProcessVO.getOrgId(), salaryProcessVO.getMonth(),
							salaryProcessVO.getYear());

					for (LeaveProcessVO leaveProcessVO : leaveProcessVOList) {
						leaveProcessVO.setApprovedStatus("APPROVED");
					}
					leaveProcessRepo.saveAll(leaveProcessVOList);
					
					updatedList.add(salaryProcessVO);
				}
			} else if ("APPROVED".equalsIgnoreCase(currentStatus)) {
				throw new ApplicationException(
						"AttendanceSummary already approved for employee: " + salaryProcessVO.getEmployeeCode());
			} else if ("REJECTED".equalsIgnoreCase(currentStatus)) {
				throw new ApplicationException(
						"AttendanceSummary already rejected for employee: " + salaryProcessVO.getEmployeeCode());
			}
		}

		salaryProcessRepo.saveAll(updatedList);

		if ("APPROVED".equalsIgnoreCase(action)) {
			message = "SalaryProcess Approved Successfully";
		} else if ("REJECTED".equalsIgnoreCase(action)) {
			message = "SalaryProcess Rejected Successfully";
		}

		Map<String, Object> response = new HashMap<>();
		response.put("salaryProcessVO", updatedList);
		response.put("message", message);
		return response;
	}

	
	@Override
	public List<SalaryProcessVO> getAllSalaryProcessByOrgId(Long orgId) {
		return Optional.ofNullable(salaryProcessRepo.getAllSalaryProcessByOrgId(orgId))
				.orElseGet(Collections::emptyList);
	}

	@Override
	public SalaryProcessVO getSalaryProcessById(Long id) {
		return salaryProcessRepo.getSalaryProcessById(id);
	}

	@Override
	public List<Map<String, Object>> getSalaryStructureForSalaryProcess(Long orgId, String employeeCode) {
		Set<Object[]> result = salaryProcessRepo.getSalaryStructureForSalaryProcess(orgId, employeeCode);
		return getSalaryStructureForSalaryProcess(result);
	}

	private List<Map<String, Object>> getSalaryStructureForSalaryProcess(Set<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("netPay", record[0] != null ? record[0].toString() : "");
			map.put("sumOfEarningAmount", record[1] != null ? record[1].toString() : "");
			map.put("sumOfDetectionAmount", record[2] != null ? record[2].toString() : "");
			map.put("otAmount", record[3] != null ? record[3].toString() : "");


			detailsList.add(map);
		}
		return detailsList;
	}

	@Override
	public List<Map<String, Object>> getLeaveDetailsforSalaryProcess(Long orgId, Long month, String year,String department,String branch, String type, String contractor) {
		Set<Object[]> result = leaveProcessRepo.getLeaveDetailsforSalaryProcess(orgId, month, year,department,branch, type,  contractor);
		return getLeaveDetailsforSalaryProcess(result);
	}

	private List<Map<String, Object>> getLeaveDetailsforSalaryProcess(Set<Object[]> result) {
		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (Object[] record : result) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeName", record[0] != null ? record[0].toString() : "");
			map.put("employeeCode", record[1] != null ? record[1].toString() : "");
			map.put("branch", record[2] != null ? record[2].toString() : "");
			map.put("department", record[3] != null ? record[3].toString() : "");

			map.put("totalCompanyWorkingDays", record[4] != null ? record[4].toString() : "0");
			map.put("totalLeave", record[5] != null ? record[5].toString() : "0");
			map.put("lopLeave", record[6] != null ? record[6].toString() : "0");
			map.put("empTotalWorkingDays", record[7] != null ? record[7].toString() : "0");
			map.put("empSalaryDays", record[8] != null ? record[8].toString() : "0");
			map.put("otHours", record[9] != null ? record[9].toString() : "0");

			detailsList.add(map);
		}
		return detailsList;
	}

//	@Override
//	public List<Map<String, Object>> getNetPayForSalaryProcess( BigDecimal grossPay,BigDecimal sumOfDetection) {
//		Set<Object[]> result = salaryProcessRepo.getNetPayForSalaryProcess(grossPay,sumOfDetection);
//		return getNetPayForSalaryProcess(result);
//	}
//	
//	private List<Map<String, Object>> getNetPayForSalaryProcess(Set<Object[]> result) {
//		List<Map<String, Object>> detailsList = new ArrayList<>();
//
//		for (Object[] record : result) {
//			Map<String, Object> map = new HashMap<>();
//			map.put("netPay", record[0] != null ? record[0].toString() : "");
//
//
//			detailsList.add(map);
//		}
//		return detailsList;
//	}
//	
	@Override
	public List<Map<String, Object>> getPayOnHandsForSalaryProcess(Long totalCompanyWorkingDays, BigDecimal grossPay,
			Long empSalaryDays,BigDecimal sumOfDetection,BigDecimal otAmount) {
		
		Set<Object[]> result;
		
	    if (empSalaryDays != null && empSalaryDays > 0) {
		 result = salaryProcessRepo.getPayOnHandsForSalaryProcess(totalCompanyWorkingDays, grossPay,
				empSalaryDays,sumOfDetection, otAmount);
		}else {
			 result = new HashSet<>();
		}
		return getPayOnHandsForSalaryProcess(result);
	}

	private List<Map<String, Object>> getPayOnHandsForSalaryProcess(Set<Object[]> result) {
	    List<Map<String, Object>> detailsList = new ArrayList<>();


	    if (result.isEmpty()) {
	        // no records from DB → return default payOnHand = "0"
	        Map<String, Object> defaultMap = new HashMap<>();
	        defaultMap.put("payOnHand", "0");
	        detailsList.add(defaultMap);
	        return detailsList;
	    }
	    
	    for (Object[] record : result) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("payOnHand", record[0] != null ? record[0].toString() : "0"); // default 0
	        detailsList.add(map);
	    }
	    return detailsList;
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
			map.put("empName", ch[0].toString());
			map.put("empCode", ch[1].toString());
			map.put("dob", ch[2].toString());
			map.put("empRole", ch[3].toString());

			List1.add(map);
		}
		return List1;

	}

	// ApprovedSalaryProcess

	@Override
	public List<SalaryProcessVO> getApprovedSalaryProcessReport(Long orgId, Long month,
			String year) {
		return salaryProcessRepo.getApprovedSalaryProcessReport(orgId, month, year);
	}

	@Override
	public List<Map<String, Object>> GetworkAniversary(Long Orgid) {
		Set<Object[]> employeeVO = employeeRepo.findWorkaniversaryByOrgId(Orgid );
		return GetworkAniversary(employeeVO);
	}

	private List<Map<String, Object>> GetworkAniversary(Set<Object[]>employeeVO) {
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
			
			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> GetnewJoineDetails(Long Orgid) {
		Set<Object[]> employeeVO = employeeRepo.findNewJoinieDtailsByOrgId(Orgid );
		return GetnewJoineDetails(employeeVO);
	}

	private List<Map<String, Object>> GetnewJoineDetails(Set<Object[]>employeeVO) {
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
			
			
			List1.add(map);
		}
		return List1;

	}
	
	//approvedpermissionrequest
	
	@Override
	public Map<String, Object> createApprovalPermissionRequest(Long orgId, Long id, String employeeCode, String action, String actionBy,String notifyCode, String notify, String screenName)
	        throws ApplicationException {

	    PermissionRequestVO permissionRequestVO = permissionRequestRepo.findByOrgIdAndIdAndEmployeeCode(orgId, id, employeeCode);
	    String message = "";

	    if (permissionRequestVO.getApproveStatus() == null
	            || (!permissionRequestVO.getApproveStatus().equalsIgnoreCase("Approved")
	            && !permissionRequestVO.getApproveStatus().equalsIgnoreCase("Rejected"))) {

	        if ("APPROVED".equalsIgnoreCase(action) || "REJECTED".equalsIgnoreCase(action)) {
	            permissionRequestVO.setApproveStatus(action);
	            permissionRequestVO.setApproveBy(actionBy);

	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
	            permissionRequestVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

	            permissionRequestRepo.save(permissionRequestVO);
	            
	            if (permissionRequestVO.getApproveStatus().equalsIgnoreCase("Approved")) {
				    message = "Approved Successfully";
				} else if (permissionRequestVO.getApproveStatus().equalsIgnoreCase("Rejected")) {
				    message = "Rejected Successfully";
				}
	        }

	    } else if (permissionRequestVO.getApproveStatus().equalsIgnoreCase("Approved")) {
	        throw new ApplicationException("This PermissionRequest Already Approved");
	    } else if (permissionRequestVO.getApproveStatus().equalsIgnoreCase("Rejected")) {
	        throw new ApplicationException("This PermissionRequest Already Rejected");
	    }

		Map<String, Object> response = new HashMap<>();
	    response.put("permissionRequestVO", permissionRequestVO);
	    response.put("message", message);
	    return response;
			}

	
	@Override
	public List<Map<String, Object>> getPendingPermissionRequest(Long orgId, String branchCode,String reportingPersonCode) {
		Set<Object[]> permissionRequestVO = permissionRequestRepo.getPendingPermissionRequest(orgId, branchCode,reportingPersonCode);
		return permissionRequestDetails(permissionRequestVO);
	}
	
	private List<Map<String, Object>> permissionRequestDetails(Set<Object[]>permissionRequestVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : permissionRequestVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("permissionRequestId", ch[0] != null ? ch[0].toString() : "");
			map.put("branch", ch[1] != null ? ch[1].toString() : "");
			map.put("branchCode", ch[2] != null ? ch[2].toString() : "");
			map.put("date", ch[3] != null ? ch[3].toString() : "");
			map.put("fromTime", ch[4] != null ? ch[4].toString() : "");
			map.put("reason", ch[5] != null ? ch[5].toString() : "");
			map.put("notify", ch[6] != null ? ch[6].toString() : "");
			map.put("orgid", ch[7] != null ? ch[7].toString() : "");
			map.put("screenCode", ch[8] != null ? ch[8].toString() : "");
			map.put("screenName", ch[9] != null ? ch[9].toString() : "");
			map.put("toTime", ch[10] != null ? ch[10].toString() : "");
			map.put("totalHours", ch[11] != null ? ch[11].toString() : "");
			map.put("employeeCode", ch[12] != null ? ch[12].toString() : "");
			map.put("employeeName", ch[13] != null ? ch[13].toString() : "");
			map.put("notifyCode", ch[14] != null ? ch[14].toString() : "");
			map.put("employeeEmail", ch[15] != null ? ch[15].toString() : "");
 
			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getApprovedPermissionRequestforTeam(Long orgId, String branchCode,String reportingPersonCode) {
		Set<Object[]> permissionRequestVO = permissionRequestRepo.getApprovedPermissionRequestforTeam(orgId, branchCode,reportingPersonCode);
		return getApprovedPermissionRequestforTeam(permissionRequestVO);
	}

	private List<Map<String, Object>> getApprovedPermissionRequestforTeam(Set<Object[]>permissionRequestVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : permissionRequestVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("permissionrequestid", ch[0] != null ? ch[0].toString() : "");
			map.put("branch", ch[1] != null ? ch[1].toString() : "");
			map.put("branchcode", ch[2] != null ? ch[2].toString() : "");
			map.put("date", ch[3] != null ? ch[3].toString() : "");
			map.put("fromtime", ch[4] != null ? ch[4].toString() : "");
			map.put("notes", ch[5] != null ? ch[5].toString() : "");
			map.put("notify", ch[6] != null ? ch[6].toString() : "");
			map.put("orgid", ch[7] != null ? ch[7].toString() : "");
			map.put("screencode", ch[8] != null ? ch[8].toString() : "");
			map.put("screenname", ch[9] != null ? ch[9].toString() : "");
			map.put("totime", ch[10] != null ? ch[10].toString() : "");
			map.put("totalhours", ch[11] != null ? ch[11].toString() : "");
			map.put("employeecode", ch[12] != null ? ch[12].toString() : "");
			map.put("employeename", ch[13] != null ? ch[13].toString() : "");
			map.put("notifycode", ch[14] != null ? ch[14].toString() : "");
			map.put("employeeemail", ch[15] != null ? ch[15].toString() : "");
 
			List1.add(map);
		}
		return List1;

	}

}
