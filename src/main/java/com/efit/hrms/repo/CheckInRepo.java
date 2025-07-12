package com.efit.hrms.repo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CheckInVO;

@Repository
public interface CheckInRepo extends JpaRepository<CheckInVO,Long>{

	Optional<CheckInVO> findTopByEmpCodeAndOrgIdAndBranchOrderByIdDesc(String empcode, long orgId, String branch);

	boolean existsByEmpCodeAndBranchAndOrgIdAndCheckInDateAndStatus(String empcode, String branch, long orgId,
			LocalDate checkInDate, String string);

	CheckInVO findTopByOrgIdAndEmpCodeAndCheckInDateAndStatusOrderByCreatedOnDesc(Long orgId, String employeeCode, LocalDate localCheckInDate, String status);

	@Query(nativeQuery = true, value = "select a.branch,a.checkindate,a.empcode,a.entrytime,a.orgid,a.empname,a.screenname,a.screencode,b.email from checkin a join employee b on b.employeecode=a.empcode where a.orgid=?1 and a.branch=?2 and b.reportingPersoncode=?3 and approvalstatus='PENDING' ")
	Set<Object[]> getRequestCheckOutByOrgId(Long orgId, String branch, String reportingPersonCode);

	
	@Query(nativeQuery = true, value = "    SELECT SUM(pending_count) AS total_pending\r\n"
			+ "FROM (\r\n"
			+ "    SELECT COUNT(*) AS pending_count \r\n"
			+ "    FROM leaverequest \r\n"
			+ "    WHERE approvestatus = 'PENDING' AND orgid = ?1 AND notifycode = ?2 AND branchcode = ?3\r\n"
			+ "\r\n"
			+ "    UNION ALL\r\n"
			+ "\r\n"
			+ "    SELECT COUNT(*) \r\n"
			+ "    FROM permissionrequest \r\n"
			+ "    WHERE approvestatus = 'PENDING' AND orgid = ?1 AND notifycode = ?2 AND branchcode = ?3\r\n"
			+ "\r\n"
			+ "    UNION ALL\r\n"
			+ "\r\n"
			+ "    SELECT COUNT(*) \r\n"
			+ "    FROM compensatoryoff \r\n"
			+ "    WHERE approvalstatus = 'PENDING' AND orgid = ?1 AND notifycode = ?2 AND branchcode = ?3\r\n"
			+ "\r\n"
			+ "    UNION ALL\r\n"
			+ "\r\n"
			+ "    SELECT COUNT(*) \r\n"
			+ "    FROM checkin a \r\n"
			+ "    JOIN employee b ON a.empcode = b.employeecode \r\n"
			+ "    WHERE a.approvalstatus = 'PENDING' AND a.orgid = ?1 AND b.reportingpersoncode = ?2 AND b.branchcode = ?3\r\n"
			+ ") AS total \r\n"
			+ " ")
	Set<Object[]> getApprovalPendingCountForDashBoard(Long orgId, String reportingPersonCode, String branchCode);

	
	@Query(nativeQuery = true, value = "select a.* from checkin a where a.checkindate=?1 and a.empcode=?2 and a.orgid=?3 and a.branch=?4 and status='OUT' ORDER BY a.createdon DESC LIMIT 1 ")
	Optional<CheckInVO> findBycheckInDateAndEmpCodeAndOrgIdAndBranchCode(String date, String empCode,
			long orgId, String branch);

	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    SUM(day_value) AS total_present_days\r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        c.checkindate,\r\n"
			+ "        TIMESTAMPDIFF(MINUTE, \r\n"
			+ "            MIN(CASE WHEN c.status = 'In' THEN c.entrytime END), \r\n"
			+ "            MAX(CASE WHEN c.status = 'Out' THEN c.entrytime END)\r\n"
			+ "        ) / 60.0 AS total_hours,\r\n"
			+ "        CASE \r\n"
			+ "            WHEN TIMESTAMPDIFF(MINUTE, \r\n"
			+ "                MIN(CASE WHEN c.status = 'In' THEN c.entrytime END), \r\n"
			+ "                MAX(CASE WHEN c.status = 'Out' THEN c.entrytime END)\r\n"
			+ "          ) / 60.0 >= oh.total_office_hours - 2 THEN 1\r\n"
			+ "            WHEN TIMESTAMPDIFF(MINUTE, \r\n"
			+ "                MIN(CASE WHEN c.status = 'In' THEN c.entrytime END), \r\n"
			+ "                MAX(CASE WHEN c.status = 'Out' THEN c.entrytime END)\r\n"
			+ "            ) / 60.0 >= oh.total_office_hours /2 - 1.5 THEN 0.5\r\n"
			+ "            ELSE 0\r\n"
			+ "        END AS day_value\r\n"
			+ "    FROM checkin c\r\n"
			+ "    CROSS JOIN (\r\n"
			+ "        SELECT \r\n"
			+ "            ROUND(TIMESTAMPDIFF(MINUTE, a.shiftin, a.shiftout) / 60.0, 1) AS total_office_hours\r\n"
			+ "        FROM company a\r\n"
			+ "        JOIN branch b ON a.companyid = b.orgid\r\n"
			+ "        WHERE a.companyid = ?3 \r\n"
			+ "          AND b.branchcode = ?4\r\n"
			+ "        LIMIT 1\r\n"
			+ "    ) AS oh\r\n"
			+ "    WHERE c.status IN ('In', 'Out')\r\n"
			+ "      AND c.checkindate BETWEEN ?1 AND ?2\r\n"
			+ "      AND c.empcode = ?5\r\n"
			+ "    GROUP BY c.checkindate, oh.total_office_hours\r\n"
			+ ") AS daily_attendance \r\n")
	Set<Object[]> getCheckInAndOutDaysForLeaveProcess(String fromDate, String toDate, Long orgId, String branchCode,
			String empCode);

	@Query("SELECT c FROM CheckInVO c WHERE c.empCode = :empCode AND c.checkInDate = :checkInDate AND c.status = :status AND c.entryTime = (" +
		       "SELECT CASE WHEN :status = 'IN' THEN MIN(c2.entryTime) ELSE MAX(c2.entryTime) END " +
		       "FROM CheckInVO c2 WHERE c2.empCode = :empCode AND c2.checkInDate = :checkInDate AND c2.status = :status)")
		Optional<CheckInVO> findMinOrMaxEntryTimeByEmpCodeAndCheckInDateAndStatus(
		    @Param("empCode") String empCode,
		    @Param("checkInDate") LocalDate checkInDate,
		    @Param("status") String status
		);

	@Query(nativeQuery = true, value = " WITH RECURSIVE DateRange AS (\r\n"
			+ "    SELECT DATE(?1) AS date\r\n"
			+ "    UNION ALL\r\n"
			+ "    SELECT DATE_ADD(date, INTERVAL 1 DAY)\r\n"
			+ "    FROM DateRange\r\n"
			+ "    WHERE date < ?2\r\n"
			+ "),\r\n"
			+ "\r\n"
			+ "WeekOffs AS (\r\n"
			+ "    SELECT \r\n"
			+ "        wo.weekoffdays,\r\n"
			+ "        wocc.weeknumber\r\n"
			+ "    FROM companyweekoff wo\r\n"
			+ "    JOIN weekoffoccurrences wocc ON wocc.companyweekoffid = wo.companyweekoffid\r\n"
			+ "    WHERE wo.companyid = ?4\r\n"
			+ "),\r\n"
			+ "\r\n"
			+ "DateWithWeekInfo AS (\r\n"
			+ "    SELECT \r\n"
			+ "        d.date,\r\n"
			+ "        DAYNAME(d.date) AS day_name,\r\n"
			+ "        (DAY(d.date) - 1) DIV 7 + 1 AS week_number\r\n"
			+ "    FROM DateRange d\r\n"
			+ "),\r\n"
			+ "\r\n"
			+ "HalfDayDates AS (\r\n"
			+ "    SELECT \r\n"
			+ "        c.checkindate AS date,\r\n"
			+ "        TIMESTAMPDIFF(MINUTE, \r\n"
			+ "          MIN(CASE WHEN c.status = 'In' THEN c.entrytime END),\r\n"
			+ "          MAX(CASE WHEN c.status = 'Out' THEN c.entrytime END)\r\n"
			+ "        ) AS worked_minutes,\r\n"
			+ "        (TIME_TO_SEC(TIMEDIFF(MAX(comp.shiftout), MIN(comp.shiftin))) / 60) AS full_shift_minutes\r\n"
			+ "    FROM checkin c\r\n"
			+ "    JOIN company comp ON comp.companyid = c.orgid\r\n"
			+ "    LEFT JOIN approvalleaves al \r\n"
			+ "      ON al.employeecode = c.empcode \r\n"
			+ "     AND al.leavedate = c.checkindate\r\n"
			+ "    WHERE c.empcode = ?3\r\n"
			+ "      AND c.checkindate BETWEEN ?1 AND ?2\r\n"
			+ "      AND EXISTS (\r\n"
			+ "          SELECT 1 FROM checkin ci \r\n"
			+ "          WHERE ci.empcode = c.empcode \r\n"
			+ "            AND ci.checkindate = c.checkindate \r\n"
			+ "            AND ci.status = 'In'\r\n"
			+ "      )\r\n"
			+ "      AND EXISTS (\r\n"
			+ "          SELECT 1 FROM checkin co \r\n"
			+ "          WHERE co.empcode = c.empcode \r\n"
			+ "            AND co.checkindate = c.checkindate \r\n"
			+ "            AND co.status = 'Out'\r\n"
			+ "      )\r\n"
			+ "    GROUP BY c.checkindate, comp.shiftin, comp.shiftout\r\n"
			+ "    HAVING\r\n"
			+ "      worked_minutes < full_shift_minutes / 2\r\n"
			+ "      AND (MAX(al.approvestatus) = 'PENDING' OR MAX(al.leavedate) IS NULL)\r\n"
			+ ")\r\n"
			+ "\r\n"
			+ "SELECT date FROM HalfDayDates\r\n"
			+ "\r\n"
			+ "UNION\r\n"
			+ "\r\n"
			+ "SELECT d.date\r\n"
			+ "FROM DateWithWeekInfo d\r\n"
			+ "LEFT JOIN checkin c_in \r\n"
			+ "  ON c_in.empcode = ?3 \r\n"
			+ " AND c_in.checkindate = d.date \r\n"
			+ " AND c_in.status = 'In'\r\n"
			+ "LEFT JOIN checkin c_out \r\n"
			+ "  ON c_out.empcode = ?3 \r\n"
			+ " AND c_out.checkindate = d.date \r\n"
			+ " AND c_out.status = 'Out'\r\n"
			+ "LEFT JOIN approvalleaves al \r\n"
			+ "  ON al.employeecode = ?3\r\n"
			+ " AND al.leavedate = d.date\r\n"
			+ "LEFT JOIN holidays h \r\n"
			+ "  ON h.holidaydate = d.date\r\n"
			+ "LEFT JOIN WeekOffs wo \r\n"
			+ "  ON wo.weekoffdays = d.day_name \r\n"
			+ " AND (wo.weeknumber = -1 OR wo.weeknumber = d.week_number)\r\n"
			+ "WHERE (c_in.empcode IS NULL OR c_out.empcode IS NULL)\r\n"
			+ "  AND al.employeecode IS NULL\r\n"
			+ "  AND h.holidaydate IS NULL\r\n"
			+ "  AND wo.weekoffdays IS NULL\r\n"
			+ "\r\n"
			+ "ORDER BY date \r\n"
			+ "")
	List<String> findByFromDateAndToDateAndEmpCodeAndOrgId(String fromDate, String toDate, String empCode, Long orgId);

	@Query(value = "SELECT \r\n"
			+ "    empcode,\r\n"
			+ "    empname,\r\n"
			+ "    DATE_FORMAT(checkindate, '%d-%m-%Y') AS entrydate,\r\n"
			+ "\r\n"
			+ "    -- Subquery to get first check-in time\r\n"
			+ "    (\r\n"
			+ "        SELECT entrytime \r\n"
			+ "        FROM checkin c1 \r\n"
			+ "        WHERE c1.status = 'IN' \r\n"
			+ "          AND c1.empcode = c.empcode \r\n"
			+ "          AND c1.checkindate = c.checkindate \r\n"
			+ "          AND c1.orgid = c.orgid \r\n"
			+ "          AND c1.branch = c.branch \r\n"
			+ "        ORDER BY c1.createdon ASC \r\n"
			+ "        LIMIT 1\r\n"
			+ "    ) AS checkintime,\r\n"
			+ "\r\n"
			+ "    -- Subquery to get last check-out time\r\n"
			+ "    (\r\n"
			+ "        SELECT entrytime \r\n"
			+ "        FROM checkin c2 \r\n"
			+ "        WHERE c2.status = 'OUT' \r\n"
			+ "          AND c2.empcode = c.empcode \r\n"
			+ "          AND c2.checkindate = c.checkindate \r\n"
			+ "          AND c2.orgid = c.orgid \r\n"
			+ "          AND c2.branch = c.branch \r\n"
			+ "        ORDER BY c2.createdon DESC \r\n"
			+ "        LIMIT 1\r\n"
			+ "    ) AS checkouttime,\r\n"
			+ "\r\n"
			+ "    -- Gross hours\r\n"
			+ "    CAST(TIMEDIFF(\r\n"
			+ "        (\r\n"
			+ "            SELECT entrytime \r\n"
			+ "            FROM checkin c2 \r\n"
			+ "            WHERE c2.status = 'OUT' \r\n"
			+ "              AND c2.empcode = c.empcode \r\n"
			+ "              AND c2.checkindate = c.checkindate \r\n"
			+ "              AND c2.orgid = c.orgid \r\n"
			+ "              AND c2.branch = c.branch \r\n"
			+ "            ORDER BY c2.createdon DESC \r\n"
			+ "            LIMIT 1\r\n"
			+ "        ),\r\n"
			+ "        (\r\n"
			+ "            SELECT entrytime \r\n"
			+ "            FROM checkin c1 \r\n"
			+ "            WHERE c1.status = 'IN' \r\n"
			+ "              AND c1.empcode = c.empcode \r\n"
			+ "              AND c1.checkindate = c.checkindate \r\n"
			+ "              AND c1.orgid = c.orgid \r\n"
			+ "              AND c1.branch = c.branch \r\n"
			+ "            ORDER BY c1.createdon ASC \r\n"
			+ "            LIMIT 1\r\n"
			+ "        )\r\n"
			+ "    ) AS CHAR) AS grosshours\r\n"
			+ "\r\n"
			+ "FROM checkin c\r\n"
			+ "WHERE c.orgid = ?1\r\n"
			+ "  AND c.branch = ?5\r\n"
			+ "  AND c.checkindate BETWEEN ?3 AND ?4  -- ?3 = fromDate, ?4 = toDate\r\n"
			+ "  AND (?2 = 'ALL' OR c.empcode = ?2)\r\n"
			+ "GROUP BY c.empcode, c.empname, c.checkindate\r\n"
			+ "ORDER BY c.checkindate ASC, c.empcode;\r\n"
			+ "",
	        nativeQuery = true)
	Set<Object[]> getCheckInOutReport(Long orgId, String employeeCode, String fromDate, String toDate, String branch);

	List<CheckInVO> findByOrgIdAndNotifyCodeAndStatus(Long orgId, String notifyCode, String string);

	boolean existsByEmpCodeAndCheckInDateAndEntryTimeAndStatus(String empCode, LocalDate checkInDate,
			LocalTime entryTime, String status);




//	Optional<CheckInVO> findTopByEmpCodeAndOrgIdAndBranchOrderByIdDesc(String empcode, long orgId, String branch);



}
