package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.DeclarationDateVO;
import com.efit.hrms.entity.DeclarationVO;

@Repository
public interface DeclarationDateRepo extends JpaRepository<DeclarationDateVO, Long>{

	@Query(nativeQuery = true, value = "select * from declarationdate where declarationdateid=?1")
	DeclarationDateVO getDeclarationDateById(Long id);

	@Query(nativeQuery = true, value = "select * from declarationdate where orgid=?1")
	List<DeclarationDateVO> getAllDeclarationDateByOrgId(Long orgId);



	
}
