package com.efit.hrms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efit.hrms.dto.ContractMasterDTO;
import com.efit.hrms.dto.OtMasterDTO;
import com.efit.hrms.dto.OtMasterDetailsDTO;
import com.efit.hrms.dto.ShiftAssignDTO;
import com.efit.hrms.dto.ShiftAssignDetailsDTO;
import com.efit.hrms.dto.ShiftMasterDTO;
import com.efit.hrms.entity.ContractMasterVO;
import com.efit.hrms.entity.OtMasterDetailsVO;
import com.efit.hrms.entity.OtMasterVO;
import com.efit.hrms.entity.ShiftAssignDetailsVO;
import com.efit.hrms.entity.ShiftAssignVO;
import com.efit.hrms.entity.ShiftMasterVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.ContractMasterRepo;
import com.efit.hrms.repo.OtMasterDetailsRepo;
import com.efit.hrms.repo.OtMasterRepo;
import com.efit.hrms.repo.ShiftAssignDetailsRepo;
import com.efit.hrms.repo.ShiftAssignRepo;
import com.efit.hrms.repo.ShiftMasterRepo;

@Service
public class ShiftMasterServiceImpl implements ShiftMasterService{

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

	

	@Override
	public Map<String, Object> createUpdateShiftMaster(List<ShiftMasterDTO> shiftMasterDTOList) throws ApplicationException {
	    List<ShiftMasterVO> savedList = new ArrayList<>();
	    int createdCount = 0;
	    int updatedCount = 0;

	    for (ShiftMasterDTO shiftMasterDTO : shiftMasterDTOList) {
	        ShiftMasterVO shiftMasterVO;

	        if (ObjectUtils.isNotEmpty(shiftMasterDTO.getId())) {
	            shiftMasterVO = shiftMasterRepo.findById(shiftMasterDTO.getId())
	                    .orElseThrow(() -> new ApplicationException("Invalid ShiftMaster ID: " + shiftMasterDTO.getId()));
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
		shiftMasterVO.setOutDate(shiftMasterDTO.getOutDate());
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
	public Map<String, Object> createUpdateContractMaster(ContractMasterDTO contractMasterDTO) throws ApplicationException {

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

	private void createUpdateContractMasterVOByContractMasterDTO(ContractMasterDTO contractMasterDTO, ContractMasterVO contractMasterVO) {
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

		contractMasterVO.setOrgId(contractMasterDTO.getOrgId());
		contractMasterVO.setBranchCode(contractMasterDTO.getBranchCode());
		contractMasterVO.setBranch(contractMasterDTO.getBranch());
		contractMasterVO.setFinYear(contractMasterDTO.getFinYear());

		contractMasterVO.setActive(contractMasterDTO.isActive());


	}
	
	
	@Override
	public List<ContractMasterVO> getAllContractMasterByOrgId(Long orgId) {
		return Optional.ofNullable(contractMasterRepo.getAllContractMasterByOrgId(orgId)).orElseGet(Collections::emptyList);
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
	
	//shiftassign
	
	
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

	    List<ShiftAssignDetailsVO> detailsList = new ArrayList<>();

	    if (dto.getShiftAssignDetailsDTO() != null && !dto.getShiftAssignDetailsDTO().isEmpty()) {
	        for (ShiftAssignDetailsDTO detailDTO : dto.getShiftAssignDetailsDTO()) {
	            ShiftAssignDetailsVO detailVO = new ShiftAssignDetailsVO();

	            detailVO.setEmployeeCode(detailDTO.getEmployeeCode());
	            detailVO.setEmployeeName(detailDTO.getEmployeeName());
	            detailVO.setShiftType(detailDTO.getShiftType());
	            detailVO.setStartTime(detailDTO.getStartTime());
	            detailVO.setEndTime(detailDTO.getEndTime());
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
	
	
}
