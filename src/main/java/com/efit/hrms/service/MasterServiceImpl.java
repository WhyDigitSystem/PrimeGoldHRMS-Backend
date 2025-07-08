package com.efit.hrms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.BranchDTO;
import com.efit.hrms.dto.DesignationLeaveDTO;
import com.efit.hrms.dto.EmployeeDTO;
import com.efit.hrms.dto.EmployeeLeaveDTO;
import com.efit.hrms.dto.ProjectMasterDTO;
import com.efit.hrms.entity.BranchVO;
import com.efit.hrms.entity.DesignationLeaveVO;
import com.efit.hrms.entity.EmployeeLeaveVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.LeaveBalanceVO;
import com.efit.hrms.entity.ProjectMasterVO;
import com.efit.hrms.entity.UserLoginRolesVO;
import com.efit.hrms.entity.UserVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.BranchRepo;
import com.efit.hrms.repo.DesignationLeaveRepo;
import com.efit.hrms.repo.EmployeeLeaveRepo;
import com.efit.hrms.repo.EmployeeRepo;
import com.efit.hrms.repo.LeaveBalanceRepo;
import com.efit.hrms.repo.ProjectMasterRepo;
import com.efit.hrms.repo.UserLoginRolesRepo;
import com.efit.hrms.repo.UserRepo;

import io.jsonwebtoken.io.IOException;

@Service
public class MasterServiceImpl implements MasterService {
	public static final Logger LOGGER = LoggerFactory.getLogger(MasterServiceImpl.class);

	@Autowired
	BranchRepo branchRepo;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	EmployeeLeaveRepo employeeLeaveRepo;

	@Autowired
	LeaveBalanceRepo leaveBalanceRepo;

	@Autowired
	DesignationLeaveRepo designationLeaveRepo;
	
    @Autowired
    ProjectMasterRepo projectMasterRepo;
    
    @Autowired
    UserLoginRolesRepo userLoginRolesRepo;
    
    @Autowired
    UserRepo userRepo;

	// Branch

	@Override
	public List<BranchVO> getAllBranch(Long orgid) {
		return branchRepo.findAll(orgid);
	}

	@Override
	public Optional<BranchVO> getBranchById(Long branchid) {

		return branchRepo.findById(branchid);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateBranch(BranchDTO branchDTO) throws Exception {
		BranchVO branchVO;
		String message = null;

		if (ObjectUtils.isEmpty(branchDTO.getId())) {
			// Check if the branch already exists for creation
			if (branchRepo.existsByBranchAndOrgId(branchDTO.getBranch(), branchDTO.getOrgId())) {
				String errorMessage = String.format("This Branch: %s Already Exists in This Organization",
						branchDTO.getBranch());
				throw new ApplicationException(errorMessage);
			}

			if (branchRepo.existsByBranchCodeAndOrgId(branchDTO.getBranchCode(), branchDTO.getOrgId())) {
				String errorMessage = String.format("This BranchCode: %s Already Exists in This Organization",
						branchDTO.getBranchCode());
				throw new ApplicationException(errorMessage);
			}

			// Create new branch
			branchVO = new BranchVO();
			branchVO.setCreatedBy(branchDTO.getCreatedBy());
			branchVO.setUpdatedBy(branchDTO.getCreatedBy());
			message = "Branch Created Successfully";
		} else {
			// Update existing branch
			branchVO = branchRepo.findById(branchDTO.getId())
					.orElseThrow(() -> new ApplicationException("Branch not found with id: " + branchDTO.getId()));

			branchVO.setUpdatedBy(branchDTO.getCreatedBy());

			if (!branchVO.getBranch().equalsIgnoreCase(branchDTO.getBranch())) {
				if (branchRepo.existsByBranchAndOrgId(branchDTO.getBranch(), branchDTO.getOrgId())) {
					String errorMessage = String.format("This Branch: %s Already Exists in This Organization",
							branchDTO.getBranch());
					throw new ApplicationException(errorMessage);
				}
				branchVO.setBranch(branchDTO.getBranch().toUpperCase());
			}

			if (!branchVO.getBranchCode().equalsIgnoreCase(branchDTO.getBranchCode())) {
				if (branchRepo.existsByBranchCodeAndOrgId(branchDTO.getBranchCode(), branchDTO.getOrgId())) {
					String errorMessage = String.format("This BranchCode: %s Already Exists in This Organization",
							branchDTO.getBranchCode());
					throw new ApplicationException(errorMessage);
				}
				branchVO.setBranchCode(branchDTO.getBranchCode().toUpperCase());
			}

			message = "Branch Updated Successfully";
		}

		getBranchVOFromBranchDTO(branchVO, branchDTO);
		branchRepo.save(branchVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("branchVO", branchVO);
		return response;
	}

	private void getBranchVOFromBranchDTO(BranchVO branchVO, BranchDTO branchDTO) {
		branchVO.setBranch(branchDTO.getBranch().toUpperCase());
		branchVO.setBranchCode(branchDTO.getBranchCode().toUpperCase());
		branchVO.setOrgId(branchDTO.getOrgId());
		branchVO.setAddressLine1(branchDTO.getAddressLine1());
		// branchVO.setAddressLine2(branchDTO.getAddressLine2());
		// branchVO.setPan(branchDTO.getPan());
		branchVO.setGstIn(branchDTO.getGstIn());
		branchVO.setContactPerson(branchDTO.getContactPerson());
		branchVO.setEmail(branchDTO.getEmail());
		branchVO.setPhone(branchDTO.getPhone());
		branchVO.setState(branchDTO.getState().toUpperCase());
		branchVO.setCity(branchDTO.getCity().toUpperCase());
		branchVO.setPinCode(branchDTO.getPinCode());
		branchVO.setCountry(branchDTO.getCountry().toUpperCase());
		// branchVO.setStateNo(branchDTO.getStateNo().toUpperCase());
		// branchVO.setStateCode(branchDTO.getStateCode().toUpperCase());
		// branchVO.setLccurrency(branchDTO.getLccurrency());
		branchVO.setCancelRemarks(branchDTO.getCancelRemarks());
		branchVO.setActive(branchDTO.isActive());
	}

	@Override
	public void deleteBranch(Long branchid) {
		branchRepo.deleteById(branchid);
	}

	// Employee

	@Override
	public List<Map<String, Object>> getEmployeesWithCompanyInfoByOrgId(Long orgId) {
	    return employeeRepo.getEmployeesWithCompanyInfoByOrgId(orgId);
	}

	
	

	@Override
	public List<EmployeeVO> getAllEmployeeByOrgIdAndEmployeeCode(Long orgId,String employeeCode) {
		return employeeRepo.getAllEmployeeByOrgIdAndEmployeeCode(orgId,employeeCode);
	}

	@Override
	public List<EmployeeVO> getAllEmployee() {
		return employeeRepo.findAll();
	} 

	@Override
	public Optional<EmployeeVO> getEmployeeById(Long employeeid) {
		return employeeRepo.findById(employeeid);
	}

	@Override
	public List<Map<String, Object>> getReportingNameForEmployee(Long orgId,String branchCode,String employeeCode) {
		Set<Object[]> result = employeeRepo.findReportingNameForEmployee(orgId,branchCode,employeeCode);
		return getReportingNameForEmployee(result);
	}

	private List<Map<String, Object>> getReportingNameForEmployee(Set<Object[]> result) {
		List<Map<String, Object>> details = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> object = new HashMap<>();
			object.put("employeeName", fs[0] != null ? fs[0].toString() : "");
			object.put("role", fs[1] != null ? fs[1].toString() : "");
			object.put("email", fs[2] != null ? fs[2].toString() : "");
			object.put("employeeCode", fs[3] != null ? fs[3].toString() : "");



			details.add(object); // Add the map to the list

		}
		return details;
	}

	@Override
	public Map<String, Object> createEmployee(EmployeeDTO employeeDTO) throws ApplicationException {
		EmployeeVO employeeVO;
		String message = null;

		if (ObjectUtils.isEmpty(employeeDTO.getId())) {
			// GETEMPLOYEE SEQUENCE API

			// Check for existing employee by employee code within the organization
			if (employeeRepo.existsByEmployeeCodeAndOrgId(employeeDTO.getEmployeeCode(), employeeDTO.getOrgId())) {
				String errorMessage = String.format("This EmployeeCode: %s Already Exists in This Organization",
						employeeDTO.getEmployeeCode());
				throw new ApplicationException(errorMessage);
			}
			// Create new employee
			employeeVO = new EmployeeVO();
			// Generate auto-incremented Employee Code
//		        String docId = employeeRepo.getEmployeeDocId(employeeDTO.getOrgId());

//		        if (docId == null) {
//		            throw new ApplicationException("Failed to generate Employee Document ID.");
//		        }

			// Update sequence_tracker to increment last_number
//		        employeeRepo.updateLastNumber(employeeDTO.getOrgId());

			employeeVO.setCreatedBy(employeeDTO.getCreatedBy());
			employeeVO.setUpdatedBy(employeeDTO.getCreatedBy());
			message = "Employee Creation Successfully";
		} else {
			// Update existing employee
			employeeVO = employeeRepo.findById(employeeDTO.getId()).orElseThrow(
					() -> new ApplicationException("ID is Not Found Any Information: " + employeeDTO.getId()));

			employeeVO.setUpdatedBy(employeeDTO.getCreatedBy());

			if (!employeeVO.getEmployeeCode().equalsIgnoreCase(employeeDTO.getEmployeeCode())) {
				if (employeeRepo.existsByEmployeeCodeAndOrgId(employeeDTO.getEmployeeCode(), employeeDTO.getOrgId())) {
					String errorMessage = String.format("This EmployeeCode: %s Already Exists in This Organization",
							employeeDTO.getEmployeeCode());
					throw new ApplicationException(errorMessage);
				}
				employeeVO.setEmployeeCode(employeeDTO.getEmployeeCode());
			}
			message = "Employee Update Successfully";
		}

		// Map the remaining fields
		getEmployeeVOFromEmployeeDTO(employeeVO, employeeDTO);

		// Save the entity
		employeeRepo.save(employeeVO);

		// Prepare the response
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("createdEmployeeVO", employeeVO);

		return response;
	}

	
	private EmployeeVO getEmployeeVOFromEmployeeDTO(EmployeeVO employeeVO, EmployeeDTO employeeDTO) throws ApplicationException {
	    employeeVO.setEmployeeCode(employeeDTO.getEmployeeCode());
	    employeeVO.setEmployeeName(employeeDTO.getEmployeeName());
	    employeeVO.setEmployeeAddress(employeeDTO.getEmployeeAddress());
	    employeeVO.setGender(employeeDTO.getGender());
	    employeeVO.setBranch(employeeDTO.getBranch());
	    employeeVO.setBranchCode(employeeDTO.getBranchCode());
	    

	    UserVO userVO =userRepo.findByEmployeeCodeAndOrgId(employeeDTO.getEmployeeCode(),employeeDTO.getOrgId());
    	
    	if(userVO!=null) {
    		userVO.setDepartment(employeeDTO.getDepartment());
    		userVO.setDesignation(employeeDTO.getDesignation());
    	userRepo.save(userVO);
    	}
	    
	    employeeVO.setDepartment(employeeDTO.getDepartment());
	    employeeVO.setDesignation(employeeDTO.getDesignation());
	    employeeVO.setDateOfBirth(employeeDTO.getDateOfBirth());
	    employeeVO.setJoiningDate(employeeDTO.getJoiningDate());
	    employeeVO.setEmail(employeeDTO.getEmail());
	    employeeVO.setBloodGroup(employeeDTO.getBloodGroup());
	    employeeVO.setMobileNo(employeeDTO.getMobileNo());
	    employeeVO.setAlternativeMobileNo(employeeDTO.getAlternativeMobileNo());
	    employeeVO.setAadharNo(employeeDTO.getAadharNo());
	    employeeVO.setPanNo(employeeDTO.getPanNo());
	    employeeVO.setAccountNo(employeeDTO.getAccountNo());
	    employeeVO.setBankName(employeeDTO.getBankName());
	    employeeVO.setIfscCode(employeeDTO.getIfscCode());
	    employeeVO.setGrade(employeeDTO.getGrade());
	    employeeVO.setTeam(employeeDTO.getTeam());
	    employeeVO.setReportnigPerson(employeeDTO.getReportingPerson());
	    employeeVO.setReportnigPersonEmail(employeeDTO.getReportingPersonEmail());
	    employeeVO.setReportningPersonCode(employeeDTO.getReportningPersonCode());
	    employeeVO.setReportingRole(employeeDTO.getReportingRole());
	    employeeVO.setResignDate(employeeDTO.getResignDate());
//	    employeeVO.setPfFlag(employeeDTO.isPfFlag());
//	    employeeVO.setEsiFlag(employeeDTO.isEsiFlag());
//	    employeeVO.setPfPercentage(employeeDTO.getPfPercentage());
//	    employeeVO.setEsiPercentage(employeeDTO.getEsiPercentage());


	    	UserLoginRolesVO userLoginRolesVO =userLoginRolesRepo.findByUserVO_EmployeeCodeAndUserVO_OrgId(employeeDTO.getEmployeeCode(),employeeDTO.getOrgId());
	    	
	    	if(userLoginRolesVO!=null) {
	    	userLoginRolesVO.setEndDate(employeeDTO.getResignDate());
	    	userLoginRolesRepo.save(userLoginRolesVO);
	    	}
	    
	    employeeVO.setOrgId(employeeDTO.getOrgId());
	    employeeVO.setActive(employeeDTO.isActive());
	    employeeVO.setUanNo(employeeDTO.getUanNo());


	    // 1. Fetch existing leave records for this employee (if updating or checking duplicates)
	    List<EmployeeLeaveVO> existingLeaves = employeeLeaveRepo.findByEmployeeVO_EmployeeCodeAndEmployeeVO_OrgId(
	        employeeDTO.getEmployeeCode(), employeeDTO.getOrgId()
	    );

	    // 2. Store existing leave codes
	    Set<String> existingLeaveCodes = existingLeaves.stream()
	        .map(EmployeeLeaveVO::getLeaveCode)
	        .collect(Collectors.toSet());

	    // 3. Validate new leave records to prevent duplicates in the request
	    Set<String> newLeaveCodes = new HashSet<>();
	    for (EmployeeLeaveDTO leaveDTO : employeeDTO.getEmployeeLeaveDTO()) {
	        String leaveCode = leaveDTO.getLeaveCode();

	        // Check for duplicate leaveCode in the same request
	        if (newLeaveCodes.contains(leaveCode)) {
	            throw new ApplicationException("Duplicate Leave Entry Found in Request: " + leaveCode);
	        }
	        newLeaveCodes.add(leaveCode);

	        // Check if leaveCode already exists for the same employee in DB
	        if (!existingLeaveCodes.contains(leaveCode) && 
	            employeeLeaveRepo.existsByLeaveCodeAndEmployeeVO_EmployeeCodeAndEmployeeVO_OrgId(
	                leaveCode, employeeDTO.getEmployeeCode(), employeeDTO.getOrgId()
	            )) {
	            throw new ApplicationException("Duplicate Leave Entry Found: " + leaveCode + " already exists for this employee.");
	        }
	    }

	    // 4. If updating, delete existing leave & balance records before inserting new ones
	    if (employeeDTO.getId() != null) {
	        employeeLeaveRepo.deleteAll(existingLeaves);

	        List<LeaveBalanceVO> leaveBalanceVOs = leaveBalanceRepo.findByEmployeeCodeAndOrgId(
	            employeeDTO.getEmployeeCode(), employeeDTO.getOrgId()
	        );
	        leaveBalanceRepo.deleteAll(leaveBalanceVOs);
	    }

	    List<LeaveBalanceVO> leaveBalanceVOs = new ArrayList<>();
	    List<EmployeeLeaveVO> employeeLeaveVOs = new ArrayList<>();

	    // 5. Process validated leave records
	    for (EmployeeLeaveDTO employeeLeaveDTO : employeeDTO.getEmployeeLeaveDTO()) {
	        String leaveCode = employeeLeaveDTO.getLeaveCode();

	        // Create EmployeeLeaveVO object
	        EmployeeLeaveVO employeeLeaveVO = new EmployeeLeaveVO();
	        employeeLeaveVO.setLeaveCode(leaveCode);
	        employeeLeaveVO.setLeaveType(employeeLeaveDTO.getLeaveType());
	        employeeLeaveVO.setTotalLeave(employeeLeaveDTO.getTotalLeave());
	        employeeLeaveVO.setEffectiveFrom(employeeLeaveDTO.getEffectiveFrom());
	        employeeLeaveVO.setEmployeeVO(employeeVO);
	        employeeLeaveVOs.add(employeeLeaveVO);

	        // Create LeaveBalanceVO object
	        LeaveBalanceVO leaveBalanceVO = new LeaveBalanceVO();
	        leaveBalanceVO.setLeaveCode(leaveCode);
	        leaveBalanceVO.setLeaveType(employeeLeaveDTO.getLeaveType());
	        leaveBalanceVO.setTotalLeave(employeeLeaveDTO.getTotalLeave());
	        leaveBalanceVO.setEmployeeName(employeeDTO.getEmployeeName());
	        leaveBalanceVO.setEmployeeCode(employeeDTO.getEmployeeCode());
	        leaveBalanceVO.setOrgId(employeeDTO.getOrgId());
	        leaveBalanceVO.setBranch(employeeDTO.getBranch());
	        leaveBalanceVO.setBranchCode(employeeDTO.getBranchCode());
	        leaveBalanceVO.setLeaveStatus("Assigned");
	        leaveBalanceVOs.add(leaveBalanceVO);
	    }

	    // 6. Save new leave records
	    leaveBalanceRepo.saveAll(leaveBalanceVOs);
	    employeeVO.setEmployeeLeaveVO(employeeLeaveVOs);

	    return employeeVO;
	}


	@Override
	public EmployeeVO uploadEmployeeImageInBloob(MultipartFile file, Long id) throws IOException, java.io.IOException {
		EmployeeVO employeeVO = employeeRepo.findById(id).get();
		 if (file != null && !file.isEmpty()) {
		        employeeVO.setProfileImage(file.getBytes());
		    }
		 return employeeRepo.save(employeeVO);
	}


	@Override
	public void deleteEmployee(Long employeeid) {
		employeeRepo.deleteById(employeeid);
	}

	@Override
	public List<Map<String, Object>> getDepartmentNameForEmployee(Long orgId) {
		Set<Object[]> result = employeeRepo.findDepartmentNameForEmployee(orgId);
		return getDepartmentName(result);
	}

	private List<Map<String, Object>> getDepartmentName(Set<Object[]> result) {
		List<Map<String, Object>> details = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> object = new HashMap<>();
			object.put("departmentName", fs[0] != null ? fs[0].toString() : "");
			details.add(object); // Add the map to the list

		}
		return details;
	}

	@Override
	public List<Map<String, Object>> getDesignationNameForEmployee(Long orgId) {
		Set<Object[]> result = employeeRepo.findDesignationNameForEmployee(orgId);
		return getDesignationName(result);
	}

	private List<Map<String, Object>> getDesignationName(Set<Object[]> result) {
		List<Map<String, Object>> details = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> object = new HashMap<>();
			object.put("designationName", fs[0] != null ? fs[0].toString() : "");
			object.put("designationcode", fs[1] != null ? fs[1].toString() : "");

			details.add(object); // Add the map to the list

		}
		return details;
	}

	// DesignationLeave

	@Override
	public Map<String, Object> createUpdateDesignationLeave(DesignationLeaveDTO designationLeaveDTO)
			throws ApplicationException {

		DesignationLeaveVO designationLeaveVO = new DesignationLeaveVO();
		String message;
//		String screenCode = "DEPT";
		if (ObjectUtils.isNotEmpty(designationLeaveDTO.getId())) {
			designationLeaveVO = designationLeaveRepo.findById(designationLeaveDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid DesignationLeave Type details"));
			if (!designationLeaveVO.getDesignation().equalsIgnoreCase(designationLeaveDTO.getDesignation())) {
				if (designationLeaveRepo.existsByDesignationAndLeaveTypeAndOrgId(designationLeaveDTO.getDesignation(),
						designationLeaveDTO.getLeaveType(), designationLeaveDTO.getOrgId())) {
					String errorMessage = String.format("The Designation: %s already exists in This Organization and LeaveType.",
							designationLeaveDTO.getDesignation());
					throw new ApplicationException(errorMessage);
				}
				designationLeaveVO.setDesignation(designationLeaveDTO.getDesignation().toUpperCase());
			}

			designationLeaveVO.setUpdatedBy(designationLeaveDTO.getCreatedBy());
			message = "DesignationLeave Updated Successfully";
		} else {

			if (designationLeaveRepo.existsByDesignationAndLeaveTypeAndOrgId(designationLeaveDTO.getDesignation(),
					designationLeaveDTO.getLeaveType(), designationLeaveDTO.getOrgId())) {
				String errorMessage = String.format("The Designation: %s already exists in This Organization and LeaveType.",
						designationLeaveDTO.getDesignation());
				throw new ApplicationException(errorMessage);
			}
			designationLeaveVO.setCreatedBy(designationLeaveDTO.getCreatedBy());
			designationLeaveVO.setUpdatedBy(designationLeaveDTO.getCreatedBy());
			message = "DesignationLeave Created Successfully";
		}

		createUpdateDesignationLeaveVOByDesignationLeaveDTO(designationLeaveDTO, designationLeaveVO);
		designationLeaveRepo.save(designationLeaveVO);
		Map<String, Object> response = new HashMap<>();
		response.put("designationLeaveVO", designationLeaveVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateDesignationLeaveVOByDesignationLeaveDTO(DesignationLeaveDTO designationLeaveDTO,
			DesignationLeaveVO designationLeaveVO) {
		designationLeaveVO.setLeaveCode(designationLeaveDTO.getLeaveCode());
		designationLeaveVO.setLeaveType(designationLeaveDTO.getLeaveType());
		designationLeaveVO.setTotalLeave(designationLeaveDTO.getTotalLeave());
		designationLeaveVO.setOrgId(designationLeaveDTO.getOrgId());
		designationLeaveVO.setDesignationCode(designationLeaveDTO.getDesignationCode());
		designationLeaveVO.setDesignation(designationLeaveDTO.getDesignation());
		designationLeaveVO.setActive(designationLeaveDTO.isActive());

	}

	@Override
	public DesignationLeaveVO getDesignationLeaveById(Long id) {

		return designationLeaveRepo.getDesignationLeaveById(id);
	}

	@Override
	public List<DesignationLeaveVO> getDesignationLeaveByOrgId(Long orgId) {
		// TODO Auto-generated method stub
		return designationLeaveRepo.getDesignationLeaveByOrgId(orgId);
	}

	@Override
	public List<Map<String, Object>> getLeaveDetailsFromDesignationLeave(Long orgId, String designationCode,String leaveApplicable) {
		Set<Object[]> result = designationLeaveRepo.getLeaveDetailsFromDesignationLeave(orgId, designationCode,leaveApplicable);
		return getLeaveDetailsFromDesignationLeave(result);
	}

	private List<Map<String, Object>> getLeaveDetailsFromDesignationLeave(Set<Object[]> result) {
		List<Map<String, Object>> details = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> object = new HashMap<>();
			object.put("leaveType", fs[0] != null ? fs[0].toString() : "");
			object.put("leaveCode", fs[1] != null ? fs[1].toString() : "");
//			object.put("carryForward", fs[2] != null ? fs[2].toString() : "");
//			object.put("effective", fs[3] != null ? fs[3].toString() : "");
//			object.put("leaveApplicable", fs[4] != null ? fs[4].toString() : "");
			object.put("totalLeave", fs[2] != null ? fs[2].toString() : "");
			object.put("designationCode", fs[3] != null ? fs[3].toString() : "");
			object.put("designation", fs[4] != null ? fs[4].toString() : "");

			details.add(object); // Add the map to the list

		}
		return details;
	}
	
	//PROJECT MASTER
	
	@Override
	public Map<String, Object> createUpdateProjectMaster(ProjectMasterDTO projectMasterDTO)
			throws ApplicationException {

		ProjectMasterVO projectMasterVO = new ProjectMasterVO();
		String message;
		if (ObjectUtils.isNotEmpty(projectMasterDTO.getId())) {
			projectMasterVO = projectMasterRepo.findById(projectMasterDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid projectMaster Type details"));
			if (!projectMasterVO.getProjectName().equalsIgnoreCase(projectMasterDTO.getProjectName())) {
				if (projectMasterRepo.existsByProjectNameAndOrgId(projectMasterDTO.getProjectName(), projectMasterDTO.getOrgId())) {
					String errorMessage = String.format("The Project: %s already exists in This Organization and LeaveType.",
							projectMasterDTO.getProjectName());
					throw new ApplicationException(errorMessage);
				}
				projectMasterVO.setProjectName(projectMasterDTO.getProjectName().toUpperCase());
			}

			projectMasterVO.setUpdatedBy(projectMasterDTO.getCreatedBy());
			message = "ProjectMaster Updated Successfully";
		} else {

			if (projectMasterRepo.existsByProjectNameAndOrgId(projectMasterDTO.getProjectName(),
					 projectMasterDTO.getOrgId())) {
				String errorMessage = String.format("The Project: %s already exists in This Organization and LeaveType.",
						projectMasterDTO.getProjectName());
				throw new ApplicationException(errorMessage);
			}
			projectMasterVO.setCreatedBy(projectMasterDTO.getCreatedBy());
			projectMasterVO.setUpdatedBy(projectMasterDTO.getCreatedBy());
			message = "ProjectMaster Created Successfully";
		}

		createUpdateProjectMasterVOByProjectMasterDTO(projectMasterDTO, projectMasterVO);
		projectMasterRepo.save(projectMasterVO);
		Map<String, Object> response = new HashMap<>();
		response.put("projectMasterVO", projectMasterVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateProjectMasterVOByProjectMasterDTO(ProjectMasterDTO projectMasterDTO,
			ProjectMasterVO projectMasterVO) {
		projectMasterVO.setProjectName(projectMasterDTO.getProjectName());
		projectMasterVO.setProjectCode(projectMasterDTO.getProjectCode());
		projectMasterVO.setDescription(projectMasterDTO.getDescription());
		projectMasterVO.setOrgId(projectMasterDTO.getOrgId());
		projectMasterVO.setActive(projectMasterDTO.isActive());

	}

	
	@Override
	public List<ProjectMasterVO> getProjectMasterByOrgId(Long orgId) {
		// TODO Auto-generated method stub
		return projectMasterRepo.getProjectMasterByOrgId(orgId);
	}

	@Override
	public ProjectMasterVO getProjectMasterById(Long id) {

		return projectMasterRepo.getProjectMasterById(id);
	}
	
}
