package com.efit.hrms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.entity.GlobalParameterVO;
import com.efit.hrms.service.GlobalParameterService;

@RestController
@RequestMapping("/api/GlobalParam")
public class GlobalParameterController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(GlobalParameterController.class);

	@Autowired
	GlobalParameterService globalParameterService;

	// Global Parameter

	@GetMapping("/getWarehouseNameByOrgIdAndBranchAndClient")
	public ResponseEntity<ResponseDTO> getWarehouseNameByOrgIdAndBranchAndClient(@RequestParam Long orgid,
			@RequestParam String branch, @RequestParam String client) {
		String methodName = "getWarehouseNameByOrgIdAndBranchAndClient()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Set<Object[]> global = new HashSet<>();
		try {
			global = globalParameterService.getWarehouseNameByOrgIdAndBranchAndClient(orgid, branch, client);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			List<Map<String, String>> getGlobal = formatt(global);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Global Parameter Warehouse information get successfully");
			responseObjectsMap.put("GlopalParameterWarehouse", getGlobal);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Global Parameter Warehouse information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// get Global Param

	private List<Map<String, String>> formatt(Set<Object[]> global) {
		List<Map<String, String>> getglobal = new ArrayList<>();
		for (Object[] parameters : global) {
			Map<String, String> param = new HashMap<>();
			param.put("warehouse", parameters[0].toString());
			getglobal.add(param);
		}
		return getglobal;
	}

	@GetMapping("/globalparam/username")
	public ResponseEntity<ResponseDTO> getGlobalParamByOrgIdAndUserId(@RequestParam Long orgid,
			@RequestParam String userId) {
		String methodName = "getCountryById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<GlobalParameterVO> globalparam = null;
		try {
			globalparam = globalParameterService.getGlobalParamByOrgIdAndUserName(orgid, userId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "GlobalParam found by ID");
			responseObjectsMap.put("globalParam", globalparam);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "GlobalParam not found for ID: ";
			responseDTO = createServiceResponseError(responseObjectsMap, "GlobalParam not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/globalparam")
	public ResponseEntity<ResponseDTO> updateGlobalParam(@RequestBody GlobalParameterVO globalParameterVO) {
		String methodName = "updateGlobalParam()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			GlobalParameterVO gloParameterVO = globalParameterService.updateGlobaParameter(globalParameterVO);
			if (gloParameterVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Global Parameter Updated successfully");
				responseObjectsMap.put("GlobalParameterVO", gloParameterVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Global Parameter not found for ID: " + globalParameterVO.getId();
				responseDTO = createServiceResponseError(responseObjectsMap, "Global Parameter Update failed",
						errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Global Parameter Update failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/globalparamBranchByUserName")
	public ResponseEntity<ResponseDTO> getGlobalParameterBranchByUserName(@RequestParam Long orgid,
			@RequestParam String userName) {
		String methodName = "getAllGlobalParameterByUserName()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Set<Object[]> globalParameters = new HashSet<>();
		try {
			globalParameters = globalParameterService.getGlobalParametersBranchAndBranchCodeByOrgIdAndUserName(orgid,
					userName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			List<Map<String, String>> formattedParameters = formattParameter(globalParameters);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Global Parameter Branch information get successfully");
			responseObjectsMap.put("GlopalParameters", formattedParameters);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Global Parameter Branch information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	private List<Map<String, String>> formattParameter(Set<Object[]> globalParameters) {
		List<Map<String, String>> formattedParameters = new ArrayList<>();
		for (Object[] parameters : globalParameters) {
			Map<String, String> param = new HashMap<>();
			param.put("branch", parameters[0].toString());
			param.put("branchcode", parameters[1].toString());
			formattedParameters.add(param);
		}
		return formattedParameters;
	}

	
	
}