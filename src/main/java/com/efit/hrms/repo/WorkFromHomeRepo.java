package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.WorkFromHomeVO;

@Repository
public interface WorkFromHomeRepo extends JpaRepository<WorkFromHomeVO, Long>{

	@Query(nativeQuery = true, value = "select * from workfromhome where workfromhomeid=?1")
	WorkFromHomeVO getWorkFromHomeById(Long id);

	@Query(nativeQuery = true, value = "select * from workfromhome where orgid=?1")
	List<WorkFromHomeVO> getWorkFromHomeByOrgId(Long orgId);

	WorkFromHomeVO findByOrgIdAndEmployeeCodeAndId(Long orgId, String employeeCode, Long id);

	@Query(nativeQuery = true, value = "select a.employeename,a.employeecode,a.wfhdate,a.workaccomplished,a.departmenthead,a.departmentheadcode,a.departmentheademail,a.screenname,a.reportingmanager,a.reportingmanagercode,a.reportingmanageremail,a.reason,a.finyear,a.orgid,a.employeeemail,a.workfromhomeid from workfromhome a INNER JOIN \r\n"
			+ "			employee b ON a.employeecode = b.employeecode where a.orgid=?1 and a.reportingmanagercode=?2 and a.branchcode=?3 and a.approvestatus='PENDING'")
	Set<Object[]> getPendingWorkFromHomeForDashBoard(Long orgId, String reportingPersonCode, String branchCode);

}
