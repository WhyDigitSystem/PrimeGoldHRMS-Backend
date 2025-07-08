package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.RegionVO;


@Repository
public interface RegionRepo extends JpaRepository<RegionVO, Long> {

	@Query("select a from RegionVO a where a.orgId=?1")
	List<RegionVO> findAll(Long orgId);

	boolean existsByRegionNameAndOrgId(String regionName,Long orgId);

	boolean existsByRegionCodeAndOrgId(String regionCode, Long orgId);
}
