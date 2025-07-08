package com.efit.hrms.service;

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
import com.efit.hrms.dto.ShiftMasterDTO;
import com.efit.hrms.entity.ContractMasterVO;
import com.efit.hrms.entity.ShiftMasterVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.ContractMasterRepo;
import com.efit.hrms.repo.ShiftMasterRepo;

@Service
public class ShiftMasterServiceImpl implements ShiftMasterService{

	public static final Logger LOGGER = LoggerFactory.getLogger(ShiftMasterServiceImpl.class);

	@Autowired
	ShiftMasterRepo shiftMasterRepo;
	
	@Autowired
	ContractMasterRepo contractMasterRepo;
	

	@Override
	public Map<String, Object> createUpdateShiftMaster(ShiftMasterDTO shiftMasterDTO) throws ApplicationException {

		ShiftMasterVO shiftMasterVO = new ShiftMasterVO();
		String message;
		String screenCode = "SM";
		if (ObjectUtils.isNotEmpty(shiftMasterDTO.getId())) {
			shiftMasterVO = shiftMasterRepo.findById(shiftMasterDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid ShiftMaster details"));

			shiftMasterVO.setUpdatedBy(shiftMasterDTO.getCreatedBy());
			message = "ShiftMaster Updated Successfully";
		} else {


			shiftMasterVO.setCreatedBy(shiftMasterDTO.getCreatedBy());
			shiftMasterVO.setUpdatedBy(shiftMasterDTO.getCreatedBy());
			message = "ShiftMaster Created Successfully";
		}

		createUpdateShiftMasterVOByShiftMasterDTO(shiftMasterDTO, shiftMasterVO);
		shiftMasterRepo.save(shiftMasterVO);
		Map<String, Object> response = new HashMap<>();
		response.put("shiftMasterVO", shiftMasterVO);
		response.put("message", message);
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
	
}
