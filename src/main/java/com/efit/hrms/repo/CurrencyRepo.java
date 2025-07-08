package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.CurrencyVO; 

public interface CurrencyRepo extends JpaRepository<CurrencyVO, Long> {


	@Query(value="select * FROM currency where orgid=?1",nativeQuery =true)
	List<CurrencyVO> findAll(Long orgid);

	@Query(value ="SELECT \r\n"
			+ "    ROW_NUMBER() OVER () AS id,\r\n"
			+ "    currency,\r\n"
			+ "    currencydesc \r\n"
			+ "FROM \r\n"
			+ "    currency \r\n"
			+ "WHERE \r\n"
			+ "    orgid =?1\r\n"
			+ "    AND active = 1 group by currency,currencydesc",nativeQuery =true)
	Set<Object[]> findCurrencyForFullGrid(Long orgId);


	boolean existsByOrgIdAndCountryAndCurrencyIgnoreCase(Long orgId, String country, String currency);

	boolean existsByOrgIdAndCountryAndCurrencyDescriptionIgnoreCase(Long orgId, String country,
			String currencyDescription);

	boolean existsByOrgIdAndCountryAndSubCurrencyIgnoreCase(Long orgId, String country, String subCurrency);



}
