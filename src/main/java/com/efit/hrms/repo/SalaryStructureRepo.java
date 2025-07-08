package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.SalaryStructureVO;

@Repository
public interface SalaryStructureRepo extends JpaRepository<SalaryStructureVO, Long>{

	@Query(nativeQuery = true,value = "select * from salarystructure a where a.orgid=?1 ")
	List<SalaryStructureVO> getAllSalaryStructureByOrgId(Long orgId);

	
	@Query(nativeQuery = true,value = "select * from salarystructure a where a.salarystructureid=?1 ")
	SalaryStructureVO getSalaryStructureById(Long id);





}
