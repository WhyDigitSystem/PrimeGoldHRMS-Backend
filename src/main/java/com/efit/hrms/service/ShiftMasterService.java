package com.efit.hrms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.efit.hrms.dto.ContractMasterDTO;
import com.efit.hrms.dto.GroupDTO;
import com.efit.hrms.dto.GroupSalaryStructureDTO;
import com.efit.hrms.dto.OtMasterDTO;
import com.efit.hrms.dto.ShiftAssignDTO;
import com.efit.hrms.dto.ShiftMasterDTO;
import com.efit.hrms.entity.ContractMasterVO;
import com.efit.hrms.entity.GroupSalaryStructureVO;
import com.efit.hrms.entity.GroupVO;
import com.efit.hrms.entity.OtMasterVO;
import com.efit.hrms.entity.ShiftAssignVO;
import com.efit.hrms.entity.ShiftMasterVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface ShiftMasterService {

	Map<String, Object> createUpdateShiftMaster(List<ShiftMasterDTO> shiftMasterDTOList) throws ApplicationException;

	List<ShiftMasterVO> getAllShiftMasterByOrgId(Long orgId);

	ShiftMasterVO getShiftMasterById(Long id);

	//ContractMaster
	Map<String, Object> createUpdateContractMaster(ContractMasterDTO contractMasterDTO) throws ApplicationException;

	List<ContractMasterVO> getAllContractMasterByOrgId(Long orgId);

	ContractMasterVO getContractMasterById(Long id);

	//OTMASTER
	Map<String, Object> createUpdateOtMaster(OtMasterDTO otMasterDTO) throws ApplicationException;

	List<OtMasterVO> getAllOtMasterByOrgId(Long orgId);

	OtMasterVO getOtMasterById(Long id);
	
	//ShiftAssign

	Map<String, Object> createUpdateShiftAssign(ShiftAssignDTO shiftAssignDTO) throws ApplicationException;

	ShiftAssignVO getShiftAssignById(Long id);

	List<ShiftAssignVO> getAllShiftAssignByOrgId(Long orgId);

	//groupstructure
	
	Map<String, Object> createUpdateGroup(@Valid GroupDTO groupDTO) throws ApplicationException;

	List<GroupVO> getGroupByOrgId(Long orgId);

	Optional<GroupVO> getPreGroupById(Long id);
	
	//GroupSalaryStructure

	Map<String, Object> createUpdateGroupSalaryStructure(GroupSalaryStructureDTO groupSalaryStructureDTO) throws ApplicationException;

	List<GroupSalaryStructureVO> getGroupSalaryStructureByOrgId(Long orgId);

	Optional<GroupSalaryStructureVO> getGroupSalaryStructureById(Long id);

	List<GroupVO> getGroupMasterByOrgIdAndGroup(Long orgId, String groupName);

}
