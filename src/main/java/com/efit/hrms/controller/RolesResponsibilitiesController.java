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
import com.efit.hrms.dto.RolesPermissionHeaderDTO;
import com.efit.hrms.entity.RolesPermissionHeaderVO;
import com.efit.hrms.service.RolesResponsibilitiesService;


@CrossOrigin
@RestController
@RequestMapping("/api/roles")
public class RolesResponsibilitiesController extends BaseController {
	public static final Logger LOGGER = LoggerFactory.getLogger(RolesResponsibilitiesController.class);

	@Autowired
	RolesResponsibilitiesService rolesResponsibilitiesService;
	
	@PutMapping("/createUpdateRoleScreenPermission")
	public ResponseEntity<ResponseDTO> createUpdateRoleScreenPermission(@RequestBody RolesPermissionHeaderDTO rolesPermissionHeaderDTO) {
		String methodName = "createUpdateRoleScreenPermission()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> roles = rolesResponsibilitiesService.createUpdateRoleScreenPermission(rolesPermissionHeaderDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, roles.get("message") );
			responseObjectsMap.put("rolesPermissionHeaderVO", roles.get("rolesPermissionHeaderVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getRolesPermissionHeaderByRoleandOrgid")
	public ResponseEntity<ResponseDTO> getRolesPermissionHeaderByRoleandOrgid(@RequestParam String role, Long orgid) {
		String methodName = "getRolesPermissionHeaderByRoleandOrgid()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<RolesPermissionHeaderVO> userVO = new ArrayList<>();
		try {
			userVO = rolesResponsibilitiesService.getRolesPermissionHeaderByRoleandOrgid(role,orgid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME_WITH_USER_ID, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, UserConstants.GET_USER_INFORMATION_SUCCESS_MESSAGE);
			responseObjectsMap.put("userVO", userVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					UserConstants.GET_USER_INFORMATION_FAILED_MESSAGE, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
}
