package com.efit.hrms.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.DeclarationDTO;
import com.efit.hrms.dto.DeclarationDateDTO;
import com.efit.hrms.dto.OneCroreFiveLacDeductionsDTO;
import com.efit.hrms.dto.OtherDeductionsDTO;
import com.efit.hrms.dto.SalaryStructureDTO;
import com.efit.hrms.dto.TaxSavingAllowancesDTO;
import com.efit.hrms.entity.DeclarationDateVO;
import com.efit.hrms.entity.DeclarationVO;
import com.efit.hrms.entity.HousePropertyVO;
import com.efit.hrms.entity.OneCroreFiveLacDeductionsVO;
import com.efit.hrms.entity.OtherDeductionsVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface ManageTaxService {

	Map<String, Object> createUpdateDeclarationDate(@Valid DeclarationDateDTO declarationDateDTO) throws ApplicationException;

	DeclarationDateVO getDeclarationDateById(Long id);

	List<DeclarationDateVO> getAllDeclarationDateByOrgId(Long orgId);
	
	Map<String, Object> createUpdateDeclaration(@Valid DeclarationDTO declarationDTO) throws ApplicationException;

	//Declaration
	DeclarationVO getDeclarationById(Long id, String finYear);

	List<DeclarationVO> getAllDeclarationByOrgId(Long orgId, String finYear);

	//oneCroreFiveLacDeductions
	
	OneCroreFiveLacDeductionsVO uploadOneCroreFiveLacDeductionsInBloob(MultipartFile file, Long id) throws IOException, IOException;

	OtherDeductionsVO uploadOtherDeductionInBloob(MultipartFile file, Long id) throws IOException, IOException;

	Map<String, Object> saveOneCroreFiveLacDeductionsList(List<OneCroreFiveLacDeductionsDTO> dtoList) throws ApplicationException;


	Map<String, Object> saveOtherDeductions(List<OtherDeductionsDTO> dtoList) throws ApplicationException;

	Map<String, Object> saveTaxSavingAllowances(List<TaxSavingAllowancesDTO> dtoList) throws ApplicationException;

	Map<String, Object> uploadHousePropertyInBloob(MultipartFile file, Long declarationId, Long housePropertyId) throws IOException;

	Map<String, Object> uploadIncomeFromOtherSourcesInBloob(MultipartFile file, Long declarationId,
			Long incomeFromOtherSourcesId) throws IOException;

	List<OneCroreFiveLacDeductionsVO> getOneCroreFiveLacDeductionsByOrgId(Long orgId);

	List<OtherDeductionsVO> getOtherDeductionsByOrgId(Long orgId);

	//MyDeclaration
	List<Map<String, Object>> getMyDeclarations(Long declarationId);






}
