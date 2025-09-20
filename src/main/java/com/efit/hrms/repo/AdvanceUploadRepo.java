package com.efit.hrms.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AdvanceUploadVO;

@Repository
public interface AdvanceUploadRepo extends JpaRepository<AdvanceUploadVO, Long> {

	Optional<AdvanceUploadVO> findByEmployeeCodeAndMonthAndYearAndOrgId(String empCode, Long month, Long year,
			Long orgId);

}
