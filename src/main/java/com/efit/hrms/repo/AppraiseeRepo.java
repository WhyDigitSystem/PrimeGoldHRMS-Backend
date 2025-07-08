package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AppraiseeVO;

@Repository
public interface AppraiseeRepo extends JpaRepository<AppraiseeVO, Long> {

	@Query(nativeQuery = true,value="select * from appraisee where orgid=?1")
	List<AppraiseeVO> getAppraisee(Long orgId);

	@Query(nativeQuery = true,value = "select e.employeecode,e.employee,e.designation,e.department,e.reportingperson,e.reportingpersoncode,e.reportingrole\r\n"
			+ " from employee e where e.orgid=?1 and e.employeecode=?2")
	Set<Object[]> getReportingPerson(Long orgId, String employeeCode);

	@Query(nativeQuery = true,value = "select * from appraisee where appraiseeid=?1")
	AppraiseeVO findByOrgId(Long orgId);

}
