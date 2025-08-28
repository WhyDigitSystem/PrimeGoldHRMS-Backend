package com.efit.hrms.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.efit.hrms.dto.PermissionRequestDTO;
import com.efit.hrms.dto.PfEsiAmountDTO;
import com.efit.hrms.dto.SalaryHeadsDTO;
import com.efit.hrms.dto.SalaryProcessDTO;
import com.efit.hrms.dto.SalaryStructureDTO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.PermissionRequestVO;
import com.efit.hrms.entity.SalaryHeadsVO;
import com.efit.hrms.entity.SalaryProcessVO;
import com.efit.hrms.entity.SalaryStructureVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface EmployeeMasterService {

	Map<String, Object> createUpdateSalaryHeads(SalaryHeadsDTO salaryHeadsDTO) throws ApplicationException;

	List<SalaryHeadsVO> getAllSalaryHeadsByOrgId(Long orgId);

	SalaryHeadsVO getSalaryHeadsById(Long id);





	//SalaryStructure
	
	List<SalaryStructureVO> getAllSalaryStructureByOrgId(Long orgId);

	 SalaryStructureVO getSalaryStructureById(Long id);

	Map<String, Object> createUpdateSalaryStructure(SalaryStructureDTO salaryStructureDTO) throws ApplicationException;
	
	List<EmployeeVO> getAllEmployeeByActive(Long orgId);

	
	//permissionRequest
	PermissionRequestVO getPermissionRequestById(Long id);
	
	 List<PermissionRequestVO> getAllPermissionRequestByOrgId(Long orgId, String branchCode);

	Map<String, Object> createUpdatePermissionRequest(PermissionRequestDTO permissionRequestDTO) throws ApplicationException;

	List<Map<String, Object>> getReportingPerson(Long orgId, String employeeCode);

	//SalaryProcess
	
	Map<String, Object> createUpdateSalaryProcess(List<SalaryProcessDTO> salaryProcessDTO) throws ApplicationException;

	List<SalaryProcessVO> getAllSalaryProcessByOrgId(Long orgId);

	SalaryProcessVO getSalaryProcessById(Long id);

	List<Map<String, Object>> getSalaryStructureForSalaryProcess(Long orgId, String employeeCode);

	List<Map<String, Object>> getLeaveDetailsforSalaryProcess(Long orgId, Long month, String year, String department, String branch, String type, String contractor);

//	List<Map<String, Object>> getNetPayForSalaryProcess( BigDecimal grossPay,
//			BigDecimal sumOfDetection);

	List<Map<String, Object>> getPayOnHandsForSalaryProcess(Long totalCompanyWorkingDays, BigDecimal grossPay,
			Long empSalaryDays, BigDecimal sumOfDetection, BigDecimal otAmount);
	
	//ApprovedSalaryProcess Report
	List<SalaryProcessVO> getApprovedSalaryProcessReport(Long orgId, Long month, String year);


// Dashboard
	
	List<Map<String, Object>> getEmpDob(Long orgId);
	
	List<Map<String, Object>> GetworkAniversary (Long OrgId);

	List<Map<String, Object>> GetnewJoineDetails (Long Orgid);

	Map<String, Object> createApprovalPermissionRequest(Long orgId, Long id, String employeeCode, String action,
			String actionBy, String notifyCode, String notify, String screenName) throws ApplicationException;

	//PermissionRequestForDashBoard
	
	List<Map<String, Object>> getPendingPermissionRequest(Long orgId, String branchCode, String reportingPersonCode);

	List<Map<String, Object>> getApprovedPermissionRequestforTeam(Long orgId, String branchCode,
			String reportingPersonCode);

	List<PfEsiAmountDTO> getPfAmountAndEsiAmountByEmployee(Long orgId, String employeeCode, String branchCode,
			BigDecimal sumOfEarnings);

	Map<String, Object> createApprovalSalaryProcess(Long orgId, List<Long> id, String action, String actionBy) throws ApplicationException;





}
