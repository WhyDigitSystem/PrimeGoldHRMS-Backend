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

	
	
//	@Query(nativeQuery = true, value =
//		    "SELECT\r\n"
//		    + "    asu.empname,\r\n"
//		    + "    asu.empcode,\r\n"
//		    + "    e.branch,\r\n"
//		    + "    e.department,\r\n"
//		    + "    SUM(asu.totaldays) AS totalcompanyworkingdays,\r\n"
//		    + "    SUM(asu.leaves) AS totalleave,\r\n"
//		    + "    SUM(asu.lop) AS lopleave,\r\n"
//		    + "    SUM(asu.present) AS emptotalworkingdays,\r\n"
//		    + "    SUM(asu.salarydays) AS empsalarydays,\r\n"
//		    + "    SUM(asu.othours) AS totalothours,"
//		    + "    SUM(asu.bankotamount) As bankotamount,"
//		    + "SUM(asu.cashotamount) As cashotamount\\\r\n"
//		    + "FROM attendancesummary asu\r\n"
//		    + "JOIN employee e \r\n"
//		    + "    ON asu.empcode = e.employeecode\r\n"
//		    + "WHERE asu.orgid = ?1\r\n"
//		    + "  AND asu.month = ?2\r\n"
//		    + "  AND asu.finyear = ?3\r\n"
//		    + "  AND asu.approvestatus = 'APPROVED'\r\n"
//		    + "  AND (?4 = 'ALL' OR e.department = ?4)\r\n"
//		    + "  AND (?5 = 'ALL' OR e.branch = ?5)\r\n"
//		    + "  AND (\r\n"
//		    + "        ?6 = 'ALL'\r\n"
//		    + "        OR (?6 = 'EMPLOYEE' AND e.type = 'EMPLOYEE')\r\n"
//		    + "        OR (?6 = 'CONTRACTOR' AND e.type = 'CONTRACTOR' AND (?7 IS NULL OR e.contractor = ?7))\r\n"
//		    + "      )\r\n"
//		    + "  AND NOT EXISTS (\r\n"
//		    + "        SELECT 1 \r\n"
//		    + "        FROM salaryprocess sp\r\n"
//		    + "        WHERE sp.orgid = asu.orgid\r\n"
//		    + "          AND sp.employeecode = asu.empcode\r\n"
//		    + "          AND sp.month = asu.month\r\n"
//		    + "          AND sp.year = asu.finyear\r\n"
//		    + "  )AND (\r\n"
//		    + "    EXISTS (\r\n"
//		    + "        SELECT 1\r\n"
//		    + "        FROM salarystructure ss\r\n"
//		    + "        WHERE ss.orgid = asu.orgid\r\n"
//		    + "          AND ss.employeecode = asu.empcode\r\n"
//		    + "    )\r\n"
//		    + "    OR\r\n"
//		    + "    EXISTS (\r\n"
//		    + "        SELECT 1\r\n"
//		    + "        FROM otherpayments j\r\n"
//		    + "        WHERE j.orgid = asu.orgid\r\n"
//		    + "          AND j.employeecode = asu.empcode\r\n"
//		    + "    )\r\n"
//		    + ")\r\n"
//		    + "\r\n"
//		    + "GROUP BY asu.empname, asu.empcode, e.branch, e.department\r\n"
//		    + "ORDER BY asu.empname ASC \r\n"
//		    + " \r\n"
//		    + ""
//		)
//		Set<Object[]> getLeaveDetailsforSalaryProcess(Long orgId, Long month, String year, String department, String branch, String type, String contractor);

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
	
	
	
	@Query(nativeQuery = true, value = "WITH attendance AS (\r\n"
			+ "    SELECT empcode employeecode,totaldays,ROUND(salarydays) salarydays,ROUND(othours/8) otdays\r\n"
			+ "    FROM attendancesummary\r\n"
			+ "    WHERE orgid=?1 AND finyear=?3 and (branch=?5 or 'ALL'=?5) AND month=?2\r\n"
			+ "),\r\n"
			+ "ss AS (\r\n"
			+ "    SELECT ss.*\r\n"
			+ "    FROM salarystructure ss\r\n"
			+ "    INNER JOIN (\r\n"
			+ "        SELECT employeecode, MAX(salarystructureid) AS max_id\r\n"
			+ "        FROM salarystructure\r\n"
			+ "        GROUP BY employeecode\r\n"
			+ "    ) latest ON ss.employeecode = latest.employeecode\r\n"
			+ "            AND ss.salarystructureid = latest.max_id\r\n"
			+ "),\r\n"
			+ "op AS (\r\n"
			+ "    SELECT op.*\r\n"
			+ "    FROM otherpayments op\r\n"
			+ "    INNER JOIN (\r\n"
			+ "        SELECT employeecode, MAX(otherpaymentsid) AS max_id\r\n"
			+ "        FROM otherpayments\r\n"
			+ "        GROUP BY employeecode\r\n"
			+ "    ) latest ON op.employeecode = latest.employeecode\r\n"
			+ "            AND op.otherpaymentsid = latest.max_id\r\n"
			+ "),\r\n"
			+ "advance AS (\r\n"
			+ "    SELECT employeecode,COALESCE(bank,0)bank,COALESCE(cash,0)cash\r\n"
			+ "    FROM advanceupload\r\n"
			+ "    WHERE orgid=?1 AND year=?3 and (branch=?5 or 'ALL'=?5) AND month=?2\r\n"
			+ "),\r\n"
			+ "j AS (\r\n"
			+ "    SELECT e.employee,e.employeecode,e.branch,e.department,att.totaldays,att.salarydays,att.otdays,\r\n"
			+ "           ROUND(COALESCE(ss.sumofearning,0) + COALESCE(op.amount,0)) AS fixedsalary,\r\n"
			+ "       ROUND(((COALESCE(ss.sumofearning,0) + COALESCE(op.amount,0)) / att.totaldays) * att.salarydays) AS presentamount,\r\n"
			+ "       ROUND(((COALESCE(ss.sumofearning,0) + COALESCE(op.amount,0)) / att.totaldays) * att.otdays) AS otamount,\r\n"
			+ "       ROUND(COALESCE(op.allowance,0)) AS allowance,\r\n"
			+ "       ROUND(((COALESCE(ss.sumofearning,0) + COALESCE(op.amount,0)) / att.totaldays) * att.salarydays) \r\n"
			+ "     + ROUND(((COALESCE(ss.sumofearning,0) + COALESCE(op.amount,0)) / att.totaldays) * att.otdays)\r\n"
			+ "     + ROUND(COALESCE(op.allowance,0)) AS totalearnings  \r\n"
			+ "    FROM employee e\r\n"
			+ "LEFT JOIN ss ON ss.employeecode = e.employeecode\r\n"
			+ "LEFT JOIN op ON op.employeecode = e.employeecode\r\n"
			+ "left join attendance att on att.employeecode=e.employeecode\r\n"
			+ "LEFT JOIN advance ad ON ad.employeecode = att.employeecode where  e.active=1 and e.type=?6 and (e.branch=?5 or ?5 ='ALL') and (e.department=?4 or ?4='ALL')\r\n"
			+ ") ,\r\n"
			+ "t as(SELECT x.employee,x.employeecode,x.branch,x.department,x.totaldays,x.salarydays,x.otdays,x.fixedsalary,x.presentamount,x.otamount,x.allowance,\r\n"
			+ "       x.totalearnings,round(x.bankearnings)bankearnings,\r\n"
			+ "       round(\r\n"
			+ "       CASE \r\n"
			+ "            WHEN e.pfflag=1 AND x.bankearnings >=15000 THEN 1800\r\n"
			+ "            WHEN e.pfflag=1 AND x.bankearnings < 15000 THEN (x.bankearnings * 12)/100\r\n"
			+ "            ELSE 0\r\n"
			+ "       END) AS pf,round(\r\n"
			+ "       CASE \r\n"
			+ "            WHEN e.esiflag=1 AND x.sumofearning >=21000 THEN 0\r\n"
			+ "            WHEN e.esiflag=1 AND x.sumofearning < 21000 THEN (x.x.sumofearning * 0.75)/100\r\n"
			+ "            ELSE 0\r\n"
			+ "       END) AS esi,round(x.bankadvance)bankadvance,round(round(x.bankearnings) - round(\r\n"
			+ "       CASE \r\n"
			+ "            WHEN e.pfflag=1 AND x.bankearnings >=15000 THEN 1800\r\n"
			+ "            WHEN e.pfflag=1 AND x.bankearnings < 15000 THEN (x.bankearnings * 12)/100\r\n"
			+ "            ELSE 0\r\n"
			+ "       END)-round(\r\n"
			+ "       CASE \r\n"
			+ "            WHEN e.esiflag=1 AND x.sumofearning >=21000 THEN 0\r\n"
			+ "            WHEN e.esiflag=1 AND x.sumofearning < 21000 THEN (x.sumofearning * 0.75)/100\r\n"
			+ "            ELSE 0\r\n"
			+ "       END)-x.bankadvance) banksalary,x.cashadvance\r\n"
			+ "FROM (\r\n"
			+ "    SELECT j.employee,j.employeecode,j.branch,j.department,j.totaldays,j.salarydays,j.otdays,j.fixedsalary,j.presentamount,j.otamount,j.allowance,\r\n"
			+ "           j.totalearnings,\r\n"
			+ "           COALESCE(ss.sumofearning,0)sumofearning,\r\n"
			+ "           CASE WHEN j.totalearnings > COALESCE(ss.sumofearning,0) THEN COALESCE(ss.sumofearning,0)  \r\n"
			+ "                ELSE j.totalearnings \r\n"
			+ "           END AS bankearnings,COALESCE(ad.bank,0)bankadvance,round(COALESCE(ad.cash,0))cashadvance           \r\n"
			+ "    FROM j left\r\n"
			+ "     JOIN ss ON j.employeecode = ss.employeecode\r\n"
			+ "     left join advance ad on j.employeecode=ad.employeecode\r\n"
			+ ") x\r\n"
			+ "JOIN employee e ON x.employeecode = e.employeecode)\r\n"
			+ "select t.employee,t.employeecode,t.branch,t.department,t.totaldays,t.salarydays,t.otdays,t.fixedsalary,t.presentamount,t.otamount,t.allowance,t.totalearnings,t.bankearnings,\r\n"
			+ "t.pf,t.esi,t.bankadvance,t.banksalary salarypaid,t.cashadvance,(t.totalearnings-(t.pf+t.esi+t.bankadvance+t.banksalary))-t.cashadvance netPay,(t.totaldays-t.salarydays)lop from t where t.employeecode not in(select employeecode from salaryprocess where orgid=?1 and year=?3 and month=?2 order by employeecode asc) order by t.employeecode asc")
	Set<Object[]> getLeaveDetailsforSalaryProcess(Long orgId, Long month, String year, String department, String branch, String type);




//	List<LeaveProcessVO> findByEmployeeCode(String employeeCode, Long orgId, String selectMonth, String selectYear);

//	List<LeaveProcessVO> findByEmployeeCode(String employeeCode, Long orgId, String month, String year);
}
