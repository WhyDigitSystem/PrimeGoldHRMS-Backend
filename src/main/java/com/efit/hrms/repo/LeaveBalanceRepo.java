package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.LeaveBalanceVO;

@Repository
public interface LeaveBalanceRepo extends JpaRepository<LeaveBalanceVO, Long>{

	List<LeaveBalanceVO> findByEmployeeCode(String employeeCode);
//
//	@Query(value ="SELECT employeecode, employee, \r\n"
//			+ "orgid, leavetype, leavecode, SUM(totalleave) AS totalleave FROM leavebalance GROUP BY \r\n"
//			+ " employeecode, employee, orgid, leavetype, leavecode"
//			+ "",nativeQuery =true)
//	List<LeaveBalanceVO> findLeaveBalanceVO();

//	@Query(value = "SELECT employeecode, employee, orgid, leavetype, leavecode, SUM(totalleave) AS totalleave,branch,branchcode "
//            + "FROM leavebalance "
//            + "GROUP BY employeecode, employee, orgid, leavetype, leavecode,branch,branchcode", nativeQuery = true)
//    List<Object[]> findBalanceLeaveRaw();
	
//	@Query(value = "SELECT employeecode, employee, orgid, leavetype, leavecode, " +
//            "CAST(SUM(totalleave) AS SIGNED) AS total_leave_balance, " +
//            "branch, branchcode " +
//            "FROM leavebalance " +
//            "GROUP BY employeecode, employee, orgid, leavetype, leavecode, branch, branchcode " +
//            "HAVING SUM(totalleave) <> 0", 
//    nativeQuery = true)
//List<Object[]> findBalanceLeaveRaw();




	List<LeaveBalanceVO> findByEmployeeCodeAndOrgId(String employeeCode, Long orgId);
//
//	List<LeaveBalanceVO> findBalanceLeaveRawByOrgId(Long companyId);

	@Query(value = "SELECT employeecode, employee, orgid, leavetype, leavecode, \r\n"
			+ "       CAST(SUM(CAST(totalleave AS DECIMAL(10,2))) AS DECIMAL(10,2)) AS total_leave_balance, \r\n"
			+ "       branch, branchcode \r\n"
			+ "FROM leavebalance \r\n"
			+ "WHERE orgid = ?1 \r\n"
			+ "GROUP BY employeecode, employee, orgid, leavetype, leavecode, branch, branchcode \r\n"
			+ "HAVING SUM(CAST(totalleave AS DECIMAL(10,2))) <> 0",
    nativeQuery = true)
	List<Object[]> findBalanceLeaveRawByOrgId(Long companyId);





}
