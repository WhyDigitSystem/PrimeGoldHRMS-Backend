package com.efit.hrms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.BranchDTO;
import com.efit.hrms.dto.DesignationLeaveDTO;
import com.efit.hrms.dto.EmployeeDTO;
import com.efit.hrms.dto.ProjectMasterDTO;
import com.efit.hrms.entity.BranchVO;
import com.efit.hrms.entity.DesignationLeaveVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.LeaveProcessVO;
import com.efit.hrms.entity.ProjectMasterVO;
import com.efit.hrms.exception.ApplicationException;

import io.jsonwebtoken.io.IOException;

@Service
public interface MasterService {

	//Branch
	List<BranchVO> getAllBranch(Long orgid);

	Optional<BranchVO> getBranchById(Long branchid);

	Map<String, Object> createUpdateBranch(BranchDTO branchDTO) throws Exception;

	void deleteBranch(Long branchid);
	
	// employee

		List<EmployeeVO> getAllEmployee();

		List<Map<String, Object>> getEmployeesWithCompanyInfoByOrgId(Long orgId);
		
		List<EmployeeVO> getAllEmployeeByOrgIdAndEmployeeCode(Long orgId, String employeeCode);
		
		EmployeeVO uploadEmployeeImageInBloob(MultipartFile file, Long id) throws IOException, java.io.IOException;


		Optional<EmployeeVO> getEmployeeById(Long employeeid);

		Map<String, Object> createEmployee(EmployeeDTO employeeDTO) throws ApplicationException;

		void deleteEmployee(Long employeeid);
		
		List<Map<String, Object>> getDepartmentNameForEmployee(Long orgId);

		List<Map<String, Object>> getDesignationNameForEmployee(Long orgId);
		
		List<Map<String, Object>> getReportingNameForEmployee(Long orgId, String branchCode, String employeeCode);

		Map<String, Object> createUpdateDesignationLeave(DesignationLeaveDTO designationLeaveDTO) throws ApplicationException;

		//AssignedLeave
		DesignationLeaveVO getDesignationLeaveById(Long id);

		List<DesignationLeaveVO> getDesignationLeaveByOrgId(Long orgId);

		List<Map<String, Object>> getLeaveDetailsFromDesignationLeave(Long orgId, String designationCode, String leaveApplicable);

		//PROJECT MASTER

		Map<String, Object> createUpdateProjectMaster(ProjectMasterDTO projectMasterDTO) throws ApplicationException;

		List<ProjectMasterVO> getProjectMasterByOrgId(Long orgId);

		ProjectMasterVO getProjectMasterById(Long id);






}
