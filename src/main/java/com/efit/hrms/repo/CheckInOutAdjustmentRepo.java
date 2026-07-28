package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CheckInOutAdjustmentVO;

@Repository
public interface CheckInOutAdjustmentRepo extends JpaRepository<CheckInOutAdjustmentVO, Long>{



	List<CheckInOutAdjustmentVO> findByOrgIdAndEmpCodeAndCheckInDate(Long orgId, String employeeCode,
			LocalDate localCheckInDate);

	@Query(nativeQuery = true, value = "select a.branch,a.checkindate,a.empcode,a.entrytime,a.orgid,a.empname,a.screenname,a.screencode,b.email from checkinoutadjustment a join employee b on b.employeecode=a.empcode where a.orgid=?1 and a.branch=?2 and b.reportingPersoncode=?3 and approvalstatus='PENDING' ")
	Set<Object[]> getRequestCheckInOutByOrgId(Long orgId, String branch, String reportingPersonCode);

	List<CheckInOutAdjustmentVO> findByOrgIdAndNotifyCode(Long orgId, String notifyCode);

	List<CheckInOutAdjustmentVO> findByOrgIdAndEmpCodeAndCheckInDateBetween(Long orgId, String employeeCode,
			LocalDate localCheckInDate, LocalDate plusDays);

}
