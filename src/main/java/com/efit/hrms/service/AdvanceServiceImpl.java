package com.efit.hrms.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.AdvanceDTO;
import com.efit.hrms.entity.AdvanceVO;
import com.efit.hrms.entity.GroupVO;
import com.efit.hrms.entity.ShiftAssignVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.AdvanceRepo;
import com.efit.hrms.repo.ShiftAssignRepo;

@Service
public class AdvanceServiceImpl implements AdvanceService {

	public static final Logger LOGGER = LoggerFactory.getLogger(AdvanceServiceImpl.class);

	@Autowired
	AdvanceRepo advanceRepo;

	@Autowired
	ShiftAssignRepo shiftAssignRepo;
	
	@Override
	public List<AdvanceVO> getAllAdvanceByOrgId(Long orgId, String branchCode) {

		return advanceRepo.getAllAdvanceByOrgId(orgId, branchCode);
	}

	@Override
	public AdvanceVO getAdvanceById(Long id) {

		return advanceRepo.getAdvanceById(id);
	}

	@Override
	public Map<String, Object> updateCreateAdvance(@Valid AdvanceDTO advanceDTO) throws ApplicationException {
//		String screenCode = "QA";
		AdvanceVO advanceVO = new AdvanceVO();
		String message;
		if (ObjectUtils.isNotEmpty(advanceDTO.getId())) {
			advanceVO = advanceRepo.findById(advanceDTO.getId())
					.orElseThrow(() -> new ApplicationException("Advance Not Found!"));
			advanceVO.setUpdatedBy(advanceDTO.getCreatedBy());
			message = "Advance Updated Successfully";
		} else {

//			String docId = quotationRepo.getQuotationDocId(quotationDTO.getOrgId(), quotationDTO.getFinYear(),
//					quotationDTO.getBranchCode(), screenCode);
//			quotationVO.setDocId(docId);
//
//			// GETDOCID LASTNO +1
//			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
//					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(quotationDTO.getOrgId(), quotationDTO.getFinYear(),
//							quotationDTO.getBranchCode(), screenCode);
//			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
//			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			advanceVO.setUpdatedBy(advanceDTO.getCreatedBy());
			advanceVO.setCreatedBy(advanceDTO.getCreatedBy());
//				createUpdateCostEstimationVOByCostEstimationDTO(costEstimationDTO, costEstimationVO);
			message = "Advance Created Successfully";
		}
		createUpdateAdvanceVOByAdvanceDTO(advanceDTO, advanceVO);
		advanceRepo.save(advanceVO);
		Map<String, Object> response = new HashMap<>();
		response.put("advanceVO", advanceVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateAdvanceVOByAdvanceDTO(@Valid AdvanceDTO advanceDTO, AdvanceVO advanceVO)
			throws ApplicationException {
		advanceVO.setBranch(advanceDTO.getBranch());
		advanceVO.setBranchCode(advanceDTO.getBranchCode());
		advanceVO.setRequestDate(advanceDTO.getRequestDate());
		advanceVO.setEmployeeName(advanceDTO.getEmployeeName());
		advanceVO.setCreatedBy(advanceDTO.getCreatedBy());
		advanceVO.setActive(advanceDTO.isActive());
		advanceVO.setFinYear(advanceDTO.getFinYear());
		advanceVO.setEmployeeCode(advanceDTO.getEmployeeCode());
		advanceVO.setOrgId(advanceDTO.getOrgId());
		advanceVO.setAdvanceAmount(advanceDTO.getAdvanceAmount());
		advanceVO.setLoanBalance(advanceDTO.getLoanBalance());
		advanceVO.setRemarks(advanceDTO.getRemarks());
		advanceVO.setDepartment(advanceDTO.getDepartment());
		advanceVO.setDesignation(advanceDTO.getDesignation());
		advanceVO.setApprove(advanceDTO.isApprove());
		advanceVO.setReasonForAdvance(advanceDTO.getReasonForAdvance());
	}

	@Override
	public List<Map<String, Object>> findEmployeeDetails(Long orgId) {
		Set<Object[]> permissionRequestVO = advanceRepo.findEmployeeDetails(orgId);
		return findEmployeeDetails(permissionRequestVO);
	}

	private List<Map<String, Object>> findEmployeeDetails(Set<Object[]> permissionRequestVO) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : permissionRequestVO) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeName", ch[0] != null ? ch[0].toString() : "");
			map.put("employeeCode", ch[1] != null ? ch[1].toString() : "");
			map.put("department", ch[2] != null ? ch[2].toString() : "");
			map.put("desigation", ch[3] != null ? ch[3].toString() : "");
			List1.add(map);
		}
		return List1;

	}
	
	
	@Override
	public AdvanceVO uploadAttachmentLogoInBloob(MultipartFile file, Long id) throws IOException {
	    String contentType = file.getContentType();
	    if (!isSupportedFileType(contentType)) {
	        throw new IllegalArgumentException("Only PDF or image files are allowed.");
	    }

	    AdvanceVO advanceVO = advanceRepo.findById(id)
	        .orElseThrow(() -> new RuntimeException("AdvanceVO not found with ID: " + id));
	    advanceVO.setAttachment(file.getBytes());

	    return advanceRepo.save(advanceVO);
	}

	private boolean isSupportedFileType(String contentType) {
	    return contentType != null && (
	        contentType.equals("application/pdf") ||
	        contentType.startsWith("image/") 
	    );
	}

	@Override
	public List<ShiftAssignVO> getAllShiftDetails(Long orgId, String shifttype, String department, String effectiveFrom,
			String effectiveTo, String type, String contractorName) {
		return shiftAssignRepo.getAllShiftDetails( orgId,shifttype, department,  effectiveFrom,
				 effectiveTo, type, contractorName);
	}
}
