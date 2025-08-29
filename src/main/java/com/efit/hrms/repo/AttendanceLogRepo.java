package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.efit.hrms.entity.AttendanceLogVO;

public interface AttendanceLogRepo extends JpaRepository<AttendanceLogVO, Long> {

	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    employeecode,\r\n"
			+ "    employeename,\r\n"
			+ "    departmentname,\r\n"
			+ "    designation,\r\n"
			+ "    attendancestatus,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN outdevice = 'SE' THEN 'Yes' \r\n"
			+ "        ELSE 'No' \r\n"
			+ "    END AS misspunch,\r\n"
			+ "    DATE_FORMAT(intime, '%H:%i') AS intime,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN outdevice = 'SE' THEN '00:00'\r\n"
			+ "        ELSE DATE_FORMAT(outtime, '%H:%i')\r\n"
			+ "    END AS outtime\r\n"
			+ "FROM attendancelog \r\n"
			+ "WHERE attendancedate = ?1\r\n"
			+ "  AND departmentname = ?2\r\n"
			+ "  AND (\r\n"
			+ "        (?3 = 'Employee' AND employeecode LIKE 'PGH%')\r\n"
			+ "     OR (?3 = 'Contract' AND employeecode LIKE 'CPGH%')\r\n"
			+ "  )\r\n"
			+ "  AND (attendancestatus = ?4 OR ?4 = 'ALL')\r\n"
			+ "  AND ( (CASE WHEN outdevice = 'SE' THEN 'Yes' ELSE 'No' END) = ?5 OR ?5 = 'ALL')\r\n"
			+ "ORDER BY employeecode ASC")
	Set<Object[]> getEmployeeAttendance(String date, String department, String employeeType, String status,
			String missPunch);

	@Query(value = "\r\n" + "	        WITH departmentmapping AS (\r\n"
			+ "	            SELECT a.departmentname AS maindepartmentname,\r\n"
			+ "	                   b.departmentname AS subdepartmentname\r\n"
			+ "	            FROM maindepartment a\r\n"
			+ "	            JOIN subdepartment b ON a.maindepartmentid = b.maindepartmentid\r\n" + "	        )\r\n"
			+ "	        SELECT m.maindepartmentname, \r\n"
			+ "	               SUM(CASE WHEN a.attendancestatus = 'Present ' THEN 1 ELSE 0 END) AS present_count,\r\n"
			+ "	               SUM(CASE WHEN a.attendancestatus = 'Absent' THEN 1 ELSE 0 END) AS absent_count,\r\n"
			+ "	               SUM(CASE WHEN a.outdevice = 'SE' THEN 1 ELSE 0 END) AS miss_count\r\n"
			+ "	        FROM attendancelog a\r\n"
			+ "	        JOIN departmentmapping m ON a.departmentname = m.subdepartmentname\r\n"
			+ "	        WHERE a.attendancedate = :date\r\n" + "	          AND (\r\n"
			+ "	                (:empType = 'Employee' AND a.employeecode LIKE 'PGH%')\r\n"
			+ "	             OR (:empType = 'Contract' AND a.employeecode LIKE 'CPGH%')\r\n" + "	          )\r\n"
			+ "	        GROUP BY m.maindepartmentname\r\n" + "	        ", nativeQuery = true)
	List<Object[]> getMainDepartments(@Param("date") String date, @Param("empType") String empType);

	@Query(value = "SELECT departmentname, \r\n"
			+ "               SUM(CASE WHEN attendancestatus = 'Present ' THEN 1 ELSE 0 END) AS present_count,\r\n"
			+ "               SUM(CASE WHEN attendancestatus = 'Absent' THEN 1 ELSE 0 END) AS absent_count,\r\n"
			+ "               SUM(CASE WHEN outdevice = 'SE' THEN 1 ELSE 0 END) AS miss_count\r\n"
			+ "        FROM attendancelog\r\n"
			+ "        WHERE attendancedate = :date\r\n"
			+ "          AND (\r\n"
			+ "                (:empType = 'Employee' AND employeecode LIKE 'PGH%')\r\n"
			+ "             OR (:empType = 'Contract' AND employeecode LIKE 'CPGH%')\r\n"
			+ "          )\r\n"
			+ "        GROUP BY departmentname", nativeQuery = true)
	List<Object[]> getSubDepartments(@Param("date") String date, @Param("empType") String empType);

}
