package com.efit.hrms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.efit.hrms.dto.AdditionalGoalsDTO;
import com.efit.hrms.dto.AppraisalPeriodDTO;
import com.efit.hrms.dto.AppraiseeDTO;
import com.efit.hrms.dto.AppraiserDTO;
import com.efit.hrms.dto.GoalsDTO;
import com.efit.hrms.dto.GradeDTO;
import com.efit.hrms.dto.HrReviewDTO;
import com.efit.hrms.dto.KpiKraDTO;
import com.efit.hrms.dto.PreGoalsDTO;
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
import com.efit.hrms.exception.ApplicationException;

@Service
public interface GoalsControllerService {
	
	//PREGOALS

	Map<String, Object> createUpdatePreGoals(@Valid PreGoalsDTO preGoalsDTO) throws ApplicationException;

	List<PreGoalsVO> getPreGoalsByOrgId(Long orgId);

	Optional<PreGoalsVO> getPreGoalsById(Long id);
	
	//MASTER
	
	//APPRAISAL Period

	Map<String, Object> createUpdateAppraisalPeriod(@Valid AppraisalPeriodDTO appraisalPeriodDTO) throws ApplicationException;

	List<AppraisalPeriodVO> getAppraisalPeriodByOrgId(Long orgId);

	Optional<AppraisalPeriodVO> getAppraisalPeriodById(Long id);
	
	//Weightage

	Map<String, Object> createUpdateWeightage(@Valid WeightageDTO weightageDTO) throws ApplicationException;

	List<WeightageVO> getWeightageByOrgId(Long orgId);

	Optional<WeightageVO> getWeightageById(Long id);
	
	// GRADE
	
	Map<String, Object> createUpdateGrade(@Valid GradeDTO gradeDTO) throws ApplicationException;

	List<GradeVO> getGradeByOrgId(Long orgId);

	Optional<GradeVO> getGradeById(Long id);
	
	//KPIKRA

	Map<String, Object> createUpdateKpiKra(@Valid KpiKraDTO kpiKraDTO) throws ApplicationException;

	List<KpiKraVO> getKpiKraByOrgId(Long orgId);

	Optional<KpiKraVO> getKpiKraById(Long id);
	
	//GOALS

	Map<String, Object> createUpdateGoals(@Valid GoalsDTO goalsDTO) throws ApplicationException;

	List<GoalsVO> getGoalsByOrgId(Long orgId);

	Optional<GoalsVO> getGoalsById(Long id);
	
	//APPRAISEE

	Map<String, Object> createUpdateAppraisee(@Valid AppraiseeDTO appraiseeDTO) throws ApplicationException;

	List<AppraiseeVO> getAppraiseeByOrgId(Long orgId);

	Optional<AppraiseeVO> getAppraiseeById(Long id);

	List<Map<String, Object>> getEmployeeDetails(Long orgId, String employeeCode);
	
	//SELFGOALS

	Map<String, Object> createUpdateSelfGoals(@Valid SelfGoalsDTO selfGoalsDTO) throws ApplicationException;

	List<SelfGoalsVO> getSelfGoalsByOrgId(Long orgId);

	Optional<SelfGoalsVO> getSelfGoalsById(Long id);

	List<Map<String, Object>> getAppraiseeFillGrid(Long orgId, String employeeCode);
	
	//APPRAISER

	Map<String, Object> createUpdateAppraiser(@Valid AppraiserDTO appraiserDTO) throws ApplicationException;

	List<AppraiserVO> getAppraiserByOrgId(Long orgId);

	Optional<AppraiserVO> getAppraiserById(Long id);

	List<Map<String, Object>> getReportingPerson(Long orgId);

	List<Map<String, Object>> getEmpUnderReportingPerson(Long orgId, String reportingPersonCode);

	List<Map<String, Object>> getAppraiserFillGrid(Long orgId, String empCode);
	
	//SCORE

	Map<String, Object> createUpdateScore(@Valid ScoreDTO scoreDTO) throws ApplicationException;

	List<ScoreVO> getScoreByOrgId(Long orgId);

	Optional<ScoreVO> getScoreById(Long id);
	
	//HRREVIEW

	Map<String, Object> createUpdateHrReview(@Valid HrReviewDTO hrReviewDTO) throws ApplicationException;

	List<HrReviewVO> getHrReviewByOrgId(Long orgId);

	Optional<HrReviewVO> getHrReviewById(Long id);

	//AppraiseeVO updateTestingAppraisee(Long orgId, String reportingHead);
	
//	SUPERVISOR1FEEDBACK

	Map<String, Object> createUpdateSupervisor1FeedBack(@Valid Supervisor1FeedBackDTO supervisor1FeedBackDTO) throws ApplicationException;

	List<Supervisor1FeedBackVO> getSupervisor1FeedbackByOrgId(Long orgId);

	Optional<Supervisor1FeedBackVO> getSupervisor1FeedbackById(Long id);
	
	//PreGoals Approved Report

	List<Map<String, Object>> getPreGoalsApprovedReport(Long orgId, String finYear, String supCode);
	
	//AdditionalGoals

	Map<String, Object> createUpdateAdditionalGoals(@Valid AdditionalGoalsDTO additionalGoalsDTO) throws ApplicationException;

	Optional<AdditionalGoalsVO> getAdditionalGoalsById(Long id);

	List<AdditionalGoalsVO> getAdditionalGoalsByOrgId(Long orgId);

	
	//Additional Goals DropDown

	List<Map<String, Object>> getAdditionalGoalsDropDownApis(Long orgId, String finYear, String branchCode,
			String designation);

	// List<AdditionalGoalsVO> getAllAdditionalGoals();

}
