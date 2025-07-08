package com.efit.hrms.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.TimeSheetDTO;
import com.efit.hrms.entity.TimeSheetVO;
import com.efit.hrms.service.TimeSheetService;

@CrossOrigin
@RestController
@RequestMapping("/api/timesheet")
public class TimeSheetController extends BaseController {

	@Autowired
	TimeSheetService timeSheetService;

	public static final Logger LOGGER = LoggerFactory.getLogger(TimeSheetController.class);

	
	//TIMESHEET
	
			@PutMapping("/createUpdateTimeSheet")
			public ResponseEntity<ResponseDTO> createUpdateTimeSheet(@Valid @RequestBody TimeSheetDTO timeSheetDTO) {
				String methodName = "createUpdateTimeSheet()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				try {
					Map<String, Object> timeSheetVO = timeSheetService.createUpdateTimeSheet(timeSheetDTO);
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, timeSheetVO.get("message"));
					responseObjectsMap.put("timeSheetVO", timeSheetVO.get("timeSheetVO"));
					responseDTO = createServiceResponse(responseObjectsMap);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
					responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
		 
			@GetMapping("/getTimeSheetByOrgId")
			public ResponseEntity<ResponseDTO> getTimeSheetByOrgId(@RequestParam Long orgId,@RequestParam (required = false) String empCode,
					@RequestParam (required = false) String date) {
				String methodName = "getTimeSheetByOrgId()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<TimeSheetVO> timeSheetVO = null;
				try {
					timeSheetVO = timeSheetService.getTimeSheetByOrgId(orgId,empCode,date);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isEmpty(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TimeSheet found by ORGID");
					responseObjectsMap.put("timeSheetVO", timeSheetVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					errorMsg = "TimeSheet not found for orgID: " + orgId;
					responseDTO = createServiceResponseError(responseObjectsMap, "TimeSheet not found", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
			
			@GetMapping("/getTimeSheetById")
			public ResponseEntity<ResponseDTO> getTimeSheetById(@RequestParam Long id) {
				String methodName = "getTimeSheetById()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				TimeSheetVO timeSheetVO = null;
				try {
					timeSheetVO = timeSheetService.getTimeSheetById(id);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isEmpty(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TimeSheet found by ID");
					responseObjectsMap.put("timeSheetVO", timeSheetVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					errorMsg = "TimeSheet not found for ID: " + id;
					responseDTO = createServiceResponseError(responseObjectsMap, "TimeSheet not found", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
			
			@GetMapping("/getApprovedLeaveForTimeSheet")
			public ResponseEntity<ResponseDTO> getApprovedLeaveForTimeSheet(
			    @RequestParam Long orgId,
			    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			    @RequestParam String employeeCode) {

			    String methodName = "getApprovedLeaveForTimeSheet()";
			    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

			    Map<String, Object> responseObjectsMap = new HashMap<>();
			    ResponseDTO responseDTO;
			    List<Map<String, Object>> timeSheetVO;

			    try {
			        // Fetch timesheet data
			        timeSheetVO = timeSheetService.getApprovedLeaveForTimeSheet(orgId, date, employeeCode);

			        // Put the timesheet data in the response
			        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TimeSheet details retrieved successfully");
			        responseObjectsMap.put("timeSheetVO", timeSheetVO); // Include the timesheet data directly

			        responseDTO = createServiceResponse(responseObjectsMap);

			    } catch (Exception e) {
			        String errorMsg = e.getMessage();
			        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve TimeSheet details", errorMsg);
			    }

			    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			    return ResponseEntity.ok().body(responseDTO);
			}
			
			@GetMapping("/getApprovedLeaveForTimeSheetReport")
			public ResponseEntity<ResponseDTO> getApprovedLeaveForTimeSheetReport(
			    @RequestParam Long orgId,
			    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
			    @RequestParam String branchCode,
			    @RequestParam String employeeCode) {

			    String methodName = "getApprovedLeaveForTimeSheetReport()";
			    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

			    Map<String, Object> responseObjectsMap = new HashMap<>();
			    ResponseDTO responseDTO;
			    List<Map<String, Object>> timeSheetVO;

			    try {
			        // Fetch timesheet data
			        timeSheetVO = timeSheetService.getApprovedLeaveForTimeSheetReport(orgId, fromDate,toDate, branchCode,employeeCode);

			        // Put the timesheet data in the response
			        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TimeSheet details retrieved successfully");
			        responseObjectsMap.put("timeSheetVO", timeSheetVO); // Include the timesheet data directly

			        responseDTO = createServiceResponse(responseObjectsMap);

			    } catch (Exception e) {
			        String errorMsg = e.getMessage();
			        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve TimeSheet details", errorMsg);
			    }

			    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			    return ResponseEntity.ok().body(responseDTO);
			}
			
			
			@GetMapping("/getHolidaysForTimeSheetReport")
			public ResponseEntity<ResponseDTO> getHolidaysForTimeSheetReport(
			    @RequestParam Long orgId,
			    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
			    @RequestParam String branchCode) {

			    String methodName = "getHolidaysForTimeSheetReport()";
			    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

			    Map<String, Object> responseObjectsMap = new HashMap<>();
			    ResponseDTO responseDTO;
			    List<Map<String, Object>> timeSheetVO;

			    try {
			        // Fetch timesheet data
			        timeSheetVO = timeSheetService.getHolidaysForTimeSheetReport(orgId, fromDate,toDate, branchCode);

			        // Put the timesheet data in the response
			        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TimeSheet Holidays details retrieved successfully");
			        responseObjectsMap.put("timeSheetVO", timeSheetVO); // Include the timesheet data directly

			        responseDTO = createServiceResponse(responseObjectsMap);

			    } catch (Exception e) {
			        String errorMsg = e.getMessage();
			        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve TimeSheet Holidays details", errorMsg);
			    }

			    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			    return ResponseEntity.ok().body(responseDTO);
			}
			
			
			
			
			@GetMapping("/getTimeSheetDescByOrgId")
			public ResponseEntity<ResponseDTO> getTimeSheetDescByOrgId(@RequestParam Long orgId,@RequestParam String empCode,@RequestParam String branchCode,@RequestParam String fromDate,@RequestParam String toDate) {
				String methodName = "getTimeSheetDescByOrgId()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<TimeSheetVO> timeSheetVO = null;
				try {
					timeSheetVO = timeSheetService.getTimeSheetDescByOrgId(orgId,empCode,branchCode,fromDate,toDate);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isEmpty(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TimeSheet found by ORGID");
					responseObjectsMap.put("timeSheetVO", timeSheetVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					errorMsg = "TimeSheet not found for orgID: " + orgId;
					responseDTO = createServiceResponseError(responseObjectsMap, "TimeSheet not found", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
}
