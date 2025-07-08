package com.efit.hrms.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.efit.hrms.dto.ContractMasterDTO;
import com.efit.hrms.dto.ShiftMasterDTO;
import com.efit.hrms.entity.ContractMasterVO;
import com.efit.hrms.entity.ShiftMasterVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface ShiftMasterService {

	Map<String, Object> createUpdateShiftMaster(ShiftMasterDTO shiftMasterDTO) throws ApplicationException;

	List<ShiftMasterVO> getAllShiftMasterByOrgId(Long orgId);

	ShiftMasterVO getShiftMasterById(Long id);

	//ContractMaster
	Map<String, Object> createUpdateContractMaster(ContractMasterDTO contractMasterDTO) throws ApplicationException;

	List<ContractMasterVO> getAllContractMasterByOrgId(Long orgId);

	ContractMasterVO getContractMasterById(Long id);

}
