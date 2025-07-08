package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.StateVO;

@Repository
public interface StateRepo extends JpaRepository<StateVO, Long> {


	@Query("select a from StateVO a where a.orgId=?1 and a.country=?2")
	List<StateVO> findByCountry(Long orgid,String country);

	@Query("select a from StateVO a where a.orgId=?1")
	List<StateVO> findAllByOrgId(Long orgid);

	boolean existsByStateCodeAndOrgId(String stateCode, Long orgId);

	boolean existsByStateNumberAndOrgId(String stateNumber, Long orgId);

	boolean existsByStateNameAndOrgId(String stateName, Long orgId);

	//boolean existsByStateCodeAndStateNameAndStateNumberAndOrgId(String stateCode, String stateName, String stateNumber,Long orgId);

	
}
