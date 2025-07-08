package com.efit.hrms.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.DeclarationDTO;
import com.efit.hrms.dto.DeclarationDateDTO;
import com.efit.hrms.dto.OneCroreFiveLacDeductionsDTO;
import com.efit.hrms.dto.OtherDeductionsDTO;
import com.efit.hrms.dto.TaxSavingAllowancesDTO;
import com.efit.hrms.entity.DeclarationDateVO;
import com.efit.hrms.entity.DeclarationVO;
import com.efit.hrms.entity.HousePropertyVO;
import com.efit.hrms.entity.IncomeFromOtherSourcesVO;
import com.efit.hrms.entity.OneCroreFiveLacDeductionsVO;
import com.efit.hrms.entity.OtherDeductionsVO;
import com.efit.hrms.entity.TaxSavingAllowancesVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.DeclarationDateRepo;
import com.efit.hrms.repo.DeclarationRepo;
import com.efit.hrms.repo.HousePropertyRepo;
import com.efit.hrms.repo.IncomeFromOtherSourcesRepo;
import com.efit.hrms.repo.MyDeclarationRepo;
import com.efit.hrms.repo.NotificationRepo;
import com.efit.hrms.repo.OneCroreFiveLacDeductionsRepo;
import com.efit.hrms.repo.OtherDeductionsRepo;
import com.efit.hrms.repo.TaxSavingAllowancesRepo;

@Service
public class ManageTaxServiceImpl implements ManageTaxService{

    private final NotificationRepo notificationRepo;

	@Autowired
	DeclarationDateRepo declarationDateRepo;
	
	@Autowired
	OneCroreFiveLacDeductionsRepo oneCroreFiveLacDeductionsRepo;
	
	@Autowired
	OtherDeductionsRepo otherDeductionsRepo;
	
	@Autowired
	TaxSavingAllowancesRepo taxSavingAllowancesRepo;
	

    @Autowired
    private HousePropertyRepo housePropertyRepo;
	
    @Autowired
    IncomeFromOtherSourcesRepo incomeFromOtherSourcesRepo;
    
    @Autowired
    MyDeclarationRepo myDeclarationRepo;
	
	@Autowired
	DeclarationRepo declarationRepo;
    ManageTaxServiceImpl(OneCroreFiveLacDeductionsRepo oneCroreFiveLacDeductionsRepo, NotificationRepo notificationRepo) {
        this.oneCroreFiveLacDeductionsRepo = oneCroreFiveLacDeductionsRepo;
        this.notificationRepo = notificationRepo;
    }


	@Override
	public Map<String, Object> createUpdateDeclarationDate(@Valid DeclarationDateDTO declarationDateDTO) throws ApplicationException {
	    DeclarationDateVO declarationDateVO;
	    String message;

	    if (ObjectUtils.isNotEmpty(declarationDateDTO.getId())) {
	        declarationDateVO = declarationDateRepo.findById(declarationDateDTO.getId())
	                .orElseThrow(() -> new ApplicationException("Invalid DeclarationDate details"));
	        declarationDateVO.setUpdatedBy(declarationDateDTO.getCreatedBy());
	        message = "DeclarationDate Updated Successfully";
	    } else {
	        declarationDateVO = new DeclarationDateVO();
	        declarationDateVO.setCreatedBy(declarationDateDTO.getCreatedBy());
	        declarationDateVO.setUpdatedBy(declarationDateDTO.getCreatedBy());
	        message = "DeclarationDate Created Successfully";
	    }

	    createUpdateDeclarationDateVOByDeclarationDateDTO(declarationDateDTO, declarationDateVO);
	    declarationDateRepo.save(declarationDateVO);

	    Map<String, Object> response = new HashMap<>();
	    response.put("declarationDateVO", declarationDateVO);
	    response.put("message", message);
	    return response;
	}


	private void createUpdateDeclarationDateVOByDeclarationDateDTO(DeclarationDateDTO declarationDateDTO, DeclarationDateVO declarationDateVO) {
		declarationDateVO.setInvestmentDate(declarationDateDTO.getInvestmentDate());
		declarationDateVO.setProofSubmissionDate(declarationDateDTO.getProofSubmissionDate());
		declarationDateVO.setOrgId(declarationDateDTO.getOrgId());
		declarationDateVO.setActive(declarationDateDTO.isActive());
		declarationDateVO.setBranch(declarationDateDTO.getBranch());
		declarationDateVO.setBranchCode(declarationDateDTO.getBranchCode());
		declarationDateVO.setFinYear(declarationDateDTO.getFinYear());

	}
	
	@Override
	public DeclarationDateVO getDeclarationDateById(Long id) {
		return declarationDateRepo.getDeclarationDateById(id);
	}
	
	@Override
	public List<DeclarationDateVO> getAllDeclarationDateByOrgId(Long orgId) {
		return declarationDateRepo.getAllDeclarationDateByOrgId(orgId);
	}

	//Declaration
	
	@Override
	public DeclarationVO getDeclarationById(Long id, String finYear) {
		return declarationRepo.getDeclarationById(id,finYear);
	}
	
	@Override
	public List<DeclarationVO> getAllDeclarationByOrgId(Long orgId,String finYear) {
		return declarationRepo.getAllDeclarationByOrgId(orgId,finYear);
	}


	@Override
	public OneCroreFiveLacDeductionsVO uploadOneCroreFiveLacDeductionsInBloob(MultipartFile file, Long id) throws IOException, java.io.IOException {
		OneCroreFiveLacDeductionsVO oneCroreFiveLacDeductionsVO = oneCroreFiveLacDeductionsRepo.findById(id).get();
		oneCroreFiveLacDeductionsVO.setProofImage(file.getBytes());
		return oneCroreFiveLacDeductionsRepo.save(oneCroreFiveLacDeductionsVO);
	}
	
	@Override
	public OtherDeductionsVO uploadOtherDeductionInBloob(MultipartFile file, Long id) throws IOException, java.io.IOException {
		OtherDeductionsVO otherDeductionsVO = otherDeductionsRepo.findById(id).get();
		otherDeductionsVO.setProofImage(file.getBytes());
		return otherDeductionsRepo.save(otherDeductionsVO);
	}

	
	
	@Override
	public Map<String, Object> createUpdateDeclaration(DeclarationDTO declarationDTO) throws ApplicationException {

		DeclarationVO declarationVO = new DeclarationVO();
		String message;
		String screenCode = "DL";
		if (ObjectUtils.isNotEmpty(declarationDTO.getId())) {
			declarationVO = declarationRepo.findById(declarationDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid declaration details"));

			declarationVO.setUpdatedBy(declarationDTO.getCreatedBy());

			message = "Declaration Updated  Successfully";
		} else {

			declarationVO.setCreatedBy(declarationDTO.getCreatedBy());
			declarationVO.setUpdatedBy(declarationDTO.getCreatedBy());
			message = "Declaration Created Successfully";
		}

		createUpdateDeclarationVOByDeclarationDTO(declarationVO, declarationDTO);
		declarationRepo.save(declarationVO);
		Map<String, Object> response = new HashMap<>();
		response.put("declarationVO", declarationVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateDeclarationVOByDeclarationDTO(DeclarationVO declarationVO,
			DeclarationDTO declarationDTO) {

		declarationVO.setOrgId(declarationDTO.getOrgId());
		declarationVO.setEmployeeName(declarationDTO.getEmployeeName());
		declarationVO.setEmployeeCode(declarationDTO.getEmployeeCode());
		declarationVO.setFinYear(declarationDTO.getFinYear());
		declarationVO.setBranch(declarationDTO.getBranch());
		declarationVO.setBranchCode(declarationDTO.getBranchCode());
		declarationVO.setDepartment(declarationDTO.getDepartment());

//
//	    if (declarationDTO.getId() != null) {
//            List<OneCroreFiveLacDeductionsVO> oneCroreFiveLacDeductionsVO = oneCroreFiveLacDeductionsRepo.findByDeclarationVO(declarationVO);
//            oneCroreFiveLacDeductionsRepo.deleteAll(oneCroreFiveLacDeductionsVO);
//        }
//
//        // Set Poll Details from PollDetailsDTO
//        List<OneCroreFiveLacDeductionsVO> oneCroreFiveLacDeductionsVOs = new ArrayList<>();
//        for (OneCroreFiveLacDeductionsDTO oneCroreFiveLacDeductionsDTO : declarationDTO.getOneCroreFiveLacDeductionsDTO()) {
//        	OneCroreFiveLacDeductionsVO oneCroreFiveLacDeductionsVO = new OneCroreFiveLacDeductionsVO();
//        	oneCroreFiveLacDeductionsVO.setSection(oneCroreFiveLacDeductionsDTO.getSection());
//        	oneCroreFiveLacDeductionsVO.setDeductions(oneCroreFiveLacDeductionsDTO.getDeductions());
//        	oneCroreFiveLacDeductionsVO.setMaxLimit(oneCroreFiveLacDeductionsDTO.getMaxLimit());
//        	oneCroreFiveLacDeductionsVO.setDeclaration(oneCroreFiveLacDeductionsDTO.getDeclaration());
//        	oneCroreFiveLacDeductionsVO.setProof(oneCroreFiveLacDeductionsDTO.getProof());
//        	oneCroreFiveLacDeductionsVO.setStatus(oneCroreFiveLacDeductionsDTO.getStatus());
//
//        	oneCroreFiveLacDeductionsVO.setDeclarationVO(declarationVO);  // Set parent reference in child
//        	oneCroreFiveLacDeductionsVOs.add(oneCroreFiveLacDeductionsVO);
//        }
//        declarationVO.setOneCroreFiveLacDeductionsVO(oneCroreFiveLacDeductionsVOs);
//        
//        if (declarationDTO.getId() != null) {
//            List<OtherDeclarationVO> otherDeclarationVO = otherDeclarationRepo.findByDeclarationVO(declarationVO);
//            otherDeclarationRepo.deleteAll(otherDeclarationVO);
//        }
//
//        // Set Poll Details from PollDetailsDTO
//        List<OtherDeclarationVO> otherDeclarationVOs = new ArrayList<>();
//        for (OtherDeclarationDTO otherDeclarationDTO : declarationDTO.getOtherDeclarationDTO()) {
//        	OtherDeclarationVO otherDeclarationVO = new OtherDeclarationVO();
//        	otherDeclarationVO.setSection(otherDeclarationDTO.getSection());
//        	otherDeclarationVO.setDeductions(otherDeclarationDTO.getDeductions());
//        	otherDeclarationVO.setMaxLimit(otherDeclarationDTO.getMaxLimit());
//        	otherDeclarationVO.setDeclaration(otherDeclarationDTO.getDeclaration());
//        	otherDeclarationVO.setProof(otherDeclarationDTO.getProof());
//        	otherDeclarationVO.setStatus(otherDeclarationDTO.getStatus());
//
//        	otherDeclarationVO.setDeclarationVO(declarationVO);  // Set parent reference in child
//        	otherDeclarationVOs.add(otherDeclarationVO);
//        }
//        declarationVO.setOtherDeclarationVO(otherDeclarationVOs);

	}
	
	
	
//	@Override
//	public Map<String, Object> createUpdateOneCroreFiveLacDeductions(OneCroreFiveLacDeductionsDTO oneCroreFiveLacDeductionsDTO) throws ApplicationException {
//
//		OneCroreFiveLacDeductionsVO oneCroreFiveLacDeductionsVO = new OneCroreFiveLacDeductionsVO();
//		String message;
////		String screenCode = "DL";
//		if (ObjectUtils.isNotEmpty(oneCroreFiveLacDeductionsDTO.getId())) {
//			oneCroreFiveLacDeductionsVO = oneCroreFiveLacDeductionsRepo.findById(oneCroreFiveLacDeductionsDTO.getId())
//					.orElseThrow(() -> new ApplicationException("Invalid oneCroreFiveLacDeductions details"));
//
//
//			message = "OneCroreFiveLacDeductions Updated  Successfully";
//		} else {
//
//			message = "OneCroreFiveLacDeductions Created Successfully";
//		}
//
//		createUpdateOneCroreFiveLacDeductionsVOByOneCroreFiveLacDeductionsDTO(oneCroreFiveLacDeductionsVO, oneCroreFiveLacDeductionsDTO);
//		declarationRepo.save(oneCroreFiveLacDeductionsVO);
//		Map<String, Object> response = new HashMap<>();
//		response.put("oneCroreFiveLacDeductionsVO", oneCroreFiveLacDeductionsVO);
//		response.put("message", message);
//		return response;
//	}
//
//	private void createUpdateOneCroreFiveLacDeductionsVOByOneCroreFiveLacDeductionsDTO(OneCroreFiveLacDeductionsVO oneCroreFiveLacDeductionsVO,
//			OneCroreFiveLacDeductionsDTO oneCroreFiveLacDeductionsDTO) {
//
//
//        // Set Poll Details from PollDetailsDTO
//        List<OneCroreFiveLacDeductionsVO> oneCroreFiveLacDeductionsVOs = new ArrayList<>();
//        for (OneCroreFiveLacDeductionsDTO oneCroreFiveLacDeductionsDTO : declarationDTO.getOneCroreFiveLacDeductionsDTO()) {
//        	OneCroreFiveLacDeductionsVO oneCroreFiveLacDeductionsVO = new OneCroreFiveLacDeductionsVO();
//        	oneCroreFiveLacDeductionsVO.setSection(oneCroreFiveLacDeductionsDTO.getSection());
//        	oneCroreFiveLacDeductionsVO.setDeductions(oneCroreFiveLacDeductionsDTO.getDeductions());
//        	oneCroreFiveLacDeductionsVO.setMaxLimit(oneCroreFiveLacDeductionsDTO.getMaxLimit());
//        	oneCroreFiveLacDeductionsVO.setDeclaration(oneCroreFiveLacDeductionsDTO.getDeclaration());
//        	oneCroreFiveLacDeductionsVO.setProof(oneCroreFiveLacDeductionsDTO.getProof());
//        	oneCroreFiveLacDeductionsVO.setStatus(oneCroreFiveLacDeductionsDTO.getStatus());
//
//        	oneCroreFiveLacDeductionsVO.setDeclarationVO(declarationVO);  // Set parent reference in child
//        	oneCroreFiveLacDeductionsVOs.add(oneCroreFiveLacDeductionsVO);
//        }
//        declarationVO.setOneCroreFiveLacDeductionsVO(oneCroreFiveLacDeductionsVOs);
//        
//        if (declarationDTO.getId() != null) {
//            List<OtherDeclarationVO> otherDeclarationVO = otherDeclarationRepo.findByDeclarationVO(declarationVO);
//            otherDeclarationRepo.deleteAll(otherDeclarationVO);
//        }
//
//        // Set Poll Details from PollDetailsDTO
//        List<OtherDeclarationVO> otherDeclarationVOs = new ArrayList<>();
//        for (OtherDeclarationDTO otherDeclarationDTO : declarationDTO.getOtherDeclarationDTO()) {
//        	OtherDeclarationVO otherDeclarationVO = new OtherDeclarationVO();
//        	otherDeclarationVO.setSection(otherDeclarationDTO.getSection());
//        	otherDeclarationVO.setDeductions(otherDeclarationDTO.getDeductions());
//        	otherDeclarationVO.setMaxLimit(otherDeclarationDTO.getMaxLimit());
//        	otherDeclarationVO.setDeclaration(otherDeclarationDTO.getDeclaration());
//        	otherDeclarationVO.setProof(otherDeclarationDTO.getProof());
//        	otherDeclarationVO.setStatus(otherDeclarationDTO.getStatus());
//
//        	otherDeclarationVO.setDeclarationVO(declarationVO);  // Set parent reference in child
//        	otherDeclarationVOs.add(otherDeclarationVO);
//        }
//        declarationVO.setOtherDeclarationVO(otherDeclarationVOs);
//
//	}
	
	
	
	@Override
	public Map<String, Object> saveOneCroreFiveLacDeductionsList(List<OneCroreFiveLacDeductionsDTO> dtoList) throws ApplicationException {
	    Map<String, Object> result = new HashMap<>();

	    if (dtoList == null || dtoList.isEmpty()) {
	        throw new ApplicationException("No data provided to save");
	    }

	    Long declarationId = dtoList.get(0).getDeclarationId(); // Assuming all rows belong to same header
	    if (declarationId == null || declarationId <= 0) {
	        throw new ApplicationException("Invalid Declaration ID");
	    }

	    DeclarationVO declaration = declarationRepo.findById(declarationId)
	            .orElseThrow(() -> new ApplicationException("Declaration ID not found: " + declarationId));

	    // Check if existing records are present for update
	    List<OneCroreFiveLacDeductionsVO> existingList = oneCroreFiveLacDeductionsRepo.findByDeclarationVO(declaration);
	    boolean isNew = existingList.isEmpty();

	    // If updating, delete existing records
	    if (!isNew) {
	        oneCroreFiveLacDeductionsRepo.deleteAll(existingList);
	    }

	    // Prepare new records
	    List<OneCroreFiveLacDeductionsVO> newList = new ArrayList<>();
	    for (OneCroreFiveLacDeductionsDTO dto : dtoList) {
	        OneCroreFiveLacDeductionsVO vo = new OneCroreFiveLacDeductionsVO();
	        vo.setSection(dto.getSection());
	        vo.setDeductions(dto.getDeductions());
	        vo.setMaxLimit(dto.getMaxLimit());
	        vo.setDeclaration(dto.getDeclaration());
	        vo.setStatus(dto.getStatus());
	        vo.setDeclarationVO(declaration); // set parent reference
	        newList.add(vo);
	    }

	    List<OneCroreFiveLacDeductionsVO> savedList = oneCroreFiveLacDeductionsRepo.saveAll(newList);

	    result.put("oneCroreFiveLacDeductionsVO", savedList);
	    result.put("message", isNew ? "OneCroreFiveLacDeductions Created Successfully" : "OneCroreFiveLacDeductions Updated Successfully");

	    return result;
	}

	@Override
	public Map<String, Object> saveOtherDeductions(List<OtherDeductionsDTO> dtoList) throws ApplicationException {
	    Map<String, Object> result = new HashMap<>();

	    if (dtoList == null || dtoList.isEmpty()) {
	        throw new ApplicationException("No data provided to save");
	    }

	    Long declarationId = dtoList.get(0).getDeclarationId(); // Assuming all items are for the same header
	    if (declarationId == null || declarationId <= 0) {
	        throw new ApplicationException("Invalid Declaration ID");
	    }

	    DeclarationVO declaration = declarationRepo.findById(declarationId)
	            .orElseThrow(() -> new ApplicationException("Declaration ID not found: " + declarationId));

	    List<OtherDeductionsVO> existingList = otherDeductionsRepo.findByDeclarationVO(declaration);
	    boolean isNew = existingList.isEmpty();

	    if (!isNew) {
	    	otherDeductionsRepo.deleteAll(existingList);
	    }


	    List<OtherDeductionsVO> newList = new ArrayList<>();
	    for (OtherDeductionsDTO dto : dtoList) {
	        OtherDeductionsVO vo = new OtherDeductionsVO();	       

	        vo.setSection(dto.getSection());
	        vo.setDeductions(dto.getDeductions());
	        vo.setMaxLimit(dto.getMaxLimit());
	        vo.setDeclaration(dto.getDeclaration());
	        vo.setProof(dto.getProof());
	        vo.setStatus(dto.getStatus());
	        vo.setDeclarationVO(declaration);

	        newList.add(vo);
	    }

	    List<OtherDeductionsVO> savedList = otherDeductionsRepo.saveAll(newList);

	    result.put("otherDeductionsVO", savedList);
	    result.put("message", isNew ? "otherDeductions Created Successfully" : "otherDeductions Updated Successfully");

	    return result;
	}


	@Override
	public Map<String, Object> saveTaxSavingAllowances(List<TaxSavingAllowancesDTO> dtoList) throws ApplicationException {
	    Map<String, Object> result = new HashMap<>();

	    if (dtoList == null || dtoList.isEmpty()) {
	        throw new ApplicationException("No data provided to save");
	    }

	    Long declarationId = dtoList.get(0).getDeclarationId(); // Assuming all items are for the same header
	    if (declarationId == null || declarationId <= 0) {
	        throw new ApplicationException("Invalid Declaration ID");
	    }

	    DeclarationVO declaration = declarationRepo.findById(declarationId)
	            .orElseThrow(() -> new ApplicationException("Declaration ID not found: " + declarationId));

	    List<TaxSavingAllowancesVO> existingList = taxSavingAllowancesRepo.findByDeclarationVO(declaration);
	    boolean isNew = existingList.isEmpty();

	    if (!isNew) {
	    	taxSavingAllowancesRepo.deleteAll(existingList);
	    }


	    List<TaxSavingAllowancesVO> newList = new ArrayList<>();
	    for (TaxSavingAllowancesDTO dto : dtoList) {
	    	TaxSavingAllowancesVO vo = new TaxSavingAllowancesVO();	       

	        vo.setSection(dto.getSection());
	        vo.setDeductionsName(dto.getDeductionsName());
	        vo.setMaxexemptionLimit(dto.getMaxexemptionLimit());
	        vo.setDeclaration(dto.getDeclaration());
	        vo.setProof(dto.getProof());
	        vo.setStatus(dto.getStatus());
	        vo.setDeclarationVO(declaration);

	        newList.add(vo);
	    }

	    List<TaxSavingAllowancesVO> savedList = taxSavingAllowancesRepo.saveAll(newList);

	    result.put("taxSavingAllowancesVO", savedList);
	    result.put("message", isNew ? "TaxSavingAllowances Created Successfully" : "TaxSavingAllowances Updated Successfully");

	    return result;
	}


	@Override
	public Map<String, Object> uploadHousePropertyInBloob(MultipartFile file, Long declarationId, Long housePropertyId) throws IOException {
	    Map<String, Object> result = new HashMap<>();

	    DeclarationVO declaration = declarationRepo.findById(declarationId)
	        .orElseThrow(() -> new RuntimeException("Declaration not found"));

	    HousePropertyVO house;

	    if (housePropertyId != null) {
	        house = housePropertyRepo.findById(housePropertyId)
	            .orElseThrow(() -> new RuntimeException("House property not found"));
	    } else {
	        house = new HousePropertyVO();
	    }

	    house.setProofImage(file.getBytes());
	    house.setDeclarationVO(declaration);

	    housePropertyRepo.save(house);

	    String base64File = Base64.getEncoder().encodeToString(house.getProofImage());

	    result.put("message", housePropertyId == null ? "HouseProperty uploaded successfully" : "HouseProperty updated successfully");
	    result.put("declarationId", declarationId);
	    result.put("housePropertyId", house.getId());
	    result.put("proofFile", base64File);

	    return result;
	}

	
	@Override
	public Map<String, Object> uploadIncomeFromOtherSourcesInBloob(MultipartFile file, Long declarationId, Long incomeFromOtherSourcesId) throws IOException {
	    Map<String, Object> result = new HashMap<>();

	    DeclarationVO declaration = declarationRepo.findById(declarationId)
	        .orElseThrow(() -> new RuntimeException("Declaration not found"));

	    IncomeFromOtherSourcesVO incomeFromOtherSources;

	    if (incomeFromOtherSourcesId != null) {
	    	incomeFromOtherSources = incomeFromOtherSourcesRepo.findById(incomeFromOtherSourcesId)
	            .orElseThrow(() -> new RuntimeException("IncomeFromOtherSources not found"));
	    } else {
	    	incomeFromOtherSources = new IncomeFromOtherSourcesVO();
	    }

	    incomeFromOtherSources.setProofImage(file.getBytes());
	    incomeFromOtherSources.setDeclarationVO(declaration);

	    incomeFromOtherSourcesRepo.save(incomeFromOtherSources);

	    String base64File = Base64.getEncoder().encodeToString(incomeFromOtherSources.getProofImage());

	    result.put("message", incomeFromOtherSourcesId == null ? "IncomeFromOtherSources uploaded successfully" : "IncomeFromOtherSources updated successfully");
	    result.put("declarationId", declarationId);
	    result.put("incomeFromOtherSourcesId", incomeFromOtherSources.getId());
	    result.put("proofFile", base64File);

	    return result;
	}
	
	@Override
	public List<OneCroreFiveLacDeductionsVO> getOneCroreFiveLacDeductionsByOrgId(Long orgId) {
		return oneCroreFiveLacDeductionsRepo.getOneCroreFiveLacDeductionsByOrgId(orgId);
	}

	@Override
	public List<OtherDeductionsVO> getOtherDeductionsByOrgId(Long orgId) {
		return otherDeductionsRepo.getOtherDeductionsByOrgId(orgId);
	}

	@Override
	 public List<Map<String, Object>> getMyDeclarations(Long declarationId) {
	        List<Object[]> rawResult = myDeclarationRepo.getMyDeclarations(declarationId);
	        List<Map<String, Object>> resultList = new ArrayList<>();

	        for (Object[] row : rawResult) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("id", ((Number) row[0]).intValue());
	            map.put("screenname", String.valueOf(row[1]));
	            map.put("recordcount", ((Number) row[2]).longValue());
	            map.put("totalmaxlimit", new BigDecimal(String.valueOf(row[3])));

	            resultList.add(map);
	        }
	        return resultList;
	    }

}
