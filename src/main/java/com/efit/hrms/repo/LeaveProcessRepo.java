package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.LeaveProcessVO;

@Repository
public interface LeaveProcessRepo extends JpaRepository<LeaveProcessVO, Long>{

	
//	@Query(nativeQuery = true, value = "WITH date_range AS (\r\n"
//			+ "			  SELECT \r\n"
//			+ "			    ABS(DATEDIFF(DATE(?2), DATE(?1))) + 1 AS totalcompanyworkingdays,\r\n"
//			+ "			       MONTH(DATE(?1)) AS month,\r\n"
//			+ "		      YEAR(DATE(?1)) AS year\r\n"
//			+ "			)\r\n"
//			+ "			SELECT \r\n"
//			+ "			   lb.employee AS employeename, \r\n"
//			+ "		  lb.employeecode, \r\n"
//			+ "			   dr.totalcompanyworkingdays, \r\n"
//			+ "		   dr.month,\r\n"
//			+ "			   dr.year, \r\n"
//			+ "		   COALESCE(b.totalleaves, 0) AS totalleaves, \r\n"
//			+ "			    COALESCE(c.lopleaves, 0) AS lopleaves,\r\n"
//			+ "		  (dr.totalcompanyworkingdays - COALESCE(b.totalleaves, 0)) AS emptotalworkingdays,\r\n"
//			+ "			    (dr.totalcompanyworkingdays - COALESCE(c.lopleaves, 0)) AS empsalarydays \r\n"
//			+ "			FROM leavebalance lb \r\n"
//			+ "			CROSS JOIN date_range dr \r\n"
//			+ "		LEFT JOIN (\r\n"
//			+ "	 SELECT employeecode, SUM(totalleave) AS totalleaves \r\n"
//			+ "			    FROM approvalleaves \r\n"
//			+ "			   WHERE leavedate BETWEEN DATE(?1) AND DATE(?2) \r\n"
//			+ "			     AND orgid = ?3\r\n"
//			+ "		  GROUP BY employeecode\r\n"
//			+ "			) b ON lb.employeecode = b.employeecode\r\n"
//			+ "			LEFT JOIN (\r\n"
//			+ "		 SELECT employeecode, SUM(totalleave) AS lopleaves \r\n"
//			+ "			    FROM approvalleaves\r\n"
//			+ "			    WHERE leavetype = 'LOSS OF PAY'\r\n"
//			+ "			      AND leavedate BETWEEN DATE(?1) AND DATE(?2) \r\n"
//			+ "			     AND orgid = ?3  \r\n"
//			+ "			   GROUP BY employeecode\r\n"
//			+ "			) c ON lb.employeecode = c.employeecode\r\n"
//			+ "			WHERE lb.orgid = ?3 \r\n"
//			+ "			AND NOT EXISTS (\r\n"
//			+ "			   SELECT 1 FROM leaveprocess lp \r\n"
//			+ "			  WHERE lp.employeecode = lb.employeecode\r\n"
//			+ "			  AND lp.year = dr.year\r\n"
//			+ "			     AND lp.month = dr.month\r\n"
//			+ "			     AND lp.orgid = ?3\r\n"
//			+ "			)\r\n"
//			+ "			GROUP BY lb.employee, lb.employeecode, dr.totalcompanyworkingdays, b.totalleaves, c.lopleaves")
//	Set<Object[]> getLeaveDetailsForLeaveProcess(String fromDate, String toDate, Long orgId);

	
	
	@Query(nativeQuery = true, value =
		    "SELECT\r\n"
		    + "    asu.empname,\r\n"
		    + "    asu.empcode,\r\n"
		    + "    e.branch,\r\n"
		    + "    e.department,\r\n"
		    + "    SUM(asu.totaldays) AS totalcompanyworkingdays,\r\n"
		    + "    SUM(asu.leaves) AS totalleave,\r\n"
		    + "    SUM(asu.lop) AS lopleave,\r\n"
		    + "    SUM(asu.present) AS emptotalworkingdays,\r\n"
		    + "    SUM(asu.salarydays) AS empsalarydays,\r\n"
		    + "    SUM(asu.othours) AS totalothours\r\n"
		    + "FROM attendancesummary asu\r\n"
		    + "JOIN employee e \r\n"
		    + "    ON asu.empcode = e.employeecode\r\n"
		    + "WHERE asu.orgid = ?1\r\n"
		    + "  AND asu.month = ?2\r\n"
		    + "  AND asu.finyear = ?3\r\n"
		    + "  AND asu.approvestatus = 'APPROVED'\r\n"
		    + "  AND (?4 = 'ALL' OR e.department = ?4)\r\n"
		    + "  AND (?5 = 'ALL' OR e.branch = ?5)\r\n"
		    + "  AND (\r\n"
		    + "        ?6 = 'ALL'\r\n"
		    + "        OR (?6 = 'EMPLOYEE' AND e.type = 'EMPLOYEE')\r\n"
		    + "        OR (?6 = 'CONTRACTOR' AND e.type = 'CONTRACTOR' AND (?7 IS NULL OR e.contractor = ?7))\r\n"
		    + "      )\r\n"
		    + "  AND NOT EXISTS (\r\n"
		    + "        SELECT 1 \r\n"
		    + "        FROM salaryprocess sp\r\n"
		    + "        WHERE sp.orgid = asu.orgid\r\n"
		    + "          AND sp.employeecode = asu.empcode\r\n"
		    + "          AND sp.month = asu.month\r\n"
		    + "          AND sp.year = asu.finyear\r\n"
		    + "  )AND EXISTS (\r\n"
		    + "    SELECT 1\r\n"
		    + "    FROM salarystructure ss\r\n"
		    + "    WHERE ss.orgid = asu.orgid\r\n"
		    + "      AND ss.employeecode = asu.empcode\r\n"
		    + ")\r\n"
		    + "\r\n"
		    + "GROUP BY asu.empname, asu.empcode, e.branch, e.department\r\n"
		    + "ORDER BY asu.empname ASC \r\n"
		    + " \r\n"
		    + ""
		)
		Set<Object[]> getLeaveDetailsforSalaryProcess(Long orgId, Long month, String year, String department, String branch, String type, String contractor);

	List<LeaveProcessVO> findByEmployeeCodeAndOrgIdAndMonthAndYear(String employeeCode, Long orgId, Long month,
			String year);

	@Query(nativeQuery = true, value = "select * from  leaveprocess where orgid=?1  ")
	List<LeaveProcessVO> getLeaveProcessByOrgId(Long orgId);


	@Query(nativeQuery = true, value = "SELECT * FROM leaveprocess \r\n"
			+ "    WHERE orgid=?1 and \r\n"
			+ "        (?2 = 'ALL' OR employeecode = ?2) AND \r\n"
			+ "        (?3 = 'ALL' OR year = ?3) AND \r\n"
			+ "        (?4 = 'ALL' OR month = ?4)  ")
	List<LeaveProcessVO> getAttandanceReport(Long orgId, String employeeCode, String year, String month);




//	List<LeaveProcessVO> findByEmployeeCode(String employeeCode, Long orgId, String selectMonth, String selectYear);

//	List<LeaveProcessVO> findByEmployeeCode(String employeeCode, Long orgId, String month, String year);
}
