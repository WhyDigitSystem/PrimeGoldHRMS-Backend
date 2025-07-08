package com.efit.hrms.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.SequenceTrackerVO;

public interface SequenceTrackerRepo extends JpaRepository<SequenceTrackerVO, Long> {
	
	
	@Query(nativeQuery = true, value = "select * from sequencetracker where companyid=?1 and companycode=?2 and branchcode=?3 and departmentcode=?4 and year=?5")
	Optional<SequenceTrackerVO> findByCompanyIdAndCompanyCodeAndBranchCodeAndDepartmentCodeAndYear(
	        Long companyId,String companyCode, String branchCode, String departmentCode, Integer year);

}
