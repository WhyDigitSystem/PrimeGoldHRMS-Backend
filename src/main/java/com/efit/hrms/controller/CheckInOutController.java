package com.efit.hrms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.AttendanceSummaryDTO;
import com.efit.hrms.dto.CheckInOutBiometricDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.entity.AttendanceDailyVO;
import com.efit.hrms.entity.AttendanceSummaryVO;
import com.efit.hrms.entity.OtCalculationVO;
import com.efit.hrms.service.CheckInOutService;


@CrossOrigin
@RestController
@RequestMapping("/api/checkinout")
public class CheckInOutController extends BaseController{

	@Autowired
	CheckInOutService checkInOutService;

	public static final Logger LOGGER = LoggerFactory.getLogger(CheckInOutController.class);
	
	
	@PutMapping("/createCheckInOutBiometric")
	public ResponseEntity<ResponseDTO> createCheckInOutBiometric(@RequestBody CheckInOutBiometricDTO checkInOutBiometricDTO) {
		String methodName = "createCheckInOutBiometric()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> checkInBiometricVO = checkInOutService.createCheckInOutBiometric(checkInOutBiometricDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, checkInBiometricVO.get("message"));
			responseObjectsMap.put("checkInBiometricVO", checkInBiometricVO.get("checkInBiometricVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	

	    @PostMapping("/checkInOutUploadExcel")
	    public ResponseEntity<String> checkInOutUploadExcel(@RequestParam("files") MultipartFile file,@RequestParam Long orgId,@RequestParam String createdBy) {
	        if (file.isEmpty()) {
	            return ResponseEntity.badRequest().body("File is empty.");
	        }

	        try {
	            String message = checkInOutService.checkInOutUploadExcel(file,orgId,createdBy);
	            return ResponseEntity.ok(message);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Upload failed: " + e.getMessage());
	        }
	    }
	

	    @GetMapping("/getLeaveDetailsForAttendanceProcess")
		public ResponseEntity<ResponseDTO> getLeaveDetailsForAttendanceProcess(@RequestParam String fromDate,
				@RequestParam String toDate, @RequestParam Long orgId,@RequestParam String department,@RequestParam String branch,@RequestParam String type,@RequestParam(required=false) String contractor) {

			String methodName = "getLeaveDetailsForLeaveProcess()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO;
			List<Map<String, Object>> leaveDetailsList;

			try {
				leaveDetailsList = checkInOutService.getLeaveDetailsForAttendanceProcess(fromDate, toDate, orgId,department,branch,type,contractor);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "AttendanceProcess details retrieved successfully");
				responseObjectsMap.put("attendanceProcessVO", leaveDetailsList); // ✅ Correct key name
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				String errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve AttendanceProcess details", errorMsg);
			}

			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

	
	
	    @PostMapping("/calculate-ot/{orgId}")
	    public ResponseEntity<List<OtCalculationVO>> calculateAndSave(@PathVariable Long orgId) {
	        List<OtCalculationVO> result = checkInOutService.generateOtAndSave(orgId);
	        return ResponseEntity.ok(result);
	    }
	   
	    
	    @GetMapping("getPendingOTHoursByOrgId")
	 		public ResponseEntity<ResponseDTO> getPendingOTHoursByOrgId(@RequestParam String fromDate,@RequestParam  String toDate,@RequestParam Long orgId,@RequestParam String employeeCode,@RequestParam String branch,@RequestParam String department,@RequestParam String type,@RequestParam (required = false) String contractor) {
	 			String methodName = "getAttendanceProcessByOrgId()";
	 			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	 			String errorMsg = null;
	 			Map<String, Object> responseObjectsMap = new HashMap<>();
	 			ResponseDTO responseDTO = null;
	 			List<OtCalculationVO> otCalculationVO = null;
	 			try {
	 				otCalculationVO = checkInOutService.getPendingOTHoursByOrgId(fromDate,   toDate,  orgId,  employeeCode,  branch,department,type,contractor);
	 			} catch (Exception e) {
	 				errorMsg = e.getMessage();
	 				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	 			}
	 			if (StringUtils.isEmpty(errorMsg)) {
	 				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "OtCalculation found by ORGID");
	 				responseObjectsMap.put("otCalculationVO", otCalculationVO);
	 				responseDTO = createServiceResponse(responseObjectsMap);
	 			} else {
	 				errorMsg = "OtCalculation not found for orgID: " + orgId;
	 				responseDTO = createServiceResponseError(responseObjectsMap, "OtCalculation not found", errorMsg);
	 			}
	 			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	 			return ResponseEntity.ok().body(responseDTO);
	 		}
	    
	    @GetMapping("getApprovedOTHoursByOrgId")
 		public ResponseEntity<ResponseDTO> getApprovedOTHoursByOrgId(@RequestParam String fromDate,@RequestParam  String toDate,@RequestParam Long orgId,@RequestParam String employeeCode,@RequestParam String branch,@RequestParam String department,@RequestParam String type,@RequestParam (required = false) String contractor) {
 			String methodName = "getAttendanceProcessByOrgId()";
 			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
 			String errorMsg = null;
 			Map<String, Object> responseObjectsMap = new HashMap<>();
 			ResponseDTO responseDTO = null;
 			List<OtCalculationVO> otCalculationVO = null;
 			try {
 				otCalculationVO = checkInOutService.getApprovedOTHoursByOrgId(fromDate,   toDate,  orgId,  employeeCode,  branch,department,type,contractor);
 			} catch (Exception e) {
 				errorMsg = e.getMessage();
 				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
 			}
 			if (StringUtils.isEmpty(errorMsg)) {
 				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "OtCalculation found by ORGID");
 				responseObjectsMap.put("otCalculationVO", otCalculationVO);
 				responseDTO = createServiceResponse(responseObjectsMap);
 			} else {
 				errorMsg = "OtCalculation not found for orgID: " + orgId;
 				responseDTO = createServiceResponseError(responseObjectsMap, "OtCalculation not found", errorMsg);
 			}
 			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
 			return ResponseEntity.ok().body(responseDTO);
 		}

	    //monthlyprocess
	    
	    @GetMapping("/getMonthlyProcess")
		public ResponseEntity<ResponseDTO> getMonthlyProcess( @RequestParam int month,
		        @RequestParam int year,
		        @RequestParam Long orgId,
		        @RequestParam String branch,
		        @RequestParam String department,
		        @RequestParam String type,@RequestParam (required = false)String contractor) {
			String methodName = "getMonthlyProcess()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<Map<String, Object>> mapp = new ArrayList<>();

			try {
				mapp = checkInOutService.getMonthlyProcess(month, year, orgId, branch, department,type,contractor);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}

			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "MonthlyProcess retrieved successfully");
				responseObjectsMap.put("monthlyProcess", mapp);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "MonthlyProcess to retrieve Charge Type", errorMsg);
			}

			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
	    
	    
	    @GetMapping("getAttendanceDailyByOrgId")
		public ResponseEntity<ResponseDTO> getAttendanceDailyByOrgId(@RequestParam String fromDate,@RequestParam  String toDate,@RequestParam Long orgId,@RequestParam String employeeCode,@RequestParam String branch) {
			String methodName = "getAttendanceProcessByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<AttendanceDailyVO> attendanceDailyVO = null;
			try {
				attendanceDailyVO = checkInOutService.getAttendanceDailyByOrgId(fromDate,   toDate,  orgId,  employeeCode,  branch);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "AttendanceDaily found by ORGID");
				responseObjectsMap.put("attendanceDailyVO", attendanceDailyVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "AttendanceDaily not found for orgID: " + orgId;
				responseDTO = createServiceResponseError(responseObjectsMap, "AttendanceDaily not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

	    //AttendanceSummary
		@PutMapping("/createUpdateAttendanceSummary")
		public ResponseEntity<ResponseDTO> createUpdateAttendanceSummary(
				@Valid @RequestBody List<AttendanceSummaryDTO> attendanceSummaryDTO) {
			String methodName = "createUpdateCompOff()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> attendanceSummaryVO = checkInOutService.createUpdateAttendanceSummary(attendanceSummaryDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, attendanceSummaryVO.get("message"));
				responseObjectsMap.put("attendanceSummaryVO", attendanceSummaryVO.get("attendanceSummaryVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@PutMapping("/createApprovalAttendanceSummary")
		public ResponseEntity<ResponseDTO> createApprovalAttendanceSummary(@RequestParam Long orgId, @RequestParam List<Long> id,@RequestParam String action, @RequestParam String actionBy) {
			String methodName = "createApprovalAttendanceSummary()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> attendanceSummaryVO = checkInOutService.createApprovalAttendanceSummary(
		                orgId, id, action, actionBy);

		        // ✅ Unwrap values
		        Object attendanceSummary = attendanceSummaryVO.get("attendanceSummaryVO");
		        String message = (String) attendanceSummaryVO.getOrDefault("message", "AttendanceSummary Approved Successfully");

		        responseObjectsMap.put("attendanceSummaryVO", attendanceSummary);
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
		
		 @GetMapping("getPendingAttendanceSummaryByOrgId")
			public ResponseEntity<ResponseDTO> getPendingAttendanceSummaryByOrgId(@RequestParam Long orgId,@RequestParam String branch) {
				String methodName = "getPendingAttendanceSummaryByOrgId()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<AttendanceSummaryVO> attendanceSummaryVO = null;
				try {
					attendanceSummaryVO = checkInOutService.getPendingAttendanceSummaryByOrgId(  orgId,   branch);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isEmpty(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Pending AttendanceSummary found by ORGID");
					responseObjectsMap.put("attendanceSummaryVO", attendanceSummaryVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					errorMsg = "AttendanceDaily not found for orgID: " + orgId;
					responseDTO = createServiceResponseError(responseObjectsMap, "Pending AttendanceSummary not found", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
		 
		 
		 @GetMapping("getApprovedAttendanceSummaryByOrgId")
		 public ResponseEntity<ResponseDTO> getAttendanceSummaryByOrgId(
		     @RequestParam String empCode,
		     @RequestParam(required = false) Integer month,
		     @RequestParam(required = false) String finYear,
		     @RequestParam Long orgId,
		     @RequestParam String branch) {
			 String methodName = "getAttendanceSummaryByOrgId()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<AttendanceSummaryVO> attendanceSummaryVO = null;
				try {
					attendanceSummaryVO = checkInOutService.getAttendanceSummaryByOrgId(  empCode, month, finYear, orgId,  branch);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isEmpty(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Approved AttendanceSummary found by ORGID");
					responseObjectsMap.put("attendanceSummaryVO", attendanceSummaryVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					errorMsg = "AttendanceDaily not found for orgID: " + orgId;
					responseDTO = createServiceResponseError(responseObjectsMap, "Approved AttendanceSummary not found", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}


}
