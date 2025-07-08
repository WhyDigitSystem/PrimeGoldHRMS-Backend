package com.efit.hrms.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.ContractMasterDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.ShiftMasterDTO;
import com.efit.hrms.entity.ContractMasterVO;
import com.efit.hrms.entity.ShiftMasterVO;
import com.efit.hrms.service.ShiftMasterService;

@CrossOrigin
@RestController
@RequestMapping("/api/shiftmaster")
public class ShiftMasterController extends BaseController{

	@Autowired
	ShiftMasterService shiftMasterService;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ShiftMasterController.class);

	@PutMapping("/createUpdateShiftMaster")
	public ResponseEntity<ResponseDTO> createUpdateShiftMaster(@RequestBody ShiftMasterDTO shiftMasterDTO) {
		String methodName = "createUpdateShiftMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> shiftMasterVO = shiftMasterService.createUpdateShiftMaster(shiftMasterDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, shiftMasterVO.get("message"));
			responseObjectsMap.put("shiftMasterVO", shiftMasterVO.get("shiftMasterVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAllShiftMasterByOrgId")
	public ResponseEntity<ResponseDTO> getAllShiftMasterByOrgId(@RequestParam Long orgId) {
	    String methodName = "getAllShiftMasterByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<ShiftMasterVO> shiftMasterVO = Optional.ofNullable(shiftMasterService.getAllShiftMasterByOrgId(orgId))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "shiftMaster information retrieved successfully By OrgId");
	        responseObjectsMap.put("shiftMasterVO", shiftMasterVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve shiftMaster information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	@GetMapping("/getShiftMasterById")
	public ResponseEntity<ResponseDTO> getShiftMasterById(@RequestParam Long id) {
	    String methodName = "getShiftMasterById()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Fetch SalaryHeadsVO safely, preventing null
	        ShiftMasterVO shiftMasterVO = Optional.ofNullable(shiftMasterService.getShiftMasterById(id))
	                                             .orElseThrow(() -> new RuntimeException("ShiftMaster not found for ID: " + id));

	        // Success response
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ShiftMaster information retrieved successfully by ID");
	        responseObjectsMap.put("shiftMasterVO", shiftMasterVO);
	        responseDTO = createServiceResponse(responseObjectsMap);

	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Error response
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve ShiftMaster information by ID", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	//ContractMaster
	
	@PutMapping("/createUpdateContractMaster")
	public ResponseEntity<ResponseDTO> createUpdateContractMaster(@RequestBody ContractMasterDTO contractMasterDTO) {
		String methodName = "createUpdateContractMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> contractMasterVO = shiftMasterService.createUpdateContractMaster(contractMasterDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, contractMasterVO.get("message"));
			responseObjectsMap.put("contractMasterVO", contractMasterVO.get("contractMasterVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAllContractMasterByOrgId")
	public ResponseEntity<ResponseDTO> getAllContractMasterByOrgId(@RequestParam Long orgId) {
	    String methodName = "getAllContractMasterByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	    	List<ContractMasterVO> contractMasterVO = Optional.ofNullable(shiftMasterService.getAllContractMasterByOrgId(orgId))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ContractMaster information retrieved successfully By OrgId");
	        responseObjectsMap.put("contractMasterVO", contractMasterVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve ContractMaster information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	
	@GetMapping("/getContractMasterById")
	public ResponseEntity<ResponseDTO> getContractMasterById(@RequestParam Long id) {
	    String methodName = "getContractMasterById()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Fetch SalaryHeadsVO safely, preventing null
	        ContractMasterVO contractMasterVO = Optional.ofNullable(shiftMasterService.getContractMasterById(id))
	                                             .orElseThrow(() -> new RuntimeException("contractMaster not found for ID: " + id));

	        // Success response
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "contractMaster information retrieved successfully by ID");
	        responseObjectsMap.put("contractMasterVO", contractMasterVO);
	        responseDTO = createServiceResponse(responseObjectsMap);

	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Error response
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve contractMaster information by ID", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}

}
