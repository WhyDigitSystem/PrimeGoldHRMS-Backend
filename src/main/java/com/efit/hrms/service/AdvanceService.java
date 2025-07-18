package com.efit.hrms.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.AdvanceDTO;
import com.efit.hrms.entity.AdvanceVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface AdvanceService {
	
	//Advance

	List<AdvanceVO> getAllAdvanceByOrgId(Long orgId, String branchCode);

	AdvanceVO getAdvanceById(Long id);

	Map<String, Object> updateCreateAdvance(AdvanceDTO quotationDTO) throws ApplicationException;

	List<Map<String, Object>> findEmployeeDetails(Long orgId);

	AdvanceVO uploadAttachmentLogoInBloob(MultipartFile file, Long id) throws IOException;

}
