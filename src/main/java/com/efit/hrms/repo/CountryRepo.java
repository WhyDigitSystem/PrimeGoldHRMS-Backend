package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.CountryVO;

public interface CountryRepo extends JpaRepository<CountryVO, Long> {

//	@Query("select a.id,a.countryname from CountryVO a where a.orgId=?1")
//	Set<Object[]> findCountryAndCountryid(Long orgId);

	@Query(value = "SELECT * FROM country WHERE orgid = ?1", nativeQuery = true)
	List<CountryVO> findAll(Long orgId);

	boolean existsByCountryNameAndCountryCodeAndOrgId(String countryName, String countryCode, Long orgId);

	boolean existsByCountryNameAndOrgId(String countryName, Long orgId);

	boolean existsByCountryCodeAndOrgId(String countryCode, Long orgId);

}
