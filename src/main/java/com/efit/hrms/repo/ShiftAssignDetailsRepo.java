package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.ShiftAssignDetailsVO;
import com.efit.hrms.entity.ShiftAssignVO;

@Repository
public interface ShiftAssignDetailsRepo extends JpaRepository<ShiftAssignDetailsVO, Long>{

	List<ShiftAssignDetailsVO> findByShiftAssignVO(ShiftAssignVO shiftAssignVO);

}
