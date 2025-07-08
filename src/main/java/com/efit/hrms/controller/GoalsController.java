package com.efit.hrms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

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
import com.efit.hrms.dto.AdditionalGoalsDTO;
import com.efit.hrms.dto.AppraisalPeriodDTO;
import com.efit.hrms.dto.AppraiseeDTO;
import com.efit.hrms.dto.AppraiserDTO;
import com.efit.hrms.dto.GoalsDTO;
import com.efit.hrms.dto.GradeDTO;
import com.efit.hrms.dto.HrReviewDTO;
import com.efit.hrms.dto.KpiKraDTO;
import com.efit.hrms.dto.PreGoalsDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.ScoreDTO;
import com.efit.hrms.dto.SelfGoalsDTO;
import com.efit.hrms.dto.Supervisor1FeedBackDTO;
import com.efit.hrms.dto.WeightageDTO;
import com.efit.hrms.entity.AdditionalGoalsVO;
import com.efit.hrms.entity.AppraisalPeriodVO;
import com.efit.hrms.entity.AppraiseeVO;
import com.efit.hrms.entity.AppraiserVO;
import com.efit.hrms.entity.GoalsVO;
import com.efit.hrms.entity.GradeVO;
import com.efit.hrms.entity.HrReviewVO;
import com.efit.hrms.entity.KpiKraVO;
import com.efit.hrms.entity.PreGoalsVO;
import com.efit.hrms.entity.ScoreVO;
import com.efit.hrms.entity.SelfGoalsVO;
import com.efit.hrms.entity.Supervisor1FeedBackVO;
import com.efit.hrms.entity.WeightageVO;
import com.efit.hrms.service.GoalsControllerService;

@CrossOrigin
@RestController
@RequestMapping("/api/goalsController")
public class GoalsController extends BaseController{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(GoalsController.class);
	
	@Autowired
    GoalsControllerService goalsControllerService;
	
	//PRE GOALS
	
	@PutMapping("/createUpdatePreGoals")
	public ResponseEntity<ResponseDTO> createUpdatePreGoals(@Valid @RequestBody PreGoalsDTO preGoalsDTO) {
		String methodName = "createUpdatePreGoals()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> preGoalsVO = goalsControllerService.createUpdatePreGoals(preGoalsDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, preGoalsVO.get("message"));
	        responseObjectsMap.put("preGoalsVO", preGoalsVO.get("preGoalsVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPreGoalsByOrgId")
	public ResponseEntity<ResponseDTO> getPreGoalsByOrgId(@RequestParam Long orgId) {
		String methodName = "getPreGoalsByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PreGoalsVO> preGoalsVO = new ArrayList<>();
		try {
			preGoalsVO = goalsControllerService.getPreGoalsByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PreGoals information get successfully");
			responseObjectsMap.put("preGoalsVO", preGoalsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "PreGoals information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPreGoalsById")
	public ResponseEntity<ResponseDTO> getPreGoalsById(@RequestParam Long id) {
		String methodName = "getPreGoalsById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<PreGoalsVO> preGoalsVO = null;
		try {
			preGoalsVO = goalsControllerService.getPreGoalsById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PreGoals information get successfully");
			responseObjectsMap.put("preGoalsVO", preGoalsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "PreGoals information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//MASTER
	
	//APPRAISAL
	
	@PutMapping("/createUpdateAppraisalPeriod")
	public ResponseEntity<ResponseDTO> createUpdateAppraisalPeriod(@Valid @RequestBody AppraisalPeriodDTO appraisalPeriodDTO) {
		String methodName = "createUpdateAppraisalPeriod()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> appraisalPeriodVO = goalsControllerService.createUpdateAppraisalPeriod(appraisalPeriodDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, appraisalPeriodVO.get("message"));
	        responseObjectsMap.put("appraisalPeriodVO", appraisalPeriodVO.get("appraisalPeriodVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAppraisalPeriodByOrgId")
	public ResponseEntity<ResponseDTO> getAppraisalPeriodByOrgId(@RequestParam Long orgId) {
		String methodName = "getAppraisalPeriodByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AppraisalPeriodVO> appraisalPeriodVO = new ArrayList<>();
		try {
			appraisalPeriodVO = goalsControllerService.getAppraisalPeriodByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Appraisal information get successfully");
			responseObjectsMap.put("appraisalVO", appraisalPeriodVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Appraisal information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAppraisalPeriodById")
	public ResponseEntity<ResponseDTO> getAppraisalPeriodById(@RequestParam Long id) {
		String methodName = "getAppraisalPeriodById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<AppraisalPeriodVO> appraisalPeriodVO = null;
		try {
			appraisalPeriodVO = goalsControllerService.getAppraisalPeriodById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Appraisal information get successfully");
			responseObjectsMap.put("appraisalVO", appraisalPeriodVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Appraisal information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//Weightage
	
	@PutMapping("/createUpdateWeightage")
	public ResponseEntity<ResponseDTO> createUpdateWeightage(@Valid @RequestBody WeightageDTO weightageDTO) {
		String methodName = "createUpdateWeightage()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> weightageVO = goalsControllerService.createUpdateWeightage(weightageDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, weightageVO.get("message"));
	        responseObjectsMap.put("weightageVO", weightageVO.get("weightageVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getWeightageByOrgId")
	public ResponseEntity<ResponseDTO> getWeightageByOrgId(@RequestParam Long orgId) {
		String methodName = "getWeightageByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<WeightageVO> weightageVO = new ArrayList<>();
		try {
			weightageVO = goalsControllerService.getWeightageByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Weightage information get successfully");
			responseObjectsMap.put("weightageVO", weightageVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Weightage information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getWeightageById")
	public ResponseEntity<ResponseDTO> getWeightageById(@RequestParam Long id) {
		String methodName = "getWeightageById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<WeightageVO> weightageVO = null;
		try {
			weightageVO = goalsControllerService.getWeightageById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Weightage information get successfully");
			responseObjectsMap.put("weightageVO", weightageVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Weightage information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	// GRADE
	
	@PutMapping("/createUpdateGrade")
	public ResponseEntity<ResponseDTO> createUpdateGrade(@Valid @RequestBody GradeDTO gradeDTO) {
		String methodName = "createUpdateGrade()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> gradeVO = goalsControllerService.createUpdateGrade(gradeDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, gradeVO.get("message"));
	        responseObjectsMap.put("gradeVO", gradeVO.get("gradeVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGradeByOrgId")
	public ResponseEntity<ResponseDTO> getGradeByOrgId(@RequestParam Long orgId) {
		String methodName = "getGradeByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GradeVO> gradeVO = new ArrayList<>();
		try {
			gradeVO = goalsControllerService.getGradeByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Grade information get successfully");
			responseObjectsMap.put("gradeVO", gradeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Grade information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGradeById")
	public ResponseEntity<ResponseDTO> getGradeById(@RequestParam Long id) {
		String methodName = "getGradeById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<GradeVO> gradeVO = null;
		try {
			gradeVO = goalsControllerService.getGradeById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Grade information get successfully");
			responseObjectsMap.put("gradeVO", gradeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Grade information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//KPIKRA
	
	@PutMapping("/createUpdateKpiKra")
	public ResponseEntity<ResponseDTO> createUpdateKpiKra(@Valid @RequestBody KpiKraDTO kpiKraDTO) {
		String methodName = "createUpdateKpiKra()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> kpiKraVO = goalsControllerService.createUpdateKpiKra(kpiKraDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, kpiKraVO.get("message"));
	        responseObjectsMap.put("kpiKraVO", kpiKraVO.get("kpiKraVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getKpiKraByOrgId")
	public ResponseEntity<ResponseDTO> getKpiKraByOrgId(@RequestParam Long orgId) {
		String methodName = "getKpiKraByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<KpiKraVO> kpiKraVO = new ArrayList<>();
		try {
			kpiKraVO = goalsControllerService.getKpiKraByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "KpiKra information get successfully");
			responseObjectsMap.put("kpiKraVO", kpiKraVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "KpiKra information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getKpiKraById")
	public ResponseEntity<ResponseDTO> getKpiKraById(@RequestParam Long id) {
		String methodName = "getKpiKraById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<KpiKraVO> kpiKraVO = null;
		try {
			kpiKraVO = goalsControllerService.getKpiKraById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "KpiKra information get successfully");
			responseObjectsMap.put("kpiKraVO", kpiKraVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "KpiKra information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	// GOALS
	
	@PutMapping("/createUpdateGoals")
	public ResponseEntity<ResponseDTO> createUpdateGoals(@Valid @RequestBody GoalsDTO goalsDTO) {
		String methodName = "createUpdateGoals()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> goalsVO = goalsControllerService.createUpdateGoals(goalsDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, goalsVO.get("message"));
	        responseObjectsMap.put("goalsVO", goalsVO.get("goalsVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGoalsByOrgId")
	public ResponseEntity<ResponseDTO> getGoalsByOrgId(@RequestParam Long orgId) {
		String methodName = "getGoalsByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GoalsVO> goalsVO = new ArrayList<>();
		try {
			goalsVO = goalsControllerService.getGoalsByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Goals information get successfully");
			responseObjectsMap.put("goalsVO", goalsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Goals information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getGoalsById")
	public ResponseEntity<ResponseDTO> getGoalsById(@RequestParam Long id) {
		String methodName = "getGoalsById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<GoalsVO> goalsVO = null;
		try {
			goalsVO = goalsControllerService.getGoalsById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Goals information get successfully");
			responseObjectsMap.put("goalsVO", goalsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Goals information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	//APPRAISEE
	
	@PutMapping("/createUpdateAppraisee")
	public ResponseEntity<ResponseDTO> createUpdateAppraisee(@Valid @RequestBody AppraiseeDTO appraiseeDTO) {
		String methodName = "createUpdateAppraisee()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> appraiseeVO = goalsControllerService.createUpdateAppraisee(appraiseeDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, appraiseeVO.get("message"));
	        responseObjectsMap.put("appraiseeVO", appraiseeVO.get("appraiseeVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAppraiseeByOrgId")
	public ResponseEntity<ResponseDTO> getAppraiseeByOrgId(@RequestParam Long orgId) {
		String methodName = "getAppraiseeByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AppraiseeVO> appraiseeVO = new ArrayList<>();
		try {
			appraiseeVO = goalsControllerService.getAppraiseeByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Appraisee information get successfully");
			responseObjectsMap.put("appraiseeVO", appraiseeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Appraisee information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAppraiseeById")
	public ResponseEntity<ResponseDTO> getAppraiseeById(@RequestParam Long id) {
		String methodName = "getAppraiseeById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<AppraiseeVO> appraiseeVO = null;
		try {
			appraiseeVO = goalsControllerService.getAppraiseeById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Appraisee information get successfully");
			responseObjectsMap.put("appraiseeVO", appraiseeVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Appraisee information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getEmployeeDetails")
	public ResponseEntity<ResponseDTO> getEmployeeDetails(@RequestParam Long orgId,@RequestParam String employeeCode) {
		String methodName = "getEmployeeDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = goalsControllerService.getEmployeeDetails(orgId,employeeCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee Details retrieved successfully");
			responseObjectsMap.put("employeeVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Employee Details", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	// SELFGOALS

	@PutMapping("/createUpdateSelfGoals")
	public ResponseEntity<ResponseDTO> createUpdateSelfGoals(@Valid @RequestBody SelfGoalsDTO selfGoalsDTO) {
		String methodName = "createUpdateSelfGoals()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> selfGoalsVO = goalsControllerService.createUpdateSelfGoals(selfGoalsDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, selfGoalsVO.get("message"));
	        responseObjectsMap.put("selfGoalsVO", selfGoalsVO.get("selfGoalsVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getSelfGoalsByOrgId")
	public ResponseEntity<ResponseDTO> getSelfGoalsByOrgId(@RequestParam Long orgId) {
		String methodName = "getSelfGoalsByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<SelfGoalsVO> selfGoalsVO = new ArrayList<>();
		try {
			selfGoalsVO = goalsControllerService.getSelfGoalsByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SelfGoals information get successfully");
			responseObjectsMap.put("selfGoalsVO", selfGoalsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "SelfGoals information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getSelfGoalsById")
	public ResponseEntity<ResponseDTO> getSelfGoalsById(@RequestParam Long id) {
		String methodName = "getSelfGoalsById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<SelfGoalsVO> selfGoalsVO = null;
		try {
			selfGoalsVO = goalsControllerService.getSelfGoalsById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SelfGoals information get successfully");
			responseObjectsMap.put("selfGoalsVO", selfGoalsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "SelfGoals information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAppraiseeFillGrid")
	public ResponseEntity<ResponseDTO> getAppraiseeFillGrid(@RequestParam Long orgId,@RequestParam String employeeCode) {
		String methodName = "getAppraiseeFillGrid()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = goalsControllerService.getAppraiseeFillGrid(orgId,employeeCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Self Goals Details retrieved successfully");
			responseObjectsMap.put("appraiseeFillGrid", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Self Goals", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//APPRAISER
	
	@PutMapping("/createUpdateAppraiser")
	public ResponseEntity<ResponseDTO> createUpdateAppraiser(@Valid @RequestBody AppraiserDTO appraiserDTO) {
		String methodName = "createUpdateAppraiser()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> appraiserVO = goalsControllerService.createUpdateAppraiser(appraiserDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, appraiserVO.get("message"));
	        responseObjectsMap.put("appraiserVO", appraiserVO.get("appraiserVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAppraiserByOrgId")
	public ResponseEntity<ResponseDTO> getAppraiserByOrgId(@RequestParam Long orgId) {
		String methodName = "getAppraiserByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AppraiserVO> appraiserVO = new ArrayList<>();
		try {
			appraiserVO = goalsControllerService.getAppraiserByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Appraiser information get successfully");
			responseObjectsMap.put("appraiserVO", appraiserVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Appraiser information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAppraiserById")
	public ResponseEntity<ResponseDTO> getAppraiserById(@RequestParam Long id) {
		String methodName = "getAppraiserById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<AppraiserVO> appraiserVO = null;
		try {
			appraiserVO = goalsControllerService.getAppraiserById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Appraiser information get successfully");
			responseObjectsMap.put("appraiserVO", appraiserVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Appraiser information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getReportingPerson")
	public ResponseEntity<ResponseDTO> getReportingPerson(@RequestParam Long orgId) {
		String methodName = "getReportingPerson()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = goalsControllerService.getReportingPerson(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Reporting Person Details retrieved successfully");
			responseObjectsMap.put("reportingPersons", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Reporting Person", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getEmpUnderReportingPerson")
	public ResponseEntity<ResponseDTO> getEmpUnderReportingPerson(@RequestParam Long orgId,@RequestParam String ReportingPersonCode) {
		String methodName = "getEmpUnderReportingPerso()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = goalsControllerService.getEmpUnderReportingPerson(orgId,ReportingPersonCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee Details retrieved successfully");
			responseObjectsMap.put("empDetails", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Employee", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAppraiserFillGrid")
	public ResponseEntity<ResponseDTO> getAppraiserFillGrid(@RequestParam Long orgId,@RequestParam String empCode) {
		String methodName = "getAppraiserFillGrid()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = goalsControllerService.getAppraiserFillGrid(orgId,empCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Appraiser FillGrid Details retrieved successfully");
			responseObjectsMap.put("empDetails", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to Appraiser FillGrid", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	// SCORE
	
	@PutMapping("/createUpdateScore")
	public ResponseEntity<ResponseDTO> createUpdateScore(@Valid @RequestBody ScoreDTO scoreDTO) {
		String methodName = "createUpdateScore()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> scoreVO = goalsControllerService.createUpdateScore(scoreDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, scoreVO.get("message"));
	        responseObjectsMap.put("scoreVO", scoreVO.get("scoreVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getScoreByOrgId")
	public ResponseEntity<ResponseDTO> getScoreByOrgId(@RequestParam Long orgId) {
		String methodName = "getScoreByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ScoreVO> scoreVO = new ArrayList<>();
		try {
			scoreVO = goalsControllerService.getScoreByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Score information get successfully");
			responseObjectsMap.put("scoreVO", scoreVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Score information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getScoreById")
	public ResponseEntity<ResponseDTO> getScoreById(@RequestParam Long id) {
		String methodName = "getScoreById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<ScoreVO> scoreVO = null;
		try {
			scoreVO = goalsControllerService.getScoreById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Score information get successfully");
			responseObjectsMap.put("scoreVO", scoreVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Score information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//HRREVIEW
	
	@PutMapping("/createUpdateHrReview")
	public ResponseEntity<ResponseDTO> createUpdateHrReview(@Valid @RequestBody HrReviewDTO hrReviewDTO) {
		String methodName = "createUpdateHrReview()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> hrReviewVO = goalsControllerService.createUpdateHrReview(hrReviewDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, hrReviewVO.get("message"));
	        responseObjectsMap.put("hrReviewVO", hrReviewVO.get("hrReviewVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getHrReviewByOrgId")
	public ResponseEntity<ResponseDTO> getHrReviewByOrgId(@RequestParam Long orgId) {
		String methodName = "getHrReviewByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<HrReviewVO> hrReviewVO = new ArrayList<>();
		try {
			hrReviewVO = goalsControllerService.getHrReviewByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "HrReview information get successfully");
			responseObjectsMap.put("hrReviewVO", hrReviewVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "HrReview  information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getHrReviewById")
	public ResponseEntity<ResponseDTO> getHrReviewById(@RequestParam Long id) {
		String methodName = "getHrReviewById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<HrReviewVO> hrReviewVO = null;
		try {
			hrReviewVO = goalsControllerService.getHrReviewById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "HrReview information get successfully");
			responseObjectsMap.put("hrReviewVO", hrReviewVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "HrReview information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
//	@PutMapping("/updateTestingAppraisee")
//	public ResponseEntity<ResponseDTO> updateTestingAppraisee(
//	        @RequestParam(required = false) Long orgId,
//	        @RequestParam(required = false) String reportingHead) {
//	    
//	    String methodName = "updateTestingAppraisee()";
//	    LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//	    String errorMsg = null;
//	    Map<String, Object> responseObjectsMap = new HashMap<>();
//	    ResponseDTO responseDTO;
//
//	    try {
//	        // Assuming this updates the ticket status internally
//	        AppraiseeVO appraiseeVO = goalsControllerService.updateTestingAppraisee(orgId,reportingHead);
//
//	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Ticket status updated successfully");
//	        responseObjectsMap.put("appraiseeVO", appraiseeVO);
//	        responseDTO = createServiceResponse(responseObjectsMap);
//	    } catch (Exception e) {
//	        errorMsg = e.getMessage();
//	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//	        responseDTO = createServiceResponseError(responseObjectsMap, "Ticket status update failed", errorMsg);
//	    }
//
//	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//	    return ResponseEntity.ok().body(responseDTO);
//	}
	
	//SUPERVISOR1FEEDBACK
	
	@PutMapping("/createUpdateSupervisor1FeedBack")
	public ResponseEntity<ResponseDTO> createUpdateSupervisor1FeedBack(@Valid @RequestBody Supervisor1FeedBackDTO supervisor1FeedBackDTO) {
		String methodName = "createUpdateSupervisor1FeedBack()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> supervisor1FeedBackVO = goalsControllerService.createUpdateSupervisor1FeedBack(supervisor1FeedBackDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, supervisor1FeedBackVO.get("message"));
	        responseObjectsMap.put("supervisor1FeedBackVO", supervisor1FeedBackVO.get("supervisor1FeedBackVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getSupervisor1FeedbackByOrgId")
	public ResponseEntity<ResponseDTO> getSupervisor1FeedbackByOrgId(@RequestParam Long orgId) {
		String methodName = "getSupervisor1FeedbackByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Supervisor1FeedBackVO> supervisor1FeedBackVO = new ArrayList<>();
		try {
			supervisor1FeedBackVO = goalsControllerService.getSupervisor1FeedbackByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Supervisor 1FeedBack information get successfully");
			responseObjectsMap.put("supervisor1FeedBackVO", supervisor1FeedBackVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Supervisor 1FeedBack information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getSupervisor1FeedbackById")
	public ResponseEntity<ResponseDTO> getSupervisor1FeedbackById(@RequestParam Long id) {
		String methodName = "getSupervisor1FeedbackById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<Supervisor1FeedBackVO> supervisor1FeedBackVO = null;
		try {
			supervisor1FeedBackVO = goalsControllerService.getSupervisor1FeedbackById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Supervisor 1FeedBack information get successfully");
			responseObjectsMap.put("supervisor1FeedBackVO", supervisor1FeedBackVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Supervisor 1FeedBack information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getPreGoalsApprovedReport")
	public ResponseEntity<ResponseDTO> getPreGoalsApprovedReport(@RequestParam Long orgId,@RequestParam String finYear,@RequestParam String supCode) {
		String methodName = "getPreGoalsApprovedReport()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = goalsControllerService.getPreGoalsApprovedReport(orgId,finYear,supCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Approved Report Details retrieved successfully");
			responseObjectsMap.put("employeeVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Approved Report", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//Additional Goals
	
	@PutMapping("/createUpdateAdditionalGoals")
	public ResponseEntity<ResponseDTO> createUpdateAdditionalGoals(@Valid @RequestBody AdditionalGoalsDTO additionalGoalsDTO) {
		String methodName = "createUpdateAdditionalGoals()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
	        Map<String, Object> additionalGoalsVO = goalsControllerService.createUpdateAdditionalGoals(additionalGoalsDTO);
	        responseObjectsMap.put(CommonConstant.STRING_MESSAGE, additionalGoalsVO.get("message"));
	        responseObjectsMap.put("additionalGoalsVO", additionalGoalsVO.get("additionalGoalsVO")); // Corrected key
	        responseDTO = createServiceResponse(responseObjectsMap);
	    } catch (Exception e) {
	        errorMsg = e.getMessage();
	        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
	        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
	    }
	    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
	    return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAdditionalGoalsByOrgId")
	public ResponseEntity<ResponseDTO> getAdditionalGoalsByOrgId(@RequestParam Long orgId) {
		String methodName = "getAdditionalGoalsByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AdditionalGoalsVO> additionalGoalsVO = new ArrayList<>();
		try {
			additionalGoalsVO = goalsControllerService.getAdditionalGoalsByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Additional Goals information get successfully");
			responseObjectsMap.put("additionalGoalsVO", additionalGoalsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Additional Goals information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAdditionalGoalsById")
	public ResponseEntity<ResponseDTO> getAdditionalGoalsById(@RequestParam Long id) {
		String methodName = "getAdditionalGoalsById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Optional<AdditionalGoalsVO> additionalGoalsVO = null;
		try {
			additionalGoalsVO = goalsControllerService.getAdditionalGoalsById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Additional Goals information get successfully");
			responseObjectsMap.put("additionalGoalsVO", additionalGoalsVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Additional Goals information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAdditionalGoalsDropDownApis")
	public ResponseEntity<ResponseDTO> getAdditionalGoalsDropDownApis(@RequestParam Long orgId,@RequestParam String finYear,@RequestParam String branchCode,
			@RequestParam String designation) {
		String methodName = "getAdditionalGoalsDropDownApis()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mapp = new ArrayList<>();

		try {
			mapp = goalsControllerService.getAdditionalGoalsDropDownApis(orgId,finYear,branchCode,designation);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Approved Report Details retrieved successfully");
			responseObjectsMap.put("employeeVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Approved Report", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
//	@GetMapping("/getAllAdditionalGoals")
//	public ResponseEntity<ResponseDTO> getAllAdditionalGoals() {
//		String methodName = "getAllAdditionalGoals()";
//		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//		String errorMsg = null;
//		Map<String, Object> responseObjectsMap = new HashMap<>();
//		ResponseDTO responseDTO = null;
//		List<AdditionalGoalsVO> additionalGoalsVO = new ArrayList<>();
//		try {
//			additionalGoalsVO = goalsControllerService.getAllAdditionalGoals();
//		} catch (Exception e) {
//			errorMsg = e.getMessage();
//			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//		}
//		if (StringUtils.isBlank(errorMsg)) {
//			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Additional Goals information get successfully");
//			responseObjectsMap.put("additionalGoalsVO", additionalGoalsVO);
//			responseDTO = createServiceResponse(responseObjectsMap);
//		} else {
//			responseDTO = createServiceResponseError(responseObjectsMap, "Additional Goals information receive failed", errorMsg);
//		}
//		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//		return ResponseEntity.ok().body(responseDTO);
//	}
}
