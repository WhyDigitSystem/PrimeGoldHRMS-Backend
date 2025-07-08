package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.FinancialYearVO;

public interface FinancialYearRepo extends JpaRepository<FinancialYearVO, Long> {
	@Query(nativeQuery = true, value = "select * from finyear where company=?1")
	List<FinancialYearVO> findFinyearByCompany(String company);

	@Query(value = "select * from financialyear where orgid=?1", nativeQuery = true)
	List<FinancialYearVO> findFinancialYearByOrgId(Long orgId);

	@Query(value = "select a from FinancialYearVO a where a.orgId=?1 and a.active=true and a.closed=false")
	List<FinancialYearVO> findAllActiveFinYear(Long orgId);

	boolean existsByFinYearAndOrgId(int finYear, Long orgId);

	boolean existsByFinYearIdentifierAndOrgId(String finYearIdentifier, Long orgId);

	boolean existsByFinYearIdAndOrgId(Long finYearId, Long orgId);

}