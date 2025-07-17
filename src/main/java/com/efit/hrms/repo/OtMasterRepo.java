package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.OtMasterVO;

@Repository
public interface OtMasterRepo extends JpaRepository<OtMasterVO, Long>{

	@Query( value = "SELECT * FROM otmaster WHERE orgid=?1",nativeQuery = true)
	List<OtMasterVO> getAllOtMasterByOrgId(Long orgId);

	@Query( value = "SELECT * FROM otmaster WHERE otmasterid=?1",nativeQuery = true)
	OtMasterVO getOtMasterById(Long id);

}
