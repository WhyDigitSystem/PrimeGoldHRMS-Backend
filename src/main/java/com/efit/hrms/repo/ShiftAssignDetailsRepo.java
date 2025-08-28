package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.ShiftAssignDetailsVO;
import com.efit.hrms.entity.ShiftAssignVO;

@Repository
public interface ShiftAssignDetailsRepo extends JpaRepository<ShiftAssignDetailsVO, Long>{

	List<ShiftAssignDetailsVO> findByShiftAssignVO(ShiftAssignVO shiftAssignVO);

	List<ShiftAssignDetailsVO> findByEmployeeCodeAndShiftAssignVO_OrgId(String empcode, long orgId);
	
	
	@Query("SELECT s FROM ShiftAssignDetailsVO s " +
		       "WHERE s.employeeCode = :empCode " +
		       "AND s.shiftAssignVO.orgId = :orgId " +
		       "AND s.effectiveFrom <= :today " +
		       "AND (s.effectiveTo IS NULL OR s.effectiveTo >= :today)")
		List<ShiftAssignDetailsVO> findApplicableShifts(@Param("empCode") String empCode,
		                                                 @Param("today") LocalDate today,
		                                                 @Param("orgId") Long orgId);


}
