package com.efit.hrms.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.SequenceConfigDTO;
import com.efit.hrms.service.SequenceService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/sequenceConfig")
public class SequenceServiceController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(SequenceServiceController.class);

	@Autowired
	SequenceService sequenceService;

	@PutMapping("/createSequenceConfig")
	public ResponseEntity<ResponseDTO> createSequenceConfig(@RequestBody SequenceConfigDTO sequenceConfigDTO) {
	    String methodName = "createSequenceConfig()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;
	    try {
	        Map<String, Object> documentType1 = sequenceService.createSequenceConfig(sequenceConfigDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, documentType1.get("message"));
	        responseObjectsMap.put("sequenceConfigVO", documentType1.get("sequenceConfigVO"));
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
