package com.efit.hrms.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.efit.hrms.dto.SequenceConfigDTO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface SequenceService {

	Map<String, Object> createSequenceConfig(SequenceConfigDTO sequenceConfigDTO) throws ApplicationException;
	

}
