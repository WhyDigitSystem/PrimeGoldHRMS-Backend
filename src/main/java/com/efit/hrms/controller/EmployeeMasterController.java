package com.efit.hrms.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.efit.hrms.dto.PermissionRequestDTO;
import com.efit.hrms.dto.PfEsiAmountDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.SalaryHeadsDTO;
import com.efit.hrms.dto.SalaryProcessDTO;
import com.efit.hrms.dto.SalaryStructureDTO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.PermissionRequestVO;
import com.efit.hrms.entity.SalaryHeadsVO;
import com.efit.hrms.entity.SalaryProcessVO;
import com.efit.hrms.entity.SalaryStructureVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.service.EmployeeMasterService;

@CrossOrigin
@RestController
@RequestMapping("/api/employeemaster")
public class EmployeeMasterController extends BaseController{

	@Autowired
	EmployeeMasterService employeeMasterService;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(BasicMasterController.class);	

	@PutMapping("/createUpdateSalaryHeads")
	public ResponseEntity<ResponseDTO> createUpdateSalaryHeads(@RequestBody SalaryHeadsDTO salaryHeadsDTO) {
	    String methodName = "createUpdateSalaryHeads()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Call service method
	        Map<String, Object> salaryHeadsResponse = employeeMasterService.createUpdateSalaryHeads(salaryHeadsDTO);

	        // Extract message and data
	        Object salaryHeadsVO = salaryHeadsResponse.get("paramObjectsMap");
	        String message = (String) salaryHeadsResponse.getOrDefault("message", "Operation completed successfully.");

	        // Populate response map
	        responseObjectsMap.put("salaryHeadsVO", salaryHeadsVO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, message);

	        // Create structured response
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (ApplicationException e) {
	        LOGGER.error("{} - Error: {}", methodName, e.getMessage(), e);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Application Error", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    } catch (Exception e) {
	        LOGGER.error("{} - Unexpected Error: {}", methodName, e.getMessage(), e);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Unexpected Error", "Something went wrong.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok(responseDTO);
	}

	
	@GetMapping("/getAllSalaryHeadsByOrgId")
	public ResponseEntity<ResponseDTO> getAllSalaryHeadsByOrgId(@RequestParam Long orgId) {
	    String methodName = "getAllSalaryHeadsByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<SalaryHeadsVO> salaryHeadsVO = Optional.ofNullable(employeeMasterService.getAllSalaryHeadsByOrgId(orgId))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SalaryHeads information retrieved successfully By OrgId");
	        responseObjectsMap.put("salaryHeadsVO", salaryHeadsVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve SalaryHeads information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	@GetMapping("/getSalaryHeadsById")
	public ResponseEntity<ResponseDTO> getSalaryHeadsById(@RequestParam Long id) {
	    String methodName = "getSalaryHeadsById()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Fetch SalaryHeadsVO safely, preventing null
	        SalaryHeadsVO salaryHeadsVO = Optional.ofNullable(employeeMasterService.getSalaryHeadsById(id))
	                                             .orElseThrow(() -> new RuntimeException("SalaryHeads not found for ID: " + id));

	        // Success response
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SalaryHeads information retrieved successfully by ID");
	        responseObjectsMap.put("salaryHeadsVO", salaryHeadsVO);
	        responseDTO = createServiceResponse(responseObjectsMap);

	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Error response
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve SalaryHeads information by ID", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	//SalaryStructure

	@GetMapping("/getAllSalaryStructureByOrgId")
	public ResponseEntity<ResponseDTO> getAllSalaryStructureByOrgId(@RequestParam Long orgId) {
	    String methodName = "getAllSalaryStructureByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<SalaryStructureVO> salaryStructureVO = Optional.ofNullable(employeeMasterService.getAllSalaryStructureByOrgId(orgId))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SalaryStructure information retrieved successfully By OrgId");
	        responseObjectsMap.put("SalaryStructureVO", salaryStructureVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve SalaryStructure information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	
	@GetMapping("/getSalaryStructureById")
	public ResponseEntity<ResponseDTO> getSalaryStructureById(@RequestParam Long id) {
	    String methodName = "getSalaryStructureById()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Fetch SalaryHeadsVO safely, preventing null
	    	SalaryStructureVO salaryStructureVO = Optional.ofNullable(employeeMasterService.getSalaryStructureById(id))
	                                             .orElseThrow(() -> new RuntimeException("SalaryStructure not found for ID: " + id));

	        // Success response
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SalaryStructure information retrieved successfully by ID");
	        responseObjectsMap.put("SalaryStructureVO", salaryStructureVO);
	        responseDTO = createServiceResponse(responseObjectsMap);

	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Error response
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve SalaryStructure information by ID", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	
	@GetMapping("/getPfAmountAndEsiAmountByEmployee")
	public ResponseEntity<ResponseDTO> getPfAmountAndEsiAmountByEmployee(
	        @RequestParam Long orgId,
	        @RequestParam String employeeCode,
	        @RequestParam String branch,
	        @RequestParam BigDecimal sumOfEarnings) {

	    String methodName = "getPfAmountAndEsiAmountByEmployee()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        List<PfEsiAmountDTO> taxList = employeeMasterService.getPfAmountAndEsiAmountByEmployee(orgId, employeeCode, branch, sumOfEarnings);

	        responseObjectsMap.put("taxDetails", taxList);
	        responseDTO = createServiceResponse(responseObjectsMap);

	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to calculate tax amounts", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}

	@PutMapping("/createUpdateSalaryStructure")
	public ResponseEntity<ResponseDTO> createUpdateSalaryStructure(@RequestBody SalaryStructureDTO salaryStructureDTO) {
	    String methodName = "createUpdateSalaryStructure()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Call service method
	        Map<String, Object> salaryStructureResponse = employeeMasterService.createUpdateSalaryStructure(salaryStructureDTO);

	        // Extract message and data
	        Object salaryStructureVO = salaryStructureResponse.get("paramObjectsMap");
	        String message = (String) salaryStructureResponse.getOrDefault("message", "Operation completed successfully.");

	        // Populate response map
	        responseObjectsMap.put("salaryStructureVO", salaryStructureVO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, message);


	        // Create structured response
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (ApplicationException e) {
	        LOGGER.error("{} - Error: {}", methodName, e.getMessage(), e);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Application Error", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    } catch (Exception e) {
	        LOGGER.error("{} - Unexpected Error: {}", methodName, e.getMessage(), e);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Unexpected Error", "Something went wrong.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/getAllEmployeeByActive")
	public ResponseEntity<ResponseDTO> getAllEmployeeByActive(@RequestParam Long orgId) {
	    String methodName = "getAllEmployeeByActive()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<EmployeeVO> employeeVO = Optional.ofNullable(employeeMasterService.getAllEmployeeByActive(orgId))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee information retrieved successfully By OrgId");
	        responseObjectsMap.put("employeeVO", employeeVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve Employee information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	//PermissionRequest
	@GetMapping("/getAllPermissionRequestByOrgId")
	public ResponseEntity<ResponseDTO> getAllPermissionRequestByOrgId(@RequestParam Long orgId,@RequestParam String branchCode) {
	    String methodName = "getAllPermissionRequestByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<PermissionRequestVO> permissionRequestVO = Optional.ofNullable(employeeMasterService.getAllPermissionRequestByOrgId(orgId,branchCode))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PermissionRequest information retrieved successfully By OrgId");
	        responseObjectsMap.put("permissionRequestVO", permissionRequestVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve PermissionRequest information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}

	@GetMapping("/getPermissionRequestById")
	public ResponseEntity<ResponseDTO> getPermissionRequestById(@RequestParam Long id) {
	    String methodName = "getPermissionRequestById()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Fetch SalaryHeadsVO safely, preventing null
	    	PermissionRequestVO permissionRequestVO = employeeMasterService.getPermissionRequestById(id);

	        // Success response
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PermissionRequest information retrieved successfully by ID");
	        responseObjectsMap.put("permissionRequestVO", permissionRequestVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Error response
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve PermissionRequest information by ID", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	@PutMapping("/createUpdatePermissionRequest")
	public ResponseEntity<ResponseDTO> createUpdatePermissionRequest(@RequestBody PermissionRequestDTO permissionRequestDTO) {
	    String methodName = "createUpdatePermissionRequest()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Call service
	        Map<String, Object> result = employeeMasterService.createUpdatePermissionRequest(permissionRequestDTO);

	        // ✅ Extract and flatten
	        Object innerParamMap = result.get("paramObjectsMap");
	        if (innerParamMap instanceof Map) {
	            responseObjectsMap.putAll((Map<String, Object>) innerParamMap);
	        }

	        // ✅ Add message separately
	        String message = (String) result.getOrDefault("message", "Permission Request completed successfully.");
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, message);

	        // ✅ Final Response
	        responseDTO = createServiceResponse(responseObjectsMap);

	    } catch (ApplicationException e) {
	        LOGGER.error("{} - Error: {}", methodName, e.getMessage(), e);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Application Error", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    } catch (Exception e) {
	        LOGGER.error("{} - Unexpected Error: {}", methodName, e.getMessage(), e);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Unexpected Error", "Something went wrong.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok(responseDTO);
	}

	
	@GetMapping("/getReportingPerson")
	public ResponseEntity<ResponseDTO> getReportingPerson(@RequestParam Long orgId,@RequestParam String employeeCode) {
		String methodName = "getReportingPerson()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = employeeMasterService.getReportingPerson(orgId,employeeCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ReportingPerson retrieved successfully");
			responseObjectsMap.put("PermisionRequestVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve ReportingPerson", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	//SalaryProcess
	
	@PutMapping("/createUpdateSalaryProcess")
	public ResponseEntity<ResponseDTO> createUpdateSalaryProcess(@RequestBody List<SalaryProcessDTO> salaryProcessDTO) {
	    String methodName = "createUpdateSalaryProcess()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Call service method
	        Map<String, Object> salaryProcessVO = employeeMasterService.createUpdateSalaryProcess(salaryProcessDTO);

	        // Extract message and data
//	        Object salaryProcess = salaryProcessVO.get("paramObjectsMap");
	        String message = (String) salaryProcessVO.getOrDefault("message", "salaryProcessVO completed successfully.");

	        // Populate response map
	        responseObjectsMap.put("salaryProcessVO", salaryProcessVO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, message);

	        // Create structured response
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (ApplicationException e) {
	        LOGGER.error("{} - Error: {}", methodName, e.getMessage(), e);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Application Error", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    } catch (Exception e) {
	        LOGGER.error("{} - Unexpected Error: {}", methodName, e.getMessage(), e);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Unexpected Error", "Something went wrong.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/getAllSalaryProcessByOrgId")
	public ResponseEntity<ResponseDTO> getAllSalaryProcessByOrgId(@RequestParam Long orgId) {
	    String methodName = "getAllSalaryProcessByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<SalaryProcessVO> salaryProcessVO = Optional.ofNullable(employeeMasterService.getAllSalaryProcessByOrgId(orgId))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "salaryProcess information retrieved successfully By OrgId");
	        responseObjectsMap.put("salaryProcessVO", salaryProcessVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve SalaryProcess information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	
	@GetMapping("/getSalaryProcessById")
	public ResponseEntity<ResponseDTO> getSalaryProcessById(@RequestParam Long id) {
	    String methodName = "getSalaryProcessById()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Fetch SalaryHeadsVO safely, preventing null
	    	SalaryProcessVO salaryProcessVO = employeeMasterService.getSalaryProcessById(id);

	        // Success response
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "salaryProcess information retrieved successfully by ID");
	        responseObjectsMap.put("salaryProcessVO", salaryProcessVO);
	        responseDTO = createServiceResponse(responseObjectsMap);

	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Error response
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve salaryProcessVO information by ID", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	

	@GetMapping("/getSalaryStructureForSalaryProcess")
	public ResponseEntity<ResponseDTO> getSalaryStructureForSalaryProcess(
	        @RequestParam Long orgId,@RequestParam String employeeCode) {

	    String methodName = "getSalaryStructureForSalaryProcess()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    List<Map<String, Object>> salaryProcessVO;

	    try {
	    	salaryProcessVO = employeeMasterService.getSalaryStructureForSalaryProcess(orgId,employeeCode);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "salaryStructure details retrieved successfully");
	        responseObjectsMap.put("salaryProcessVO", salaryProcessVO); // ✅ Correct key name
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve salaryStructure details", errorMsg);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getLeaveDetailsforSalaryProcess")
	public ResponseEntity<ResponseDTO> getLeaveDetailsforSalaryProcess(@RequestParam Long orgId,
			@RequestParam Long month, @RequestParam String year,@RequestParam String department,@RequestParam String branch) {

		String methodName = "getLeaveDetailsforSalaryProcess()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> salaryProcessVO;

		try {
			salaryProcessVO = employeeMasterService.getLeaveDetailsforSalaryProcess(orgId, month, year,department,branch);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "salaryProcess details retrieved successfully");
			responseObjectsMap.put("salaryProcessVO", salaryProcessVO); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve salaryProcess details",
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
			mapp = employeeMasterService.getEmpDob(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee DOB retrieved successfully");
			responseObjectsMap.put("empDob", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Employee DOB to retrieve Charge Type", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	

	
//	@GetMapping("/getNetPayForSalaryProcess")
//	public ResponseEntity<ResponseDTO> getNetPayForSalaryProcess(
//	        @RequestParam BigDecimal grossPay,@RequestParam BigDecimal sumOfDetection) {
//
//	    String methodName = "getNetPayForSalaryProcess()";
//	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//
//	    Map<String, Object> responseObjectsMap = new HashMap<>();
//	    ResponseDTO responseDTO;
//	    List<Map<String, Object>> salaryProcessVO;
//
//	    try {
//	    	salaryProcessVO = employeeMasterService.getNetPayForSalaryProcess(grossPay,sumOfDetection);
//	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "NetPay details retrieved successfully");
//	        responseObjectsMap.put("salaryProcessVO", salaryProcessVO); // ✅ Correct key name
//	        responseDTO = createServiceResponse(responseObjectsMap);
//	    } catch (Exception e) {
//	        String errorMsg = e.getMessage();
//	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve NetPay details", errorMsg);
//	    }
//
//	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//	    return ResponseEntity.ok().body(responseDTO);
//	}
	
	@GetMapping("/getPayOnHandsForSalaryProcess")
	public ResponseEntity<ResponseDTO> getPayOnHandsForSalaryProcess(
	        @RequestParam Long totalCompanyWorkingDays,@RequestParam BigDecimal grossPay,@RequestParam Long empSalaryDays,@RequestParam BigDecimal sumOfDetection) {

	    String methodName = "getPayOnHandsForSalaryProcess()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    List<Map<String, Object>> salaryProcessVO;

	    try {
	    	salaryProcessVO = employeeMasterService.getPayOnHandsForSalaryProcess(totalCompanyWorkingDays,grossPay,empSalaryDays,sumOfDetection);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "NetPay details retrieved successfully");
	        responseObjectsMap.put("salaryProcessVO", salaryProcessVO); // ✅ Correct key name
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve NetPay details", errorMsg);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getApprovedSalaryProcessReport")
	public ResponseEntity<ResponseDTO> getApprovedSalaryProcessReport(@RequestParam Long orgId,@RequestParam Long month ,@RequestParam String Year ) {
	    String methodName = "getApprovedSalaryProcessReport()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Fetch SalaryHeadsVO safely, preventing null
	    	List<SalaryProcessVO> salaryProcessVO = employeeMasterService.getApprovedSalaryProcessReport(orgId,month,Year);

	        // Success response
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ApprovedsalaryProcessReport information retrieved successfully by OrgId");
	        responseObjectsMap.put("salaryProcessVO", salaryProcessVO);
	        responseDTO = createServiceResponse(responseObjectsMap);

	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Error response
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve ApprovedsalaryProcessReport information by ID", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	// dashboard
	
	@GetMapping("/Getworkaniversary")
	public ResponseEntity<ResponseDTO> Getworkaniversary(@RequestParam Long orgId) {
		String methodName = "Getworkaniversary()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = employeeMasterService.GetworkAniversary(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee Work Aniversary retrieved successfully");
			responseObjectsMap.put("employee", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Employee Work Aniversary to retrieve Charge Type", errorMsg);
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
			mapp = employeeMasterService.GetnewJoineDetails(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "New Employee retrieved successfully");
			responseObjectsMap.put("employee", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "New Employee to retrieve Charge Type", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	
	//APPROVED PERMISSION REQUEST
	
	@PutMapping("/createApprovalPermissionRequest")
	public ResponseEntity<ResponseDTO> createApprovalPermissionRequest(@RequestParam Long orgId, @RequestParam Long id,
			@RequestParam String employeeCode, @RequestParam String action, @RequestParam String actionBy,@RequestParam String notifyCode, @RequestParam String notify,@RequestParam String screenName) {
		String methodName = "createApprovalPermissionRequest()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			 Map<String, Object> result = employeeMasterService.createApprovalPermissionRequest(
		                orgId, id, employeeCode, action, actionBy, notifyCode, notify,screenName);
		        
		        // ✅ Use putAll to avoid nested structure
		        responseObjectsMap.putAll(result);
		        responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getPendingPermissionRequest")
	public ResponseEntity<ResponseDTO> getPendingPermissionRequest(@RequestParam Long orgId,@RequestParam String branchCode,@RequestParam String reportingPersonCode) {
	    String methodName = "getPendingPermissionRequest()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	    	List<Map<String, Object>> permissionRequestVO = employeeMasterService.getPendingPermissionRequest(orgId,branchCode,reportingPersonCode);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Pending PermissionRequest information retrieved successfully ");
	        responseObjectsMap.put("permissionRequestVO", permissionRequestVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve Pending PermissionRequest information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	@GetMapping("/getApprovedPermissionRequestforTeam")
	public ResponseEntity<ResponseDTO> getApprovedPermissionRequestforTeam(@RequestParam Long orgId,@RequestParam String branchCode,@RequestParam String reportingPersonCode) {
	    String methodName = "getApprovedPermissionRequestforTeam()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	    	List<Map<String, Object>> permissionRequestVO = employeeMasterService.getApprovedPermissionRequestforTeam(orgId,branchCode,reportingPersonCode);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Approved PermissionRequest information retrieved successfully ");
	        responseObjectsMap.put("permissionRequestVO", permissionRequestVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve Approved PermissionRequest information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}

}



