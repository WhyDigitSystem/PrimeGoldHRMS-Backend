package com.efit.hrms.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.efit.hrms.dto.AnnouncementDTO;
import com.efit.hrms.dto.CalendarDTO;
import com.efit.hrms.dto.CheckInOutAdjustmentDTO;
import com.efit.hrms.dto.CheckinRequestDTO;
import com.efit.hrms.dto.CircularDTO;
import com.efit.hrms.dto.EmployeeCodeConfigDTO;
import com.efit.hrms.dto.EmployeeDTOnew;
import com.efit.hrms.dto.HolidayDTO;
import com.efit.hrms.dto.PollVoteDTO;
import com.efit.hrms.dto.PollsDTO;
import com.efit.hrms.dto.PraiseDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.TaskDTO;
import com.efit.hrms.dto.UserNameDTO;
import com.efit.hrms.entity.AnnouncementVO;
import com.efit.hrms.entity.CalendarVO;
import com.efit.hrms.entity.CheckInOutAdjustmentVO;
import com.efit.hrms.entity.CheckInVO;
import com.efit.hrms.entity.CircularVO;
import com.efit.hrms.entity.CompanyVO;
import com.efit.hrms.entity.EmployeeCodeConfigVO;
import com.efit.hrms.entity.HolidayVO;
import com.efit.hrms.entity.PollsVO;
import com.efit.hrms.service.BasicMasterService;

@CrossOrigin
@RestController
@RequestMapping("/api/basicmaster")
public class BasicMasterController extends BaseController {

	@Autowired
	BasicMasterService basicMasterService;

	public static final Logger LOGGER = LoggerFactory.getLogger(BasicMasterController.class);

	@PutMapping("/createCheckInOut")
	public ResponseEntity<ResponseDTO> createCheckInOut(@RequestBody UserNameDTO userNameDTO) {
		String methodName = "createCheckInOut()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> checkInVO = basicMasterService.createCheckInOut(userNameDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, checkInVO.get("message"));
			responseObjectsMap.put("checkInVO", checkInVO.get("checkInVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// RequestCheckOut

	// ApprovedCheckout
	@PutMapping("/createRequestCheckOut")
	public ResponseEntity<ResponseDTO> createRequestCheckOut(@RequestBody CheckinRequestDTO checkinRequestDTO) {

		String methodName = "createRequestCheckOut()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			// Convert checkOutDate from String to LocalDate
			Map<String, Object> checkInVO = basicMasterService.createRequestCheckOut(checkinRequestDTO);

			// ✅ FIX: unwrap nested data
			Object checkinData = checkInVO.get("checkInVO");
			String message = (String) checkInVO.getOrDefault("message", "Check-out request submitted successfully.");

			responseObjectsMap.put("checkInVO", checkinData);
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

	@GetMapping("/getRequestCheckOutByOrgId")
	public ResponseEntity<ResponseDTO> getRequestCheckOutByOrgId(@RequestParam Long orgId, @RequestParam String branch,
			@RequestParam String reportingPersoncode) {
		String methodName = "getRequestCheckOutByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> checkInVO = null;
		try {
			checkInVO = basicMasterService.getRequestCheckOutByOrgId(orgId, branch, reportingPersoncode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "checkOut found by ORGID");
			responseObjectsMap.put("checkInVO", checkInVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "checkIn not found for orgID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "checkOut not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// ApprovedCheckout
	@PutMapping("/createApprovalCheckOut")
	public ResponseEntity<ResponseDTO> createApprovalCheckOut(@RequestParam Long orgId,
			@RequestParam String employeeCode, @RequestParam String action, @RequestParam String actionBy,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String checkOutDate,
			@RequestParam String notifyCode, @RequestParam String notify, @RequestParam String screenName) {

		String methodName = "createApprovalCheckOut()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			// Convert checkOutDate from String to LocalDate
			LocalDate localCheckOutDate = LocalDate.parse(checkOutDate);

			Map<String, Object> checkInVO = basicMasterService.createApprovalCheckOut(orgId, employeeCode, action,
					actionBy, localCheckOutDate, notifyCode, notify, screenName);

			// ✅ FIX: Unwrap to avoid double nesting
			Object checkinData = checkInVO.get("checkInVO");
			String message = (String) checkInVO.getOrDefault("message", "Check-out approved successfully.");

			responseObjectsMap.put("checkInVO", checkinData);
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

	// requestcheckinoutadjustment

	@PutMapping("/createCheckInOutAdjustment")
	public ResponseEntity<ResponseDTO> createCheckInOutAdjustment(
			@RequestBody CheckInOutAdjustmentDTO checkInOutAdjustmentDTO) {

		
		String methodName = "createCheckInOutAdjustment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		

		try {
			// Convert checkOutDate from String to LocalDate
			Map<String, Object> result = basicMasterService.createCheckInOutAdjustment(checkInOutAdjustmentDTO);

			// ✅ Extract values from the result
			List<CheckInOutAdjustmentVO> entries = (List<CheckInOutAdjustmentVO>) result.get("entries");
			String message = (String) result.getOrDefault("message", "Check-in/out created successfully.");

			// ✅ Set flat response
			responseObjectsMap.put("checkInOutAdjustmentVO", entries);
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

	// approval adjustment checkinout

	@PutMapping("/createApprovalCheckInOutAdjustment")
	public ResponseEntity<ResponseDTO> createApprovalCheckInOutAdjustment(@RequestParam Long orgId,
			@RequestParam String employeeCode, @RequestParam String action, @RequestParam String actionBy,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String checkOutDate,
			@RequestParam String notifyCode, @RequestParam String notify, @RequestParam String screenName) {

		String methodName = "createApprovalCheckInOutAdjustment()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;

		try {
			// Convert checkOutDate from String to LocalDate
			LocalDate localCheckOutDate = LocalDate.parse(checkOutDate);

			// Call the service
			Map<String, Object> result = basicMasterService.createApprovalCheckInOutAdjustment(orgId, employeeCode,
					action, actionBy, localCheckOutDate, notifyCode, notify, screenName);

			// ✅ Extract and flatten
			List<CheckInOutAdjustmentVO> adjustmentList = (List<CheckInOutAdjustmentVO>) result
					.get("checkInOutAdjustmentList");
			String message = (String) result.getOrDefault("message", "Status updated successfully.");

			// ✅ Set response
			responseObjectsMap.put("checkInOutAdjustmentVO", adjustmentList);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, message);

			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getRequestCheckInOutByOrgId")
	public ResponseEntity<ResponseDTO> getRequestCheckInOutByOrgId(@RequestParam Long orgId,
			@RequestParam String branch, @RequestParam String reportingPersoncode) {
		String methodName = "getRequestCheckOutByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> checkInOutAdjustmentVO = null;
		try {
			checkInOutAdjustmentVO = basicMasterService.getRequestCheckInOutByOrgId(orgId, branch, reportingPersoncode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "checkInOut found by ORGID");
			responseObjectsMap.put("checkInOutAdjustmentVO", checkInOutAdjustmentVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "checkIn not found for orgID: " + orgId;
			responseDTO = createServiceResponseError(responseObjectsMap, "checkInOut not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// holiday

	@PutMapping("/createUpdateHolidays")
	public ResponseEntity<ResponseDTO> createUpdateHolidays(@RequestBody HolidayDTO holidayDTO) {
		String methodName = "createUpdateHolidays()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> holidayVO = basicMasterService.createUpdateHolidays(holidayDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, holidayVO.get("message"));
			responseObjectsMap.put("holidayVO", holidayVO.get("holidayVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllHolidayByOrgId")
	public ResponseEntity<ResponseDTO> getAllHolidayByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllHolidayByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<HolidayVO> holidayVO = new ArrayList<>();
		try {
			holidayVO = basicMasterService.getAllHolidayByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Holiday information get successfully By OrgId");
			responseObjectsMap.put("holidayVO", holidayVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Holiday information receive failed By OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getHolidayById")
	public ResponseEntity<ResponseDTO> getHolidayById(@RequestParam Long id) {
		String methodName = "getHolidayById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		HolidayVO holidayVO = new HolidayVO();
		try {
			holidayVO = basicMasterService.getHolidayById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Holiday information get successfully By id");
			responseObjectsMap.put("holidayVO", holidayVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Holiday information receive failedById",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/uploadHolidayImageInBloob")
	public ResponseEntity<ResponseDTO> uploadHolidayImageInBloob(@RequestParam("file") MultipartFile file,
			@RequestParam Long id) {
		String methodName = "uploadHolidayImageInBloob()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		HolidayVO holidayVO = null;
		try {
			holidayVO = basicMasterService.uploadHolidayImageInBloob(file, id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error("Unable To Upload holidayImage", methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "HolidayImage Successfully Upload");
			responseObjectsMap.put("holidayVO", holidayVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "HolidayImage Upload Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// checkstatus

	@GetMapping("/chkStatus/{empcode}")
	public ResponseEntity<ResponseDTO> getStatusByEmpcode(@PathVariable String empcode) {
		String methodName = "getStatusByEmpcode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;

		try {
			List<Map<String, Object>> result = basicMasterService.getStatusByEmpcode(empcode);

			if (!result.isEmpty()) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee Status retrieved successfully");
				responseObjectsMap.put("EmployeeStatus", result.get(0)); // single record
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				String errorMsg = "Employee not found for ID: " + empcode;
				responseDTO = createServiceResponseError(responseObjectsMap, "Employee Status not found", errorMsg);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Employee Status",
					errorMsg);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/attendance")
	public ResponseEntity<ResponseDTO> getAttendanceByEmpcode(@RequestParam String empcode, @RequestParam String month,
			@RequestParam String orgId,  @RequestParam String branchCode) {

		String methodName = "getAttendanceByEmpcode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			// Convert month to integer (handles "05" or "5")
			Integer monthInt = Integer.parseInt(month);

			List<Map<String, Object>> attendanceList = basicMasterService.getAttendanceByEmpcode(empcode, monthInt,
					orgId, branchCode);

			if (attendanceList.isEmpty()) {
				errorMsg = "No attendance data found for empcode: " + empcode;
				responseDTO = createServiceResponseError(responseObjectsMap, "Attendance not found", errorMsg);
			} else {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Attendance found for empcode");
				responseObjectsMap.put("Attendance", attendanceList);
				responseDTO = createServiceResponse(responseObjectsMap);
			}

		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Error fetching attendance", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/excelUploadForHolidays")
	public ResponseEntity<ResponseDTO> excelUploadForChargeCode(@RequestParam MultipartFile[] files,
			@RequestParam(required = false) String createdBy, @RequestParam Long orgId) {
		String methodName = "excelUploadForHolidays()";
		int totalRows = 0;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		int successfulUploads = 0;
		ResponseDTO responseDTO = null;
		try {
			// Call service method to process Excel upload
			basicMasterService.excelUploadForHolidays(files, createdBy, orgId);
			// Retrieve the counts after processing
			totalRows = basicMasterService.getTotalRows(); // Get total rows processed
			successfulUploads = basicMasterService.getSuccessfulUploads(); // Get successful uploads count
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			responseObjectsMap.put("message", "Excel Upload For Holidays successful"); // Directly include the
																						// message here
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For Holidays Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
/// circular

	@PutMapping("/createUpdateCircular")
	public ResponseEntity<ResponseDTO> createUpdateCircular(@RequestBody CircularDTO circularDTO) {
		String methodName = "createUpdateCircular()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> circularVO = basicMasterService.createUpdatecircular(circularDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, circularVO.get("message"));
			responseObjectsMap.put("circularVO", circularVO.get("circularVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllCircularByOrgId")
	public ResponseEntity<ResponseDTO> getAllCircularByOrgId(@RequestParam Long orgId, @RequestParam String branchCode,
			@RequestParam(required = false) String department, @RequestParam String type) {
		String methodName = "getAllCircularByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CircularVO> circularVO = new ArrayList<>();
		try {
			circularVO = basicMasterService.getAllCircularByOrgId(orgId, branchCode, department, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Circular information get successfully By OrgId");
			responseObjectsMap.put("circularVO", circularVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Circular information receive failed By OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getCircularById")
	public ResponseEntity<ResponseDTO> getCircularById(@RequestParam Long id) {
		String methodName = "getCircularById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CircularVO circularVO = new CircularVO();
		try {
			circularVO = basicMasterService.getCircularById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Circular information get successfully By id");
			responseObjectsMap.put("circularVO", circularVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Circular information receive failedById",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// polls//

	@PutMapping("/createUpdatepolls")
	public ResponseEntity<ResponseDTO> createUpdatepolls(@RequestBody PollsDTO pollsDTO) {
		String methodName = "createUpdatepolls()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> pollsVO = basicMasterService.createUpdatepolls(pollsDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, pollsVO.get("message"));
			responseObjectsMap.put("pollsVO", pollsVO.get("pollsVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllPollsByOrgId")
	public ResponseEntity<ResponseDTO> getAllPollsByOrgId(@RequestParam Long orgId, @RequestParam String branchCode,
			@RequestParam(required = false) String department, @RequestParam String type) {
		String methodName = "getAllPollsByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PollsVO> pollsVO = new ArrayList<>();
		try {
			pollsVO = basicMasterService.getAllPollsByOrgId(orgId, branchCode, department, type);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "polls information get successfully By OrgId");
			responseObjectsMap.put("pollsVO", pollsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "polls information receive failed By OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getPollById")
	public ResponseEntity<ResponseDTO> getPollById(@RequestParam Long id) {
		String methodName = "getPollById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		PollsVO pollsVO = new PollsVO();
		try {
			pollsVO = basicMasterService.getPollById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Poll information get successfully By id");
			responseObjectsMap.put("pollsVO", pollsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Poll information receive failedById",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/createUpdatepollVote")
	public ResponseEntity<ResponseDTO> createUpdatepollVote(@RequestBody List<PollVoteDTO> pollVoteDTO) {
		String methodName = "createUpdatepollVote()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> pollVoteVO = basicMasterService.createUpdatepollVote(pollVoteDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, pollVoteVO.get("message"));
			responseObjectsMap.put("pollVoteVO", pollVoteVO.get("pollVoteVO1"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPollResultForUser")
	public ResponseEntity<ResponseDTO> getPollResultForUser(@RequestParam Long orgId, String userName, Long pollId) {
		String methodName = "getPollResultForUser()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.getPollResultForUser(orgId, userName, pollId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "User Poll Result Details retrieved successfully");
			responseObjectsMap.put("pollVoteVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"User Poll Result to retrieve Customer Details", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPollResultForHR")
	public ResponseEntity<ResponseDTO> getPollResultForHR(@RequestParam Long orgId, Long pollId) {
		String methodName = "getPollResultForHR()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.getPollResultForHR(orgId, pollId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, " Poll Result Details retrieved successfully");
			responseObjectsMap.put("pollVoteVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, " Poll Result to retrieve Customer Details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Announcement//

	@PutMapping("/createUpdateAnnouncement")
	public ResponseEntity<ResponseDTO> createUpdateAnnouncement(@RequestBody AnnouncementDTO announcementDTO) {
		String methodName = "createUpdateAnnouncement()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> announcementVO = basicMasterService.createUpdateAnnouncement(announcementDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, announcementVO.get("message"));
			responseObjectsMap.put("announcementVO", announcementVO.get("announcementVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/GetAnnouncementByOrgId")
	public ResponseEntity<ResponseDTO> GetAnnouncementByOrgId(@RequestParam Long orgId,
			@RequestParam String branchCode) {
		String methodName = "GetAnnouncementByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AnnouncementVO> announcementVO = new ArrayList<>();
		try {
			announcementVO = basicMasterService.GetAnnouncementByOrgId(orgId, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Announcement information get successfully By OrgId");
			responseObjectsMap.put("announcementVO", announcementVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Announcement information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/GetAnnouncementById")
	public ResponseEntity<ResponseDTO> GetAnnouncementById(@RequestParam Long id) {
		String methodName = "GetAnnouncementById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		AnnouncementVO announcementVO = new AnnouncementVO();
		try {
			announcementVO = basicMasterService.GetAnnouncementById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Announcement information get successfully By id");
			responseObjectsMap.put("announcementVO", announcementVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"AnnouncementVO information receive failedById", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/uploadPostImageInBloob")
	public ResponseEntity<ResponseDTO> uploadPostImageInBloob(@RequestParam("file") MultipartFile file,
			@RequestParam Long id) {
		String methodName = "uploadPostImageInBloob()";
		LOGGER.debug("Starting Method: " + methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CircularVO circularVO = null;

		try {
			circularVO = basicMasterService.uploadPostImageInBloob(file, id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error("Unable to Upload PostImage: " + errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put("message", "PostImage Successfully Uploaded");
			responseObjectsMap.put("circularVO", circularVO);
			responseDTO = createServiceResponse(responseObjectsMap); // Assuming this is your custom response method
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "PostImage Upload Failed", errorMsg);
		}

		LOGGER.debug("Ending Method: " + methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	// Praise//

	@PutMapping("/createUpdatePraise")
	public ResponseEntity<ResponseDTO> createUpdatePraise(@RequestBody PraiseDTO praiseDTO) {
		String methodName = "createUpdatePraise()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> praiseVO = basicMasterService.createUpdatePraise(praiseDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, praiseVO.get("message"));
			responseObjectsMap.put("praiseVO", praiseVO.get("praiseVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/GetCountOfPraiseByOrgIdAndCircularId")
	public ResponseEntity<ResponseDTO> GetCountOfPraiseByOrgIdAndCircularId(@RequestParam Long circularid, Long orgId) {
		String methodName = "GetCountOfPraiseByOrgIdAndCircularId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> praiseVO = new ArrayList<>();
		try {
			praiseVO = basicMasterService.GetCountOfPraiseByOrgIdAndCircularId(circularid, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Count of praise get successfully By circularId and  OrgId");
			responseObjectsMap.put("praiseVO", praiseVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Count of praise failed By circularId and  OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// Task//

	@PutMapping("/createUpdateTask")
	public ResponseEntity<ResponseDTO> createUpdateTask(@RequestBody TaskDTO taskDTO) {
		String methodName = "createUpdateTask()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> taskVO = basicMasterService.createUpdateTask(taskDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, taskVO.get("message"));
			responseObjectsMap.put("taskVO", taskVO.get("taskVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/GetNewTask")
	public ResponseEntity<ResponseDTO> GetNewTask(@RequestParam String AssignedTo, Long orgId) {
		String methodName = "GetNewTask()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> taskVO = new ArrayList<>();
		try {
			taskVO = basicMasterService.GetNewTask(AssignedTo, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "new task get successfully For user and  OrgId");
			responseObjectsMap.put("taskVO", taskVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "new task failed for user and  OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/GetPendingTask")
	public ResponseEntity<ResponseDTO> GetPendingTask(@RequestParam String AssignedTo, Long orgId) {
		String methodName = "GetPendingTask()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> taskVO = new ArrayList<>();
		try {
			taskVO = basicMasterService.GetPendingTask(AssignedTo, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Pending task get successfully For user and  OrgId");
			responseObjectsMap.put("taskVO", taskVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Pending task failed for user and  OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/GetCountofPendingTask")
	public ResponseEntity<ResponseDTO> GetCountofPendingTask(@RequestParam String AssignedTo, Long orgId) {
		String methodName = "GetCountofPendingTask()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> taskVO = new ArrayList<>();
		try {
			taskVO = basicMasterService.GetCountofPendingTask(AssignedTo, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Count of Pending task get successfully For user and  OrgId");
			responseObjectsMap.put("taskVO", taskVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Count of Pending task failed for user and  OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/GetCountofNewTask")
	public ResponseEntity<ResponseDTO> GetCountofNewTask(@RequestParam String AssignedTo, Long orgId) {
		String methodName = "GetCountofNewTask()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> taskVO = new ArrayList<>();
		try {
			taskVO = basicMasterService.GetCountofNewTask(AssignedTo, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Count of New task get successfully For user and  OrgId");
			responseObjectsMap.put("taskVO", taskVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Count of New task failed for user and  OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
//

	@GetMapping("/GetNewAssignedTask")
	public ResponseEntity<ResponseDTO> GetNewAssignedTask(@RequestParam String AssignedBy, Long orgId) {
		String methodName = "GetNewAssignedTask()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> taskVO = new ArrayList<>();
		try {
			taskVO = basicMasterService.GetNewAssignedTask(AssignedBy, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"new task get successfully For Assigned by  and  OrgId");
			responseObjectsMap.put("taskVO", taskVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "new task failed for Assigned by  and  OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/GetPendingAssignedTask")
	public ResponseEntity<ResponseDTO> GetPendingAssignedTask(@RequestParam String AssignedBy, Long orgId) {
		String methodName = "GetPendingAssignedTask()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> taskVO = new ArrayList<>();
		try {
			taskVO = basicMasterService.GetPendingAssignedTask(AssignedBy, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Pending task get successfully For Assigned by and  OrgId");
			responseObjectsMap.put("taskVO", taskVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Pending task failed for Assigned by and  OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/GetCountofPendingAssignedTask")
	public ResponseEntity<ResponseDTO> GetCountofPendingAssignedTask(@RequestParam String AssignedBy, Long orgId) {
		String methodName = "GetCountofPendingAssignedTask()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> taskVO = new ArrayList<>();
		try {
			taskVO = basicMasterService.GetCountofPendingAssignedTask(AssignedBy, orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Count of Pending task get successfully For Assinged By and  OrgId");
			responseObjectsMap.put("taskVO", taskVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Count of Pending task failed for Assinged By and  OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/GetCountofNewAssignedTask")
	public ResponseEntity<ResponseDTO> GetCountofNewAssignedTask(@RequestParam Long orgId, String AssignedBy) {
		String methodName = "GetCountofNewAssignedTask()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> taskVO = new ArrayList<>();
		try {
			taskVO = basicMasterService.GetCountofNewAssignedTask(orgId, AssignedBy);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Count of New task get successfully For user and  OrgId");
			responseObjectsMap.put("taskVO", taskVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Count of New task failed for user and  OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getEmpDob")
	public ResponseEntity<ResponseDTO> getEmpDob(@RequestParam Long orgId) {
		String methodName = "getEmpDob()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.getEmpDob(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee DOB retrieved successfully");
			responseObjectsMap.put("empDob", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Employee DOB to retrieve Charge Type",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/Getworkaniversary")
	public ResponseEntity<ResponseDTO> Getworkaniversary(@RequestParam Long orgId) {
		String methodName = "Getworkaniversary()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.GetworkAniversary(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee Work Aniversary retrieved successfully");
			responseObjectsMap.put("employee", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Employee Work Aniversary to retrieve Charge Type", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/GetnewJoineDetails")
	public ResponseEntity<ResponseDTO> GetnewJoineDetails(@RequestParam Long orgId) {
		String methodName = "GetnewJoineDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.GetnewJoineDetails(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "New Employee retrieved successfully");
			responseObjectsMap.put("employee", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "New Employee to retrieve Charge Type",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getpayslipemployeedetails")
	public ResponseEntity<ResponseDTO> getpayslipemployeedetails(@RequestParam Long orgId, String Employeecode) {
		String methodName = "getpayslipemployeedetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.getpayslipemployeedetails(orgId, Employeecode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payslip Employee retrieved successfully");
			responseObjectsMap.put("employee", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payslip Employee to retrieve Charge Type",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getpayslipPayOnHandAmount")
	public ResponseEntity<ResponseDTO> getpayslipPayOnHandAmount(@RequestParam Long orgId, String Employeecode,@RequestParam Long month,@RequestParam String year) {
		String methodName = "getpayslipPayOnHandAmount()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.getpayslipPayOnHandAmount(  orgId,  Employeecode,  month,  year);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payslip PayOnHand Details retrieved successfully");
			responseObjectsMap.put("payslip", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payslip PayOnHand Details to retrieve Charge Type",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getpayslipearningdetails")
	public ResponseEntity<ResponseDTO> getpayslipearningdetails(@RequestParam Long orgId, String Employeecode,
			Long Month, Long year) {
		String methodName = "getpayslipearningdetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.getpayslipearningdetails(orgId, Employeecode, Month, year);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payslip Earnings retrieved successfully");
			responseObjectsMap.put("employee", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payslip Earnings  retrievd unsucessful",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getpayslipdeductiondetails")
	public ResponseEntity<ResponseDTO> getpayslipdeductiondetails(@RequestParam Long orgId, String Employeecode,
			Long Month, Long year) {
		String methodName = "getpayslipdeductiondetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.getpayslipdeductiondetails(orgId, Employeecode, Month, year);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payslip deduction retrieved successfully");
			responseObjectsMap.put("employee", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payslip deduction  retrievd unsucessful",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getpayslipCompanydetails")
	public ResponseEntity<ResponseDTO> getpayslipCompanydetails(@RequestParam Long orgId) {
		String methodName = "getpayslipCompanydetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.getpayslipCompanydetails(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payslip Company Details retrieved successfully");
			responseObjectsMap.put("Company", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payslip Company Details retrieve Charge Type",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getpaysliphandsondetails")
	public ResponseEntity<ResponseDTO> getpaysliphandsondetails(@RequestParam Long orgId, String Employeecode,
			Long Month, Long year) {
		String methodName = "getpaysliphandsondetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = basicMasterService.getpaysliphandsondetails(orgId, Employeecode, Month, year);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Payslip Handson retrieved successfully");
			responseObjectsMap.put("employee", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Payslip handson retrievd unsucessful",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// getAttendanceReport

	@GetMapping("getTodayAttendanceReportByOrgId")
	public ResponseEntity<ResponseDTO> getTodayAttendanceReportByOrgId(@RequestParam Long orgId,
			@RequestParam String branch, @RequestParam String date) {

		String methodName = "getTodayAttendanceReportByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> attendanceReport = null;
		try {
			attendanceReport = basicMasterService.getTodayAttendanceReportByOrgId(orgId, branch, date);
			if (attendanceReport.isEmpty()) {
				errorMsg = "No attendance Report data found for Orgid: " + orgId;
				responseDTO = createServiceResponseError(responseObjectsMap, "Attendance Report not found", errorMsg);
			} else {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Attendance Report found for empcode");
				responseObjectsMap.put("attendanceReport", attendanceReport);
				responseDTO = createServiceResponse(responseObjectsMap);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Error fetching attendance Report", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Calendar

	@PutMapping("/createUpdateCalendar")
	public ResponseEntity<ResponseDTO> createUpdateCalendar(@RequestBody CalendarDTO calendarDTO) {
		String methodName = "createUpdateCalendar()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> calendarVO = basicMasterService.createUpdateCalendar(calendarDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, calendarVO.get("message"));
			responseObjectsMap.put("calendarVO", calendarVO.get("calendarVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllCalendarByOrgId")
	public ResponseEntity<ResponseDTO> getAllCalendarByOrgId(@RequestParam Long orgId, @RequestParam String branchCode,
			@RequestParam String empCode) {
		String methodName = "getAllCalendarByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CalendarVO> calendarVO = new ArrayList<>();
		try {
			calendarVO = basicMasterService.getAllCalendarByOrgId(orgId, branchCode, empCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Calendar information get successfully By OrgId");
			responseObjectsMap.put("calendarVO", calendarVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Calendar information receive failed By OrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getCalendarById")
	public ResponseEntity<ResponseDTO> getCalendarById(@RequestParam Long id) {
		String methodName = "getCalendarById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CalendarVO calendarVO = new CalendarVO();
		try {
			calendarVO = basicMasterService.getCalendarById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "calendar information get successfully By id");
			responseObjectsMap.put("calendarVO", calendarVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "calendar information receive failedById",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getApprovalPendingCountForDashBoard")
	public ResponseEntity<ResponseDTO> getApprovalPendingCountForDashBoard(@RequestParam Long orgId,
			@RequestParam String reportingPersonCode, @RequestParam String branchCode) {

		String methodName = "getApprovalPendingCountForDashBoard()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> approvalCount;

		try {
			approvalCount = basicMasterService.getApprovalPendingCountForDashBoard(orgId, reportingPersonCode,
					branchCode);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "approvalCount details retrieved successfully");
			responseObjectsMap.put("approvalCount", approvalCount); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve approvalCount details",
					errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//CalendarNotification		

	@GetMapping("/getCalendarNotificationByOrgId")
	public ResponseEntity<ResponseDTO> getCalendarNotificationByOrgId(@RequestParam Long orgId,
			@RequestParam String branchCode, @RequestParam String empCode) {
		String methodName = "getCalendarNotificationByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CalendarVO> calendarVO = new ArrayList<>();
		try {
			calendarVO = basicMasterService.getCalendarNotificationByOrgId(orgId, branchCode, empCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Calendar Notification information get successfully By OrgId");
			responseObjectsMap.put("calendarVO", calendarVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Calendar Notification information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@PutMapping("/createEmployeeCodeConfig")
	public ResponseEntity<ResponseDTO> createEmployeeCodeConfig(
			@RequestBody EmployeeCodeConfigDTO employeeCodeConfigDTO) {
		String methodName = "createEmployeeCodeConfig()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			EmployeeCodeConfigVO employeeCodeConfigVO = basicMasterService
					.createEmployeeCodeConfig(employeeCodeConfigDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee Code Config Created Successfully");
			responseObjectsMap.put("employeeCodeConfigVO", employeeCodeConfigVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/getEmplCode")
	public ResponseEntity<ResponseDTO> getEmplCode(@RequestBody EmployeeDTOnew employeeDTOnew) {
		String methodName = "getEmplCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			String pollVoteVO = basicMasterService.generateEmployeeCodeByOrgId(employeeDTOnew);
			
			responseObjectsMap.put("pollVoteVO", pollVoteVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

}
