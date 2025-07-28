package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AttendanceDailyVO;
import com.efit.hrms.entity.AttendanceSummaryVO;

@Repository
public interface AttendanceSummaryRepo extends JpaRepository<AttendanceSummaryVO, Long>{

	@Query(nativeQuery = true, value = "    SELECT *\r\n"
			+ "    FROM attendancesummary a \r\n"
			+ "    WHERE a.orgid = ?1 and branchcode=?2 \r\n"
			+" and a.approvestatus='PENDING'\r\n"
			+ " ")
	List<AttendanceSummaryVO> getPendingAttendanceSummaryByOrgId( Long orgId,
			String branch);
	
	
	

}
