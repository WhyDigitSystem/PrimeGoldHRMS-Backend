package com.efit.hrms.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.CompensatoryOffDTO;
import com.efit.hrms.dto.LeaveProcessDTO;
import com.efit.hrms.dto.LeaveRequestDTO;
import com.efit.hrms.dto.LeaveTypeDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.TravelRequestDTO;
import com.efit.hrms.dto.WorkFromHomeDTO;
import com.efit.hrms.entity.CompensatoryOffVO;
import com.efit.hrms.entity.LeaveProcessVO;
import com.efit.hrms.entity.LeaveRequestVO;
import com.efit.hrms.entity.LeaveTypeVO;
import com.efit.hrms.entity.TravelRequestVO;
import com.efit.hrms.entity.WorkFromHomeVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.service.LeaveProcessService;

@CrossOrigin
@RestController
@RequestMapping("/api/leaveprocess")
public class LeaveProcessController extends BaseController {

	@Autowired
	LeaveProcessService leaveProcessService;

	public static final Logger LOGGER = LoggerFactory.getLogger(LeaveProcessController.class);

	// Leave Type

	@PutMapping("/createUpdateLeaveType")
	public ResponseEntity<ResponseDTO> createUpdateLeaveType(@Valid @RequestBody LeaveTypeDTO LeavetypeDTO) {
		String methodName = "createUpdateLeaveType()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> leaveTypeVO = leaveProcessService.createUpdateLeaveType(LeavetypeDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, leaveTypeVO.get("message"));
			responseObjectsMap.put("leaveTypeVO", leaveTypeVO.get("leaveTypeVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("getLeaveTypeById")
	public ResponseEntity<ResponseDTO> getLeaveTypeById(@RequestParam Long id) {
		String methodName = "getLeaveTypeById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		LeaveTypeVO leaveTypeVO = null;
		try {
			leaveTypeVO = leaveProcessService.getLeaveTypeById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "leaveType found by ID");
			responseObjectsMap.put("leaveTypeVO", leaveTypeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "leaveType not found for ID: " + id;
			responseDTO = createServiceResponseError(responseObjectsMap, "leaveType not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("getLeaveTypeByOrgId")
	public ResponseEntity<ResponseDTO> getLeaveTypeByOrgId(@RequestParam Long orgId) {
		String methodName = "getLeaveTypeByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<LeaveTypeVO> leaveTypeVO = null;
		try {
			leaveTypeVO = leaveProcessService.getLeaveTypeByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "leaveType found by ORGID");
			responseObjectsMap.put("leaveTypeVO", leaveTypeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "leaveType not found for orgID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "leaveType not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// leave request

	@PutMapping("/createUpdateLeaveRequest")
	public ResponseEntity<ResponseDTO> createUpdateLeaveRequest(@Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
		String methodName = "createUpdateLeaveRequest()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> leaveRequestVO = leaveProcessService.createUpdateLeaveRequest(leaveRequestDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, leaveRequestVO.get("message"));
			responseObjectsMap.put("leaveRequestVO", leaveRequestVO.get("leaveRequestVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getLeaveRequestById")
	public ResponseEntity<ResponseDTO> getLeaveRequestById(@RequestParam Long id) {
		String methodName = "getLeaveRequestById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		LeaveRequestVO leaveRequestVO = null;
		try {
			leaveRequestVO = leaveProcessService.getLeaverequestById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "leave Request found by ID");
			responseObjectsMap.put("leaveRequestVO", leaveRequestVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "Leave Request not found for ID: " + id;
			responseDTO = createServiceResponseError(responseObjectsMap, "leave Request not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getLeaveRequestByOrgId")
	public ResponseEntity<ResponseDTO> getLeaveRequestByOrgId(@RequestParam Long orgId,
			@RequestParam String employeeCode) {
		String methodName = "getLeaveRequestByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<LeaveRequestVO> leaveRequestVO = null;
		try {
			leaveRequestVO = leaveProcessService.getLeaveRequestByOrgId(orgId, employeeCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "leaveRequest found by ORGID");
			responseObjectsMap.put("leaveRequestVO", leaveRequestVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "leaveRequest not found for orgID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "leaveRequest not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// getleavetype

	@GetMapping("/getAllLeaveTypeFromLeaveMaster")
	public ResponseEntity<ResponseDTO> getAllLeaveTypeFromLeaveMaster(@RequestParam Long orgId,
			@RequestParam String employeeCode) {
		String methodName = "getAllLeaveTypeFromLeaveMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> leaveRequestVO = new ArrayList<>();
		try {
			leaveRequestVO = leaveProcessService.getAllLeaveTypeFromLeaveMaster(orgId, employeeCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "leaveRequest" + " information get successfully");
			responseObjectsMap.put("leaveRequestVO", leaveRequestVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "leaveRequest information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//	@GetMapping("/getTotalLeaveFromLeaveBalance")
//	public ResponseEntity<ResponseDTO> getTotalLeaveFromLeaveBalance(@RequestParam Long orgId,
//			@RequestParam String employeeCode,@RequestParam String leavetype) {
//		String methodName = "getTotalLeaveFromLeaveBalance()";
//		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//		String errorMsg = null;
//		Map<String, Object> responseObjectsMap = new HashMap<>();
//		ResponseDTO responseDTO = null;
//		List<Map<String, Object>> leaveBalanceVO = new ArrayList<>();
//		try {
//			leaveBalanceVO = leaveProcessService.getTotalLeaveFromLeaveBalance(orgId, employeeCode,leavetype);
//		} catch (Exception e) {
//			errorMsg = e.getMessage();
//			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//		}
//		if (StringUtils.isBlank(errorMsg)) {
//			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TotalLeave" + " information get successfully");
//			responseObjectsMap.put("leaveBalanceVO", leaveBalanceVO);
//			responseDTO = createServiceResponse(responseObjectsMap);
//		} else {
//			responseDTO = createServiceResponseError(responseObjectsMap, "TotalLeave information receive failed",
//					errorMsg);
//		}
//		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//		return ResponseEntity.ok().body(responseDTO);
//	}

	// ApprovalLeave

	@PutMapping("/createApprovalLeave")
	public ResponseEntity<ResponseDTO> createApprovalLeave(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String employeeCode, @RequestParam String action, @RequestParam String actionBy,
			@RequestParam String notifyCode, @RequestParam String notify,@RequestParam String screenName) {
		String methodName = "createApprovalLeave()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> result = leaveProcessService.createApprovalLeave(orgId, id, employeeCode, action,
					actionBy, notifyCode, notify,screenName);

			// ✅ Correct keys from the returned map
			responseObjectsMap.put("leaveRequestVO", result.get("leaveRequestVO"));
			responseObjectsMap.put("message", result.get("message"));

			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/calculateLeavedays")
	public ResponseEntity<?> calculateLeavedays(@RequestParam Long orgId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
			@RequestParam String selectLeave) throws ApplicationException {

		Map<String, Object> result = leaveProcessService.calculateLeavedays(orgId, fromDate, toDate, selectLeave);
		return ResponseEntity.ok(result);
	}

	// leaveprocess

	@PutMapping("/createUpdateLeaveProcess")
	public ResponseEntity<ResponseDTO> createUpdateLeaveProcess(
			@Valid @RequestBody List<LeaveProcessDTO> leaveProcessDTO) {
		String methodName = "createUpdateLeaveProcess()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> leaveProcessVO = leaveProcessService.createUpdateLeaveProcess(leaveProcessDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, leaveProcessVO.get("message"));
			responseObjectsMap.put("leaveProcessVO", leaveProcessVO.get("leaveProcessVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getLeaveProcessByOrgId")
	public ResponseEntity<ResponseDTO> getLeaveProcessByOrgId(@RequestParam Long orgId) {
		String methodName = "getLeaveProcessByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<LeaveProcessVO> leaveProcessVO = null;
		try {
			leaveProcessVO = leaveProcessService.getLeaveProcessByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "leaveProcess found by ORGID");
			responseObjectsMap.put("leaveProcessVO", leaveProcessVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "leaveProcess not found for orgID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "leaveProcess not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getLeaveDetailsForLeaveProcess")
	public ResponseEntity<ResponseDTO> getLeaveDetailsForLeaveProcess(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam Long orgId,@RequestParam String department,@RequestParam String branch) {

		String methodName = "getLeaveDetailsForLeaveProcess()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> leaveDetailsList;

		try {
			leaveDetailsList = leaveProcessService.getLeaveDetailsForLeaveProcess(fromDate, toDate, orgId,department,branch);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Leave details retrieved successfully");
			responseObjectsMap.put("leaveProcessVO", leaveDetailsList); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve leave details", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCheckInAndOutDaysForLeaveProcess")
	public ResponseEntity<ResponseDTO> getCheckInAndOutDaysForLeaveProcess(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam Long orgId, @RequestParam String branchCode,
			@RequestParam String empCode) {

		String methodName = "getCheckInAndOutDaysForLeaveProcess()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> checkInOutDetailsList;

		try {
			checkInOutDetailsList = leaveProcessService.getCheckInAndOutDaysForLeaveProcess(fromDate, toDate, orgId,
					branchCode, empCode);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "checkinout details retrieved successfully");
			responseObjectsMap.put("checkInOutDetailsList", checkInOutDetailsList); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve checkinout details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// upload leaveprocess

	@PostMapping("/uploadLeaveProcess")
	public ResponseEntity<ResponseDTO> uploadLeaveProcess(@RequestParam("files") MultipartFile file,
			@RequestParam("orgId") Long orgId, @RequestParam("createdBy") String createdBy) {
		String methodName = "uploadLeaveProcess()";
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;

		try {
			// Call service method to process Excel upload
			leaveProcessService.uploadLeaveData(file, orgId, createdBy);

			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "AttandanceProcess data uploaded successfully");
			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// INDEXBOXLEAVE APPROVAL

	@GetMapping("/getLeaveRequestForDashBoard")
	public ResponseEntity<ResponseDTO> getLeaveRequestForDashBoard(@RequestParam Long orgId,
			@RequestParam String reportingPersonCode, @RequestParam String branchCode) {

		String methodName = "getLeaveRequestForDashBoard()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> leaveRequestVO;

		try {
			leaveRequestVO = leaveProcessService.getLeaveRequestForDashBoard(orgId, reportingPersonCode, branchCode);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Leave Request details retrieved successfully");
			responseObjectsMap.put("leaveRequestVO", leaveRequestVO); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve leave Request details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllApprovedLeaveForTeam")
	public ResponseEntity<ResponseDTO> getAllApprovedLeaveForTeam(@RequestParam Long orgId,
			@RequestParam String reportingPersonCode, @RequestParam String branchCode) {

		String methodName = "getAllApprovedLeaveForTeam()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> leaveRequestVO;

		try {
			leaveRequestVO = leaveProcessService.getAllApprovedLeaveForTeam(orgId, reportingPersonCode, branchCode);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Leave Request details retrieved successfully");
			responseObjectsMap.put("leaveRequestVO", leaveRequestVO); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve leave Request details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Comp-off API

	@GetMapping("/getCompensatoryOffVOById")
	public ResponseEntity<ResponseDTO> getCompensatoryOffVOById(@RequestParam Long id) {
		String methodName = "getCompensatoryOffVOById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CompensatoryOffVO compensatoryOffVO = null;
		try {
			compensatoryOffVO = leaveProcessService.getCompensatoryOffVOById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "compensatoryOff found by ID");
			responseObjectsMap.put("compensatoryOffVO", compensatoryOffVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "compensatoryOff not found for ID: " + id;
			responseDTO = createServiceResponseError(responseObjectsMap, "compensatoryOff not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCompensatoryOffByOrgId")
	public ResponseEntity<ResponseDTO> getCompensatoryOffByOrgId(@RequestParam Long orgId,
			@RequestParam String empCode) {
		String methodName = "getCompensatoryOffByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CompensatoryOffVO> compensatoryOffVO = null;
		try {
			compensatoryOffVO = leaveProcessService.getCompensatoryOffByOrgId(orgId, empCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "compensatoryOff found by ORGID");
			responseObjectsMap.put("compensatoryOffVO", compensatoryOffVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "compensatoryOff not found for orgID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "compensatoryOff not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/createUpdateCompOff")
	public ResponseEntity<ResponseDTO> createUpdateCompOff(
			@Valid @RequestBody List<CompensatoryOffDTO> compensatoryOffDTO) {
		String methodName = "createUpdateCompOff()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> compensatoryOffVO = leaveProcessService.createUpdateCompOff(compensatoryOffDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, compensatoryOffVO.get("message"));
			responseObjectsMap.put("compensatoryOffVO", compensatoryOffVO.get("compensatoryOffVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCompoffRequestForDashBoard")
	public ResponseEntity<ResponseDTO> getCompoffRequestForDashBoard(@RequestParam Long orgId,
			@RequestParam String reportingPersonCode, @RequestParam String branchCode) {

		String methodName = "getCompoffRequestForDashBoard()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> compensatoryOffVO;

		try {
			compensatoryOffVO = leaveProcessService.getCompoffRequestForDashBoard(orgId, reportingPersonCode,
					branchCode);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "compensatoryOff details retrieved successfully");
			responseObjectsMap.put("compensatoryOffVO", compensatoryOffVO); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve compensatoryOff details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/createApprovalCompOff")
	public ResponseEntity<ResponseDTO> createApprovalCompOff(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String employeeCode, @RequestParam String action, @RequestParam String actionBy,@RequestParam String notifyCode, @RequestParam String notify,@RequestParam String screenName) {
		String methodName = "createApprovalCompOff()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> compensatoryOffVO = leaveProcessService.createApprovalCompOff(
	                orgId, id, employeeCode, action, actionBy, notifyCode, notify,screenName);

	        // ✅ Unwrap values
	        Object compOffData = compensatoryOffVO.get("compensatoryOffVO");
	        String message = (String) compensatoryOffVO.getOrDefault("message", "Comp Off Approved Successfully");

	        responseObjectsMap.put("compensatoryOffVO", compOffData);
	        responseObjectsMap.put("message", message);

	        responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// checkin upload

	@PostMapping("/uploadcheckin")
	public ResponseEntity<Map<String, Object>> uploadExcelCheckIn(
	        @RequestParam("files") MultipartFile files,
	        @RequestParam("orgId") Long orgId) {

	    Map<String, Object> serviceResponse = leaveProcessService.uploadExcelData(files, orgId);

	    Map<String, Object> response = new HashMap<>();
	    Map<String, Object> paramObjectsMap = new HashMap<>();

	    paramObjectsMap.put("message", serviceResponse.get("message"));
	    if (serviceResponse.containsKey("duplicates")) {
	        paramObjectsMap.put("duplicates", serviceResponse.get("duplicates"));
	    }

	    boolean isSuccess = "CheckInOut data uploaded successfully".equals(serviceResponse.get("message"));

	    response.put("statusFlag", isSuccess ? "Ok" : "Error");
	    response.put("status", isSuccess);
	    response.put("paramObjectsMap", paramObjectsMap);

	    return ResponseEntity.ok(response);
	}


	// Attandance Report
	@GetMapping("/getAttandanceReport")
	public ResponseEntity<ResponseDTO> getAttendanceReport(@RequestParam(required = false) Long orgId,
			@RequestParam(required = false) String employeeCode, @RequestParam(required = false) String year,
			@RequestParam(required = false) String month) {
		String methodName = "getAttandanceReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<LeaveProcessVO> leaveProcessVO = new ArrayList<>();
		try {
			leaveProcessVO = leaveProcessService.getAttandanceReport(orgId, employeeCode, year, month);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "AttandanceReport" + " information get successfully");
			responseObjectsMap.put("leaveProcessVO", leaveProcessVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "AttandanceReport information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getCheckInOutReport")
	public ResponseEntity<ResponseDTO> getCheckInOutReport(@RequestParam(required = false) Long orgId,
			@RequestParam(required = false) String employeeCode, @RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate, @RequestParam(required = false) String branch) {
		String methodName = "getCheckInOutReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> checkInVO = new ArrayList<>();
		try {
			checkInVO = leaveProcessService.getCheckInOutReport(orgId, employeeCode, fromDate, toDate, branch);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CheckInOutReport" + " information get successfully");
			responseObjectsMap.put("checkInVO", checkInVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "CheckInOutReport information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	//ApprovalAll request
	
	@PutMapping("/createUnifiedApprovalAllTypes")
	public ResponseEntity<ResponseDTO> createUnifiedApprovalAllTypes(
	        @RequestParam Long orgId,
	        @RequestParam String action,
	        @RequestParam String actionBy,
	        @RequestParam String notifyCode,
	        @RequestParam String notify) {

	    String methodName = "createUnifiedApprovalAllTypes()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        String message = leaveProcessService.createUnifiedApprovalAllTypes(orgId, action, actionBy, notifyCode, notify);

	        responseObjectsMap.put("message", message);
	        responseDTO = createServiceResponse(responseObjectsMap);

	    } catch (Exception e) {
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, e.getMessage());
	        responseDTO = createServiceResponseError(responseObjectsMap, e.getMessage(), e.getMessage());
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	//TicketRequest
	
	@PutMapping("/createUpdateTravelRequest")
	public ResponseEntity<ResponseDTO> createUpdateTravelRequest(@Valid @RequestBody TravelRequestDTO travelRequestDTO) {
		String methodName = "createUpdateTravelRequest()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> travelRequestVO = leaveProcessService.createUpdateTravelRequest(travelRequestDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, travelRequestVO.get("message"));
			responseObjectsMap.put("travelRequestVO", travelRequestVO.get("travelRequestVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("getTravelRequestById")
	public ResponseEntity<ResponseDTO> getTravelRequestById(@RequestParam Long id) {
		String methodName = "getTravelRequestById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		TravelRequestVO travelRequestVO = null;
		try {
			travelRequestVO = leaveProcessService.getTravelRequestById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TravelRequest found by ID");
			responseObjectsMap.put("travelRequestVO", travelRequestVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "TravelRequest not found for ID: " + id;
			responseDTO = createServiceResponseError(responseObjectsMap, "TravelRequest not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("getTravelRequestByOrgId")
	public ResponseEntity<ResponseDTO> getTravelRequestByOrgId(@RequestParam Long orgId) {
		String methodName = "getTravelRequestByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<TravelRequestVO> travelRequestVO = null;
		try {
			travelRequestVO = leaveProcessService.getTravelRequestByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TravelRequest found by ORGID");
			responseObjectsMap.put("travelRequestVO", travelRequestVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "TravelRequest not found for orgID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "TravelRequest not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	//WORKFROMHOME
		
	@PutMapping("/createUpdateWorkFromHome")
	public ResponseEntity<ResponseDTO> createUpdateWorkFromHome(@Valid @RequestBody WorkFromHomeDTO workFromHomeDTO) {
		String methodName = "createUpdateWorkFromHome()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> workFromHomeVO = leaveProcessService.createUpdateWorkFromHome(workFromHomeDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, workFromHomeVO.get("message"));
			responseObjectsMap.put("workFromHomeVO", workFromHomeVO.get("workFromHomeVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("getWorkFromHomeById")
	public ResponseEntity<ResponseDTO> getWorkFromHomeById(@RequestParam Long id) {
		String methodName = "getWorkFromHomeById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		WorkFromHomeVO workFromHomeVO = null;
		try {
			workFromHomeVO = leaveProcessService.getWorkFromHomeById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "WorkFromHome found by ID");
			responseObjectsMap.put("workFromHomeVO", workFromHomeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "WorkFromHome not found for ID: " + id;
			responseDTO = createServiceResponseError(responseObjectsMap, "WorkFromHome not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("getWorkFromHomeByOrgId")
	public ResponseEntity<ResponseDTO> getWorkFromHomeByOrgId(@RequestParam Long orgId) {
		String methodName = "getWorkFromHomeByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<WorkFromHomeVO> workFromHomeVO = null;
		try {
			workFromHomeVO = leaveProcessService.getWorkFromHomeByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "WorkFromHome found by ORGID");
			responseObjectsMap.put("workFromHomeVO", workFromHomeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "WorkFromHome not found for orgID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "WorkFromHome not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	@PutMapping("/createApprovalTicketRequest")
	public ResponseEntity<ResponseDTO> createApprovalTravelRequest(@RequestParam Long orgId, 
	        @RequestParam String employeeCode, @RequestParam String action, @RequestParam String actionBy,@RequestParam Long id,@RequestParam String notifyCode, @RequestParam String notify, @RequestParam String screenName) {

	    String methodName = "createApprovalTravelRequest()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        

	          Map<String, Object> travelRequestVO = leaveProcessService.createApprovalTravelRequest(
	                  orgId, employeeCode, action, actionBy, id, notifyCode, notify,screenName);

	          // ✅ FIX: Unwrap to avoid double nesting
	          Object checkinData = travelRequestVO.get("travelRequestVO");
	          String message = (String) travelRequestVO.getOrDefault("message", "Travel Request approved successfully.");

	          responseObjectsMap.put("travelRequestVO", travelRequestVO);
	          responseObjectsMap.put("message", message);

	          responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@PutMapping("/createApprovalWorkFromHome")
	public ResponseEntity<ResponseDTO> createApprovalWorkFromHome(@RequestParam Long orgId, 
	        @RequestParam String employeeCode, @RequestParam String action, @RequestParam String actionBy,@RequestParam Long id,@RequestParam String notifyCode, @RequestParam String notify, @RequestParam String screenName) {

	    String methodName = "createApprovalWorkFromHome()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        

	          Map<String, Object> workFromHomeVO = leaveProcessService.createApprovalWorkFromHome(
	                  orgId, employeeCode, action, actionBy, id, notifyCode, notify,screenName);

	          // ✅ FIX: Unwrap to avoid double nesting
	          Object workFromHome = workFromHomeVO.get("workFromHomeVO");
	          String message = (String) workFromHomeVO.getOrDefault("message", "WorkFromHome approved successfully.");

	          responseObjectsMap.put("workFromHomeVO", workFromHomeVO);
	          responseObjectsMap.put("message", message);

	          responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getPendingWorkFromHomeForDashBoard")
	public ResponseEntity<ResponseDTO> getPendingWorkFromHomeForDashBoard(@RequestParam Long orgId,
			@RequestParam String reportingPersonCode, @RequestParam String branchCode) {

		String methodName = "getLeaveRequestForDashBoard()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> workFromHomeVO;

		try {
			workFromHomeVO = leaveProcessService.getPendingWorkFromHomeForDashBoard(orgId, reportingPersonCode, branchCode);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "WorkFromHome details retrieved successfully");
			responseObjectsMap.put("workFromHomeVO", workFromHomeVO); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve WorkFromHome details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	@GetMapping("/getPendingTravelRequestForDashBoard")
	public ResponseEntity<ResponseDTO> getPendingTravelRequestForDashBoard(@RequestParam Long orgId,
			@RequestParam String reportingPersonCode, @RequestParam String branchCode) {

		String methodName = "getPendingTravelRequestForDashBoard()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> travelRequestVO;

		try {
			travelRequestVO = leaveProcessService.getPendingTravelRequestForDashBoard(orgId, reportingPersonCode, branchCode);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TravelRequest details retrieved successfully");
			responseObjectsMap.put("travelRequestVO", travelRequestVO); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve TravelRequest details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
}
