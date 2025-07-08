package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.SalaryHeadsVO;

@Repository
public interface SalaryHeadsRepo extends JpaRepository<SalaryHeadsVO, Long>{

	@Query(nativeQuery = true,value = "select * from salaryheads a where a.orgid=?1  ")
	List<SalaryHeadsVO> getAllSalaryHeadsByOrgId(Long orgId);

	@Query(nativeQuery = true,value = "select * from salaryheads a where a.salaryheadsid=?1 ")
	SalaryHeadsVO getSalaryHeadsById(Long id);

	boolean existsByHeadingAndOrgId(String heading, Long orgId);

	boolean existsByCodeAndOrgId(String code, Long orgId);

}
