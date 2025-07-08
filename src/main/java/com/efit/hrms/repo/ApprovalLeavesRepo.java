package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.ApprovalLeavesVO;

@Repository
public interface ApprovalLeavesRepo extends JpaRepository<ApprovalLeavesVO, Long>{

	List<ApprovalLeavesVO> findByLeaveDateAndOrgIdAndEmployeeCodeAndBranchCode(LocalDate date, Long orgId,
			String employeeCode, String branchCode);
	@Query(
		    nativeQuery = true,
		    value = "SELECT " +
		            "    MIN(CASE WHEN c.status = 'IN' THEN c.entrytime END) AS checkin, " +
		            "    MAX(CASE WHEN c.status = 'OUT' THEN c.entrytime END) AS checkout, " +
		            "    CONCAT( " +
		            "        LPAD(FLOOR(TIMESTAMPDIFF(MINUTE, " +
		            "            MIN(CASE WHEN c.status = 'IN' THEN c.entrytime END), " +
		            "            MAX(CASE WHEN c.status = 'OUT' THEN c.entrytime END) " +
		            "        ) / 60), 2, '0'), ':', " +
		            "        LPAD(MOD(TIMESTAMPDIFF(MINUTE, " +
		            "            MIN(CASE WHEN c.status = 'IN' THEN c.entrytime END), " +
		            "            MAX(CASE WHEN c.status = 'OUT' THEN c.entrytime END) " +
		            "        ), 60), 2, '0') " +
		            "    ) AS totalduration, " +
		            "    CASE " +
		            "        WHEN EXISTS ( " +
		            "            SELECT 1 " +
		            "            FROM approvalleaves a " +
		            "            WHERE a.orgid = ?1 " +
		            "              AND a.leavedate = ?2 " +
		            "              AND a.employeecode = ?3 " +
		            "        ) THEN 'LEAVE' " +
		            "        ELSE 'PRESENT' " +
		            "    END AS employeestatus " +
		            "FROM checkin c " +
		            "WHERE c.orgid = ?1 " +
		            "  AND c.checkindate = ?2 " +
		            "  AND c.empcode = ?3"
		)
		Set<Object[]> getApprovedLeaveForTimeSheet(Long orgId, LocalDate date, String employeeCode);
		
		
		@Query(nativeQuery = true, value = "SELECT * FROM approvalleaves a WHERE a.leavedate BETWEEN ?1 AND ?2 AND a.orgid = ?3 AND a.branchcode = ?4 and employeecode=?5")
	List<ApprovalLeavesVO> findByFromDateAndToDateAndOrgIdAndBranchCodeAndEmployeeCode(String fromDate, String toDate,
			Long orgId, String branchCode, String empCode);
		
		@Query(nativeQuery = true, value = "SELECT a.leavedate,a.leavetype FROM approvalleaves a WHERE a.leavedate BETWEEN ?2 AND ?3 AND a.orgid = ?1 AND a.branchcode = ?4 and employeecode=?5")
		Set<Object[]> getApprovedLeaveForTimeSheetReport(Long orgId, LocalDate fromDate, LocalDate toDate,
				String branchCode, String employeeCode);


}
