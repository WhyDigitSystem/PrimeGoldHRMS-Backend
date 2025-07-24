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
		    value = "WITH company_cte AS ( " +
		            "    SELECT * FROM company WHERE companyid = ?1 " +
		            "), " +
		            "shift_cte AS ( " +
		            "    SELECT sd.employeecode, " +
		            "           TIME_TO_SEC(TIMEDIFF(STR_TO_DATE(sd.outtime, '%H:%i'), STR_TO_DATE(sd.intime, '%H:%i'))) AS working_sec, " +
		            "           sd.effectivefrom, sd.effectiveto " +
		            "    FROM shiftassigndetails sd " +
		            "    JOIN shiftassign sa ON sd.shiftassignid = sa.shiftassignid " +
		            "    WHERE sa.orgid = ?1 AND sd.active = 1 " +
		            "), " +
		            "attendance_cte AS ( " +
		            "    SELECT ap.empcode, ap.empname, ap.branch, ap.branchcode, ap.checkindate, " +
		            "           MIN(CASE WHEN LOWER(ap.status) = 'in' THEN ap.entrytime END) AS intime, " +
		            "           MAX(CASE WHEN LOWER(ap.status) = 'out' THEN ap.entrytime END) AS outtime " +
		            "    FROM attendanceprocess ap " +
		            "    JOIN company_cte c ON ap.orgid = c.companyid " +
		            "    JOIN employee e ON ap.empcode = e.employeecode " +
		            "    WHERE ap.orgid = ?1 " +
		            "      AND FIND_IN_SET(ap.attendancemode, c.attendancemode) " +
		            "      AND c.otflag = 1 " +
		            "      AND e.otflag = 1 " +
		            "      AND ( " +
		            "          c.ottype = 'ALL' OR " +
		            "          (c.ottype = 'EMPLOYEE' AND LOWER(e.type) = 'employee') OR " +
		            "          (c.ottype = 'CONTRACTOR' AND LOWER(e.type) = 'contractor') " +
		            "      ) " +
		            "      AND ap.checkindate BETWEEN DATE_FORMAT(CURDATE(), '%Y-%m-01') AND CURDATE() " +
		            "    GROUP BY ap.empcode, ap.empname, ap.branch, ap.branchcode, ap.checkindate " +
		            "), " +
		            "daytype_cte AS ( " +
		            "    SELECT ds.empcode, ds.checkindate, " +
		            "           CASE " +
		            "               WHEN h.holidaydate IS NOT NULL THEN 'Holiday' " +
		            "               WHEN EXISTS ( " +
		            "                   SELECT 1 FROM companyweekoff cw " +
		            "                   WHERE cw.companyid = ?1 AND UPPER(cw.weekoffdays) = UPPER(DAYNAME(ds.checkindate)) " +
		            "               ) THEN 'Weekly-Off' " +
		            "               ELSE 'Regular' " +
		            "           END AS daytype " +
		            "    FROM attendance_cte ds " +
		            "    LEFT JOIN holidays h ON h.orgid = ?1 AND h.holidaydate = ds.checkindate " +
		            "), " +
		            "ot_policy_cte AS ( " +
		            "    SELECT om.otmasterid, om.otcategory, om.ottype, " +
		            "           omd.minhours * 3600 AS min_sec, " +
		            "           omd.maxhours * 3600 AS max_sec, " +
		            "           omd.otrate, omd.slab, omd.effectivefrom, omd.effectiveto " +
		            "    FROM otmaster om " +
		            "    JOIN otmasterdetails omd ON omd.otmasterid = om.otmasterid " +
		            "    WHERE om.orgid = ?1 AND om.active = 1 AND omd.applicable = 1 " +
		            "), " +
		            "combined_cte AS ( " +
		            "    SELECT a.empcode, a.empname, a.branch, a.branchcode, a.checkindate, " +
		            "           STR_TO_DATE(a.intime, '%H:%i:%s') AS intime, " +
		            "           STR_TO_DATE(a.outtime, '%H:%i:%s') AS outtime, " +
		            "           TIME_TO_SEC(TIMEDIFF( " +
		            "               COALESCE(STR_TO_DATE(a.outtime, '%H:%i:%s'), '00:00:00'), " +
		            "               COALESCE(STR_TO_DATE(a.intime, '%H:%i:%s'), '00:00:00') " +
		            "           )) AS work_sec, " +
		            "           sft.working_sec, d.daytype " +
		            "    FROM attendance_cte a " +
		            "    LEFT JOIN shift_cte sft ON sft.employeecode = a.empcode " +
		            "        AND a.checkindate BETWEEN sft.effectivefrom AND sft.effectiveto " +
		            "    JOIN daytype_cte d ON d.empcode = a.empcode AND d.checkindate = a.checkindate " +
		            "), " +
		            "ot_calc_cte AS ( " +
		            "    SELECT c.*, COALESCE(c.work_sec - COALESCE(c.working_sec, 0), 0) AS raw_ot_sec " +
		            "    FROM combined_cte c " +
		            "), " +
		            "ot_final_ranked AS ( " +
		            "    SELECT o.*, p.otcategory, p.ottype, p.min_sec, p.max_sec, p.otrate, p.slab, " +
		            "           ROW_NUMBER() OVER (PARTITION BY o.empcode, o.checkindate ORDER BY p.min_sec DESC) AS rn " +
		            "    FROM ot_calc_cte o " +
		            "    JOIN ot_policy_cte p ON p.otcategory = o.daytype " +
		            "        AND o.checkindate BETWEEN p.effectivefrom AND p.effectiveto " +
		            ") " +
		            "SELECT empcode, empname, checkindate, " +
		            "       TIME_FORMAT(intime, '%H:%i:%s') AS intime, " +
		            "       TIME_FORMAT(outtime, '%H:%i:%s') AS outtime, " +
		            "       FLOOR( " +
		            "           CASE " +
		            "               WHEN raw_ot_sec < min_sec THEN 0 " +
		            "               WHEN ottype = 'Hourly' OR ottype = 'Slab' THEN raw_ot_sec / 3600 " +
		            "               ELSE 0 " +
		            "           END " +
		            "       ) AS othours, " +
		            "       FLOOR( " +
		            "           CASE " +
		            "               WHEN raw_ot_sec < min_sec THEN 0 " +
		            "               WHEN ottype = 'Hourly' OR ottype = 'Slab' THEN FLOOR(raw_ot_sec / 3600) * CAST(otrate AS DECIMAL) " +
		            "               ELSE 0 " +
		            "           END " +
		            "       ) AS otamount, " +
		            "       otrate AS rate, ottype, otcategory " +
		            "FROM ot_final_ranked " +
		            "WHERE rn = 1 AND raw_ot_sec >= min_sec " +
		            "ORDER BY checkindate, empcode",
		    nativeQuery = true
		)
		List<Object[]> getFinalOtRecords(Long orgId);



	Optional<OtCalculationVO> findByEmpcodeAndCheckindate(String empcode, LocalDate checkindate);

}
