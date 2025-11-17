package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AttendanceDailyVO;
import com.efit.hrms.entity.AttendanceSummaryVO;

@Repository
public interface AttendanceSummaryRepo extends JpaRepository<AttendanceSummaryVO, Long>{

	@Query(nativeQuery = true, value = 
		    "SELECT * " +
		    "FROM attendancesummary a " +
		    "WHERE a.orgid = ?1 " +
		    "AND (?2 = 'All' OR a.branch = ?2) " +
		    "AND a.approvestatus = 'PENDING'"
		)
	List<AttendanceSummaryVO> getPendingAttendanceSummaryByOrgId( Long orgId,
			String branch);

	@Query(nativeQuery = true, value = " SELECT * FROM attendancesummary a\r\n"
			+ "    WHERE (?1 = 'All' OR a.empcode = ?1)"
			+ "     AND (?2 IS NULL OR a.month = ?2)\r\n"
			+ "      AND (?3 IS NULL OR a.finyear = ?3)\r\n"
			+ "      AND a.orgid = ?4\r\n"
			+ "      AND (?5 = 'All' OR UPPER(a.branch) = UPPER(?5))\r\n"
			+ "      AND a.approvestatus = 'APPROVED'")   
	List<AttendanceSummaryVO> getAttendanceSummaryByOrgId(String empCode,Integer month,String finYear,Long orgId, String branch);
	
	
	

}
