package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.ShiftAssignVO;

@Repository
public interface ShiftAssignRepo extends JpaRepository<ShiftAssignVO, Long>{

	@Query( value = "SELECT * FROM shiftassign WHERE shiftassignid=?1",nativeQuery = true)
	ShiftAssignVO getShiftAssignById(Long id);
	
	@Query( value = "SELECT * FROM shiftassign WHERE orgid=?1",nativeQuery = true)
	List<ShiftAssignVO>  getAllShiftAssignByOrgId(Long orgId);

}
