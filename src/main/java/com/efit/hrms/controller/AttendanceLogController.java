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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.entity.AttendanceLogVO;
import com.efit.hrms.service.AttendanceLogService;


@RestController
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
	
	@GetMapping("/api/WebAPI/GetAttendanceInOutProcessedET")
	public List<AttendanceLogVO> getAttendance1(@RequestParam String StartDate,@RequestParam String EndDate) {
		String methodName = "getAttendance1()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AttendanceLogVO> attendanceLogVO = new ArrayList<>();
		try {
			attendanceLogVO = attendanceLogService.getAllAttendanceLogDetails1(StartDate, EndDate);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		
		return attendanceLogVO;
	}

}
