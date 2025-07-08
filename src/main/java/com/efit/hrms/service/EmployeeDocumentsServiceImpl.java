package com.efit.hrms.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efit.hrms.dto.EmployeeDocumentsDTO;
import com.efit.hrms.entity.CompensatoryOffVO;
import com.efit.hrms.entity.EmployeeDocumentsVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.EmployeeDocumentsRepo;

@Service
public class EmployeeDocumentsServiceImpl implements EmployeeDocumentsService{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDocumentsServiceImpl.class);

	
	@Autowired
	EmployeeDocumentsRepo employeeDocumentsRepo;
	
	
	@Override
	public EmployeeDocumentsVO uploadDocument(EmployeeDocumentsDTO dto) throws IOException {
	    EmployeeDocumentsVO vo = new EmployeeDocumentsVO();
	    vo.setEmployeeCode(dto.getEmployeeCode());
	    vo.setEmployeeName(dto.getEmployeeName());
	    vo.setDocumentName(dto.getDocumentName());
//	    vo.setDocumentType(dto.getDocumentType());
	    vo.setOrgId(dto.getOrgId());
	    vo.setFileData(dto.getFile().getBytes());
	    vo.setFileName(dto.getFile().getOriginalFilename());
	    vo.setContentType(dto.getFile().getContentType());

	    // Save the document in the repository
	    EmployeeDocumentsVO savedDocument = employeeDocumentsRepo.save(vo);

	    return savedDocument;  // Return the saved document with all details
	}


	@Override
	public List<EmployeeDocumentsVO> getEmployeeDocumentsByEmpCodeAndOrgId(Long orgId,String EmployeeCode) {

		return employeeDocumentsRepo.getEmployeeDocumentsByEmpCodeAndOrgId(orgId,EmployeeCode);
	}

	@Override
    public void deleteEmployeeDocById(Long id) throws ApplicationException {
        EmployeeDocumentsVO vo = employeeDocumentsRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("Invalid EmployeeDoc ID: " + id));
        employeeDocumentsRepo.delete(vo);
    }

}
