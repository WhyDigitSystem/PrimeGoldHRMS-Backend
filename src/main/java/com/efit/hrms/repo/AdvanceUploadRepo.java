package com.efit.hrms.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AdvanceUploadVO;

@Repository
public interface AdvanceUploadRepo extends JpaRepository<AdvanceUploadVO, Long> {

	Optional<AdvanceUploadVO> findByEmployeeCodeAndMonthAndYearAndOrgId(String empCode, Long month, Long year,
			Long orgId);

	@Query( value = "SELECT * FROM advanceupload WHERE orgid =?1 and month=?2 and year=?3 ",nativeQuery = true)
	List<AdvanceUploadVO> getAllAdvanceUploadByOrgId(Long orgId, Long month, Long year);

}
