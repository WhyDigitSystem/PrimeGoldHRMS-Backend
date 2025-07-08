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

	
	@Query(nativeQuery = true, value = "WITH RECURSIVE date_series AS (\r\n"
			+ "    SELECT DATE(?1) AS dt\r\n"
			+ "    UNION ALL\r\n"
			+ "    SELECT DATE_ADD(dt, INTERVAL 1 DAY)\r\n"
			+ "    FROM date_series\r\n"
			+ "    WHERE dt < DATE(?2)\r\n"
			+ "),\r\n"
			+ "month_count AS (\r\n"
			+ "    SELECT \r\n"
			+ "        MONTH(dt) AS month,\r\n"
			+ "        YEAR(dt) AS year,\r\n"
			+ "        COUNT(*) AS days_in_month\r\n"
			+ "    FROM date_series\r\n"
			+ "    GROUP BY MONTH(dt), YEAR(dt)\r\n"
			+ "),\r\n"
			+ "max_month_info AS (\r\n"
			+ "    SELECT \r\n"
			+ "        month,\r\n"
			+ "        year,\r\n"
			+ "        (SELECT COUNT(*) FROM date_series) AS totalcompanyworkingdays\r\n"
			+ "    FROM month_count\r\n"
			+ "    ORDER BY days_in_month DESC\r\n"
			
			+ "    LIMIT 1\r\n"
			+ ")\r\n"
			+ "SELECT \r\n"
			+ "    lb.employee AS employeename,\r\n"
			+ "    lb.employeecode,\r\n"
			+ "    mm.totalcompanyworkingdays,\r\n"
			+ "    mm.month,\r\n"
			+ "    mm.year,\r\n"
			+ "    COALESCE(b.totalleaves, 0) AS totalleaves,\r\n"
			+ "    COALESCE(c.lopleaves, 0) AS lopleaves,\r\n"
//			+ "    (mm.totalcompanyworkingdays - COALESCE(b.totalleaves, 0)) AS emptotalworkingdays,\r\n"
			+ "    (mm.totalcompanyworkingdays - COALESCE(c.lopleaves, 0)) AS empsalarydays\r\n"
			+ "FROM leavebalance lb\r\n"
			+ "CROSS JOIN max_month_info mm\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT \r\n"
			+ "        employeecode, \r\n"
			+ "        SUM(totalleave) AS totalleaves \r\n"
			+ "    FROM approvalleaves \r\n"
			+ "    WHERE leavedate BETWEEN DATE(?1) AND DATE(?2)\r\n"
			+ "      AND orgid = ?3\r\n"
			+ "    GROUP BY employeecode\r\n"
			+ ") b ON lb.employeecode = b.employeecode\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT \r\n"
			+ "        employeecode, \r\n"
			+ "        SUM(totalleave) AS lopleaves \r\n"
			+ "    FROM approvalleaves\r\n"
			+ "    WHERE leavetype = 'LOSS OF PAY'\r\n"
			+ "      AND leavedate BETWEEN DATE(?1) AND DATE(?2)\r\n"
			+ "      AND orgid = ?3\r\n"
			+ "    GROUP BY employeecode\r\n"
			+ ") c ON lb.employeecode = c.employeecode\r\n"
			+ "WHERE lb.orgid = ?3\r\n"
			+ "  AND NOT EXISTS (\r\n"
			+ "    SELECT 1 \r\n"
			+ "    FROM leaveprocess lp \r\n"
			+ "    WHERE lp.employeecode = lb.employeecode\r\n"
			+ "      AND lp.year = mm.year\r\n"
			+ "      AND lp.month = mm.month\r\n"
			+ "      AND lp.orgid = ?3\r\n"
			+ ")\r\n"
			+ "GROUP BY \r\n"
			+ "    lb.employee, lb.employeecode, mm.totalcompanyworkingdays, mm.month, mm.year, b.totalleaves, c.lopleaves \r\n"
			+ "")
	Set<Object[]> getLeaveDetailsForLeaveProcess(String fromDate, String toDate, Long orgId);

	
	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "    employeename,\r\n"
			+ "    employeecode,\r\n"
			+ "    SUM(totalcompanyworkingdays) AS totalcompanyworkingdays,\r\n"
			+ "    SUM(totalleave) AS totalleave,\r\n"
			+ "    SUM(lopleave) AS lopleave,\r\n"
			+ "    SUM(emptotalworkingdays) AS emptotalworkingdays,\r\n"
			+ "    SUM(empsalarydays) AS empsalarydays\r\n"  
			+ "FROM leaveprocess\r\n"
			+ "WHERE orgid = ?1 \r\n"
			+ "    AND month = ?2 \r\n"
			+ "    AND year = ?3 \r\n"
			+ "    AND approvedstatus = 'PENDING'\r\n"
			+ "GROUP BY employeename, employeecode\r\n"
			+ "")
	Set<Object[]> getLeaveDetailsforSalaryProcess(Long orgId, Long month, String year);

 
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
