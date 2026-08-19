package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AttendanceDailyVO;
import com.efit.hrms.entity.OtMasterVO;

@Repository
public interface OtMasterRepo extends JpaRepository<OtMasterVO, Long>{

	@Query( value = "SELECT * FROM otmaster WHERE orgid=?1",nativeQuery = true)
	List<OtMasterVO> getAllOtMasterByOrgId(Long orgId);

	@Query( value = "SELECT * FROM otmaster WHERE otmasterid=?1",nativeQuery = true)
	OtMasterVO getOtMasterById(Long id);

	@Query(value = "SELECT e.employeecode, e.employee " +
            "FROM employee e " +
            "WHERE e.orgid = ?1 " +
            "  AND (?2 = 'ALL' OR e.branch = ?2) " +
            "  AND (?3 = 'ALL' OR e.department = ?3) " +
            "  AND ( ?4 = 'ALL' " +
            "        OR (?4 = 'EMPLOYEE' AND e.type = 'EMPLOYEE') " +
            "        OR (?4 = 'CONTRACTOR' AND e.type = 'CONTRACTOR' AND (?5 IS NULL OR e.contractor = ?5)) " +
            "      )", nativeQuery = true)
List<Object[]> getEmployeeNameForApprovalOtProcess(Long orgId, String branch, String department, String type, String contractor);


    @Query(
        value =
            "WITH " +

            "report_period AS ( " +
            "    SELECT " +
            "        CAST(:fromDate AS DATE) AS from_date, " +
            "        CAST(:toDate AS DATE) AS to_date " +
            "), " +

            "company_settings AS ( " +
            "    SELECT " +
            "        c.companyid, " +
            "        c.shifthours, " +
            "        c.oteligiblehours " +
            "    FROM company c " +
            "    WHERE c.companyid = :orgId " +
            "), " +

            "attendance_raw AS ( " +
            "    SELECT " +
            "        ad.attendancedailyid, " +
            "        ad.checkindate, " +
            "        ad.checkoutdate, " +
            "        ad.empcode, " +
            "        ad.empname, " +
            "        ad.intime, " +
            "        ad.outtime, " +
            "        TIMESTAMP(ad.checkindate, ad.intime) AS in_datetime, " +
            "        TIMESTAMP(ad.checkoutdate, ad.outtime) AS out_datetime " +
            "    FROM attendancedaily ad " +
            "    CROSS JOIN report_period rp " +
            "    WHERE ad.checkindate >= rp.from_date " +
            "      AND ad.checkindate < DATE_ADD(rp.to_date, INTERVAL 1 DAY) " +
            "      AND ad.empcode IS NOT NULL " +
            "      AND TRIM(ad.empcode) <> '' " +
            "      AND ad.intime IS NOT NULL " +
            "      AND ad.outtime IS NOT NULL " +
            "      AND ad.checkindate IS NOT NULL " +
            "      AND ad.checkoutdate IS NOT NULL " +
            "), " +

            "daily_attendance AS ( " +
            "    SELECT " +
            "        ar.empcode AS employeecode, " +
            "        MAX(ar.empname) AS employeename, " +
            "        ar.checkindate AS attendancedate, " +
            "        MIN(ar.in_datetime) AS first_in, " +
            "        MAX(ar.out_datetime) AS last_out, " +
            "        TIMESTAMPDIFF( " +
            "            MINUTE, " +
            "            MIN(ar.in_datetime), " +
            "            MAX(ar.out_datetime) " +
            "        ) AS actual_work_minutes " +
            "    FROM attendance_raw ar " +
            "    GROUP BY " +
            "        ar.empcode, " +
            "        ar.checkindate " +
            "), " +

            "employee_ot AS ( " +
            "    SELECT " +
            "        e.employeecode, " +
            "        e.otflag " +
            "    FROM employee e " +
            "    WHERE e.orgid = :orgId " +
            "      AND e.otflag = 1 " +
            "), " +

            "daily_employee AS ( " +
            "    SELECT " +
            "        da.employeecode, " +
            "        da.employeename, " +
            "        da.attendancedate, " +
            "        da.first_in, " +
            "        da.last_out, " +
            "        da.actual_work_minutes, " +
            "        eo.otflag AS employee_otflag " +
            "    FROM daily_attendance da " +
            "    INNER JOIN employee_ot eo " +
            "        ON eo.employeecode = da.employeecode " +
            "), " +

            "daily_work AS ( " +
            "    SELECT " +
            "        de.employeecode, " +
            "        de.employeename, " +
            "        de.attendancedate, " +
            "        de.first_in, " +
            "        de.last_out, " +
            "        de.actual_work_minutes, " +
            "        de.employee_otflag, " +
            "        cs.shifthours, " +
            "        cs.oteligiblehours, " +
            "        ROUND(cs.shifthours * 60) AS shift_minutes, " +
            "        ROUND(de.actual_work_minutes / 60, 2) AS actual_work_hours, " +
            "        GREATEST( " +
            "            de.actual_work_minutes - ROUND(cs.shifthours * 60), " +
            "            0 " +
            "        ) AS extra_work_minutes " +
            "    FROM daily_employee de " +
            "    CROSS JOIN company_settings cs " +
            "), " +

            "daily_ot AS ( " +
            "    SELECT " +
            "        dw.employeecode, " +
            "        dw.employeename, " +
            "        dw.attendancedate, " +
            "        dw.first_in, " +
            "        dw.last_out, " +
            "        dw.actual_work_minutes, " +
            "        dw.employee_otflag, " +
            "        dw.shifthours, " +
            "        dw.oteligiblehours, " +
            "        dw.actual_work_hours, " +

            "        CASE " +
            "            WHEN dw.extra_work_minutes >= " +
            "                 (COALESCE(dw.oteligiblehours, 0) * 60) " +
            "            THEN ROUND(dw.extra_work_minutes / 60, 2) " +
            "            ELSE 0 " +
            "        END AS ot_hours, " +

            "        CASE " +
            "            WHEN dw.extra_work_minutes >= " +
            "                 (COALESCE(dw.oteligiblehours, 0) * 60) " +
            "            THEN 1 " +
            "            ELSE 0 " +
            "        END AS ot_days " +

            "    FROM daily_work dw " +
            "), " +

            "employee_monthly AS ( " +
            "    SELECT " +
            "        employeecode, " +
            "        MAX(employeename) AS employeename, " +
            "        MIN(first_in) AS first_in, " +
            "        MAX(last_out) AS last_out, " +
            "        COUNT(DISTINCT attendancedate) AS present, " +
            "        ROUND(SUM(shifthours), 2) AS shift_hours, " +
            "        ROUND(SUM(actual_work_minutes) / 60, 2) AS actual_work_hours, " +
            "        SUM(ot_days) AS ot_days, " +
            "        ROUND(SUM(ot_hours), 2) AS ot_hours, " +

            "        GROUP_CONCAT( " +
            "            CASE " +
            "                WHEN ot_days = 1 " +
            "                THEN DATE_FORMAT(attendancedate, '%Y-%m-%d') " +
            "                ELSE NULL " +
            "            END " +
            "            ORDER BY attendancedate " +
            "            SEPARATOR ', ' " +
            "        ) AS ot_dates " +

            "    FROM daily_ot " +
            "    GROUP BY employeecode " +
            ") " +

            "SELECT " +

            "    ROW_NUMBER() OVER ( " +
            "        ORDER BY em.ot_days DESC, em.employeecode ASC " +
            "    ) AS `S.No`, " +

            "    em.employeecode AS `Employee Code`, " +

            "    em.employeename AS `Employee Name`, " +

            "    DATE_FORMAT( " +
            "        em.first_in, " +
            "        '%Y-%m-%d %H:%i:%s' " +
            "    ) AS `First In`, " +

            "    DATE_FORMAT( " +
            "        em.last_out, " +
            "        '%Y-%m-%d %H:%i:%s' " +
            "    ) AS `Last Out`, " +

            "    DATEDIFF( " +
            "        rp.to_date, " +
            "        rp.from_date " +
            "    ) + 1 AS `Total Days`, " +

            "    em.present AS `Present`, " +

            "    ROUND( " +
            "        em.shift_hours, " +
            "        2 " +
            "    ) AS `Shift Hours`, " +

            "    ROUND( " +
            "        em.actual_work_hours, " +
            "        2 " +
            "    ) AS `Actual Work Hours`, " +

            "    em.ot_days AS `OT Days`, " +

            "    ROUND( " +
            "        em.ot_hours, " +
            "        2 " +
            "    ) AS `OT Hours`, " +

            "    COALESCE( " +
            "        em.ot_dates, " +
            "        '' " +
            "    ) AS `OT Dates`, " +

            "    CASE " +
            "        WHEN em.ot_days > 0 " +
            "        THEN 'TRUE' " +
            "        ELSE 'FALSE' " +
            "    END AS `OT Status` " +

            "FROM employee_monthly em " +

            "CROSS JOIN report_period rp " +

            "ORDER BY " +
            "    em.ot_days DESC, " +
            "    em.employeecode ASC",

        nativeQuery = true
    )
    List<Map<String, Object>> getOvertimeReport(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("orgId") Long orgId
    );





}
