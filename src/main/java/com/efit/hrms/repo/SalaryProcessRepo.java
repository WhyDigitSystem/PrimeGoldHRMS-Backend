package com.efit.hrms.repo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.SalaryProcessVO;

@Repository
public interface SalaryProcessRepo extends JpaRepository<SalaryProcessVO, Long> {

	@Query(nativeQuery = true, value = "select * from salaryprocess a where a.orgid=?1 ")
	List<SalaryProcessVO> getAllSalaryProcessByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from salaryprocess a where a.salaryprocessid=?1 ")
	SalaryProcessVO getSalaryProcessById(Long id);

	@Query(nativeQuery = true, value = "select amount , sumofearning,sumofdetection from salarystructure a where a.orgid=?1 and employeecode=?2 ORDER BY a.date DESC \r\n"
			+ "LIMIT 1 ")
	Set<Object[]> getSalaryStructureForSalaryProcess(Long orgId, String employeeCode);

//	@Query(value = "            SELECT ROUND((?2 / ?1) * ?3) - ?4 AS netPay \r\n"
//			+ "", nativeQuery = true)
//	Set<Object[]> getNetPayForSalaryProcess( Long totalCompanyWorkingDays, BigDecimal grossPay,
//			Long empSalaryDays, BigDecimal sumOfDetection);

//	@Query(value = "SELECT ROUND(CAST(?1 AS DECIMAL) - CAST(?2 AS DECIMAL), 2) AS netPay", 
//		       nativeQuery = true)
//		Set<Object[]> getNetPayForSalaryProcess(BigDecimal grossPay, BigDecimal sumOfDetection);


	@Query(value = "SELECT ROUND((CAST(?2 AS DECIMAL) / CAST(?1 AS DECIMAL)) * CAST(?3 AS DECIMAL) - CAST(?4 AS DECIMAL), 2) AS payonhand", 
		       nativeQuery = true)
	Set<Object[]> getPayOnHandsForSalaryProcess(Long totalCompanyWorkingDays, BigDecimal grossPay, Long empSalaryDays,
			BigDecimal sumOfDetection);
	
	

	@Query(nativeQuery = true, value = "SELECT * \r\n"
			+ "FROM salaryprocess a \r\n"
			+ "WHERE a.orgid = ?1 \r\n"
			+ " \r\n"
			+ "  AND (a.month = ?2 OR ?2 = 0) \r\n"
			+ "  AND (a.year = ?3 OR ?3 = 'ALL') ")
	List<SalaryProcessVO> getApprovedSalaryProcessReport(Long orgId, Long month, String year);
	
	@Query(nativeQuery = true, value = "select distinct(a.employee),a.orgid,a.employeecode ,a.joiningdate, a.designation,a.department ,a.branch,a.branchcode,a.accountno,a.panno,a.uanno,empsalarydays as effectiveworkingdays,totalcompanyworkingdays as monthdays,a.bankname,s.lopleave from employee a, salaryprocess s \r\n"
			+ "where a.employeecode =?2\r\n"
			+ " and a.employeecode = s.employeecode\r\n"
			+ "and a.orgid=?1")
	Set<Object[]> findpayslipemployeeandearningsdetails(Long orgId, String Employeecode );
	
	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    employeename,\r\n"
			+ "    orgid,\r\n"
			+ "    employeecode,\r\n"
			+ "    heading,\r\n"
			+ "    amount,\r\n"
			+ "    totalcompanyworkingdays,\r\n"
			+ "    empsalarydays,\r\n"
			+ "    actuals,\r\n"
			+ "    payslipeffectivedate\r\n"
			+ "FROM (\r\n"
			+ "    -- Individual earnings\r\n"
			+ "    SELECT \r\n"
			+ "        b.employeename,\r\n"
			+ "        b.orgid,\r\n"
			+ "        b.employeecode,\r\n"
			+ "        d.heading,\r\n"
			+ "        d.amount,\r\n"
			+ "        s.totalcompanyworkingdays,\r\n"
			+ "        s.empsalarydays,\r\n"
			+ "        ROUND((d.amount / s.totalcompanyworkingdays) * s.empsalarydays, 0) AS actuals,\r\n"
			+ "        a.payslipeffectivedate\r\n"
			+ "    FROM \r\n"
			+ "        salarystructure b,\r\n"
			+ "        employee a,\r\n"
			+ "        salaryearningdetails d,\r\n"
			+ "        salaryprocess s\r\n"
			+ "    WHERE \r\n"
			+ "        b.salarystructureid = d.salarystructureid\r\n"
			+ "        AND b.employeecode = s.employeecode\r\n"
			+ "        AND a.employeecode = b.employeecode\r\n"
			+ "        AND b.active = 1\r\n"
			+ "        AND b.employeecode = ?2\r\n"
			+ "        AND b.orgid = ?1\r\n"
			+ "        AND s.month = ?3\r\n"
			+ "        AND s.year = ?4\r\n"
			+ "        AND (?3 >= MONTH(a.payslipeffectivedate) OR YEAR(a.payslipeffectivedate) <= ?4)\r\n"
			+ "    GROUP BY  \r\n"
			+ "        b.employeename,\r\n"
			+ "        b.orgid,\r\n"
			+ "        b.employeecode,\r\n"
			+ "        d.heading,\r\n"
			+ "        d.amount,\r\n"
			+ "        s.totalcompanyworkingdays,\r\n"
			+ "        s.empsalarydays,\r\n"
			+ "        a.payslipeffectivedate\r\n"
			+ "\r\n"
			+ "    UNION\r\n"
			+ "\r\n"
			+ "    -- Total earnings\r\n"
			+ "    SELECT \r\n"
			+ "        '' AS employeename,\r\n"
			+ "        '' AS orgid,\r\n"
			+ "        '' AS employeecode,\r\n"
			+ "        'Total Earnings' AS heading,\r\n"
			+ "        SUM(d.amount) AS amount,\r\n"
			+ "        0 AS totalcompanyworkingdays,\r\n"
			+ "        0 AS empsalarydays,\r\n"
			+ "        SUM(ROUND((d.amount / s.totalcompanyworkingdays) * s.empsalarydays, 0)) AS actuals,\r\n"
			+ "        a.payslipeffectivedate\r\n"
			+ "    FROM \r\n"
			+ "        salarystructure b,\r\n"
			+ "        employee a,\r\n"
			+ "        salaryearningdetails d,\r\n"
			+ "        salaryprocess s\r\n"
			+ "    WHERE \r\n"
			+ "        b.salarystructureid = d.salarystructureid\r\n"
			+ "        AND b.employeecode = s.employeecode\r\n"
			+ "        AND a.employeecode = b.employeecode\r\n"
			+ "        AND b.active = 1\r\n"
			+ "        AND b.employeecode = ?2\r\n"
			+ "        AND b.orgid = ?1\r\n"
			+ "        AND s.month = ?3\r\n"
			+ "        AND s.year = ?4\r\n"
			+ "        AND (?3 >= MONTH(a.payslipeffectivedate) OR YEAR(a.payslipeffectivedate) <= ?4)\r\n"
			+ "    GROUP BY \r\n"
			+ "        a.payslipeffectivedate,\r\n"
			+ "        s.totalcompanyworkingdays\r\n"
			+ ") AS a\r\n"
			+ "")
	List<Object[]> findpayslipearningsdetails(Long orgId, String employeeCode, Long month, Long year);

	@Query(value = "SELECT employee, orgid, employeecode, heading, amount FROM ( \n" +
	        "  SELECT a.employee, a.orgid, a.employeecode, c.heading, c.amount \n" +
	        "  FROM employee a, salarystructure b, salarydetectiondetails c \n" +
	        "  WHERE a.employeecode = b.employeecode \n" +
	        "    AND c.salarystructureid = b.salarystructureid \n" +
	        "    AND b.active = 1 \n" +
	        "    AND b.employeecode = ?2 \n" +
	        "    AND b.orgid = ?1 \n" +
	        "    AND (YEAR(a.payslipeffectivedate) < ?4 OR (YEAR(a.payslipeffectivedate) = ?4 AND MONTH(a.payslipeffectivedate) <= ?3)) \n" +
	        "  UNION \n" +
	        "  SELECT '' AS employee, '' AS orgid, '' AS employeecode, 'Total Deduction' AS heading, SUM(c.amount) \n" +
	        "  FROM employee a, salarystructure b, salarydetectiondetails c \n" +
	        "  WHERE a.employeecode = b.employeecode \n" +
	        "    AND c.salarystructureid = b.salarystructureid \n" +
	        "    AND b.active = 1 \n" +
	        "    AND b.employeecode = ?2 \n" +
	        "    AND b.orgid = ?1 \n" +
	        "    AND (YEAR(a.payslipeffectivedate) < ?4 OR (YEAR(a.payslipeffectivedate) = ?4 AND MONTH(a.payslipeffectivedate) <= ?3)) \n" +
	        ") a", nativeQuery = true)
	Set<Object[]> findpayslipdeductionsdetails(Long orgId, String Employeecode , Long Month,Long year);
	
	@Query(nativeQuery = true, value ="        SELECT \r\n"
			+ "    employee,\r\n"
			+ "    orgid,\r\n"
			+ "    employeecode,\r\n"
			+ "    SUM(CASE \r\n"
			+ "            WHEN heading = 'Total Earnings' THEN total \r\n"
			+ "            ELSE -total \r\n"
			+ "        END) AS totalsum\r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        a.employee,\r\n"
			+ "        a.orgid,\r\n"
			+ "        a.employeecode,\r\n"
			+ "        'Total Earnings' AS heading,\r\n"
			+ "        SUM(ROUND((d.amount / s.emptotalworkingdays) * s.empsalarydays, 0)) AS total\r\n"
			+ "    FROM \r\n"
			+ "        salarystructure b\r\n"
			+ "        JOIN salaryearningdetails d ON b.salarystructureid = d.salarystructureid\r\n"
			+ "        JOIN salaryprocess s ON b.employeecode = s.employeecode\r\n"
			+ "        JOIN employee a ON a.employeecode = s.employeecode\r\n"
			+ "    WHERE \r\n"
			+ "        b.active = 1\r\n"
			+ "        AND b.employeecode = ?2\r\n"
			+ "        AND b.orgid = ?1\r\n"
			+ "        AND MONTH(a.payslipeffectivedate) >= ?3\r\n"
			+ "        AND s.month = ?3\r\n"
			+ "        AND s.year = ?4\r\n"
			+ "    GROUP BY \r\n"
			+ "        a.employee,\r\n"
			+ "        a.orgid,\r\n"
			+ "        a.employeecode\r\n"
			+ "    UNION \r\n"
			+ "    SELECT \r\n"
			+ "        a.employee,\r\n"
			+ "        a.orgid,\r\n"
			+ "        a.employeecode,\r\n"
			+ "        'Total Deduction' AS heading,\r\n"
			+ "        SUM(c.amount)\r\n"
			+ "    FROM \r\n"
			+ "        employee a\r\n"
			+ "        JOIN salarystructure b ON a.employeecode = b.employeecode\r\n"
			+ "        JOIN salarydetectiondetails c ON c.salarystructureid = b.salarystructureid\r\n"
			+ "    WHERE \r\n"
			+ "        b.active = 1\r\n"
			+ "        AND b.employeecode = ?2\r\n"
			+ "        AND b.orgid = ?1\r\n"
			+ "        AND ?3 >= MONTH(a.payslipeffectivedate)\r\n"
			+ "        AND ?4 >= YEAR(a.payslipeffectivedate)\r\n"
			+ "    GROUP BY \r\n"
			+ "        a.employee,\r\n"
			+ "        a.orgid,\r\n"
			+ "        a.employeecode\r\n"
			+ ") a\r\n"
			+ "GROUP BY \r\n"
			+ "    employee,\r\n"
			+ "    orgid,\r\n"
			+ "    employeecode")
			Set<Object[]> findpayslipshandsondetails(Long orgId, String employeecode, Long month, Long year);


			@Query(nativeQuery = true, value = "select companyid,companycode,companyname,address,city,state,zipcode,companylogo from company c \r\n"
					+ "where companyid =?1")
			Set<Object[]> findpayslipcompanydetails (Long orgId );

			@Query("SELECT s FROM SalaryProcessVO s WHERE s.orgId = ?1 AND s.employeeCode = ?2 AND s.month = ?3 AND s.year = ?4")
			List<SalaryProcessVO> findByOrgIdAndEmployeeCodeAndMonthAndYear(Long orgId, String employeeCode, Long month, String year);

	



}
