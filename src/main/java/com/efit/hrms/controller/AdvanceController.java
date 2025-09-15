package com.efit.hrms.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.efit.hrms.dto.AdvanceDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.entity.AdvanceVO;
import com.efit.hrms.entity.ShiftAssignVO;
import com.efit.hrms.service.AdvanceService;

@CrossOrigin
@RestController
@RequestMapping("/api/advance")
public class AdvanceController extends BaseController {

	@Autowired
	AdvanceService advanceService;

	public static final Logger LOGGER = LoggerFactory.getLogger(AdvanceController.class);

	@GetMapping("/getAllAdvanceByOrgId")
	public ResponseEntity<ResponseDTO> getAllQuotationByOrgId(@RequestParam Long orgId,
			@RequestParam String branchCode) {
		String methodName = "getAllAdvanceByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AdvanceVO> advanceVO = new ArrayList<>();
		try {
			advanceVO = advanceService.getAllAdvanceByOrgId(orgId, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Advance information get successfully ByOrgId");
			responseObjectsMap.put("advanceVO", advanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Advance information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	
	@GetMapping("/getEmployeeAdvanceSalary")
	public ResponseEntity<ResponseDTO> getEmployeeAdvanceSalary(@RequestParam Long orgId,
			@RequestParam String branchCode,@RequestParam String employeeCode,@RequestParam BigDecimal payOnHand,Long month, String year) {
		String methodName = "getAllAdvanceByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>>  advanceVO = new ArrayList<>();
		try {
			advanceVO = advanceService.getEmployeeAdvanceSalary(orgId, branchCode,employeeCode,payOnHand, month,  year);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Advance Salary information get successfully ByOrgId");
			responseObjectsMap.put("advanceVO", advanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Advance Salary information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	
	@GetMapping("/getAdvanceById")
	public ResponseEntity<ResponseDTO> getAdvanceById(@RequestParam Long id) {
		String methodName = "getAdvanceById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		AdvanceVO advanceVO = new AdvanceVO();
		try {
			advanceVO = advanceService.getAdvanceById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Advance get successfully By id");
			responseObjectsMap.put("advanceVO", advanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Advance information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateAdvance")
	public ResponseEntity<ResponseDTO> updateCreateAdvance(@RequestBody AdvanceDTO advanceDTO) {
		String methodName = "updateCreateAdvance()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> advanceVO = advanceService.updateCreateAdvance(advanceDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, advanceVO.get("message"));
			responseObjectsMap.put("advanceVO", advanceVO.get("advanceVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/findEmployeeDetails")
	public ResponseEntity<ResponseDTO> findEmployeeDetails(@RequestParam Long orgId) {
		String methodName = "findEmployeeDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;

		try {
			List<Map<String, Object>> advanceVO = advanceService.findEmployeeDetails(orgId);

			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Pending EmployeeDetails information retrieved successfully ");
			responseObjectsMap.put("advanceVO", advanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);

			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok(responseDTO);

		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve Pending EmployeeDetails information By OrgId", errorMsg);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
		}
	}

	@PostMapping("/uploadAttachmentLogoInBloob")
	public ResponseEntity<ResponseDTO> uploadAttachmentLogoInBloob(@RequestParam("file") MultipartFile file,
			@RequestParam Long id) {
		String methodName = "uploadAttachmentLogoInBloob()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		AdvanceVO advanceVO = null;
		try {
			advanceVO = advanceService.uploadAttachmentLogoInBloob(file, id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error("Unable To Upload PartImage", methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Attachment Logo Successfully Upload");
			responseObjectsMap.put("advanceVO", advanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Attachment Logo Upload Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllShiftDetails")
	public ResponseEntity<ResponseDTO> getAllShiftDetails(
	    @RequestParam Long orgId,
	    @RequestParam String shifttype,
	    @RequestParam String department,
	    @RequestParam String effectiveFrom,
	    @RequestParam String effectiveTo,
	    @RequestParam String type,
	    @RequestParam(required = false) String contractorName) {
		String methodName = "getAllShiftDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ShiftAssignVO> shiftAssignVO = new ArrayList<>();
		try {
			shiftAssignVO = advanceService.getAllShiftDetails(orgId, shifttype, department, effectiveFrom, effectiveTo,
					type, contractorName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ShiftAssign information get successfully ByOrgId");
			responseObjectsMap.put("shiftAssignVO", shiftAssignVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ShiftAssign information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
}
