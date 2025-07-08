package com.efit.hrms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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
import com.efit.hrms.dto.DeclarationDTO;
import com.efit.hrms.dto.DeclarationDateDTO;
import com.efit.hrms.dto.OneCroreFiveLacDeductionsDTO;
import com.efit.hrms.dto.OtherDeductionsDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.TaxSavingAllowancesDTO;
import com.efit.hrms.entity.DeclarationDateVO;
import com.efit.hrms.entity.DeclarationVO;
import com.efit.hrms.entity.HousePropertyVO;
import com.efit.hrms.entity.OneCroreFiveLacDeductionsVO;
import com.efit.hrms.entity.OtherDeductionsVO;
import com.efit.hrms.service.ManageTaxService;

@CrossOrigin
@RestController
@RequestMapping("/api/managetax")
public class ManageTaxController extends BaseController{

	
	public static final Logger LOGGER = LoggerFactory.getLogger(ManageTaxController.class);

	@Autowired
	ManageTaxService manageTaxService;
	
	
	@PutMapping("/createUpdateDeclarationDate")
	public ResponseEntity<ResponseDTO> createUpdateDeclarationDate(@Valid @RequestBody DeclarationDateDTO declarationDateDTO) {
	    String methodName = "createUpdateDeclarationDate()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        Map<String, Object> resultMap = manageTaxService.createUpdateDeclarationDate(declarationDateDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, resultMap.get("message"));
	        responseObjectsMap.put("declarationDateVO", resultMap.get("declarationDateVO"));
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok(responseDTO);
	}

	
	@GetMapping("getDeclarationDateById")
	public ResponseEntity<ResponseDTO> getDeclarationDateById(@RequestParam Long id) {
		String methodName = "getDeclarationDateById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		DeclarationDateVO declarationDateVO = null;
		try {
			declarationDateVO = manageTaxService.getDeclarationDateById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "declarationDate found by ID");
			responseObjectsMap.put("declarationDateVO", declarationDateVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "declarationDate not found for ID: " + id;
			responseDTO = createServiceResponseError(responseObjectsMap, "declarationDate not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAllDeclarationDateByOrgId")
	public ResponseEntity<ResponseDTO> getAllDeclarationDateByOrgId(@RequestParam Long orgId) {
	    String methodName = "getAllDeclarationDateByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<DeclarationDateVO> declarationDateVO = manageTaxService.getAllDeclarationDateByOrgId(orgId);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DeclarationDate information retrieved successfully By OrgId");
	        responseObjectsMap.put("declarationDateVO", declarationDateVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve DeclarationDate information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	//Declaration
	
	@GetMapping("getDeclarationById")
	public ResponseEntity<ResponseDTO> getDeclarationById(@RequestParam Long id,@RequestParam String finYear) {
		String methodName = "getDeclarationById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		DeclarationVO declarationVO = null;
		try {
			declarationVO = manageTaxService.getDeclarationById(id,finYear);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "declaration found by ID");
			responseObjectsMap.put("declarationVO", declarationVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "declaration not found for ID: " + id;
			responseDTO = createServiceResponseError(responseObjectsMap, "declaration not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@GetMapping("/getAllDeclarationByOrgId")
	public ResponseEntity<ResponseDTO> getAllDeclarationByOrgId(@RequestParam Long orgId,@RequestParam String finYear) {
	    String methodName = "getAllDeclarationByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<DeclarationVO> declarationVO = manageTaxService.getAllDeclarationByOrgId(orgId,finYear);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Declaration information retrieved successfully By OrgId");
	        responseObjectsMap.put("declarationVO", declarationVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve Declaration information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	@PutMapping("/createUpdateDeclaration")
	public ResponseEntity<ResponseDTO> createUpdateDeclaration(@RequestBody DeclarationDTO declarationDTO) {
	    String methodName = "createUpdateDeclaration()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;

	    try {
	        Map<String, Object> declarationVO = manageTaxService.createUpdateDeclaration(declarationDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, declarationVO.get("message"));
	        responseObjectsMap.put("declarationVO", declarationVO.get("declarationVO"));
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        LOGGER.error("Error in {}: {}", methodName, e.getMessage(), e);
	        responseDTO = createServiceResponseError(responseObjectsMap, e.getMessage(), e.getMessage());
	    }

	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok(responseDTO);
	}

	@PutMapping("/saveOneCroreFiveLacDeductionsList")
	public ResponseEntity<ResponseDTO> saveOneCroreFiveLacDeductionsList(@RequestBody List<OneCroreFiveLacDeductionsDTO> dtoList) {
	    Map<String, Object> paramObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = new ResponseDTO();

	    try {
	        Map<String, Object> result = manageTaxService.saveOneCroreFiveLacDeductionsList(dtoList);

	        paramObjectsMap.put("oneCroreFiveLacDeductionsVO", result.get("oneCroreFiveLacDeductionsVO"));
	        paramObjectsMap.put("message", result.get("message"));

	        responseDTO.setStatusFlag("Ok");
	        responseDTO.setStatus(true);
	        responseDTO.setParamObjectsMap(paramObjectsMap);
	    } catch (Exception e) {
	        responseDTO.setStatusFlag("Error");
	        responseDTO.setStatus(false);
	        paramObjectsMap.put("message", e.getMessage());
	        responseDTO.setParamObjectsMap(paramObjectsMap);
	    }

	    return ResponseEntity.ok(responseDTO);
	}
	
	
	@GetMapping("/getOneCroreFiveLacDeductionsByOrgId")
	public ResponseEntity<ResponseDTO> getOneCroreFiveLacDeductionsByOrgId(@RequestParam Long orgId) {
	    String methodName = "getOneCroreFiveLacDeductionsByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<OneCroreFiveLacDeductionsVO> oneCroreFiveLacDeductionsVO = manageTaxService.getOneCroreFiveLacDeductionsByOrgId(orgId);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "OneCroreFiveLacDeductions information retrieved successfully By OrgId");
	        responseObjectsMap.put("oneCroreFiveLacDeductionsVO", oneCroreFiveLacDeductionsVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve OneCroreFiveLacDeductions information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}

	@PutMapping("/saveOtherDeductions")
	public ResponseEntity<ResponseDTO> saveOtherDeductions(@RequestBody List<OtherDeductionsDTO> dtoList) {
	    Map<String, Object> paramObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = new ResponseDTO();

	    try {
	        Map<String, Object> result = manageTaxService.saveOtherDeductions(dtoList);

	        paramObjectsMap.put("otherDeductionsVO", result.get("otherDeductionsVO"));
	        paramObjectsMap.put("message", result.get("message"));

	        responseDTO.setStatusFlag("Ok");
	        responseDTO.setStatus(true);
	        responseDTO.setParamObjectsMap(paramObjectsMap);

	    } catch (Exception e) {
	        responseDTO.setStatusFlag("Error");
	        responseDTO.setStatus(false);
	        paramObjectsMap.put("message", e.getMessage());
	        responseDTO.setParamObjectsMap(paramObjectsMap);
	    }

	    return ResponseEntity.ok(responseDTO);
	}
	
	@GetMapping("/getOtherDeductionsByOrgId")
	public ResponseEntity<ResponseDTO> getOtherDeductionsByOrgId(@RequestParam Long orgId) {
	    String methodName = "getOtherDeductionsByOrgId()";
	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);

	    Map<String, Object> responseObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO;
	    
	    try {
	        // Fetch Salary Heads and handle nulls safely
	        List<OtherDeductionsVO> otherDeductionsVO = manageTaxService.getOtherDeductionsByOrgId(orgId);
	        
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "OtherDeductions information retrieved successfully By OrgId");
	        responseObjectsMap.put("otherDeductionsVO", otherDeductionsVO);
	        responseDTO = createServiceResponse(responseObjectsMap);
	        
	        LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	        return ResponseEntity.ok(responseDTO);

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);

	        responseDTO = createServiceResponseError(responseObjectsMap, 
	                     "Failed to retrieve otherDeductionsVO information By OrgId", errorMsg);
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
	    }
	}
	
	@PutMapping("/saveTaxSavingAllowances")
	public ResponseEntity<ResponseDTO> saveTaxSavingAllowances(@RequestBody List<TaxSavingAllowancesDTO> dtoList) {
	    Map<String, Object> paramObjectsMap = new HashMap<>();
	    ResponseDTO responseDTO = new ResponseDTO();

	    try {
	        Map<String, Object> result = manageTaxService.saveTaxSavingAllowances(dtoList);

	        paramObjectsMap.put("taxSavingAllowancesVO", result.get("taxSavingAllowancesVO"));
	        paramObjectsMap.put("message", result.get("message"));

	        responseDTO.setStatusFlag("Ok");
	        responseDTO.setStatus(true);
	        responseDTO.setParamObjectsMap(paramObjectsMap);

	    } catch (Exception e) {
	        responseDTO.setStatusFlag("Error");
	        responseDTO.setStatus(false);
	        paramObjectsMap.put("message", e.getMessage());
	        responseDTO.setParamObjectsMap(paramObjectsMap);
	    }

	    return ResponseEntity.ok(responseDTO);
	}


	//upload proof OneCroreFiveLacDeductions
	@PostMapping("/uploadOneCroreFiveLacDeductionsInBloob")
	public ResponseEntity<ResponseDTO> uploadHolidayImageInBloob(@RequestParam("file") MultipartFile file,
			@RequestParam Long id) {
		String methodName = "uploadOneCroreFiveLacDeductionsInBloob()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		OneCroreFiveLacDeductionsVO oneCroreFiveLacDeductionsVO = null;
		try {
			oneCroreFiveLacDeductionsVO = manageTaxService.uploadOneCroreFiveLacDeductionsInBloob(file, id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error("Unable To Upload uploadOneCroreFiveLacDeductions", methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "OneCroreFiveLacDeductions Successfully Upload");
			responseObjectsMap.put("oneCroreFiveLacDeductionsVO", oneCroreFiveLacDeductionsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "OneCroreFiveLacDeductions Upload Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//OtherDeductions
	
	@PostMapping("/uploadOtherDeductionInBloob")
	public ResponseEntity<ResponseDTO> uploadOtherDeductionInBloob(@RequestParam("file") MultipartFile file,
			@RequestParam Long id) {
		String methodName = "uploadOtherDeductionInBloob()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		OtherDeductionsVO otherDeclarationVO = null;
		try {
			otherDeclarationVO = manageTaxService.uploadOtherDeductionInBloob(file, id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error("Unable To Upload uploadOneCroreFiveLacDeductions", methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "otherDeclaration Successfully Upload");
			responseObjectsMap.put("otherDeclarationVO", otherDeclarationVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "otherDeclaration Upload Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	//HouseProperty
	
	@PostMapping("/uploadHousePropertyInBloob")
	public ResponseEntity<ResponseDTO> uploadHousePropertyInBloob(@RequestParam("file") MultipartFile file,
			@RequestParam Long declarationId,@RequestParam(required = false) Long housePropertyId) {
		String methodName = "uploadHousePropertyInBloob()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Map<String, Object> housePropertyVO = null;
		try {
			housePropertyVO = manageTaxService.uploadHousePropertyInBloob(file,declarationId,housePropertyId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error("Unable To Upload uploadHousePropertyInBloob", methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "houseProperty Successfully Upload");
			responseObjectsMap.put("housePropertyVO", housePropertyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "houseProperty Upload Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	//IncomeFromOtherSources
	
	
	@PostMapping("/uploadIncomeFromOtherSourcesInBloob")
	public ResponseEntity<ResponseDTO> uploadIncomeFromOtherSourcesInBloob(@RequestParam("file") MultipartFile file,
			@RequestParam Long declarationId,@RequestParam(required = false) Long incomeFromOtherSourcesId) {
		String methodName = "uploadIncomeFromOtherSourcesInBloob()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Map<String, Object> incomeFromOtherSourcesVO = null;
		try {
			incomeFromOtherSourcesVO = manageTaxService.uploadIncomeFromOtherSourcesInBloob(file,declarationId,incomeFromOtherSourcesId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error("Unable To Upload uploadIncomeFromOtherSourcesInBloob", methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "IncomeFromOtherSources Successfully Upload");
			responseObjectsMap.put("incomeFromOtherSourcesVO", incomeFromOtherSourcesVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "IncomeFromOtherSources Upload Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	//MyDeclaration
	
	@GetMapping("/getMyDeclarations")
	public ResponseEntity<ResponseDTO> getMyDeclarations(@RequestParam Long declarationId) {
		String methodName = "getMyDeclarations()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = manageTaxService.getMyDeclarations(declarationId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "MyDeclaration Details retrieved successfully");
			responseObjectsMap.put("myDeclarations", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "MyDeclaration Details to retrieve Charge Type", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

}
