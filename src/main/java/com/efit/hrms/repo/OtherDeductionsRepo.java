package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.DeclarationVO;
import com.efit.hrms.entity.OtherDeductionsVO;

@Repository
public interface OtherDeductionsRepo extends JpaRepository<OtherDeductionsVO, Long>{

	List<OtherDeductionsVO> findByDeclarationVO(DeclarationVO declarationVO);

	@Query(nativeQuery = true, value = "select * from otherdeductions where orgid=?1")
	List<OtherDeductionsVO> getOtherDeductionsByOrgId(Long orgId);

}
