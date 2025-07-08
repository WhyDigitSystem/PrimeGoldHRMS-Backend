package com.efit.hrms.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.EmployeeDocumentsDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.entity.EmployeeDocumentsVO;
import com.efit.hrms.entity.LeaveTypeVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.service.EmployeeDocumentsService;


@CrossOrigin
@RestController
@RequestMapping("/api/employeedocuments")
public class EmployeeDocumentsController extends BaseController{


	@Autowired
	EmployeeDocumentsService employeeDocumentsService;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(BasicMasterController.class);
	
	@PostMapping("/uploademployeeDoc")
	public ResponseEntity<ResponseDTO> upload(@ModelAttribute EmployeeDocumentsDTO dto) {
	    String methodName = "uploademployeeDoc()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        // Get the uploaded document details
	        EmployeeDocumentsVO savedDocument = employeeDocumentsService.uploadDocument(dto);

	        // Success response
	        responseObjectsMap.put("document", savedDocument);
	        responseObjectsMap.put("message", "Document uploaded successfully");
	        responseDTO = createServiceResponse(responseObjectsMap);

	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Failure response
	        responseObjectsMap.put("error", errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Error uploading document", errorMsg);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	
	@GetMapping("getEmployeeDocumentsByEmpCodeAndOrgId")
	public ResponseEntity<ResponseDTO> getEmployeeDocumentsByEmpCodeAndOrgId(@RequestParam Long orgId,@RequestParam String employeeCode) {
		String methodName = "getEmployeeDocumentsByEmpCodeAndOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<EmployeeDocumentsVO> employeeDocumentsVO = null;
		try {
			employeeDocumentsVO = employeeDocumentsService.getEmployeeDocumentsByEmpCodeAndOrgId(orgId,employeeCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "employeeDocuments found by EmpCode And OrgId");
			responseObjectsMap.put("employeeDocumentsVO", employeeDocumentsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "employeeDocuments not found for OrgId: " + orgId+ " And EmpCode"  + employeeCode;
			responseDTO = createServiceResponseError(responseObjectsMap, "employeeDocuments not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@DeleteMapping("/employeeDocDeleteById/{id}")
	public ResponseEntity<ResponseDTO> deleteCompOff(@PathVariable Long id) {

	    String methodName = "deleteCompOff()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    String errorMsg = null;
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = null;

	    try {
	        employeeDocumentsService.deleteEmployeeDocById(id);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	    }

	    if (StringUtils.isEmpty(errorMsg)) {
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee Document deleted successfully");
	        responseObjectsMap.put("deletedDocId", id);
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } else {
	        errorMsg = "Failed to delete Employee Document with ID: " + id;
	        responseDTO = createServiceResponseError(responseObjectsMap, "Employee Document deletion failed", errorMsg);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}


}
