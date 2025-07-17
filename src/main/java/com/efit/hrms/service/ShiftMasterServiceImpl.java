package com.efit.hrms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efit.hrms.dto.ContractMasterDTO;
import com.efit.hrms.dto.GroupDTO;
import com.efit.hrms.dto.GroupDetailsDTO;
import com.efit.hrms.dto.GroupSalaryDeductionsDTO;
import com.efit.hrms.dto.GroupSalaryEarningsDTO;
import com.efit.hrms.dto.GroupSalaryStructureDTO;
import com.efit.hrms.dto.OtMasterDTO;
import com.efit.hrms.dto.OtMasterDetailsDTO;
import com.efit.hrms.dto.ShiftAssignDTO;
import com.efit.hrms.dto.ShiftAssignDetailsDTO;
import com.efit.hrms.dto.ShiftMasterDTO;
import com.efit.hrms.entity.ContractMasterVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.GroupDetailsVO;
import com.efit.hrms.entity.GroupSalaryDeductionsVO;
import com.efit.hrms.entity.GroupSalaryEarningsVO;
import com.efit.hrms.entity.GroupSalaryStructureVO;
import com.efit.hrms.entity.GroupVO;
import com.efit.hrms.entity.OtMasterDetailsVO;
import com.efit.hrms.entity.OtMasterVO;
import com.efit.hrms.entity.SalaryDetectionDetailsVO;
import com.efit.hrms.entity.SalaryEarningDetailsVO;
import com.efit.hrms.entity.SalaryStructureVO;
import com.efit.hrms.entity.ShiftAssignDetailsVO;
import com.efit.hrms.entity.ShiftAssignVO;
import com.efit.hrms.entity.ShiftMasterVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.exception.GroupDetailsRepo;
import com.efit.hrms.repo.ContractMasterRepo;
import com.efit.hrms.repo.EmployeeRepo;
import com.efit.hrms.repo.GroupRepo;
import com.efit.hrms.repo.GroupSalaryDeductionsRepo;
import com.efit.hrms.repo.GroupSalaryEarningsRepo;
import com.efit.hrms.repo.GroupSalaryStructureRepo;
import com.efit.hrms.repo.NotificationRepo;
import com.efit.hrms.repo.OtMasterDetailsRepo;
import com.efit.hrms.repo.OtMasterRepo;
import com.efit.hrms.repo.SalaryStructureRepo;
import com.efit.hrms.repo.ShiftAssignDetailsRepo;
import com.efit.hrms.repo.ShiftAssignRepo;
import com.efit.hrms.repo.ShiftMasterRepo;

@Service
public class ShiftMasterServiceImpl implements ShiftMasterService {

	private final NotificationRepo notificationRepo;

	public static final Logger LOGGER = LoggerFactory.getLogger(ShiftMasterServiceImpl.class);

	@Autowired
	ShiftMasterRepo shiftMasterRepo;

	@Autowired
	ContractMasterRepo contractMasterRepo;

	@Autowired
	OtMasterRepo otMasterRepo;

	@Autowired
	OtMasterDetailsRepo otMasterDetailsRepo;

	@Autowired
	ShiftAssignRepo shiftAssignRepo;

	@Autowired
	ShiftAssignDetailsRepo shiftAssignDetailsRepo;

	@Autowired
	GroupRepo groupRepo;

	@Autowired
	GroupDetailsRepo groupDetailsRepo;

	@Autowired
	GroupSalaryStructureRepo groupSalaryStructureRepo;

	@Autowired
	GroupSalaryEarningsRepo groupSalaryEarningsRepo;

	@Autowired
	GroupSalaryDeductionsRepo groupSalaryDeductionsRepo;
	
	@Autowired
	SalaryStructureRepo salaryStructureRepo;

	@Autowired
	EmployeeRepo employeeRepo;

	ShiftMasterServiceImpl(NotificationRepo notificationRepo) {
		this.notificationRepo = notificationRepo;
	}

	@Override
	public Map<String, Object> createUpdateShiftMaster(List<ShiftMasterDTO> shiftMasterDTOList)
			throws ApplicationException {
		List<ShiftMasterVO> savedList = new ArrayList<>();
		int createdCount = 0;
		int updatedCount = 0;

		for (ShiftMasterDTO shiftMasterDTO : shiftMasterDTOList) {
			ShiftMasterVO shiftMasterVO;

			if (ObjectUtils.isNotEmpty(shiftMasterDTO.getId())) {
				shiftMasterVO = shiftMasterRepo.findById(shiftMasterDTO.getId()).orElseThrow(
						() -> new ApplicationException("Invalid ShiftMaster ID: " + shiftMasterDTO.getId()));
				shiftMasterVO.setUpdatedBy(shiftMasterDTO.getCreatedBy());
				updatedCount++;
			} else {
				shiftMasterVO = new ShiftMasterVO();
				shiftMasterVO.setCreatedBy(shiftMasterDTO.getCreatedBy());
				shiftMasterVO.setUpdatedBy(shiftMasterDTO.getCreatedBy());
				createdCount++;
			}

			createUpdateShiftMasterVOByShiftMasterDTO(shiftMasterDTO, shiftMasterVO);
			savedList.add(shiftMasterRepo.save(shiftMasterVO));
		}

		// ✅ Return only one message based on operation
		String message;
		if (createdCount > 0 && updatedCount == 0) {
			message = "ShiftMaster Created Successfully";
		} else if (updatedCount > 0 && createdCount == 0) {
			message = "ShiftMaster Updated Successfully";
		} else {
			// Optional: throw error, or default to a message
			message = "ShiftMaster Created and Updated Successfully";
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("shiftMasterList", savedList);
		return response;
	}

	private void createUpdateShiftMasterVOByShiftMasterDTO(ShiftMasterDTO shiftMasterDTO, ShiftMasterVO shiftMasterVO) {
		shiftMasterVO.setShiftCode(shiftMasterDTO.getShiftCode());
		shiftMasterVO.setShift(shiftMasterDTO.getShift());
		shiftMasterVO.setInTime(shiftMasterDTO.getInTime());
		shiftMasterVO.setOutTime(shiftMasterDTO.getOutTime());
		shiftMasterVO.setBreakTime(shiftMasterDTO.getBreakTime());
		shiftMasterVO.setGraceTime(shiftMasterDTO.getGraceTime());
		shiftMasterVO.setNightShift(shiftMasterDTO.isNightShift());

		shiftMasterVO.setOrgId(shiftMasterDTO.getOrgId());
		shiftMasterVO.setBranchCode(shiftMasterDTO.getBranchCode());
		shiftMasterVO.setBranch(shiftMasterDTO.getBranch());
		shiftMasterVO.setFinYear(shiftMasterDTO.getFinYear());

		shiftMasterVO.setActive(shiftMasterDTO.isActive());

	}

	@Override
	public List<ShiftMasterVO> getAllShiftMasterByOrgId(Long orgId) {
		return Optional.ofNullable(shiftMasterRepo.getAllShiftMasterByOrgId(orgId)).orElseGet(Collections::emptyList);
	}

	@Override
	public ShiftMasterVO getShiftMasterById(Long id) {
		return Optional.ofNullable(shiftMasterRepo.getShiftMasterById(id))
				.orElseThrow(() -> new RuntimeException("ShiftMaster not found for ID: " + id));
	}

//ContractMaster

	@Override
	public Map<String, Object> createUpdateContractMaster(ContractMasterDTO contractMasterDTO)
			throws ApplicationException {

		ContractMasterVO contractMasterVO = new ContractMasterVO();
		String message;
		String screenCode = "SM";
		if (ObjectUtils.isNotEmpty(contractMasterDTO.getId())) {
			contractMasterVO = contractMasterRepo.findById(contractMasterDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid contractMaster details"));

			contractMasterVO.setUpdatedBy(contractMasterDTO.getCreatedBy());
			message = "ShiftMaster Updated Successfully";
		} else {

			contractMasterVO.setCreatedBy(contractMasterDTO.getCreatedBy());
			contractMasterVO.setUpdatedBy(contractMasterDTO.getCreatedBy());
			message = "ContractMaster Created Successfully";
		}

		createUpdateContractMasterVOByContractMasterDTO(contractMasterDTO, contractMasterVO);
		contractMasterRepo.save(contractMasterVO);
		Map<String, Object> response = new HashMap<>();
		response.put("contractMasterVO", contractMasterVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateContractMasterVOByContractMasterDTO(ContractMasterDTO contractMasterDTO,
			ContractMasterVO contractMasterVO) {
		contractMasterVO.setContractorCode(contractMasterDTO.getContractorCode());
		contractMasterVO.setContractor(contractMasterDTO.getContractor());
		contractMasterVO.setContactPerson(contractMasterDTO.getContactPerson());
		contractMasterVO.setContactNumber(contractMasterDTO.getContactNumber());
		contractMasterVO.setStartDate(contractMasterDTO.getStartDate());
		contractMasterVO.setEndDate(contractMasterDTO.getEndDate());
		contractMasterVO.setAddress(contractMasterDTO.getAddress());
		contractMasterVO.setStatus(contractMasterDTO.getStatus());
		contractMasterVO.setPanNo(contractMasterDTO.getPanNo());
		contractMasterVO.setGst(contractMasterDTO.getGst());
		contractMasterVO.setEmail(contractMasterDTO.getEmail());
		contractMasterVO.setContractValue(contractMasterDTO.getContractValue());
		contractMasterVO.setRemarks(contractMasterDTO.getRemarks());
		contractMasterVO.setContractType(contractMasterDTO.getContractType());
		contractMasterVO.setRenewalRequired(contractMasterDTO.isRenewalRequired());

		contractMasterVO.setOrgId(contractMasterDTO.getOrgId());
		contractMasterVO.setBranchCode(contractMasterDTO.getBranchCode());
		contractMasterVO.setBranch(contractMasterDTO.getBranch());
		contractMasterVO.setFinYear(contractMasterDTO.getFinYear());

		contractMasterVO.setActive(contractMasterDTO.isActive());

	}

	@Override
	public List<ContractMasterVO> getAllContractMasterByOrgId(Long orgId) {
		return Optional.ofNullable(contractMasterRepo.getAllContractMasterByOrgId(orgId))
				.orElseGet(Collections::emptyList);
	}

	@Override
	public ContractMasterVO getContractMasterById(Long id) {
		return Optional.ofNullable(contractMasterRepo.getContractMasterById(id))
				.orElseThrow(() -> new RuntimeException("ContractMaster not found for ID: " + id));
	}

	@Override
	public Map<String, Object> createUpdateOtMaster(OtMasterDTO otMasterDTO) throws ApplicationException {

		OtMasterVO otMasterVO;
		String message;

		if (ObjectUtils.isNotEmpty(otMasterDTO.getId())) {
			// Fetch existing OT Master
			otMasterVO = otMasterRepo.findById(otMasterDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid OT Master details"));

			// Delete old child records
			List<OtMasterDetailsVO> existingDetails = otMasterDetailsRepo.findByOtMasterVO(otMasterVO);
			otMasterDetailsRepo.deleteAll(existingDetails);

			otMasterVO.setUpdatedBy(otMasterDTO.getCreatedBy());
			message = "OT Master Updated Successfully";
		} else {
			otMasterVO = new OtMasterVO();
			otMasterVO.setCreatedBy(otMasterDTO.getCreatedBy());
			otMasterVO.setUpdatedBy(otMasterDTO.getCreatedBy());
			message = "OT Master Created Successfully";
		}

		// Map DTO to VO
		createUpdateOtMasterVOByOtMasterDTO(otMasterDTO, otMasterVO);

		// Save parent with new child records
		otMasterRepo.save(otMasterVO);

		Map<String, Object> response = new HashMap<>();
		response.put("otMasterVO", otMasterVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateOtMasterVOByOtMasterDTO(OtMasterDTO otMasterDTO, OtMasterVO otMasterVO) {
		// Set basic fields
		otMasterVO.setOtType(otMasterDTO.getOtType());
		otMasterVO.setOtCategory(otMasterDTO.getOtCategory());
		otMasterVO.setOrgId(otMasterDTO.getOrgId());
		otMasterVO.setBranch(otMasterDTO.getBranch());
		otMasterVO.setBranchCode(otMasterDTO.getBranchCode());
		otMasterVO.setFinYear(otMasterDTO.getFinYear());
		otMasterVO.setCreatedBy(otMasterDTO.getCreatedBy());
		otMasterVO.setActive(otMasterDTO.isActive());

		// Clear existing detail list (if update)
		if (otMasterVO.getOtMasterDetailsVO() != null) {
			otMasterVO.getOtMasterDetailsVO().clear();
		}

		// Map OtMasterDetailsDTO list
		if (otMasterDTO.getOtMasterDetailsDTO() != null && !otMasterDTO.getOtMasterDetailsDTO().isEmpty()) {
			List<OtMasterDetailsVO> detailVOList = new ArrayList<>();

			for (OtMasterDetailsDTO dto : otMasterDTO.getOtMasterDetailsDTO()) {
				OtMasterDetailsVO vo = new OtMasterDetailsVO();

				vo.setSlab(dto.getSlab());
				vo.setMinHours(dto.getMinHours());
				vo.setMaxHours(dto.getMaxHours());
				vo.setOtrate(dto.getOtrate());
				vo.setEffectiveFrom(dto.getEffectiveFrom());
				vo.setEffectiveTo(dto.getEffectiveTo());
				vo.setApplicable(dto.isApplicable());

				vo.setOtMasterVO(otMasterVO); // Set back-reference to parent
				detailVOList.add(vo);
			}

			otMasterVO.setOtMasterDetailsVO(detailVOList); // Set in parent VO
		}
	}

	@Override
	public List<OtMasterVO> getAllOtMasterByOrgId(Long orgId) {
		return Optional.ofNullable(otMasterRepo.getAllOtMasterByOrgId(orgId)).orElseGet(Collections::emptyList);
	}

	@Override
	public OtMasterVO getOtMasterById(Long id) {
		return Optional.ofNullable(otMasterRepo.getOtMasterById(id))
				.orElseThrow(() -> new RuntimeException("OtMaster not found for ID: " + id));
	}

	// shiftassign

	@Override
	public Map<String, Object> createUpdateShiftAssign(ShiftAssignDTO shiftAssignDTO) throws ApplicationException {
		ShiftAssignVO shiftAssignVO;
		String message;

		if (ObjectUtils.isNotEmpty(shiftAssignDTO.getId())) {
			// Fetch existing ShiftAssignVO
			shiftAssignVO = shiftAssignRepo.findById(shiftAssignDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid shiftAssign Master details"));

			// Delete existing child records
			List<ShiftAssignDetailsVO> existingDetails = shiftAssignDetailsRepo.findByShiftAssignVO(shiftAssignVO);
			shiftAssignDetailsRepo.deleteAll(existingDetails);

			shiftAssignVO.setUpdatedBy(shiftAssignDTO.getCreatedBy());
			message = "ShiftAssign Updated Successfully";
		} else {
			shiftAssignVO = new ShiftAssignVO();
			shiftAssignVO.setCreatedBy(shiftAssignDTO.getCreatedBy());
			shiftAssignVO.setUpdatedBy(shiftAssignDTO.getCreatedBy());
			message = "ShiftAssign Created Successfully";
		}

		// Map DTO to VO
		createUpdateShiftAssignVOByShiftAssignDTO(shiftAssignDTO, shiftAssignVO);

		// Save parent with new child records
		shiftAssignRepo.save(shiftAssignVO);

		Map<String, Object> response = new HashMap<>();
		response.put("shiftAssignVO", shiftAssignVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateShiftAssignVOByShiftAssignDTO(ShiftAssignDTO dto, ShiftAssignVO vo) {
		vo.setShiftType(dto.getShiftType());
		vo.setDescription(dto.getDescription());
		vo.setOrgId(dto.getOrgId());
		vo.setBranch(dto.getBranch());
		vo.setBranchCode(dto.getBranchCode());
		vo.setFinYear(dto.getFinYear());
		vo.setActive(dto.isActive());
		vo.setType(dto.getType());
		vo.setContractor(dto.getContractor());


		List<ShiftAssignDetailsVO> detailsList = new ArrayList<>();

		if (dto.getShiftAssignDetailsDTO() != null && !dto.getShiftAssignDetailsDTO().isEmpty()) {
			for (ShiftAssignDetailsDTO detailDTO : dto.getShiftAssignDetailsDTO()) {
				ShiftAssignDetailsVO detailVO = new ShiftAssignDetailsVO();

				detailVO.setEmployeeCode(detailDTO.getEmployeeCode());
				detailVO.setEmployeeName(detailDTO.getEmployeeName());
				detailVO.setShiftType(detailDTO.getShiftType());
				detailVO.setInTime(detailDTO.getInTime());
				detailVO.setOutTime(detailDTO.getOutTime());
				detailVO.setHours(detailDTO.getHours());
				detailVO.setEffectiveFrom(detailDTO.getEffectiveFrom());
				detailVO.setEffectiveTo(detailDTO.getEffectiveTo());

				detailVO.setShiftAssignVO(vo); // Set parent reference
				detailsList.add(detailVO);
			}
		}

		vo.setShiftAssignDetailsVO(detailsList);
	}

	@Override
	public ShiftAssignVO getShiftAssignById(Long id) {
		return Optional.ofNullable(shiftAssignRepo.getShiftAssignById(id))
				.orElseThrow(() -> new RuntimeException("ShiftAssign not found for ID: " + id));
	}

	@Override
	public List<ShiftAssignVO> getAllShiftAssignByOrgId(Long orgId) {
		return Optional.ofNullable(shiftAssignRepo.getAllShiftAssignByOrgId(orgId)).orElseGet(Collections::emptyList);
	}
	

	@Override
	public List<ShiftMasterVO> getAllShiftMasterByOrgIdAndShiftAndBranchCode(Long orgId, String shift,String shiftCode, String branchCode) {
		return Optional.ofNullable(shiftMasterRepo.getAllShiftMasterByOrgIdAndShiftAndBranchCode(orgId,  shift,shiftCode,  branchCode)).orElseGet(Collections::emptyList);
	}

	// groupstructure

	@Transactional
	@Override
	public Map<String, Object> createUpdateGroup(@Valid GroupDTO groupDTO) throws ApplicationException {
		String message;

		GroupVO groupVO = null;

		if (ObjectUtils.isEmpty(groupDTO.getId())) {

			if (groupRepo.existsByGroupName(groupDTO.getGroupName())) {

				String errorMessage = String.format("This Group: %s Already Exists in This Organization",
						groupDTO.getGroupName());
				throw new ApplicationException(errorMessage);

			}

			groupVO = new GroupVO();

			groupVO.setCreatedBy(groupDTO.getCreatedBy());
			groupVO.setUpdatedBy(groupDTO.getCreatedBy());

			message = "Group Creation SuccessFully";

		} else {

			groupVO = groupRepo.findById(groupDTO.getId())
					.orElseThrow(() -> new ApplicationException("Group  not found with id: " + groupDTO.getId()));

			if (!groupVO.getGroupName().equals(groupDTO.getGroupName())) {

				if (groupRepo.existsByGroupName(groupDTO.getGroupName())) {

					String errorMessage = String.format("This Group: %s Already Exists in This Organization",
							groupDTO.getGroupName());
					throw new ApplicationException(errorMessage);

				}
			}

			List<GroupDetailsVO> existingDetails = groupDetailsRepo.findByGroupVO(groupVO);
			groupDetailsRepo.deleteAll(existingDetails);

			groupVO.setUpdatedBy(groupDTO.getCreatedBy());

			message = "Group Updation SuccessFully";
		}

		groupVO = getGroupDTOFormGroupDTO(groupVO, groupDTO);
		groupRepo.save(groupVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("groupVO", groupVO);
		return response;
	}

	private GroupVO getGroupDTOFormGroupDTO(GroupVO groupVO, GroupDTO groupDTO) throws ApplicationException {

		groupVO.setGroupName(groupDTO.getGroupName());
		groupVO.setCancelRemark(groupDTO.getCancelRemark());
		groupVO.setFinYear(groupDTO.getFinYear());
		groupVO.setOrgId(groupDTO.getOrgId());
		groupVO.setActive(groupDTO.isActive());
		groupVO.setBranchName(groupDTO.getBranchName());
		groupVO.setBranchCode(groupDTO.getBranchCode());

		List<GroupDetailsVO> groupDetailsVOs = new ArrayList<>();

		for (GroupDetailsDTO groupDetailsDTO : groupDTO.getGroupDetailsDTO()) {

			GroupDetailsVO groupDetailsVO = new GroupDetailsVO();

			groupDetailsVO.setCode(groupDetailsDTO.getCode());
			groupDetailsVO.setDepartment(groupDetailsDTO.getDepartment());
			groupDetailsVO.setName(groupDetailsDTO.getName());

			groupDetailsVO.setGroupVO(groupVO);
			groupDetailsVOs.add(groupDetailsVO);

		}

		groupVO.setGroupDetailsVO(groupDetailsVOs);

		return groupVO;
	}

	@Override
	public List<GroupVO> getGroupByOrgId(Long orgId) {
		return groupRepo.getGroupByOrgId(orgId);
	}

	@Override
	public Optional<GroupVO> getPreGroupById(Long id) {
		return groupRepo.findById(id);
	}

	// GroupsalaryStructure
	@Transactional
	@Override
	public Map<String, Object> createUpdateGroupSalaryStructure(GroupSalaryStructureDTO dto) throws ApplicationException {
	    GroupSalaryStructureVO vo;
	    String message;

	    if (ObjectUtils.isNotEmpty(dto.getId())) {
	        vo = groupSalaryStructureRepo.findById(dto.getId())
	                .orElseThrow(() -> new ApplicationException("Invalid groupSalaryStructure Master details"));

	        groupSalaryEarningsRepo.deleteAll(groupSalaryEarningsRepo.findByGroupSalaryStructureVO(vo));
	        groupSalaryDeductionsRepo.deleteAll(groupSalaryDeductionsRepo.findByGroupSalaryStructureVO(vo));

	        vo.setUpdatedBy(dto.getCreatedBy());
	        message = "GroupSalaryStructure Updated Successfully";
	    } else {
	        vo = new GroupSalaryStructureVO();
	        vo.setCreatedBy(dto.getCreatedBy());
	        vo.setUpdatedBy(dto.getCreatedBy());
	        message = "GroupSalaryStructure Created Successfully";
	    }

	    vo.setGroupName(dto.getGroupName());
	    vo.setOrgId(dto.getOrgId());
	    vo.setBranchName(dto.getBranchName());
	    vo.setBranchCode(dto.getBranchCode());
	    vo.setFinYear(dto.getFinYear());
	    vo.setActive(dto.isActive());

	    List<GroupSalaryEarningsVO> earningsList = new ArrayList<>();
	    BigDecimal sumOfEarnings = BigDecimal.ZERO;
	    if (dto.getGroupSalaryEarningsDTO() != null) {
	        for (GroupSalaryEarningsDTO e : dto.getGroupSalaryEarningsDTO()) {
	            GroupSalaryEarningsVO voE = new GroupSalaryEarningsVO();
	            voE.setHeading(e.getHeading());
	            voE.setAmount(e.getAmount());
	            voE.setGroupSalaryStructureVO(vo);
	            sumOfEarnings = sumOfEarnings.add(e.getAmount());
	            earningsList.add(voE);
	        }
	    }

	    List<GroupSalaryDeductionsVO> deductionList = new ArrayList<>();
	    BigDecimal sumOfDeductions = BigDecimal.ZERO;
	    if (dto.getGroupSalaryDeductionsDTO() != null) {
	        for (GroupSalaryDeductionsDTO d : dto.getGroupSalaryDeductionsDTO()) {
	            GroupSalaryDeductionsVO voD = new GroupSalaryDeductionsVO();
	            voD.setHeading(d.getHeading());
	            voD.setAmount(d.getAmount());
	            voD.setGroupSalaryStructureVO(vo);
	            sumOfDeductions = sumOfDeductions.add(d.getAmount());
	            deductionList.add(voD);
	        }
	    }

	    vo.setGroupSalaryEarningsVO(earningsList);
	    vo.setGroupSalaryDeductionsVO(deductionList);

	    groupSalaryStructureRepo.save(vo);

	    List<GroupVO> groupVOList = groupRepo.findByGroupNameAndOrgIdAndBranchCode(
	            dto.getGroupName(), dto.getOrgId(), dto.getBranchCode());


	    if (groupVOList != null && !groupVOList.isEmpty()) {
	    	for (GroupVO group : groupVOList) {
	            for (GroupDetailsVO detail : group.getGroupDetailsVO()) {
	                EmployeeVO employee = employeeRepo.findByEmployeeCode(detail.getCode());

	                if (employee != null) {

	                    SalaryStructureVO salaryVO = new SalaryStructureVO();
	                    salaryVO.setEmployeeCode(employee.getEmployeeCode());
	                    salaryVO.setEmployeeName(employee.getEmployeeName());
	                    salaryVO.setDateOfBirth(employee.getDateOfBirth());
	                    salaryVO.setGrade(employee.getGrade());
	                    salaryVO.setDepartment(employee.getDepartment());
	                    salaryVO.setPanNo(employee.getPanNo());
	                    salaryVO.setBankAccountNo(employee.getAccountNo());
	                    salaryVO.setDateOfJoining(employee.getJoiningDate());
	                    salaryVO.setDesignation(employee.getDesignation());
	                    salaryVO.setBranch(employee.getBranch());
	                    salaryVO.setBranchCode(employee.getBranchCode());
	                    salaryVO.setOrgId(employee.getOrgId());
	                    salaryVO.setCreatedBy(dto.getCreatedBy());
	                    salaryVO.setSumOfEarning(sumOfEarnings);
	                    salaryVO.setSumOfDetection(sumOfDeductions);
	                    salaryVO.setAmount(sumOfEarnings.subtract(sumOfDeductions));

	                    List<SalaryEarningDetailsVO> earningDetails = new ArrayList<>();
	                    for (GroupSalaryEarningsVO gEarn : earningsList) {

	                        SalaryEarningDetailsVO sEarn = new SalaryEarningDetailsVO();
	                        sEarn.setHeading(gEarn.getHeading());
	                        sEarn.setAmount(gEarn.getAmount());
	                        sEarn.setSalaryStructureVO(salaryVO);
	                        earningDetails.add(sEarn);
	                    }

	                    List<SalaryDetectionDetailsVO> deductionDetails = new ArrayList<>();
	                    for (GroupSalaryDeductionsVO gDed : deductionList) {

	                        SalaryDetectionDetailsVO sDed = new SalaryDetectionDetailsVO();
	                        sDed.setHeading(gDed.getHeading());
	                        sDed.setAmount(gDed.getAmount());
	                        sDed.setSalaryStructureVO(salaryVO);
	                        deductionDetails.add(sDed);
	                    }

	                    salaryVO.setSalaryEarningDetailsVO(earningDetails);
	                    salaryVO.setSalaryDetectionDetailsVO(deductionDetails);

	                    salaryStructureRepo.saveAndFlush(salaryVO);
	                } else {
	                    System.out.println("Employee not found for code: " + detail.getCode());
	                }
	            }
	        }
	    } else {
	        System.out.println("Group not found or empty for: " + dto.getGroupName());
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("groupSalaryStructureVO", vo);
	    response.put("message", message);
	    return response;
	}

	@Override
	public List<GroupSalaryStructureVO> getGroupSalaryStructureByOrgId(Long orgId) {
		return Optional.ofNullable(groupSalaryStructureRepo.getGroupSalaryStructureByOrgId(orgId))
				.orElseGet(Collections::emptyList);
	}

	@Override
	public Optional<GroupSalaryStructureVO> getGroupSalaryStructureById(Long id) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(groupSalaryStructureRepo.getGroupSalaryStructureById(id))
				.orElseThrow(() -> new RuntimeException("GroupSalaryStructure not found for ID: " + id));
	}

	@Override
	public List<GroupVO> getGroupMasterByOrgIdAndGroup(Long orgId, String groupName) {
		return groupRepo.getGroupMasterByOrgIdAndGroup(orgId, groupName);
	}

	
	//SHIFTASSIGNFILTER
	

	@Override
	public List<EmployeeVO> getShiftAssignByOrgIdAndType(Long orgId,String type, String contractor,String department) {
		return Optional.ofNullable(shiftAssignRepo.getShiftAssignByOrgIdAndType(orgId,type,contractor,department))
				.orElseGet(Collections::emptyList);
}
	
	
	
	
	@Override
	public List<Map<String, Object>> getAllEmployeeAndShiftMasterDetails(Long orgId, String type, String contractor,
	                                                                     String department, String shift, String shiftCode, String branchCode) {
	    Set<Object[]> result = shiftAssignRepo.getAllEmployeeAndShiftMasterDetails(orgId, type, contractor, department, shift, shiftCode, branchCode);
	    return mapEmployeeShiftData(result);
	}

	private List<Map<String, Object>> mapEmployeeShiftData(Set<Object[]> result) {
	    List<Map<String, Object>> list = new ArrayList<>();
	    for (Object[] record : result) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("employeeName", record[0]);
	        map.put("employeeCode", record[1]);
	        map.put("department", record[2]);
	        map.put("branch", record[3]);
	        map.put("shiftCode", record[4]);
	        map.put("branchCode", record[5]);
	        map.put("shift", record[6]);
	        map.put("inTime", record[7]);
	        map.put("outTime", record[8]);
	        map.put("nightShift", record[9]);
	        list.add(map);
	    }
	    return list;
	}

	
}
