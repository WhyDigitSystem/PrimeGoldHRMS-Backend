package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.ShiftAssignVO;

@Repository
public interface ShiftAssignRepo extends JpaRepository<ShiftAssignVO, Long>{

	@Query( value = "SELECT * FROM shiftassign WHERE shiftassignid=?1",nativeQuery = true)
	ShiftAssignVO getShiftAssignById(Long id);
	
	@Query( value = "SELECT * FROM shiftassign WHERE orgid=?1",nativeQuery = true)
	List<ShiftAssignVO>  getAllShiftAssignByOrgId(Long orgId);
	
	@Query("SELECT e FROM EmployeeVO e " +
		       "WHERE e.orgId = ?1 " +
		       "AND (e.employeeType = ?2 OR (e.employeeType = ?3 AND e.contractor = ?3)) " +
		       "AND (e.department = ?4 OR ?4 = 'All')\r\n"
		       + " and e.active = true ")
		List<EmployeeVO> getShiftAssignByOrgIdAndType(Long orgId, String type, String contractor, String department);

//	@Query( value = "SELECT \r\n"
//			+ "        e.employee, e.employeecode, e.department, e.branch, \r\n"
//			+ "        s.shiftcode, s.branchcode, s.shift, s.intime, s.outtime, s.nightshift \r\n"
//			+ "    FROM employee e \r\n"
//			+ "    JOIN shiftmaster s ON s.orgid = e.orgid AND s.branchcode = e.branchcode \r\n"
//			+ "    WHERE \r\n"
//			+ "        e.orgid = ?1\r\n"
//			+ "        AND (e.type = ?2 OR (e.type = ?3 AND e.contractor = ?3))\r\n"
//			+ "        AND (e.department = ?4 OR ?4 = 'All')\r\n"
//			+ "        AND s.shift = ?5\r\n"
//			+ "        AND s.shiftcode = ?6\r\n"
//			+ "        AND s.branchcode = ?7\r\n"
//			+ "        AND e.active = true",nativeQuery = true)
//	Set<Object[]> getAllEmployeeAndShiftMasterDetails(Long orgId, String type, String contractor, String department,
//			String shift, String shiftCode, String branchCode);

	
	@Query(value = "SELECT \r\n"
	        + "    e.employee, e.employeecode, e.department, e.branch, \r\n"
	        + "    s.shiftcode, s.branchcode, s.shift, s.intime, s.outtime, s.nightshift \r\n"
	        + "FROM employee e \r\n"
	        + "JOIN shiftmaster s ON s.orgid = e.orgid AND s.branchcode = e.branchcode \r\n"
	        + "LEFT JOIN (\r\n"
	        + "    SELECT employeecode, shiftcode, MAX(effectivefrom) AS latest_effective \r\n"
	        + "    FROM shiftassigndetails \r\n"
	        + "    GROUP BY employeecode, shiftcode\r\n"
	        + ") sa ON sa.employeecode = e.employeecode AND sa.shiftcode = s.shiftcode \r\n"
	        + "WHERE \r\n"
	        + "    e.orgid = ?1 \r\n"
	        + "    AND (e.type = ?2 OR (e.type = ?3 AND (?3 IS NOT NULL AND e.contractor = ?3))) \r\n"
	        + "    AND (e.department = ?4 OR ?4 = 'All') \r\n"
	        + "    AND s.shift = ?5 \r\n"
	        + "    AND s.shiftcode = ?6 \r\n"
	        + "    AND s.branchcode = ?7 \r\n"
	        + "    AND e.active = true \r\n"
	        + "    AND (sa.latest_effective IS NULL OR sa.latest_effective < ?8)", nativeQuery = true)
	Set<Object[]> getAllEmployeeAndShiftMasterDetails(
	        Long orgId,
	        String type,
	        String contractor,
	        String department,
	        String shift,
	        String shiftCode,
	        String branchCode,
	        LocalDate effectiveFrom
	);

	@Query(value = "SELECT *\r\n"
			+ "FROM shiftassign a\r\n"
			+ "JOIN shiftassigndetails a1 ON a.shiftassignid = a1.shiftassignid\r\n"
			+ "WHERE a.orgid = ?1\r\n"
			+ "  AND a.shifttype = ?2\r\n"
			+ "  AND (a.department = ?3 OR ?3 = 'ALL')  \r\n"
			+ "  AND (?4 IS NULL OR a1.effectivefrom >= ?4)\r\n"
			+ "  AND (?5 IS NULL OR a1.effectiveto <= ?5)\r\n"
			+ "  AND (\r\n"
			+ "    (?6 = 'Contractor' AND a.type =?6 AND a.contractor = ?7)\r\n"
			+ "    OR (?6 != 'Contractor' AND (?6 = 'ALL' OR a.type = ?6))\r\n"
			+ "  )", nativeQuery = true)
	List<ShiftAssignVO> getAllShiftDetails(Long orgId, String shifttype, String department, String effectiveFrom,
			String effectiveTo, String type, String contractorName);
	

}
