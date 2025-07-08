package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.TravelRequestVO;

@Repository
public interface TravelRequestRepo extends JpaRepository<TravelRequestVO, Long> {

	@Query(nativeQuery = true, value = "select * from travelrequest where travelrequestid=?1")
	TravelRequestVO getTravelRequestById(Long id);

	@Query(nativeQuery = true, value = "select * from travelrequest where orgid=?1")
	List<TravelRequestVO> getTravelRequestByOrgId(Long orgId);

	TravelRequestVO findByOrgIdAndEmployeeCodeAndId(Long orgId, String employeeCode, Long id);

	@Query(nativeQuery = true, value = "select a.employeecode,a.employeename,a.approvingauthorities,a.fromdate,a.modeoftravel,a.todate,a.travelreason,a.screenname,a.screencode,a.approvingauthoritiescode,a.approvingauthoritiesemail,a.finyear,a.orgid,a.employeeemail,id from travelrequest a INNER JOIN \r\n"
			+ "			employee b ON a.employeecode = b.employeecode where a.orgid=?1 and a.approvingauthoritiescode=?2 and a.branchcode=?3 and a.approvestatus='PENDING'")
	Set<Object[]> getPendingTravelRequestForDashBoard(Long orgId, String reportingPersonCode, String branchCode);

}
