package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efit.hrms.entity.SequenceConfigVO;
public interface SequenceConfigRepo extends JpaRepository<SequenceConfigVO, Long>{

	   boolean existsByCompanyId(Long companyId);

	    boolean existsByCompanyCode(String companyCode); // Fix here

//	
//
//	boolean existsByOrgIdAndScreenCode(Long orgId, String screenCode);
//
//	boolean existsByOrgIdAndDocCode(Long orgId, String docCode);
//
//	List<DocumentTypeVO> findAllByOrgId(Long orgId);

}
