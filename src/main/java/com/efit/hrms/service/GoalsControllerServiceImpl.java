package com.efit.hrms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efit.hrms.dto.AdditionalGoalsDTO;
import com.efit.hrms.dto.AppraisalPeriodDTO;
import com.efit.hrms.dto.AppraiseeDTO;
import com.efit.hrms.dto.AppraiseeDetailsDTO;
import com.efit.hrms.dto.AppraiserDTO;
import com.efit.hrms.dto.AppraiserDetailsDTO;
import com.efit.hrms.dto.GoalsDTO;
import com.efit.hrms.dto.GoalsDetailsDTO;
import com.efit.hrms.dto.GradeDTO;
import com.efit.hrms.dto.HrReviewDTO;
import com.efit.hrms.dto.KpiDTO;
import com.efit.hrms.dto.KpiKraDTO;
import com.efit.hrms.dto.KpiKraDetailsDTO;
import com.efit.hrms.dto.PreGoalsDTO;
import com.efit.hrms.dto.PreGoalsDetailsDTO;
import com.efit.hrms.dto.ScoreDTO;
import com.efit.hrms.dto.SelfGoalsDTO;
import com.efit.hrms.dto.SelfGoalsDetailsDTO;
import com.efit.hrms.dto.Supervisor1FeedBackDTO;
import com.efit.hrms.dto.WeightageDTO;
import com.efit.hrms.entity.AdditionalGoalsVO;
import com.efit.hrms.entity.AppraisalPeriodVO;
import com.efit.hrms.entity.AppraiseeDetailsVO;
import com.efit.hrms.entity.AppraiseeVO;
import com.efit.hrms.entity.AppraiserDetailsVO;
import com.efit.hrms.entity.AppraiserVO;
import com.efit.hrms.entity.GoalsDetailsVO;
import com.efit.hrms.entity.GoalsVO;
import com.efit.hrms.entity.GradeVO;
import com.efit.hrms.entity.HrReviewVO;
import com.efit.hrms.entity.KpiKraDetailsVO;
import com.efit.hrms.entity.KpiKraVO;
import com.efit.hrms.entity.KpiVO;
import com.efit.hrms.entity.PreGoalsDetailsVO;
import com.efit.hrms.entity.PreGoalsVO;
import com.efit.hrms.entity.ScoreVO;
import com.efit.hrms.entity.SelfGoalsDetailsVO;
import com.efit.hrms.entity.SelfGoalsVO;
import com.efit.hrms.entity.Supervisor1FeedBackVO;
import com.efit.hrms.entity.WeightageVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.AdditionalGoalsRepo;
import com.efit.hrms.repo.AppraisalPeriodRepo;
import com.efit.hrms.repo.AppraiseeDetailsRepo;
import com.efit.hrms.repo.AppraiseeRepo;
import com.efit.hrms.repo.AppraiserDetailsRepo;
import com.efit.hrms.repo.AppraiserRepo;
import com.efit.hrms.repo.GoalsDetailsRepo;
import com.efit.hrms.repo.GoalsRepo;
import com.efit.hrms.repo.GradeRepo;
import com.efit.hrms.repo.HrReviewRepo;
import com.efit.hrms.repo.KpiKraDetailsRepo;
import com.efit.hrms.repo.KpiKraRepo;
import com.efit.hrms.repo.KpiRepo;
import com.efit.hrms.repo.PreGoalsDetailsRepo;
import com.efit.hrms.repo.PreGoalsRepo;
import com.efit.hrms.repo.ScoreRepo;
import com.efit.hrms.repo.SelfGoalsDetailsRepo;
import com.efit.hrms.repo.SelfGoalsRepo;
import com.efit.hrms.repo.Supervisor1FeedBackRepo;
import com.efit.hrms.repo.WeightageRepo;

@Service
public class GoalsControllerServiceImpl implements GoalsControllerService {

	public static final Logger LOGGER = LoggerFactory.getLogger(GoalsControllerServiceImpl.class);

	@Autowired
	PreGoalsRepo preGoalsRepo;

	@Autowired
	PreGoalsDetailsRepo preGoalsDetailsRepo;

	@Autowired
	AppraisalPeriodRepo appraisalPeriodRepo;

	@Autowired
	WeightageRepo weightageRepo;

	@Autowired
	GradeRepo gradeRepo;

	@Autowired
	KpiKraRepo kpiKraRepo;

	@Autowired
	KpiKraDetailsRepo kpiKraDetailsRepo;

	@Autowired
	KpiRepo kpiRepo;

	@Autowired
	GoalsRepo goalsRepo;

	@Autowired
	GoalsDetailsRepo goalsDetailsRepo;

	@Autowired
	AppraiseeRepo appraiseeRepo;

	@Autowired
	SelfGoalsRepo selfGoalsRepo;

	@Autowired
	SelfGoalsDetailsRepo selfGoalsDetailsRepo;

	@Autowired
	AppraiseeDetailsRepo appraiseeDetailsRepo;

	@Autowired
	AppraiserRepo appraiserRepo;

	@Autowired
	AppraiserDetailsRepo appraiserDetailsRepo;

	@Autowired
	ScoreRepo scoreRepo;

	@Autowired
	HrReviewRepo hrReviewRepo;

	@Autowired
	Supervisor1FeedBackRepo supervisor1FeedBackRepo;

	@Autowired
	AdditionalGoalsRepo additionalGoalsRepo;

	@Override
	public Map<String, Object> createUpdatePreGoals(@Valid PreGoalsDTO preGoalsDTO) throws ApplicationException {

		String message;

		PreGoalsVO preGoalsVO = null;

		if (ObjectUtils.isEmpty(preGoalsDTO.getId())) {

			preGoalsVO = new PreGoalsVO();

			preGoalsVO.setCreatedBy(preGoalsDTO.getCreatedBy());
			preGoalsVO.setUpdatedBy(preGoalsDTO.getCreatedBy());

			message = "PreGoals Creation SuccessFully";

		} else {

			preGoalsVO = preGoalsRepo.findById(preGoalsDTO.getId())
					.orElseThrow(() -> new ApplicationException("PreGoals  not found with id: " + preGoalsDTO.getId()));

			preGoalsVO.setUpdatedBy(preGoalsDTO.getCreatedBy());

			message = "PreGoals Updation SuccessFully";
		}

		preGoalsVO = getPreGoalsVOFormPreGoalsDTO(preGoalsVO, preGoalsDTO);
		preGoalsRepo.save(preGoalsVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("preGoalsVO", preGoalsVO);
		return response;
	}

	private PreGoalsVO getPreGoalsVOFormPreGoalsDTO(PreGoalsVO preGoalsVO, @Valid PreGoalsDTO preGoalsDTO) {

		preGoalsVO.setAppraisalId(preGoalsDTO.getAppraisalId());
		preGoalsVO.setCode(preGoalsDTO.getCode());
		preGoalsVO.setName(preGoalsDTO.getName());
		preGoalsVO.setOrgId(preGoalsDTO.getOrgId());
		preGoalsVO.setSupervisorCode(preGoalsDTO.getSupervisorCode());
		preGoalsVO.setSupervisorName(preGoalsDTO.getSupervisorName());
		preGoalsVO.setActive(preGoalsDTO.isActive());
		preGoalsVO.setFinYear(preGoalsDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(preGoalsDTO.getId())) {
			List<PreGoalsDetailsVO> goalsDetailsVOs = preGoalsDetailsRepo.findByPreGoalsVO(preGoalsVO);
			preGoalsDetailsRepo.deleteAll(goalsDetailsVOs);

		}

		List<PreGoalsDetailsVO> preGoalsDetailsVOs = new ArrayList<>();
		for (PreGoalsDetailsDTO preGoalsDetailsDTO : preGoalsDTO.getPreGoalsDetailsDTO()) {
			PreGoalsDetailsVO preGoalsDetailsVO = new PreGoalsDetailsVO();

			preGoalsDetailsVO.setArea(preGoalsDetailsDTO.getArea());
			preGoalsDetailsVO.setKeyPerformanceIndicator(preGoalsDetailsDTO.getKeyPerformanceIndicator());
			preGoalsDetailsVO.setGoals(preGoalsDetailsDTO.getGoals());

			preGoalsDetailsVO.setPreGoalsVO(preGoalsVO);
			preGoalsDetailsVOs.add(preGoalsDetailsVO);

		}

		preGoalsVO.setPreGoalsDetailsVO(preGoalsDetailsVOs);

		return preGoalsVO;
	}

	@Override
	public List<PreGoalsVO> getPreGoalsByOrgId(Long orgId) {
		return preGoalsRepo.getPreGoals(orgId);
	}

	@Override
	public Optional<PreGoalsVO> getPreGoalsById(Long id) {
		return preGoalsRepo.findById(id);
	}

	// APPRAISAL

	@Override
	public Map<String, Object> createUpdateAppraisalPeriod(@Valid AppraisalPeriodDTO appraisalPeriodDTO)
			throws ApplicationException {

		String message;

		AppraisalPeriodVO appraisalPeriodVO = null;

		if (ObjectUtils.isEmpty(appraisalPeriodDTO.getId())) {

			if (appraisalPeriodRepo.existsByAppraisalId(appraisalPeriodDTO.getAppraisalId())) {

				String errorMessage = String.format("This AppraisalID: %s Already Exists in This Organization",
						appraisalPeriodDTO.getAppraisalId());
				throw new ApplicationException(errorMessage);

			}

			appraisalPeriodVO = new AppraisalPeriodVO();

			appraisalPeriodVO.setCreatedBy(appraisalPeriodDTO.getCreatedBy());
			appraisalPeriodVO.setUpdatedBy(appraisalPeriodDTO.getCreatedBy());

			message = "Appraisal Creation SuccessFully";

		} else {

			appraisalPeriodVO = appraisalPeriodRepo.findById(appraisalPeriodDTO.getId()).orElseThrow(
					() -> new ApplicationException("Appraisal  not found with id: " + appraisalPeriodDTO.getId()));

			if (!appraisalPeriodVO.getAppraisalId().equals(appraisalPeriodDTO.getAppraisalId())) {

				if (appraisalPeriodRepo.existsByAppraisalId(appraisalPeriodDTO.getAppraisalId())) {

					String errorMessage = String.format("This AppraisalID: %s Already Exists in This Organization",
							appraisalPeriodDTO.getAppraisalId());
					throw new ApplicationException(errorMessage);

				}

				appraisalPeriodVO.setAppraisalId(appraisalPeriodDTO.getAppraisalId());
			}

			appraisalPeriodVO.setUpdatedBy(appraisalPeriodDTO.getCreatedBy());

			message = "Appraisal Updation SuccessFully";
		}

		appraisalPeriodVO = getAppraisalVOFormAppraisalDTO(appraisalPeriodVO, appraisalPeriodDTO);
		appraisalPeriodRepo.save(appraisalPeriodVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("appraisalPeriodVO", appraisalPeriodVO);
		return response;

	}

	private AppraisalPeriodVO getAppraisalVOFormAppraisalDTO(AppraisalPeriodVO appraisalPeriodVO,
			@Valid AppraisalPeriodDTO appraisalPeriodDTO) {

		appraisalPeriodVO.setAppraisalId(appraisalPeriodDTO.getAppraisalId());
		appraisalPeriodVO.setFinYear(appraisalPeriodDTO.getFinYear());
		appraisalPeriodVO.setActive(appraisalPeriodDTO.isActive());
		appraisalPeriodVO.setType(appraisalPeriodDTO.getType());
		appraisalPeriodVO.setEffectiveForm(appraisalPeriodDTO.getEffectiveForm());
		appraisalPeriodVO.setEffectiveTo(appraisalPeriodDTO.getEffectiveTo());
		appraisalPeriodVO.setOrgId(appraisalPeriodDTO.getOrgId());

		return appraisalPeriodVO;
	}

	@Override
	public List<AppraisalPeriodVO> getAppraisalPeriodByOrgId(Long orgId) {
		return appraisalPeriodRepo.getAppraisal(orgId);
	}

	@Override
	public Optional<AppraisalPeriodVO> getAppraisalPeriodById(Long id) {
		return appraisalPeriodRepo.findById(id);
	}

	@Override
	public Map<String, Object> createUpdateWeightage(@Valid WeightageDTO weightageDTO) throws ApplicationException {

		String message;

		WeightageVO weightageVO = null;

		if (ObjectUtils.isEmpty(weightageDTO.getId())) {

			weightageVO = new WeightageVO();

			weightageVO.setCreatedBy(weightageDTO.getCreatedBy());
			weightageVO.setUpdatedBy(weightageDTO.getCreatedBy());

			message = "Weightage Creation SuccessFully";

		} else {

			weightageVO = weightageRepo.findById(weightageDTO.getId()).orElseThrow(
					() -> new ApplicationException("Weightage  not found with id: " + weightageDTO.getId()));

			weightageVO.setUpdatedBy(weightageDTO.getCreatedBy());

			message = "Weightage Updation SuccessFully";

		}

		weightageVO = getWeightageVOFromWeightageDTO(weightageVO, weightageDTO);
		weightageRepo.save(weightageVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("weightageVO", weightageVO);
		return response;

	}

	private WeightageVO getWeightageVOFromWeightageDTO(WeightageVO weightageVO, @Valid WeightageDTO weightageDTO) {

		weightageVO.setActive(weightageDTO.isActive());
		weightageVO.setLevel(weightageDTO.getLevel());
		weightageVO.setBusinessOperations(weightageDTO.getBusinessOperations());
		weightageVO.setValueCreation(weightageDTO.getValueCreation());
		weightageVO.setPeopleEngagement(weightageDTO.getPeopleEngagement());
		weightageVO.setRemarks(weightageDTO.getRemarks());
		weightageVO.setInvlId(weightageDTO.getInvlId());
		weightageVO.setOrgId(weightageDTO.getOrgId());
		weightageVO.setFinYear(weightageDTO.getFinYear());
		return weightageVO;
	}

	@Override
	public List<WeightageVO> getWeightageByOrgId(Long orgId) {
		return weightageRepo.getWeightage(orgId);
	}

	@Override
	public Optional<WeightageVO> getWeightageById(Long id) {
		return weightageRepo.findById(id);
	}

	@Override
	public Map<String, Object> createUpdateGrade(@Valid GradeDTO gradeDTO) throws ApplicationException {

		String message;

		GradeVO gradeVO = null;

		if (ObjectUtils.isEmpty(gradeDTO.getId())) {

			gradeVO = new GradeVO();

			gradeVO.setCreatedBy(gradeDTO.getCreatedBy());
			gradeVO.setUpdatedBy(gradeDTO.getCreatedBy());

			message = "Grade Creation SuccessFully";

		} else {

			gradeVO = gradeRepo.findById(gradeDTO.getId())
					.orElseThrow(() -> new ApplicationException("Weightage  not found with id: " + gradeDTO.getId()));

			gradeVO.setUpdatedBy(gradeDTO.getCreatedBy());

			message = "Grade Updation SuccessFully";

		}

		gradeVO = getGradeVOFromGradeDTO(gradeVO, gradeDTO);
		gradeRepo.save(gradeVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("gradeVO", gradeVO);
		return response;

	}

	private GradeVO getGradeVOFromGradeDTO(GradeVO gradeVO, @Valid GradeDTO gradeDTO) {

		gradeVO.setRangeFrom(gradeDTO.getRangeFrom());
		gradeVO.setRangeTo(gradeDTO.getRangeTo());
		gradeVO.setIndications(gradeDTO.getIndications());
		gradeVO.setActive(gradeDTO.isActive());
		gradeVO.setOrgId(gradeDTO.getOrgId());
		gradeVO.setFinYear(gradeDTO.getFinYear());
		gradeVO.setGrade(gradeDTO.getGrade());
		gradeVO.setScore(gradeDTO.getScore());

		return gradeVO;
	}

	@Override
	public List<GradeVO> getGradeByOrgId(Long orgId) {
		return gradeRepo.getGrade(orgId);
	}

	@Override
	public Optional<GradeVO> getGradeById(Long id) {
		return gradeRepo.findById(id);
	}

	@Override
	public Map<String, Object> createUpdateKpiKra(@Valid KpiKraDTO kpiKraDTO) throws ApplicationException {

		String message;

		KpiKraVO kpiKraVO = null;

		if (ObjectUtils.isEmpty(kpiKraDTO.getId())) {

			kpiKraVO = new KpiKraVO();

			kpiKraVO.setCreatedBy(kpiKraDTO.getCreatedBy());
			kpiKraVO.setUpdatedBy(kpiKraDTO.getCreatedBy());

			message = "KpiKra Creation SuccessFully";

		} else {

			kpiKraVO = kpiKraRepo.findById(kpiKraDTO.getId())
					.orElseThrow(() -> new ApplicationException("KpiKra  not found with id: " + kpiKraDTO.getId()));

			kpiKraVO.setUpdatedBy(kpiKraDTO.getCreatedBy());

			message = "KpiKra Updation SuccessFully";

		}

		kpiKraVO = getKpiKraVOkpiKraDTO(kpiKraVO, kpiKraDTO);
		kpiKraRepo.save(kpiKraVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("kpiKraVO", kpiKraVO);
		return response;

	}

	private KpiKraVO getKpiKraVOkpiKraDTO(KpiKraVO kpiKraVO, @Valid KpiKraDTO kpiKraDTO) {

		kpiKraVO.setAppraisalId(kpiKraDTO.getAppraisalId());
		kpiKraVO.setActive(kpiKraDTO.isActive());
		kpiKraVO.setOrgId(kpiKraDTO.getOrgId());
		kpiKraVO.setFinYear(kpiKraDTO.getFinYear());
		kpiKraVO.setBranchCode(kpiKraDTO.getBranchCode());
		kpiKraVO.setBranch(kpiKraDTO.getBranch());

		if (ObjectUtils.isNotEmpty(kpiKraDTO.getId())) {

			List<KpiKraDetailsVO> kpiKraDetailsVOs = kpiKraDetailsRepo.findByKpiKraVO(kpiKraVO);
			kpiKraDetailsRepo.deleteAll(kpiKraDetailsVOs);

			List<KpiVO> kpiVOs = kpiRepo.findByKpiKraVO(kpiKraVO);
			kpiRepo.deleteAll(kpiVOs);

		}

		List<KpiVO> kpiVOs = new ArrayList<>();
		for (KpiDTO kpiDTO : kpiKraDTO.getKpiDTO()) {
			KpiVO kpiVO = new KpiVO();

			kpiVO.setKpiId(kpiDTO.getKpiId());
			kpiVO.setKpiDescription(kpiDTO.getKpiDescription());

			kpiVO.setKpiKraVO(kpiKraVO);
			kpiVOs.add(kpiVO);

		}

		kpiKraVO.setKpiVO(kpiVOs);

		List<KpiKraDetailsVO> kpiKraDetailsVOs = new ArrayList<>();
		for (KpiKraDetailsDTO kpiKraDetailsDTO : kpiKraDTO.getKpiKraDetailsDTO()) {
			KpiKraDetailsVO kpiKraDetailsVO = new KpiKraDetailsVO();

			kpiKraDetailsVO.setKpiId(kpiKraDetailsDTO.getKpiId());
			kpiKraDetailsVO.setKpiDescription(kpiKraDetailsDTO.getKpiDescription());
			kpiKraDetailsVO.setKraId(kpiKraDetailsDTO.getKraId());
			kpiKraDetailsVO.setRo(kpiKraDetailsDTO.getRo());
			kpiKraDetailsVO.setKraDescription(kpiKraDetailsDTO.getKraDescription());

			kpiKraDetailsVO.setKpiKraVO(kpiKraVO);
			kpiKraDetailsVOs.add(kpiKraDetailsVO);

		}

		kpiKraVO.setKpiKraDetailsVO(kpiKraDetailsVOs);

		return kpiKraVO;
	}

	@Override
	public List<KpiKraVO> getKpiKraByOrgId(Long orgId) {
		return kpiKraRepo.getKpiKra(orgId);
	}

	@Override
	public Optional<KpiKraVO> getKpiKraById(Long id) {
		return kpiKraRepo.findById(id);
	}

	// GOALS

	@Override
	public Map<String, Object> createUpdateGoals(@Valid GoalsDTO goalsDTO) throws ApplicationException {

		String message;

		GoalsVO goalsVO;

		if (ObjectUtils.isEmpty(goalsDTO.getId())) {

			goalsVO = new GoalsVO();

			goalsVO.setCreatedBy(goalsDTO.getCreatedBy());
			goalsVO.setUpdatedBy(goalsDTO.getCreatedBy());

			message = "Goals Creation SuccessFully";

		} else {

			goalsVO = goalsRepo.findById(goalsDTO.getId())
					.orElseThrow(() -> new ApplicationException("Goals  not found with id: " + goalsDTO.getId()));

			goalsVO.setUpdatedBy(goalsDTO.getCreatedBy());

			message = "Goals Updation SuccessFully";

		}

		goalsVO = getGoalsVoFromGoalsDTO(goalsVO, goalsDTO);
		goalsRepo.save(goalsVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("goalsVO", goalsVO);
		return response;

	}

	private GoalsVO getGoalsVoFromGoalsDTO(GoalsVO goalsVO, @Valid GoalsDTO goalsDTO) {

		goalsVO.setActive(goalsDTO.isActive());
		goalsVO.setAppraisalId(goalsDTO.getAppraisalId());
		goalsVO.setDepartment(goalsDTO.getDepartment());
		goalsVO.setOrgId(goalsDTO.getOrgId());
		goalsVO.setFinYear(goalsDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(goalsDTO.getId())) {

			List<GoalsDetailsVO> goalsDetailsVOs = goalsDetailsRepo.findByGoalsVO(goalsVO);
			goalsDetailsRepo.deleteAll(goalsDetailsVOs);

		}

		List<GoalsDetailsVO> goalsDetailsVOs = new ArrayList<>();
		for (GoalsDetailsDTO goalsDetailsDTO : goalsDTO.getGoalsDetailsDTO()) {
			GoalsDetailsVO goalsDetailsVO = new GoalsDetailsVO();

			goalsDetailsVO.setArea(goalsDetailsDTO.getArea());
			goalsDetailsVO.setIndicators(goalsDetailsDTO.getIndicators());
			goalsDetailsVO.setGoals(goalsDetailsDTO.getGoals());

			goalsDetailsVO.setGoalsVO(goalsVO);
			goalsDetailsVOs.add(goalsDetailsVO);

		}

		goalsVO.setGoalsDetailsVO(goalsDetailsVOs);

		return goalsVO;
	}

	@Override
	public List<GoalsVO> getGoalsByOrgId(Long orgId) {
		return goalsRepo.getGoals(orgId);
	}

	@Override
	public Optional<GoalsVO> getGoalsById(Long id) {
		return goalsRepo.findById(id);
	}

	@Override
	public Map<String, Object> createUpdateAppraisee(@Valid AppraiseeDTO appraiseeDTO) throws ApplicationException {

		String message;

		AppraiseeVO appraiseeVO = null;

		if (ObjectUtils.isEmpty(appraiseeDTO.getId())) {

			appraiseeVO = new AppraiseeVO();

			appraiseeVO.setCreatedBy(appraiseeDTO.getCreatedBy());
			appraiseeVO.setUpdatedBy(appraiseeDTO.getCreatedBy());

			message = "Appraisee Creation SuccessFully";

		} else {

			appraiseeVO = appraiseeRepo.findById(appraiseeDTO.getId()).orElseThrow(
					() -> new ApplicationException("Appraisee  not found with id: " + appraiseeDTO.getId()));

			appraiseeVO.setUpdatedBy(appraiseeDTO.getCreatedBy());

			message = "Appraisee Updation SuccessFully";

		}

		appraiseeVO = getAppraiseeVOFromAppraiseeDTO(appraiseeVO, appraiseeDTO);
		appraiseeRepo.save(appraiseeVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("appraiseeVO", appraiseeVO);
		return response;

	}

	private AppraiseeVO getAppraiseeVOFromAppraiseeDTO(AppraiseeVO appraiseeVO, @Valid AppraiseeDTO appraiseeDTO) {

		appraiseeVO.setName(appraiseeDTO.getName());
		appraiseeVO.setCode(appraiseeDTO.getCode());
		appraiseeVO.setDesignation(appraiseeDTO.getDesignation());
		appraiseeVO.setDepartment(appraiseeDTO.getDepartment());
		appraiseeVO.setBranch(appraiseeDTO.getBranch());
		appraiseeVO.setReportingHead(appraiseeDTO.getReportingHead());
		appraiseeVO.setReportingHeadCode(appraiseeDTO.getReportingHeadCode());
		appraiseeVO.setReportingHeadDesignation(appraiseeDTO.getReportingHeadDesignation());
		appraiseeVO.setOrgId(appraiseeDTO.getOrgId());
		appraiseeVO.setCreatedBy(appraiseeDTO.getCreatedBy());
		appraiseeVO.setFinYear(appraiseeDTO.getFinYear());
		appraiseeVO.setActive(appraiseeDTO.isActive());

		if (ObjectUtils.isNotEmpty(appraiseeDTO.getId())) {
			List<AppraiseeDetailsVO> appraiseeDetailsVOs = appraiseeDetailsRepo.findByAppraiseeVO(appraiseeVO);
			appraiseeDetailsRepo.deleteAll(appraiseeDetailsVOs);

		}

		List<AppraiseeDetailsVO> selfGoalsDetailsVOs = new ArrayList<>();
		for (AppraiseeDetailsDTO appraiseeDetailsDTO : appraiseeDTO.getAppraiseeDetailsDTO()) {
			AppraiseeDetailsVO appraiseeDetailsVO = new AppraiseeDetailsVO();

			appraiseeDetailsVO.setGoals(appraiseeDetailsDTO.getGoals());
			appraiseeDetailsVO.setArea(appraiseeDetailsDTO.getArea());
			appraiseeDetailsVO.setKeyPerformanceIndicator(appraiseeDetailsDTO.getKeyPerformanceIndicator());
			appraiseeDetailsVO.setReMarks(appraiseeDetailsDTO.getReMarks());

			appraiseeDetailsVO.setAppraiseeVO(appraiseeVO);
			selfGoalsDetailsVOs.add(appraiseeDetailsVO);

		}

		appraiseeVO.setAppraiseeDetailsVO(selfGoalsDetailsVOs);

		return appraiseeVO;
	}

	@Override
	public List<AppraiseeVO> getAppraiseeByOrgId(Long orgId) {
		return appraiseeRepo.getAppraisee(orgId);

	}

	@Override
	public Optional<AppraiseeVO> getAppraiseeById(Long id) {
		return appraiseeRepo.findById(id);
	}

	@Override
	public List<Map<String, Object>> getEmployeeDetails(Long orgId, String employeeCode) {
		Set<Object[]> chType = appraiseeRepo.getReportingPerson(orgId, employeeCode);
		return ReportingPersonDetails(chType);
	}

	private List<Map<String, Object>> ReportingPersonDetails(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("empCode", (ch != null && ch.length > 0 && ch[0] != null) ? ch[0].toString() : "");
			map.put("empName", (ch != null && ch.length > 1 && ch[1] != null) ? ch[1].toString() : "");
			map.put("empDesignation", (ch != null && ch.length > 2 && ch[2] != null) ? ch[2].toString() : "");
			map.put("department", (ch != null && ch.length > 3 && ch[3] != null) ? ch[3].toString() : "");
			map.put("reportingPerson", (ch != null && ch.length > 4 && ch[4] != null) ? ch[4].toString() : "");
			map.put("reportingPersonCode", (ch != null && ch.length > 5 && ch[5] != null) ? ch[5].toString() : "");
			map.put("reportingPersonRole", (ch != null && ch.length > 6 && ch[6] != null) ? ch[6].toString() : "");
			List1.add(map);
		}
		return List1;

	}

	@Override
	public Map<String, Object> createUpdateSelfGoals(@Valid SelfGoalsDTO selfGoalsDTO) throws ApplicationException {

		String message;

		SelfGoalsVO selfGoalsVO = null;

		if (ObjectUtils.isEmpty(selfGoalsDTO.getId())) {

			selfGoalsVO = new SelfGoalsVO();

			selfGoalsVO.setCreatedBy(selfGoalsDTO.getCreatedBy());
			selfGoalsVO.setUpdatedBy(selfGoalsDTO.getCreatedBy());

			message = "PreGoals Creation SuccessFully";

		} else {

			selfGoalsVO = selfGoalsRepo.findById(selfGoalsDTO.getId()).orElseThrow(
					() -> new ApplicationException("SelfGoals  not found with id: " + selfGoalsDTO.getId()));

			selfGoalsVO.setUpdatedBy(selfGoalsDTO.getCreatedBy());

			message = "PreGoals Updation SuccessFully";
		}

		selfGoalsVO = getSelfGoalsVOFormSelfGoalsDTO(selfGoalsVO, selfGoalsDTO);
		selfGoalsRepo.save(selfGoalsVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("selfGoalsVO", selfGoalsVO);
		return response;
	}

	private SelfGoalsVO getSelfGoalsVOFormSelfGoalsDTO(SelfGoalsVO selfGoalsVO, @Valid SelfGoalsDTO selfGoalsDTO) {

		selfGoalsVO.setAppraisalId(selfGoalsDTO.getAppraisalId());
		selfGoalsVO.setCode(selfGoalsDTO.getCode());
		selfGoalsVO.setName(selfGoalsDTO.getName());
		selfGoalsVO.setOrgId(selfGoalsDTO.getOrgId());
		selfGoalsVO.setSupervisorCode(selfGoalsDTO.getSupervisorCode());
		selfGoalsVO.setSupervisorName(selfGoalsDTO.getSupervisorName());
		selfGoalsVO.setActive(selfGoalsDTO.isActive());
		selfGoalsVO.setFinYear(selfGoalsDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(selfGoalsDTO.getId())) {
			List<SelfGoalsDetailsVO> goalsDetailsVOs = selfGoalsDetailsRepo.findBySelfGoalsVO(selfGoalsVO);
			selfGoalsDetailsRepo.deleteAll(goalsDetailsVOs);

		}

		List<SelfGoalsDetailsVO> selfGoalsDetailsVOs = new ArrayList<>();
		for (SelfGoalsDetailsDTO selfGoalsDetailsDTO : selfGoalsDTO.getSelfGoalsDetailsDTO()) {
			SelfGoalsDetailsVO selfGoalsDetailsVO = new SelfGoalsDetailsVO();

			selfGoalsDetailsVO.setArea(selfGoalsDetailsDTO.getArea());
			selfGoalsDetailsVO.setKeyPerformanceIndicator(selfGoalsDetailsDTO.getKeyPerformanceIndicator());
			selfGoalsDetailsVO.setGoals(selfGoalsDetailsDTO.getGoals());

			selfGoalsDetailsVO.setSelfGoalsVO(selfGoalsVO);
			selfGoalsDetailsVOs.add(selfGoalsDetailsVO);

		}

		selfGoalsVO.setSelfGoalsDetailsVO(selfGoalsDetailsVOs);

		return selfGoalsVO;
	}

	@Override
	public List<SelfGoalsVO> getSelfGoalsByOrgId(Long orgId) {
		return selfGoalsRepo.getSelfGoals(orgId);
	}

	@Override
	public Optional<SelfGoalsVO> getSelfGoalsById(Long id) {
		return selfGoalsRepo.findById(id);
	}

	@Override
	public List<Map<String, Object>> getAppraiseeFillGrid(Long orgId, String employeeCode) {

		Set<Object[]> chType = selfGoalsRepo.getAppraisee(orgId, employeeCode);
		return getAppraiseeFill(chType);
	}

	private List<Map<String, Object>> getAppraiseeFill(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("appraisalId",
					(ch != null && ch.length > 0 && ch[0] != null) ? Long.valueOf(ch[0].toString()) : null);
			map.put("empCode", (ch != null && ch.length > 1 && ch[1] != null) ? ch[1].toString() : "");
			map.put("empName", (ch != null && ch.length > 2 && ch[2] != null) ? ch[2].toString() : "");
			map.put("supervisorCode", (ch != null && ch.length > 3 && ch[3] != null) ? ch[3].toString() : "");
			map.put("supervisorName", (ch != null && ch.length > 4 && ch[4] != null) ? ch[4].toString() : "");
			map.put("area", (ch != null && ch.length > 5 && ch[5] != null) ? ch[5].toString() : "");
			map.put("goals", (ch != null && ch.length > 6 && ch[6] != null) ? ch[6].toString() : "");
			map.put("keyPerformanceIndicator", (ch != null && ch.length > 7 && ch[7] != null) ? ch[7].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public Map<String, Object> createUpdateAppraiser(@Valid AppraiserDTO appraiserDTO) throws ApplicationException {
		String message;

		AppraiserVO appraiserVO = null;

		if (ObjectUtils.isEmpty(appraiserDTO.getId())) {

			appraiserVO = new AppraiserVO();

			appraiserVO.setCreatedBy(appraiserDTO.getCreatedBy());
			appraiserVO.setUpdatedBy(appraiserDTO.getCreatedBy());

			message = "Appraiser Creation SuccessFully";

		} else {

			appraiserVO = appraiserRepo.findById(appraiserDTO.getId()).orElseThrow(
					() -> new ApplicationException("Appraiser  not found with id: " + appraiserDTO.getId()));

			appraiserVO.setUpdatedBy(appraiserDTO.getCreatedBy());

			message = "Appraisee Updation SuccessFully";

		}

		appraiserVO = getAppraiserVOFromAppraiserDTO(appraiserVO, appraiserDTO);
		appraiserRepo.save(appraiserVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("appraiserVO", appraiserVO);
		return response;
	}

	private AppraiserVO getAppraiserVOFromAppraiserDTO(AppraiserVO appraiserVO, @Valid AppraiserDTO appraiserDTO) {

		appraiserVO.setAppraisalId(appraiserDTO.getAppraisalId());
		appraiserVO.setEmpName(appraiserDTO.getEmpName());
		appraiserVO.setEmpCode(appraiserDTO.getEmpCode());
		appraiserVO.setDepartment(appraiserDTO.getDepartment());
		appraiserVO.setSupCode(appraiserDTO.getSupCode());
		appraiserVO.setSupName(appraiserDTO.getSupName());
		appraiserVO.setFinYear(appraiserDTO.getFinYear());
		appraiserVO.setCreatedBy(appraiserDTO.getCreatedBy());
		appraiserVO.setActive(appraiserDTO.isActive());
		appraiserVO.setBranch(appraiserDTO.getBranch());
		appraiserVO.setOrgId(appraiserDTO.getOrgId());

		if (ObjectUtils.isNotEmpty(appraiserDTO.getId())) {
			List<AppraiserDetailsVO> appraiserDetailsVOs = appraiserDetailsRepo.findByAppraiserVO(appraiserVO);
			appraiserDetailsRepo.deleteAll(appraiserDetailsVOs);

		}

		List<AppraiserDetailsVO> appraiserDetailsVOs = new ArrayList<>();
		for (AppraiserDetailsDTO appraiserDetailsDTO : appraiserDTO.getAppraiserDetailsDTO()) {
			AppraiserDetailsVO appraiserDetailsVO = new AppraiserDetailsVO();

			appraiserDetailsVO.setGoals(appraiserDetailsDTO.getGoals());
			appraiserDetailsVO.setArea(appraiserDetailsDTO.getArea());
			appraiserDetailsVO.setKeyPerformanceIndicator(appraiserDetailsDTO.getKeyPerformanceIndicator());
			appraiserDetailsVO.setReMarks(appraiserDetailsDTO.getReMarks());
			appraiserDetailsVO.setInput(appraiserDetailsDTO.getInput());
			appraiserDetailsVO.setScore(appraiserDetailsDTO.getScore());
			appraiserDetailsVO.setAppraiserVO(appraiserVO);
			appraiserDetailsVOs.add(appraiserDetailsVO);
		}

		appraiserVO.setAppraiserDetailsVO(appraiserDetailsVOs);

		return appraiserVO;
	}

	@Override
	public List<AppraiserVO> getAppraiserByOrgId(Long orgId) {
		return appraiserRepo.getAppraiser(orgId);
	}

	@Override
	public Optional<AppraiserVO> getAppraiserById(Long id) {
		return appraiserRepo.findById(id);
	}

	@Override
	public List<Map<String, Object>> getReportingPerson(Long orgId) {
		Set<Object[]> chType = appraiserRepo.getReporting(orgId);
		return ReportingPerson(chType);
	}

	private List<Map<String, Object>> ReportingPerson(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("reportingPerson", (ch != null && ch.length > 0 && ch[0] != null) ? ch[0].toString() : "");
			map.put("reportingPersonCode", (ch != null && ch.length > 1 && ch[1] != null) ? ch[1].toString() : "");
			map.put("reportingPersonRole", (ch != null && ch.length > 2 && ch[2] != null) ? ch[2].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getEmpUnderReportingPerson(Long orgId, String reportingPersonCode) {
		Set<Object[]> chType = appraiserRepo.getEmp(orgId, reportingPersonCode);
		return getEmpUnderReporting(chType);
	}

	private List<Map<String, Object>> getEmpUnderReporting(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("empName", (ch != null && ch.length > 0 && ch[0] != null) ? ch[0].toString() : "");
			map.put("empCode", (ch != null && ch.length > 1 && ch[1] != null) ? ch[1].toString() : "");
			map.put("department", (ch != null && ch.length > 2 && ch[2] != null) ? ch[2].toString() : "");
			map.put("designation", (ch != null && ch.length > 3 && ch[3] != null) ? ch[3].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getAppraiserFillGrid(Long orgId, String empCode) {

		Set<Object[]> chType = appraiserRepo.getAppraiserFillGrid(orgId, empCode);
		return getAppraiserFill(chType);
	}

	private List<Map<String, Object>> getAppraiserFill(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("area", (ch != null && ch.length > 0 && ch[0] != null) ? ch[0].toString() : "");
			map.put("goals", (ch != null && ch.length > 1 && ch[1] != null) ? ch[1].toString() : "");
			map.put("keyPerformanceIndicator", (ch != null && ch.length > 2 && ch[2] != null) ? ch[2].toString() : "");
			map.put("remarks", (ch != null && ch.length > 3 && ch[3] != null) ? ch[3].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public Map<String, Object> createUpdateScore(@Valid ScoreDTO scoreDTO) throws ApplicationException {
		String message;

		ScoreVO scoreVO = null;

		if (ObjectUtils.isEmpty(scoreDTO.getId())) {

			scoreVO = new ScoreVO();

			scoreVO.setCreatedBy(scoreDTO.getCreatedBy());
			scoreVO.setUpdatedBy(scoreDTO.getCreatedBy());

			message = "Score Creation SuccessFully";

		} else {

			scoreVO = scoreRepo.findById(scoreDTO.getId())
					.orElseThrow(() -> new ApplicationException("Score  not found with id: " + scoreDTO.getId()));

			scoreVO.setUpdatedBy(scoreDTO.getCreatedBy());

			message = "Score Updation SuccessFully";

		}

		scoreVO = getScoreVOFromScoreDTO(scoreVO, scoreDTO);
		scoreRepo.save(scoreVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("scoreVO", scoreVO);
		return response;
	}

	private ScoreVO getScoreVOFromScoreDTO(ScoreVO scoreVO, @Valid ScoreDTO scoreDTO) {

		scoreVO.setInput(scoreDTO.getInput());
		scoreVO.setOrgId(scoreDTO.getOrgId());
		scoreVO.setScore(scoreDTO.getScore());
		scoreVO.setFinYear(scoreDTO.getFinYear());

		return scoreVO;
	}

	@Override
	public List<ScoreVO> getScoreByOrgId(Long orgId) {
		return scoreRepo.getScore(orgId);
	}

	@Override
	public Optional<ScoreVO> getScoreById(Long id) {
		return scoreRepo.findById(id);
	}

	@Override
	public Map<String, Object> createUpdateHrReview(@Valid HrReviewDTO hrReviewDTO) throws ApplicationException {

		String message;

		HrReviewVO hrReviewVO = null;

		if (ObjectUtils.isEmpty(hrReviewDTO.getId())) {

			hrReviewVO = new HrReviewVO();

			hrReviewVO.setCreatedBy(hrReviewDTO.getCreatedBy());
			hrReviewVO.setUpdatedBy(hrReviewDTO.getCreatedBy());

			message = "HrReview Creation SuccessFully";

		} else {

			hrReviewVO = hrReviewRepo.findById(hrReviewDTO.getId())
					.orElseThrow(() -> new ApplicationException("HrReview  not found with id: " + hrReviewDTO.getId()));

			hrReviewVO.setUpdatedBy(hrReviewDTO.getCreatedBy());

			message = "HrReview Updation SuccessFully";

		}

		hrReviewVO = getHrReviewVOFromHrReviewDTO(hrReviewVO, hrReviewDTO);
		hrReviewRepo.save(hrReviewVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("hrReviewVO", hrReviewVO);
		return response;
	}

	private HrReviewVO getHrReviewVOFromHrReviewDTO(HrReviewVO hrReviewVO, @Valid HrReviewDTO hrReviewDTO) {

		hrReviewVO.setCode(hrReviewDTO.getCode());
		hrReviewVO.setName(hrReviewDTO.getName());
		hrReviewVO.setElgibilityOfPromption(hrReviewDTO.getElgibilityOfPromption());
		hrReviewVO.setElgibilityOfIncrement(hrReviewDTO.getElgibilityOfIncrement());
		hrReviewVO.setScore(hrReviewDTO.getScore());
		hrReviewVO.setAdheranceOfEmployeeEngagement(hrReviewDTO.getAdheranceOfEmployeeEngagement());
		hrReviewVO.setRemrks(hrReviewDTO.getRemrks());
		hrReviewVO.setFinYear(hrReviewDTO.getFinYear());
		hrReviewVO.setPromotionStatus(hrReviewDTO.getPromotionStatus());
		hrReviewVO.setOrgId(hrReviewDTO.getOrgId());

		return hrReviewVO;
	}

	@Override
	public List<HrReviewVO> getHrReviewByOrgId(Long orgId) {
		return hrReviewRepo.getHrReview(orgId);
	}

	@Override
	public Optional<HrReviewVO> getHrReviewById(Long id) {
		return hrReviewRepo.findById(id);
	}

//	@Override
//	public AppraiseeVO updateTestingAppraisee(Long orgId, String reportingHead) {
//
//		AppraiseeVO appraiseeVO = appraiseeRepo.findByOrgId(orgId);
//
//		if (appraiseeVO == null) {
//			throw new RuntimeException("Ticket Records not found for this ID");
//		}
//
//		appraiseeVO.setReportingHead(reportingHead);
//
//		if (appraiseeVO.getAppraiseeDetailsVO() != null) {
//			for (AppraiseeDetailsVO detail : appraiseeVO.getAppraiseeDetailsVO()) {
//				detail.setKeyPerformanceIndicator(reportingHead); // or any logic you need
//			}
//		}
//
//		// Save to persist both parent and child updates
//		return appraiseeRepo.save(appraiseeVO);
//	}

	@Override
	public Map<String, Object> createUpdateSupervisor1FeedBack(@Valid Supervisor1FeedBackDTO supervisor1FeedBackDTO)
			throws ApplicationException {

		String message;

		Supervisor1FeedBackVO supervisor1FeedBackVO;

		if (ObjectUtils.isEmpty(supervisor1FeedBackDTO.getId())) {

			supervisor1FeedBackVO = new Supervisor1FeedBackVO();

			supervisor1FeedBackVO.setCreatedBy(supervisor1FeedBackDTO.getCreatedBy());
			supervisor1FeedBackVO.setUpdatedBy(supervisor1FeedBackDTO.getCreatedBy());

			message = "Supervisor1FeedBack Creation SuccessFully";

		} else {

			supervisor1FeedBackVO = supervisor1FeedBackRepo.findById(supervisor1FeedBackDTO.getId())
					.orElseThrow(() -> new ApplicationException(
							"supervisor1 FeedBack  not found with id: " + supervisor1FeedBackDTO.getId()));

			supervisor1FeedBackVO.setUpdatedBy(supervisor1FeedBackDTO.getCreatedBy());

			message = "Supervisor1FeedBack Updation SuccessFully";

		}

		supervisor1FeedBackVO = getsupervisor1FeedBackVOFromSupervisor1FeedBackDTO(supervisor1FeedBackVO,
				supervisor1FeedBackDTO);
		supervisor1FeedBackRepo.save(supervisor1FeedBackVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("supervisor1FeedBackVO", supervisor1FeedBackVO);
		return response;

	}

	private Supervisor1FeedBackVO getsupervisor1FeedBackVOFromSupervisor1FeedBackDTO(
			Supervisor1FeedBackVO supervisor1FeedBackVO, @Valid Supervisor1FeedBackDTO supervisor1FeedBackDTO) {

		supervisor1FeedBackVO.setCode(supervisor1FeedBackDTO.getCode());
		supervisor1FeedBackVO.setName(supervisor1FeedBackDTO.getName());
		supervisor1FeedBackVO.setElgiblityOfPromption(supervisor1FeedBackDTO.getElgiblityOfPromption());
		supervisor1FeedBackVO.setElgiblityOfIncrement(supervisor1FeedBackDTO.getElgiblityOfIncrement());
		supervisor1FeedBackVO.setScore(supervisor1FeedBackDTO.getScore());
		supervisor1FeedBackVO.setAdheranceOfEmployee(supervisor1FeedBackDTO.getAdheranceOfEmployee());
		supervisor1FeedBackVO.setHrRemarks(supervisor1FeedBackDTO.getHrRemarks());
		supervisor1FeedBackVO.setOrgId(supervisor1FeedBackDTO.getOrgId());
		supervisor1FeedBackVO.setCreatedBy(supervisor1FeedBackDTO.getCreatedBy());
		supervisor1FeedBackVO.setFinYear(supervisor1FeedBackDTO.getFinYear());

		return supervisor1FeedBackVO;
	}

	@Override
	public List<Supervisor1FeedBackVO> getSupervisor1FeedbackByOrgId(Long orgId) {
		return supervisor1FeedBackRepo.getSupervisor1FeedBack(orgId);
	}

	@Override
	public Optional<Supervisor1FeedBackVO> getSupervisor1FeedbackById(Long id) {
		return supervisor1FeedBackRepo.findById(id);
	}

	// PreGoalsApprovedReport

	@Override
	public List<Map<String, Object>> getPreGoalsApprovedReport(Long orgId, String finYear, String supCode) {
		Set<Object[]> chType = preGoalsRepo.getPreGoals(orgId, finYear, supCode);
		return getPreGoalsApproved(chType);
	}

	private List<Map<String, Object>> getPreGoalsApproved(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("empCode", (ch != null && ch.length > 0 && ch[0] != null) ? ch[0].toString() : "");
			map.put("empName", (ch != null && ch.length > 1 && ch[1] != null) ? ch[1].toString() : "");
			map.put("submittedOn", (ch != null && ch.length > 2 && ch[2] != null) ? ch[2].toString() : "");
			map.put("supCode", (ch != null && ch.length > 3 && ch[3] != null) ? ch[3].toString() : "");
			map.put("supName", (ch != null && ch.length > 2 && ch[4] != null) ? ch[4].toString() : "");
			map.put("approvedOn", (ch != null && ch.length > 3 && ch[5] != null) ? ch[5].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public Map<String, Object> createUpdateAdditionalGoals(@Valid AdditionalGoalsDTO additionalGoalsDTO)
			throws ApplicationException {

		String message;

		AdditionalGoalsVO additionalGoalsVO = null;

		if (ObjectUtils.isEmpty(additionalGoalsDTO.getId())) {

			additionalGoalsVO = new AdditionalGoalsVO();

			additionalGoalsVO.setCreatedBy(additionalGoalsDTO.getCreatedBy());
			additionalGoalsVO.setUpdatedBy(additionalGoalsDTO.getCreatedBy());

			message = "Additional Goals Creation SuccessFully";

		} else {

			additionalGoalsVO = additionalGoalsRepo.findById(additionalGoalsDTO.getId())
					.orElseThrow(() -> new ApplicationException(
							"Additional Goals  not found with id: " + additionalGoalsDTO.getId()));

			additionalGoalsVO.setUpdatedBy(additionalGoalsDTO.getCreatedBy());

			message = "PreGoals Updation SuccessFully";
		}

		additionalGoalsVO = getAdditionalGoalsVOFormAdditionalGoalsDTO(additionalGoalsVO, additionalGoalsDTO);
		additionalGoalsRepo.save(additionalGoalsVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("additionalGoalsVO", additionalGoalsVO);
		return response;
	}

	private AdditionalGoalsVO getAdditionalGoalsVOFormAdditionalGoalsDTO(AdditionalGoalsVO additionalGoalsVO,
			@Valid AdditionalGoalsDTO additionalGoalsDTO) {

		additionalGoalsVO.setAreaOfImportance(additionalGoalsDTO.getAreaOfImportance());
		additionalGoalsVO.setKeyPerformanceIndicator(additionalGoalsDTO.getKeyPerformanceIndicator());
		additionalGoalsVO.setPerformanceIndicator(additionalGoalsDTO.getPerformanceIndicator());
		additionalGoalsVO.setGoal(additionalGoalsDTO.getGoal());
		//additionalGoalsVO.setRemarks(additionalGoalsDTO.getRemarks());
		additionalGoalsVO.setFinYear(additionalGoalsDTO.getFinYear());
	//	additionalGoalsVO.setCancel(additionalGoalsDTO.isCancel()); // boolean field uses `isCancel()`
		additionalGoalsVO.setOrgId(additionalGoalsDTO.getOrgId());

		return additionalGoalsVO;
	}

	@Override
	public Optional<AdditionalGoalsVO> getAdditionalGoalsById(Long id) {
		return additionalGoalsRepo.findById(id);
	};

	@Override
	public List<AdditionalGoalsVO> getAdditionalGoalsByOrgId(Long orgId) {
		return additionalGoalsRepo.getAdditionalGoals(orgId);
	}

	@Override
	public List<Map<String, Object>> getAdditionalGoalsDropDownApis(Long orgId, String finYear, String branchCode,
			String designation) {
		Set<Object[]> chType = additionalGoalsRepo.getAdditionalGoals(orgId, finYear, branchCode,designation);
		return getAdditional(chType);
	}

	private List<Map<String, Object>> getAdditional(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("areaOfImportance", (ch != null && ch.length > 0 && ch[0] != null) ? ch[0].toString() : "");
			map.put("keyPerformanceIndicators", (ch != null && ch.length > 1 && ch[1] != null) ? ch[1].toString() : "");
			
			List1.add(map);
		}
		return List1;
	}

//	@Override
//	public List<AdditionalGoalsVO> getAllAdditionalGoals() {
//		// TODO Auto-generated method stub
//		return additionalGoalsRepo.findAll();
//	}
}
