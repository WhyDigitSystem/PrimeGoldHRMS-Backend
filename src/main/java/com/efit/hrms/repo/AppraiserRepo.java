package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AppraiseeVO;
import com.efit.hrms.entity.AppraiserVO;

@Repository
public interface AppraiserRepo extends JpaRepository<AppraiserVO, Long> {

	@Query(nativeQuery = true, value = "select * from appraiser where orgid=?1")
	List<AppraiserVO> getAppraiser(Long orgId);

	@Query(nativeQuery = true, value = "select e.reportingperson,e.reportingpersoncode,e.reportingrole from employee e where\r\n"
			+ " e.orgid=?1 group by e.reportingperson,e.reportingpersoncode,e.reportingrole")
	Set<Object[]> getReporting(Long orgId);

	@Query(nativeQuery = true, value = "select e.employeecode,e.employee,e.department,e.designation from employee e where \r\n"
			+ "e.reportingpersoncode=?2 and \r\n"
			+ "e.orgid=?1 group by e.employeecode,e.employee,e.department,e.designation")
	Set<Object[]> getEmp(Long orgId, String reportingPersonCode);

	@Query(nativeQuery = true, value = "select d.area,d.goals,d.keyperformanceindicator,d.remarks from appraisee a join appraiseedetails d on a.appraiseeid=d.appraiseeid\r\n"
			+ "  where a.code=?2 and a.orgid=?1 group by d.area,d.goals,d.keyperformanceindicator,d.remarks")
	Set<Object[]> getAppraiserFillGrid(Long orgId, String empCode);

}
