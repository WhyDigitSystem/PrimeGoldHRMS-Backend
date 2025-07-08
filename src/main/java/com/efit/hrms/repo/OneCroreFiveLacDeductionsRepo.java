package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.DeclarationVO;
import com.efit.hrms.entity.OneCroreFiveLacDeductionsVO;

@Repository
public interface OneCroreFiveLacDeductionsRepo extends JpaRepository<OneCroreFiveLacDeductionsVO, Long>{

	List<OneCroreFiveLacDeductionsVO> findByDeclarationVO(DeclarationVO declarationVO);

	@Query(nativeQuery = true, value = "select * from oneCroreFiveLacDeductions where orgid=?1")
	List<OneCroreFiveLacDeductionsVO> getOneCroreFiveLacDeductionsByOrgId(Long orgId);

}
