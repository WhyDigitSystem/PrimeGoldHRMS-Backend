package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.efit.hrms.entity.AttendanceLogVO;

public interface AttendanceLogRepo extends JpaRepository<AttendanceLogVO, Long> {

	@Query(nativeQuery = true, value = "SELECT\r\n"
			+ "    a.employeecode,\r\n"
			+ "    a.employeename,\r\n"
			+ "    b.SubDepartment,\r\n"
			+ "    a.designation,\r\n"
			+ "    a.attendancestatus,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN a.outdevice = 'SE' THEN 'Yes' \r\n"
			+ "        ELSE 'No' \r\n"
			+ "    END AS misspunch,\r\n"
			+ "    DATE_FORMAT(a.intime, '%H:%i') AS intime,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN a.outdevice = 'SE' THEN '00:00'\r\n"
			+ "        ELSE DATE_FORMAT(a.outtime, '%H:%i')\r\n"
			+ "    END AS outtime\r\n"
			+ "FROM attendancelog a,employeemaster b\r\n"
			+ "WHERE a.employeecode=b.employeecode and a.attendancedate = ?1\r\n"
			+ "  AND b.SubDepartment=?2\r\n"
			+ "  AND (\r\n"
			+ "        (?3 = 'Employee' AND a.employeecode LIKE 'PGH%')\r\n"
			+ "     OR (?3 = 'Contract' AND a.employeecode LIKE 'CPGH%')\r\n"
			+ "  )\r\n"
			+ "  AND (a.attendancestatus = ?4 OR ?4 = 'ALL')\r\n"
			+ "  AND ((CASE WHEN a.outdevice = 'SE' THEN 'Yes' ELSE 'No' END) = ?5 OR ?5 = 'ALL')\r\n"
			+ "ORDER BY a.employeecode, DATE_FORMAT(a.intime, '%H:%i') ASC")
	Set<Object[]> getEmployeeAttendance(String date, String department, String employeeType, String status,
			String missPunch);

	@Query(value = "SELECT m.Department, \r\n"
			+ "       SUM(CASE WHEN a.attendancestatus = 'Present' THEN 1 ELSE 0 END) AS present_count,\r\n"
			+ "       SUM(CASE WHEN a.attendancestatus = 'Absent' THEN 1 ELSE 0 END) AS absent_count,\r\n"
			+ "       SUM(CASE WHEN a.outdevice = 'SE' THEN 1 ELSE 0 END) AS miss_count\r\n"
			+ "FROM  attendancelog a ,employeemaster m where a.employeecode=m.employeecode\r\n"
			+ "      AND a.attendancedate = ?1\r\n"
			+ "      AND (\r\n"
			+ "            (?2 = 'Employee' AND a.employeecode LIKE 'PGH%')\r\n"
			+ "         OR (?2 = 'Contract' AND a.employeecode LIKE 'CPGH%')\r\n"
			+ "      )\r\n"
			+ "GROUP BY m.Department", nativeQuery = true)
	List<Object[]> getMainDepartments(@Param("date") String date, @Param("empType") String empType);

	@Query(value = "SELECT b.SubDepartment,\r\n"
			+ "       SUM(CASE WHEN a.attendancestatus = 'Present' THEN 1 ELSE 0 END) AS present_count,\r\n"
			+ "       SUM(CASE WHEN a.attendancestatus = 'Absent' THEN 1 ELSE 0 END) AS absent_count,\r\n"
			+ "       SUM(CASE WHEN a.outdevice = 'SE' THEN 1 ELSE 0 END) AS miss_count\r\n"
			+ "FROM attendancelog a\r\n"
			+ "JOIN employeemaster b ON a.employeecode = b.Employeecode\r\n"
			+ "WHERE a.attendancedate = ?1\r\n"
			+ "  AND (\r\n"
			+ "        (?2 = 'Employee' AND a.employeecode LIKE 'PGH%')\r\n"
			+ "     OR (?2 = 'Contract' AND a.employeecode LIKE 'CPGH%')\r\n"
			+ "      )\r\n"
			+ "GROUP BY b.SubDepartment", nativeQuery = true)
	List<Object[]> getSubDepartments(@Param("date") String date, @Param("empType") String empType);

	@Modifying
    @Transactional
    @Query(value = "DELETE FROM attendancelog " +
                   "WHERE attendancedate=?1", nativeQuery = true)
	void deleteByAttendanceDate(String date);

	
	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    m.team,\r\n"
			+ "    SUM(CASE WHEN a.attendancestatus = 'Present' THEN 1 ELSE 0 END) AS present_count,\r\n"
			+ "    SUM(CASE WHEN a.attendancestatus = 'Absent' THEN 1 ELSE 0 END) \r\n"
			+ "        + SUM(CASE WHEN a.attendancestatus IS NULL THEN 1 ELSE 0 END) AS absent_count,\r\n"
			+ "    SUM(CASE WHEN a.outdevice = 'SE' THEN 1 ELSE 0 END) AS miss_count\r\n"
			+ "FROM employeemaster m\r\n"
			+ "LEFT JOIN attendancelog a \r\n"
			+ "       ON a.employeecode = m.EmployeeCode\r\n"
			+ "      AND a.attendancedate = ?1\r\n"
			+ "WHERE m.team NOT IN ('Default', '')\r\n"
			+ "  AND ?2 = 'Contract'       -- ✅ Instead of 'Contract' = 'Contract'\r\n"
			+ "  AND m.EmployeeCode LIKE 'CPGH%'\r\n"
			+ "GROUP BY m.team")
	List<Object[]> getContractMainDepartments(String date, String empType);

	@Query(nativeQuery = true, value = "SELECT b.SubDepartment,\r\n"
			+ "       SUM(CASE WHEN a.attendancestatus = 'Present' THEN 1 ELSE 0 END) AS present_count,\r\n"
			+ "       SUM(CASE WHEN a.attendancestatus = 'Absent' THEN 1 ELSE 0 END) AS absent_count,\r\n"
			+ "       SUM(CASE WHEN a.outdevice = 'SE' THEN 1 ELSE 0 END) AS miss_count,b.Team\r\n"
			+ "FROM attendancelog a\r\n"
			+ "JOIN employeemaster b ON a.employeecode = b.employeecode\r\n"
			+ "WHERE a.attendancedate = ?1 and b.team=?2\r\n"
			+ "  AND 'Contract' = 'Contract' AND a.employeecode LIKE 'CPGH%'\r\n"
			+ "and  b.team not in('Default','')\r\n"
			+ "GROUP BY b.SubDepartment,b.team")
	List<Object[]> getContractorSubDepartments(String date, String mainDept);

	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    b.employeecode,\r\n"
			+ "    b.employeename,\r\n"
			+ "    b.SubDepartment,\r\n"
			+ "    b.designation,\r\n"
			+ "    a.attendancestatus,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN a.outdevice = 'SE' THEN 'Yes'\r\n"
			+ "        ELSE 'No'\r\n"
			+ "    END AS misspunch,\r\n"
			+ "    DATE_FORMAT(a.intime, '%H:%i') AS intime,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN a.outdevice = 'SE' THEN '00:00'\r\n"
			+ "        ELSE DATE_FORMAT(a.outtime, '%H:%i')\r\n"
			+ "    END AS outtime,\r\n"
			+ "    b.team\r\n"
			+ "FROM employeemaster b\r\n"
			+ "LEFT JOIN attendancelog a \r\n"
			+ "       ON a.employeecode = b.employeecode\r\n"
			+ "      AND a.attendancedate = ?1\r\n"
			+ "WHERE b.SubDepartment = ?2\r\n"
			+ "  AND b.team = ?5\r\n"
			+ "  AND b.employeecode LIKE 'CPGH%'\r\n"
			+ "  AND (a.attendancestatus = ?3 OR ?3 = 'ALL')\r\n"
			+ "  AND ((CASE WHEN a.outdevice = 'SE' THEN 'Yes' ELSE 'No' END) = ?4 OR ?4 = 'ALL')\r\n"
			+ "ORDER BY b.employeecode, intime ASC")
	Set<Object[]> getEmployeeAttendanceContractor(String date, String department, String status,
			String missPunch, String mainDepartment);

	@Query(nativeQuery = true, value = "SELECT * FROM attendancelog a WHERE a.attendanceDate BETWEEN ?1 AND ?2")
	List<AttendanceLogVO> findByAttendanceDateBetween(LocalDate fromDate, LocalDate toDate);



}
