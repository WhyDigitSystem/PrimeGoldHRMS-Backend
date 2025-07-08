package com.efit.hrms.service;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efit.hrms.dto.SequenceConfigDTO;
import com.efit.hrms.entity.SequenceConfigVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.SequenceConfigRepo;

@Service
public class SequenceServiceImpl  implements SequenceService {
	
	@Autowired
	SequenceConfigRepo sequenceConfigRepo;

	
	@Override
	public Map<String, Object> createSequenceConfig(SequenceConfigDTO sequenceConfigDTO) throws ApplicationException {
	    SequenceConfigVO sequenceConfigVO;
	    String message;

	    // If ID is present, update existing configuration
	    if (!ObjectUtils.isEmpty(sequenceConfigDTO.getId())) {
	        sequenceConfigVO = sequenceConfigRepo.findById(sequenceConfigDTO.getId())
	                .orElseThrow(() -> new ApplicationException("Sequence configuration not found"));

	        // Check if Company ID needs to be updated
	        if (!sequenceConfigVO.getCompanyId().equals(sequenceConfigDTO.getCompanyId())) {
	            if (sequenceConfigRepo.existsByCompanyId(sequenceConfigDTO.getCompanyId())) {
	                throw new ApplicationException("CompanyId already exists.");
	            }
	            sequenceConfigVO.setCompanyId(sequenceConfigDTO.getCompanyId());
	        }

	        // Check if Company Code needs to be updated
	        if (!sequenceConfigVO.getCompanyCode().equalsIgnoreCase(sequenceConfigDTO.getCompanyCode())) {
	            if (sequenceConfigRepo.existsByCompanyCode(sequenceConfigDTO.getCompanyCode())) {
	                throw new ApplicationException("Doc Code already exists.");
	            }
	            sequenceConfigVO.setCompanyCode(sequenceConfigDTO.getCompanyCode());
	        }

	        sequenceConfigVO.setUpdatedBy(sequenceConfigDTO.getCreatedBy());
	        message = "Document Type Updated successfully";
	    } 
	    // If no ID, create a new configuration
	    else {
	        if (sequenceConfigRepo.existsByCompanyId(sequenceConfigDTO.getCompanyId())) {
	            throw new ApplicationException("CompanyId already exists.");
	        }
	        if (sequenceConfigRepo.existsByCompanyCode(sequenceConfigDTO.getCompanyCode())) {
	            throw new ApplicationException("Doc Code already exists.");
	        }

	        sequenceConfigVO = new SequenceConfigVO();
	        sequenceConfigVO.setCompanyId(sequenceConfigDTO.getCompanyId());
	        sequenceConfigVO.setCompanyCode(sequenceConfigDTO.getCompanyCode());
	        sequenceConfigVO.setCreatedBy(sequenceConfigDTO.getCreatedBy());
	        sequenceConfigVO.setUpdatedBy(sequenceConfigDTO.getCreatedBy());
	        message = "Document Type Created successfully";
	    }

	    // Map remaining fields from DTO to VO
	    mapSequenceDTOToSequenceVO(sequenceConfigDTO, sequenceConfigVO);

	    // Save the entity
	    sequenceConfigRepo.save(sequenceConfigVO);

	    // Prepare response
	    Map<String, Object> response = new HashMap<>();
	    response.put("sequenceConfigVO", sequenceConfigVO);
	    response.put("message", message);
	    
	    return response;
	}


	private void mapSequenceDTOToSequenceVO(SequenceConfigDTO sequenceConfigDTO, SequenceConfigVO sequenceConfigVO) {

		sequenceConfigVO.setBranchCode(sequenceConfigDTO.getBranchCode());
		sequenceConfigVO.setDepartmentCode(sequenceConfigDTO.getDepartmentCode());
		sequenceConfigVO.setStartingFrom(sequenceConfigDTO.getStartingFrom());
		sequenceConfigVO.setNoOfDigits(sequenceConfigDTO.getNoOfDigits());
		sequenceConfigVO.setSeparators(sequenceConfigDTO.getSeparators());
		sequenceConfigVO.setFormat(sequenceConfigDTO.getCompanyId() + sequenceConfigDTO.getCompanyCode());
	}

}
