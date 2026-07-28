package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.DesignationVO;

@Repository
public interface DesignationRepo extends JpaRepository<DesignationVO, Long>{

	@Query(nativeQuery = true,value = "select * from designation where orgid=?1")
	List<DesignationVO> findDesignationByOrgId(Long orgId);

	@Query(nativeQuery = true,value = "select * from designation where id=?1")
	DesignationVO findDesignationById(Long id);

	boolean existsByDesignationCodeAndOrgId(String designationCode, Long orgId);

	boolean existsByDesignationNameAndOrgId(String designationName, Long orgId);

	boolean existsByDesignationNameAndDesignationCodeAndOrgId(String designationName, String designationCode,
			Long orgId);


	DesignationVO findByOrgIdAndDesignationName(Long orgId, String designation);

}
