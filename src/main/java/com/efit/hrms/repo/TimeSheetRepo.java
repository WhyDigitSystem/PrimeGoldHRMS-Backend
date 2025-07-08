package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.TimeSheetVO;

@Repository
public interface TimeSheetRepo extends JpaRepository<TimeSheetVO, Long>{

	@Query(value = "SELECT * FROM timesheet e WHERE e.orgid=?1 and e.employeecode=?2 and date=?3",nativeQuery = true )
	List<TimeSheetVO> getTimeSheetByOrgId(Long orgId, String empCode, String date);

	@Query(value = "SELECT * FROM timesheet  WHERE timesheetid = ?1 ", nativeQuery = true)
	TimeSheetVO getTimeSheetById(Long id);

	@Query(value = "SELECT * FROM timesheet e WHERE e.orgid = ?1 AND e.employeecode = ?2 AND e.branchCode = ?3 AND e.date BETWEEN ?4 AND ?5", nativeQuery = true)
	List<TimeSheetVO> getTimeSheetDescByOrgId(Long orgId, String empCode, String branchCode, String fromDate, String toDate);

	
}
