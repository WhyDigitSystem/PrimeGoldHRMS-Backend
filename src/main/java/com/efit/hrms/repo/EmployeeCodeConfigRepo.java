package com.efit.hrms.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efit.hrms.entity.EmployeeCodeConfigVO;

public interface EmployeeCodeConfigRepo extends JpaRepository<EmployeeCodeConfigVO, Long> {
	
	Optional<EmployeeCodeConfigVO> findByOrgId(Long orgId);

}
