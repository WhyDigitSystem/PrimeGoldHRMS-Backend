package com.efit.hrms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.UserNameDTO;
import com.efit.hrms.service.CheckInOutService;

@CrossOrigin
@RestController
@RequestMapping("/api/checkinout")
public class CheckInOutController extends BaseController{

	@Autowired
	CheckInOutService checkInOutService;

	public static final Logger LOGGER = LoggerFactory.getLogger(CheckInOutController.class);
	
	
//	@PutMapping("/createCheckInOut")
//	public ResponseEntity<ResponseDTO> createCheckInOut(@RequestBody UserNameDTO userNameDTO) {
//		String methodName = "createCheckInOut()";
//		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
//		String errorMsg = null;
//		ResponseDTO responseDTO = null;
//		try {
//			Map<String, Object> checkInVO = checkInOutService.createCheckInOut(userNameDTO);
//			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, checkInVO.get("message"));
//			responseObjectsMap.put("checkInVO", checkInVO.get("checkInVO"));
//			responseDTO = createServiceResponse(responseObjectsMap);
//		} catch (Exception e) {
//			errorMsg = e.getMessage();
//			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
//		}
//		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//		return ResponseEntity.ok().body(responseDTO);
//	}
//	
//	

	    @PutMapping("/checkInOutUploadExcel")
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
	

	
	
	
	
	
	
}
