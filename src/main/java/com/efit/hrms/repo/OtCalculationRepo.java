package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.OtCalculationVO;

@Repository
public interface OtCalculationRepo extends JpaRepository<OtCalculationVO, Long>{

	@Query(
		    value = "WITH company_cte AS (\r\n"
		    		+ "    SELECT * FROM company WHERE companyid = ?1\r\n"
		    		+ "),\r\n"
		    		+ "shift_cte AS (\r\n"
		    		+ "    SELECT sd.employeecode,\r\n"
		    		+ "           TIME_TO_SEC(TIMEDIFF(STR_TO_DATE(sd.outtime, '%H:%i'), STR_TO_DATE(sd.intime, '%H:%i'))) AS working_sec,\r\n"
		    		+ "           sd.effectivefrom,\r\n"
		    		+ "           sd.effectiveto\r\n"
		    		+ "    FROM shiftassigndetails sd\r\n"
		    		+ "    JOIN shiftassign sa ON sd.shiftassignid = sa.shiftassignid\r\n"
		    		+ "    WHERE sa.orgid = ?1 AND sd.active = 1\r\n"
		    		+ "),\r\n"
		    		+ "attendance_cte AS (\r\n"
		    		+ "    SELECT ap.empcode, ap.empname, ap.branch, ap.branchcode, ap.checkindate,\r\n"
		    		+ "           MIN(CASE WHEN LOWER(ap.status) = 'in' THEN ap.entrytime END) AS intime,\r\n"
		    		+ "           MAX(CASE WHEN LOWER(ap.status) = 'out' THEN ap.entrytime END) AS outtime\r\n"
		    		+ "    FROM attendanceprocess ap\r\n"
		    		+ "    JOIN company_cte c ON ap.orgid = c.companyid\r\n"
		    		+ "    JOIN employee e ON ap.empcode = e.employeecode\r\n"
		    		+ "    WHERE ap.orgid = ?1\r\n"
		    		+ "      AND FIND_IN_SET(ap.attendancemode, c.attendancemode)\r\n"
		    		+ "      AND c.otflag = 1\r\n"
		    		+ "      AND e.otflag = 1\r\n"
		    		+ "      AND (\r\n"
		    		+ "          c.ottype = 'ALL' OR\r\n"
		    		+ "          (c.ottype = 'EMPLOYEE' AND LOWER(e.type) = 'employee') OR\r\n"
		    		+ "          (c.ottype = 'CONTRACTOR' AND LOWER(e.type) = 'contractor')\r\n"
		    		+ "      )\r\n"
		    		+ "      AND ap.checkindate BETWEEN DATE_FORMAT(CURDATE(), '%Y-%m-01') AND CURDATE()\r\n"
		    		+ "    GROUP BY ap.empcode, ap.empname, ap.branch, ap.branchcode, ap.checkindate\r\n"
		    		+ "),\r\n"
		    		+ "daytype_cte AS (\r\n"
		    		+ "    SELECT ds.empcode, ds.checkindate,\r\n"
		    		+ "           CASE\r\n"
		    		+ "               WHEN h.holidaydate IS NOT NULL THEN 'Holiday'\r\n"
		    		+ "               WHEN EXISTS (\r\n"
		    		+ "                   SELECT 1 FROM companyweekoff cw\r\n"
		    		+ "                   WHERE cw.companyid = ?1 AND UPPER(cw.weekoffdays) = UPPER(DAYNAME(ds.checkindate))\r\n"
		    		+ "               ) THEN 'Weekly-Off'\r\n"
		    		+ "               ELSE 'Regular'\r\n"
		    		+ "           END AS daytype\r\n"
		    		+ "    FROM attendance_cte ds\r\n"
		    		+ "    LEFT JOIN holidays h ON h.orgid = ?1 AND h.holidaydate = ds.checkindate\r\n"
		    		+ "),\r\n"
		    		+ "ot_policy_cte AS (\r\n"
		    		+ "    SELECT om.otmasterid, om.otcategory, om.ottype,\r\n"
		    		+ "           omd.minhours * 3600 AS min_sec,\r\n"
		    		+ "           omd.maxhours * 3600 AS max_sec,\r\n"
		    		+ "           omd.otrate, omd.slab, omd.effectivefrom, omd.effectiveto\r\n"
		    		+ "    FROM otmaster om\r\n"
		    		+ "    JOIN otmasterdetails omd ON omd.otmasterid = om.otmasterid\r\n"
		    		+ "    WHERE om.orgid = ?1 AND om.active = 1 AND omd.applicable = 1\r\n"
		    		+ "),\r\n"
		    		+ "combined_cte AS (\r\n"
		    		+ "    SELECT a.empcode, a.empname, a.branch, a.branchcode, a.checkindate,\r\n"
		    		+ "           STR_TO_DATE(a.intime, '%H:%i:%s') AS intime,\r\n"
		    		+ "           STR_TO_DATE(a.outtime, '%H:%i:%s') AS outtime,\r\n"
		    		+ "           TIME_TO_SEC(TIMEDIFF(\r\n"
		    		+ "               COALESCE(STR_TO_DATE(a.outtime, '%H:%i:%s'), '00:00:00'),\r\n"
		    		+ "               COALESCE(STR_TO_DATE(a.intime, '%H:%i:%s'), '00:00:00')\r\n"
		    		+ "           )) AS work_sec,\r\n"
		    		+ "           sft.working_sec, d.daytype\r\n"
		    		+ "    FROM attendance_cte a\r\n"
		    		+ "    LEFT JOIN shift_cte sft ON sft.employeecode = a.empcode\r\n"
		    		+ "        AND a.checkindate BETWEEN sft.effectivefrom AND sft.effectiveto\r\n"
		    		+ "    JOIN daytype_cte d ON d.empcode = a.empcode AND d.checkindate = a.checkindate\r\n"
		    		+ "),\r\n"
		    		+ "ot_calc_cte AS (\r\n"
		    		+ "    SELECT c.*, COALESCE(c.work_sec - COALESCE(c.working_sec, 0), 0) AS raw_ot_sec\r\n"
		    		+ "    FROM combined_cte c\r\n"
		    		+ "),\r\n"
		    		+ "ot_final_ranked AS (\r\n"
		    		+ "    SELECT o.*, p.otcategory, p.ottype, p.min_sec, p.max_sec, p.otrate, p.slab,\r\n"
		    		+ "           ROW_NUMBER() OVER (PARTITION BY o.empcode, o.checkindate ORDER BY p.min_sec DESC) AS rn\r\n"
		    		+ "    FROM ot_calc_cte o\r\n"
		    		+ "    JOIN ot_policy_cte p ON p.otcategory = o.daytype\r\n"
		    		+ "        AND o.checkindate BETWEEN p.effectivefrom AND p.effectiveto\r\n"
		    		+ ")\r\n"
		    		+ "SELECT empcode, empname, checkindate,\r\n"
		    		+ "       TIME_FORMAT(intime, '%H:%i:%s') AS intime,\r\n"
		    		+ "       TIME_FORMAT(outtime, '%H:%i:%s') AS outtime,\r\n"
		    		+ "       \r\n"
		    		+ "       -- ✅ Integer OT Hours\r\n"
		    		+ "       FLOOR(\r\n"
		    		+ "           CASE\r\n"
		    		+ "               WHEN raw_ot_sec < min_sec THEN 0\r\n"
		    		+ "               WHEN ottype IN ('Hourly', 'Slab') THEN raw_ot_sec / 3600\r\n"
		    		+ "               ELSE 0\r\n"
		    		+ "           END\r\n"
		    		+ "       ) AS othours,\r\n"
		    		+ "\r\n"
		    		+ "       -- ✅ OT Amount = OT Hours * fixed rate\r\n"
		    		+ "       FLOOR(\r\n"
		    		+ "           CASE\r\n"
		    		+ "               WHEN raw_ot_sec < min_sec THEN 0\r\n"
		    		+ "               WHEN ottype IN ('Hourly', 'Slab') THEN FLOOR(raw_ot_sec / 3600) * CAST(otrate AS DECIMAL(10,2))\r\n"
		    		+ "               ELSE 0\r\n"
		    		+ "           END\r\n"
		    		+ "       ) AS otamount,\r\n"
		    		+ "\r\n"
		    		+ "       otrate AS rate, ottype, otcategory\r\n"
		    		+ "FROM ot_final_ranked\r\n"
		    		+ "WHERE rn = 1 AND raw_ot_sec >= min_sec\r\n"
		    		+ "ORDER BY checkindate, empcode;\r\n"
		    		+ "",
		    nativeQuery = true
		)
		List<Object[]> getFinalOtRecords(Long orgId);



	Optional<OtCalculationVO> findByEmpcodeAndCheckindate(String empcode, LocalDate checkindate);

}
