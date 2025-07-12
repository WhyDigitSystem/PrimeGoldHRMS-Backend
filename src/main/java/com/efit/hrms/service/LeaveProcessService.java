package com.efit.hrms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.CompensatoryOffDTO;
import com.efit.hrms.dto.LeaveProcessDTO;
import com.efit.hrms.dto.LeaveRequestDTO;
import com.efit.hrms.dto.LeaveTypeDTO;
import com.efit.hrms.dto.TravelRequestDTO;
import com.efit.hrms.dto.WorkFromHomeDTO;
import com.efit.hrms.entity.CheckInVO;
import com.efit.hrms.entity.CompensatoryOffVO;
import com.efit.hrms.entity.LeaveProcessVO;
import com.efit.hrms.entity.LeaveRequestVO;
import com.efit.hrms.entity.LeaveTypeVO;
import com.efit.hrms.entity.TravelRequestVO;
import com.efit.hrms.entity.WorkFromHomeVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface LeaveProcessService {

	// leaveType

	Map<String, Object> createUpdateLeaveType(LeaveTypeDTO leaveTypeDTO) throws ApplicationException;

	LeaveTypeVO getLeaveTypeById(Long id);

	List<LeaveTypeVO> getLeaveTypeByOrgId(Long orgId);

	// leave Request

	
	Map<String, Object> createUpdateLeaveRequest(LeaveRequestDTO leaveRequestDTO) throws ApplicationException;

	
	LeaveRequestVO getLeaverequestById(Long id);

	List<LeaveRequestVO> getLeaveRequestByOrgId(Long orgId, String employeeCode);

	List<Map<String, Object>> getAllLeaveTypeFromLeaveMaster(Long orgId, String employeeCode);
	
//	List<Map<String, Object>> getTotalLeaveFromLeaveBalance(Long orgId, String employeeCode, String leavetype);

	Map<String, Object> createApprovalLeave(Long orgId, Long id,String employeeCode,String action, String actionBy, String notifyCode, String notify, String screenName) throws ApplicationException;

	Map<String, Object> calculateLeavedays(Long orgId, LocalDate fromDate, LocalDate toDate, String selectLeave) throws ApplicationException;

	//Leave Process
	
//	Map<String, Object> createUpdateLeaveProcess(@Valid LeaveProcessDTO leaveProcessDTO) throws ApplicationException;

	List<Map<String, Object>> getLeaveDetailsForLeaveProcess(String fromDate, String toDate, Long orgId);

	List<Map<String, Object>> getCheckInAndOutDaysForLeaveProcess(String fromDate, String toDate, Long orgId,
			String branchCode, String empCode);
	
	List<LeaveProcessVO> getLeaveProcessByOrgId(Long orgId);

	Map<String, Object> createUpdateLeaveProcess(@Valid List<LeaveProcessDTO> leaveProcessDTO)
			throws ApplicationException;

	//UploadLeaveProcess
	
	void uploadLeaveData(MultipartFile file, Long orgId, String createdBy) throws Exception;

	//LeaveRequestForDashBoard
	
	List<Map<String, Object>> getLeaveRequestForDashBoard(Long orgId, String reportingPersonCode, String branchCode);

	List<Map<String, Object>> getAllApprovedLeaveForTeam(Long orgId, String reportingPersonCode, String branchCode);

	
	//Compoff api
	
	CompensatoryOffVO getCompensatoryOffVOById(Long id);

	List<CompensatoryOffVO> getCompensatoryOffByOrgId(Long orgId, String empCode);

	Map<String, Object> createUpdateCompOff(@Valid List<CompensatoryOffDTO> compensatoryOffDTO) throws ApplicationException;

	Map<String, Object> createApprovalCompOff(Long orgId, Long id, String employeeCode, String action, String actionBy, String notifyCode, String notify, String screenName) throws ApplicationException;

	List<Map<String, Object>> getCompoffRequestForDashBoard(Long orgId, String reportingPersonCode, String branchCode);

	//upload checkinout

	Map<String, Object> uploadExcelData(MultipartFile files,Long orgId);

	//AttandanceReport
	
	List<LeaveProcessVO> getAttandanceReport(Long orgId, String employeeCode, String year, String month);

	List<Map<String, Object>> getCheckInOutReport(Long orgId, String employeeCode, String fromDate, String toDate ,String branch);

	//ApprovalAll
	
	String createUnifiedApprovalAllTypes(Long orgId, String action, String actionBy, String notifyCode, String notify) throws ApplicationException;

	//TravelRequest
	
	Map<String, Object> createUpdateTravelRequest(@Valid TravelRequestDTO travelRequestDTO) throws ApplicationException;

	TravelRequestVO getTravelRequestById(Long id);

	List<TravelRequestVO> getTravelRequestByOrgId(Long orgId);

	//WORKFROMHOME
	
	Map<String, Object> createUpdateWorkFromHome(@Valid WorkFromHomeDTO workFromHomeDTO) throws ApplicationException;

	WorkFromHomeVO getWorkFromHomeById(Long id);

	List<WorkFromHomeVO> getWorkFromHomeByOrgId(Long orgId);

	Map<String, Object> createApprovalTravelRequest(Long orgId, String employeeCode, String action, String actionBy,
			Long id, String notifyCode, String notify, String screenName) throws ApplicationException;

	Map<String, Object> createApprovalWorkFromHome(Long orgId, String employeeCode, String action, String actionBy,
			Long id, String notifyCode, String notify, String screenName) throws ApplicationException;

	List<Map<String, Object>> getPendingWorkFromHomeForDashBoard(Long orgId, String reportingPersonCode,
			String branchCode);

	List<Map<String, Object>> getPendingTravelRequestForDashBoard(Long orgId, String reportingPersonCode,
			String branchCode);

	










}
