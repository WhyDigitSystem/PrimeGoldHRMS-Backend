package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AttendanceDailyVO;

@Repository
public interface AttendanceDailyRepo extends JpaRepository<AttendanceDailyVO, Long>{

//	AttendanceDailyVO findByEmpCodeAndCheckInDateAndOrgIdAndBranchAndStatus(String empcode, LocalDate today, long orgId,
//			String branch, String string);

	AttendanceDailyVO findByEmpCodeAndCheckInDateAndOrgIdAndBranch(String empcode, LocalDate today, long orgId,
			String branch);

	@Query(nativeQuery = true, value = "    SELECT *\r\n"
			+ "    FROM attendancedaily a join employee e on e.employeecode=a.empcode\r\n"
			+ "    WHERE a.orgid = ?3\r\n"
			+ "      AND (?4 = 'ALL' OR a.empcode = ?4)\r\n"
			+ "      AND (?5 = 'All' OR a.branch = ?5)\r\n"
			+ "      AND a.checkindate BETWEEN ?1 AND ?2 and e.active=1\r\n"
			+ " ")
	List<AttendanceDailyVO> getAttendanceDailyByOrgId(String fromDate, String toDate, Long orgId, String employeeCode,
			String branch);

	

}
