package com.efit.hrms.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.efit.hrms.dto.ContractMasterDTO;
import com.efit.hrms.dto.GroupDTO;
import com.efit.hrms.dto.GroupSalaryStructureDTO;
import com.efit.hrms.dto.OtMasterDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.ShiftAssignDTO;
import com.efit.hrms.dto.ShiftMasterDTO;
import com.efit.hrms.entity.ContractMasterVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.GroupSalaryStructureVO;
import com.efit.hrms.entity.GroupVO;
import com.efit.hrms.entity.OtMasterVO;
import com.efit.hrms.entity.ShiftAssignVO;
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
	public ResponseEntity<ResponseDTO> createUpdateShiftMaster(@RequestBody List<ShiftMasterDTO> shiftMasterDTOList) {
	    String methodName = "createUpdateShiftMaster()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    String errorMsg = null;
	    ResponseDTO responseDTO = null;
	    try {
	        Map<String, Object> shiftMasterVO = shiftMasterService.createUpdateShiftMaster(shiftMasterDTOList);

	        // ✅ Correct keys
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, shiftMasterVO.get("message"));
	        responseObjectsMap.put("shiftMasterList", shiftMasterVO.get("shiftMasterList")); // fix this

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
	
	@PutMapping("/createUpdateOtMaster")
	public ResponseEntity<ResponseDTO> createUpdateOtMaster(@RequestBody OtMasterDTO otMasterDTO) {
		String methodName = "createUpdateOtMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> otMasterVO = shiftMasterService.createUpdateOtMaster(otMasterDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, otMasterVO.get("message"));
			responseObjectsMap.put("otMasterVO", otMasterVO.get("otMasterVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getAllOtMasterByOrgId")
	public ResponseEntity<ResponseDTO> getAllOtMasterByOrgId(@RequestParam Long orgId) {
	    String methodName = "getAllOtMasterByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	    	List<OtMasterVO> otMasterVO = Optional.ofNullable(shiftMasterService.getAllOtMasterByOrgId(orgId))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "OtMaster information retrieved successfully By OrgId");
	        responseObjectsMap.put("otMasterVO", otMasterVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve OtMaster information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	
	@GetMapping("/getOtMasterById")
	public ResponseEntity<ResponseDTO> getOtMasterById(@RequestParam Long id) {
	    String methodName = "getOtMasterById()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Fetch SalaryHeadsVO safely, preventing null
	        OtMasterVO otMasterVO = Optional.ofNullable(shiftMasterService.getOtMasterById(id))
	                                             .orElseThrow(() -> new RuntimeException("otMaster not found for ID: " + id));

	        // Success response
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "otMaster information retrieved successfully by ID");
	        responseObjectsMap.put("otMasterVO", otMasterVO);
	        responseDTO = createServiceResponse(responseObjectsMap);

	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Error response
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve otMaster information by ID", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	
	//ShiftAssign
	
	@PutMapping("/createUpdateShiftAssign")
	public ResponseEntity<ResponseDTO> createUpdateShiftAssign(@RequestBody ShiftAssignDTO shiftAssignDTO) {
		String methodName = "createUpdateShiftAssign()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> shiftAssignVO = shiftMasterService.createUpdateShiftAssign(shiftAssignDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, shiftAssignVO.get("message"));
			responseObjectsMap.put("shiftAssignVO", shiftAssignVO.get("shiftAssignVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getShiftAssignById")
	public ResponseEntity<ResponseDTO> getShiftAssignById(@RequestParam Long id) {
	    String methodName = "getShiftAssignById()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        // Fetch SalaryHeadsVO safely, preventing null
	        ShiftAssignVO shiftAssignVO = Optional.ofNullable(shiftMasterService.getShiftAssignById(id))
	                                             .orElseThrow(() -> new RuntimeException("ShiftAssign not found for ID: " + id));

	        // Success response
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ShiftAssign information retrieved successfully by ID");
	        responseObjectsMap.put("shiftAssignVO", shiftAssignVO);
	        responseDTO = createServiceResponse(responseObjectsMap);

	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        // Error response
	        responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve ShiftAssign information by ID", errorMsg);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	@GetMapping("/getAllShiftAssignByOrgId")
	public ResponseEntity<ResponseDTO> getAllShiftAssignByOrgId(@RequestParam Long orgId) {
	    String methodName = "getAllShiftAssignByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	    	List<ShiftAssignVO> shiftAssignVO = Optional.ofNullable(shiftMasterService.getAllShiftAssignByOrgId(orgId))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ShiftAssign information retrieved successfully By OrgId");
	        responseObjectsMap.put("shiftAssignVO", shiftAssignVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve ShiftAssign information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	
	@GetMapping("/getAllShiftMasterByOrgIdAndShiftAndBranchCode")
	public ResponseEntity<ResponseDTO> getAllShiftMasterByOrgIdAndShiftAndBranchCode(@RequestParam Long orgId,@RequestParam String shift ,@RequestParam String shiftCode,@RequestParam String branchCode) {
	    String methodName = "getAllShiftMasterByOrgIdAndShiftAndBranchCode()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	    	List<ShiftMasterVO> shiftMasterVO = Optional.ofNullable(shiftMasterService.getAllShiftMasterByOrgIdAndShiftAndBranchCode(orgId,shift,shiftCode,branchCode))
	                                                    .orElseGet(Collections::emptyList);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ShiftMaster information retrieved successfully By OrgId And Type");
	        responseObjectsMap.put("shiftMasterVO", shiftMasterVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve ShiftMaster information By OrgId And Type", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	
	//groupstructure
	
	@PutMapping("/createUpdateGroupMaster")
	public ResponseEntity<ResponseDTO> createUpdateGroup(@Valid @RequestBody GroupDTO groupDTO) {
		String methodName = "createUpdateGroup()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> groupVO = shiftMasterService.createUpdateGroup(groupDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, groupVO.get("message"));
	        responseObjectsMap.put("groupVO", groupVO.get("groupVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGroupMasterByOrgId")
	public ResponseEntity<ResponseDTO> getGroupByOrgId(@RequestParam Long orgId) {
		String methodName = "getGroupByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GroupVO> groupVO = new ArrayList<>();
		try {
			groupVO = shiftMasterService.getGroupByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group information get successfully");
			responseObjectsMap.put("groupVO", groupVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGroupMasterById")
	public ResponseEntity<ResponseDTO> getPreGroupById(@RequestParam Long id) {
		String methodName = "getPreGroupById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<GroupVO> groupVO = null;
		try {
			groupVO = shiftMasterService.getPreGroupById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group information get successfully");
			responseObjectsMap.put("groupVO", groupVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	//groupsalarystructure
	
	@PutMapping("/createUpdateGroupSalaryStructure")
	public ResponseEntity<ResponseDTO> createUpdateGroupSalaryStructure(@RequestBody GroupSalaryStructureDTO groupSalaryStructureDTO) {
		String methodName = "createUpdateGroupSalaryStructure()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> groupSalaryStructureVO = shiftMasterService.createUpdateGroupSalaryStructure(groupSalaryStructureDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, groupSalaryStructureVO.get("message"));
			responseObjectsMap.put("groupSalaryStructureVO", groupSalaryStructureVO.get("groupSalaryStructureVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGroupSalaryStructureByOrgId")
	public ResponseEntity<ResponseDTO> getGroupSalaryStructureByOrgId(@RequestParam Long orgId) {
		String methodName = "getGroupSalaryStructureByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GroupSalaryStructureVO> groupSalaryStructureVO = new ArrayList<>();
		try {
			groupSalaryStructureVO = shiftMasterService.getGroupSalaryStructureByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "GroupSalaryStructure information get successfully");
			responseObjectsMap.put("groupSalaryStructureVO", groupSalaryStructureVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "GroupSalaryStructure information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGroupSalaryStructureById")
	public ResponseEntity<ResponseDTO> getGroupSalaryStructureById(@RequestParam Long id) {
		String methodName = "getGroupSalaryStructureById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<GroupSalaryStructureVO> groupSalaryStructureVO = null;
		try {
			groupSalaryStructureVO = shiftMasterService.getGroupSalaryStructureById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "GroupSalaryStructure information get successfully");
			responseObjectsMap.put("groupSalaryStructureVO", groupSalaryStructureVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "GroupSalaryStructure information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getGroupMasterByOrgIdAndGroup")
	public ResponseEntity<ResponseDTO> getGroupMasterByOrgIdAndGroup(@RequestParam Long orgId,@RequestParam String groupName) {
		String methodName = "getGroupMasterByOrgIdAndGroup()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GroupVO> groupVO = new ArrayList<>();
		try {
			groupVO = shiftMasterService.getGroupMasterByOrgIdAndGroup(orgId,groupName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Group information get successfully");
			responseObjectsMap.put("groupVO", groupVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Group information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	//shiftassignfilter
	
	@GetMapping("/getShiftAssignByOrgIdAndType")
	public ResponseEntity<ResponseDTO> getShiftAssignByOrgIdAndType(@RequestParam Long orgId,@RequestParam String type,@RequestParam(required = false) String contractor,@RequestParam String department) {
		String methodName = "getShiftAssignByOrgIdAndType()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<EmployeeVO> employeeVO = new ArrayList<>();
		try {
			employeeVO = shiftMasterService.getShiftAssignByOrgIdAndType(orgId,type,contractor,department);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee information get successfully");
			responseObjectsMap.put("employeeVO", employeeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Employee information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	
	@GetMapping("/getAllEmployeeAndShiftMasterDetails")
	public ResponseEntity<ResponseDTO> getAllEmployeeAndShiftMasterDetails(
	    @RequestParam Long orgId,
	    @RequestParam String type,
	    @RequestParam(required = false) String contractor,
	    @RequestParam String department,
	    @RequestParam String shift,
	    @RequestParam String shiftCode,
	    @RequestParam String branchCode,
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveFrom // ✅ Fix
	) {
		String methodName = "getAllEmployeeAndShiftMasterDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;
		List<Map<String, Object>> shiftAssignVO;

		try {
			shiftAssignVO = shiftMasterService.getAllEmployeeAndShiftMasterDetails( orgId,type,contractor,department,shift,shiftCode,branchCode,effectiveFrom);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ShiftAssign details retrieved successfully");
			responseObjectsMap.put("shiftAssignVO", shiftAssignVO); // ✅ Correct key name
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve ShiftAssign details", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

}
