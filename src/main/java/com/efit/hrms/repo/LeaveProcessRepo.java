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
		    "WITH RECURSIVE date_series AS ( \n" +
		    "    SELECT DATE(?1) AS dt \n" +
		    "    UNION ALL \n" +
		    "    SELECT DATE_ADD(dt, INTERVAL 1 DAY) \n" +
		    "    FROM date_series \n" +
		    "    WHERE dt < DATE(?2) \n" +
		    "), \n" +
		    "month_count AS ( \n" +
		    "    SELECT  \n" +
		    "        MONTH(dt) AS month, \n" +
		    "        YEAR(dt) AS year, \n" +
		    "        COUNT(*) AS days_in_month \n" +
		    "    FROM date_series \n" +
		    "    GROUP BY MONTH(dt), YEAR(dt) \n" +
		    "), \n" +
		    "max_month_info AS ( \n" +
		    "    SELECT  \n" +
		    "        month, \n" +
		    "        year, \n" +
		    "        (SELECT COUNT(*) FROM date_series) AS totalcompanyworkingdays \n" +
		    "    FROM month_count \n" +
		    "    ORDER BY days_in_month DESC \n" +
		    "    LIMIT 1 \n" +
		    ") \n" +
		    "SELECT  \n" +
		    "    lb.employee AS employeename, \n" +
		    "    lb.employeecode, \n" +
		    "    e.branch, \n" +
		    "    e.department, \n" +
		    "    mm.totalcompanyworkingdays, \n" +
		    "    mm.month, \n" +
		    "    mm.year, \n" +
		    "    COALESCE(b.totalleaves, 0) AS totalleaves, \n" +
		    "    COALESCE(c.lopleaves, 0) AS lopleaves, \n" +
		    "    (mm.totalcompanyworkingdays - COALESCE(c.lopleaves, 0)) AS empsalarydays, \n" +
		    "    (mm.totalcompanyworkingdays - COALESCE(b.totalleaves, 0)) AS emptotalworkingdays \n" +
		    "FROM leavebalance lb \n" +
		    "JOIN employee e ON lb.employeecode = e.employeecode \n" +
		    "CROSS JOIN max_month_info mm \n" +
		    "LEFT JOIN ( \n" +
		    "    SELECT  \n" +
		    "        employeecode,  \n" +
		    "        SUM(totalleave) AS totalleaves  \n" +
		    "    FROM approvalleaves  \n" +
		    "    WHERE leavedate BETWEEN DATE(?1) AND DATE(?2) \n" +
		    "      AND orgid = ?3 \n" +
		    "    GROUP BY employeecode \n" +
		    ") b ON lb.employeecode = b.employeecode \n" +
		    "LEFT JOIN ( \n" +
		    "    SELECT  \n" +
		    "        employeecode,  \n" +
		    "        SUM(totalleave) AS lopleaves  \n" +
		    "    FROM approvalleaves \n" +
		    "    WHERE leavetype = 'LOSS OF PAY' \n" +
		    "      AND leavedate BETWEEN DATE(?1) AND DATE(?2) \n" +
		    "      AND orgid = ?3 \n" +
		    "    GROUP BY employeecode \n" +
		    ") c ON lb.employeecode = c.employeecode \n" +
		    "WHERE lb.orgid = ?3 \n" +
		    "AND (?4 = 'ALL' OR e.department = ?4) \n" +
		    "AND (?5 = 'ALL' OR e.branch = ?5) \n" +
		    "AND NOT EXISTS ( \n" +
		    "    SELECT 1  \n" +
		    "    FROM leaveprocess lp  \n" +
		    "    WHERE lp.employeecode = lb.employeecode \n" +
		    "      AND lp.year = mm.year \n" +
		    "      AND lp.month = mm.month \n" +
		    "      AND lp.orgid = ?3 \n" +
		    ") \n" +
		    "GROUP BY  \n" +
		    "    lb.employee, lb.employeecode, e.branch, e.department, \n" +
		    "    mm.totalcompanyworkingdays, mm.month, mm.year, b.totalleaves, c.lopleaves \n" +
		    "ORDER BY lb.employee ASC"
		)
		Set<Object[]> getLeaveDetailsForLeaveProcess(String fromDate, String toDate, Long orgId, String department, String branch);

	
	@Query(nativeQuery = true, value =
		    "SELECT \r\n" +
		    "    lp.employeename,\r\n" +
		    "    lp.employeecode,\r\n" +
		    "    e.branch,\r\n" +
		    "    e.department,\r\n" +
		    "    SUM(lp.totalcompanyworkingdays) AS totalcompanyworkingdays,\r\n" +
		    "    SUM(lp.totalleave) AS totalleave,\r\n" +
		    "    SUM(lp.lopleave) AS lopleave,\r\n" +
		    "    SUM(lp.emptotalworkingdays) AS emptotalworkingdays,\r\n" +
		    "    SUM(lp.empsalarydays) AS empsalarydays\r\n" +
		    "FROM leaveprocess lp\r\n" +
		    "JOIN employee e ON lp.employeecode = e.employeecode\r\n" +
		    "WHERE lp.orgid = ?1\r\n" +
		    "  AND lp.month = ?2\r\n" +
		    "  AND lp.year = ?3\r\n" +
		    "  AND lp.approvedstatus = 'PENDING'\r\n" +
		    "  AND (?4 = 'ALL' OR e.department = ?4)\r\n" +
		    "  AND (?5 = 'ALL' OR e.branch = ?5)\r\n" +
		    "GROUP BY lp.employeename, lp.employeecode, e.branch, e.department\r\n" +
		    "ORDER BY lp.employeename ASC"
		)
		Set<Object[]> getLeaveDetailsforSalaryProcess(Long orgId, Long month, String year, String department, String branch);

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
