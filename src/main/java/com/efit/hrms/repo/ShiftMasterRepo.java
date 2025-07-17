package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.ShiftMasterVO;

@Repository
public interface ShiftMasterRepo extends JpaRepository<ShiftMasterVO, Long>{

	@Query( value = "SELECT * FROM shiftmaster WHERE orgid =?1",nativeQuery = true)
	List<ShiftMasterVO> getAllShiftMasterByOrgId(Long orgId);

	@Query( value = "SELECT * FROM shiftmaster WHERE shiftmasterid =?1",nativeQuery = true)
	ShiftMasterVO getShiftMasterById(Long id);

	@Query( value = "SELECT * FROM shiftmaster WHERE orgid=?1 and shift=?2 and shiftcode=?3 and branchcode=?4",nativeQuery = true)
	 List<ShiftMasterVO>  getAllShiftMasterByOrgIdAndShiftAndBranchCode(Long orgId, String shift,String shiftCode, String branchCode);
}
