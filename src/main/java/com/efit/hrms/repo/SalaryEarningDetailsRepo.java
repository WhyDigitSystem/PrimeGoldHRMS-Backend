package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.SalaryEarningDetailsVO;
import com.efit.hrms.entity.SalaryStructureVO;

@Repository
public interface SalaryEarningDetailsRepo extends JpaRepository<SalaryEarningDetailsVO, Long> {

	List<SalaryEarningDetailsVO> findBySalaryStructureVO(SalaryStructureVO salaryStructureVO);


	boolean existsByHeadingAndSalaryStructureVO_OrgIdAndSalaryStructureVO_EmployeeCode(String heading, Long orgId,
			String employeeCode);


	void deleteBySalaryStructureVO(SalaryStructureVO savedSalaryStructureVO);



}