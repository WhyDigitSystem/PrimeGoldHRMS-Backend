package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AdvanceVO;
import com.efit.hrms.entity.ShiftAssignVO;

@Repository
public interface AdvanceRepo extends JpaRepository<AdvanceVO, Long> {

	@Query(nativeQuery = true, value = "select * from advance a where a.orgid=?1 and a.branchcode=?2 ")
	List<AdvanceVO> getAllAdvanceByOrgId(Long orgId, String branchCode);

	@Query(nativeQuery = true, value = "select * from advance a where a.advanceid=?1 ")
	AdvanceVO getAdvanceById(Long id);

	@Query(nativeQuery = true, value = "select employee,employeecode,department,designation from employee where orgid=?1 and active= 1")
	Set<Object[]> findEmployeeDetails(Long orgId);

//	@Query(value = "SELECT *\r\n"
//			+ "FROM shiftassign a\r\n"
//			+ "JOIN shiftassigndetails a1 ON a.shiftassignid = a1.shiftassignid\r\n"
//			+ "WHERE a.orgid = ?1\r\n"
//			+ "  AND a.shifttype = ?2\r\n"
//			+ "  AND (a.department = ?3 OR ?3 = 'ALL')  \r\n"
//			+ "  AND (?4 IS NULL OR a1.effectivefrom >= ?4)\r\n"
//			+ "  AND (?5 IS NULL OR a1.effectiveto <= ?5)\r\n"
//			+ "  AND (\r\n"
//			+ "    (?6 = 'Contractor' AND a.type =?6 AND a.contractor = ?7)\r\n"
//			+ "    OR (?6 != 'Contractor' AND (?6 = 'ALL' OR a.type = ?6))\r\n"
//			+ "  )", nativeQuery = true)
//	List<ShiftAssignVO> getAllShiftDetails(Long orgId, String shifttype, String department, LocalDate effectiveFrom,
//			LocalDate effectiveTo, String type, String contractorName);

}
