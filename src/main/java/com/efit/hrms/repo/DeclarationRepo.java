package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.DeclarationDateVO;
import com.efit.hrms.entity.DeclarationVO;

@Repository
public interface DeclarationRepo extends JpaRepository<DeclarationVO, Long>{

	@Query(nativeQuery = true, value = "select * from declaration where declarationid=?1 and finYear=?2")
	DeclarationVO getDeclarationById(Long id, String finYear);

	@Query(nativeQuery = true, value = "select * from declaration where orgid=?1 and finyear=?2")
	List<DeclarationVO> getAllDeclarationByOrgId(Long orgId, String finYear);


}
