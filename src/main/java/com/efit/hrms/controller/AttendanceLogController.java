package com.efit.hrms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.DepartmentResponse;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.entity.AttendanceLogVO;
import com.efit.hrms.service.AttendanceLogService;


@RestController
@RequestMapping("/api/AttendanceLogController")
public class AttendanceLogController extends BaseController {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AttendanceLogController.class);
	
	@Autowired
	AttendanceLogService attendanceLogService; 
	
	
	@GetMapping("/getAttendance")
	public ResponseEntity<ResponseDTO> getAttendance(@RequestParam String startDate,@RequestParam String endDate) {
		String methodName = "getAttendance()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AttendanceLogVO> attendanceLogVO = new ArrayList<>();
		try {
			attendanceLogVO = attendanceLogService.getAllAttendanceLogDetails(startDate, endDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Attendance information get successfully");
			responseObjectsMap.put("attendanceLogVO", attendanceLogVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Attendance information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getEmployeeAttendanceDashboard")
	public ResponseEntity<ResponseDTO> getEmployeeAttendanceDashboard(@RequestParam String date,@RequestParam String department,@RequestParam String employeeType,@RequestParam String status,@RequestParam String missPunch) {
		String methodName = "getEmployeeAttendanceDashboard()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> attendanceLogVO = new ArrayList<>();
		try {
			attendanceLogVO = attendanceLogService.getEmployeeAttendanceDetails(date, department, employeeType, status, missPunch);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Attendance information get successfully");
			responseObjectsMap.put("attendanceLogVO", attendanceLogVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Attendance information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	
	
	@GetMapping("/dashboard")
	public ResponseEntity<ResponseDTO> getDashboard(@RequestParam String date,
            @RequestParam(defaultValue = "Employee") String empType) {
		String methodName = "dashboard()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Map<String, DepartmentResponse> dashBoard= new HashMap<>();
		try {
			dashBoard = attendanceLogService.getDashboard(date, empType);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "dashBoard information get successfully");
			responseObjectsMap.put("dashBoard", dashBoard);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "dashBoard information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

}
