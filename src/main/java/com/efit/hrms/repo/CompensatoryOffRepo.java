package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CompensatoryOffVO;

@Repository
public interface CompensatoryOffRepo extends JpaRepository< CompensatoryOffVO, Long> {

	@Query(nativeQuery = true, value = "select * from compensatoryoff where compensatoryoffid=?1")
	CompensatoryOffVO getCompensatoryOffVOById(Long id);

	@Query(nativeQuery = true, value = "select * from compensatoryoff where orgid=?1 and employeecode=?2")
	List<CompensatoryOffVO> getCompensatoryOffByOrgId(Long orgId, String empCode);

	CompensatoryOffVO findByOrgIdAndIdAndEmployeeCode(Long orgId, Long id, String employeeCode);

	List<CompensatoryOffVO> findByBranchAndEmployeeCodeAndOrgIdAndLeaveCodeAndCompOffDate(String branch,
			String employeeCode, Long orgId, String leaveCode, LocalDate compOffDate);

	@Query(nativeQuery = true, value = "select a.employeename,a.employeecode,a.leavetype,a.compoffdate,a.totaldays,a.notes,a.compensatoryoffid,b.email,a.screenname from compensatoryoff a INNER JOIN \r\n"
			+ "employee b ON a.employeecode = b.employeecode where a.orgid=?1 and a.notifycode=?2 and a.branchcode=?3 and approvalstatus='PENDING'")
	Set<Object[]> getCompoffRequestForDashBoard(Long orgId, String reportingPersonCode, String branchCode);

	List<CompensatoryOffVO> findByOrgIdAndNotifyCode(Long orgId, String notifyCode);


}
