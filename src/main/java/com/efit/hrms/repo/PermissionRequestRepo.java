package com.efit.hrms.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.PermissionRequestVO;

public interface PermissionRequestRepo extends JpaRepository<PermissionRequestVO, Long>{

	@Query(nativeQuery = true,value = "select * from permissionrequest a where a.permissionrequestid=?1 ")
	PermissionRequestVO getPermissionRequestById(Long id);

	@Query(nativeQuery = true,value = "select * from permissionrequest a where a.orgid=?1 and branchcode=?2 ")
	List<PermissionRequestVO>  getAllPermissionRequestByOrgId(Long orgId, String branchCode);

	@Query(nativeQuery = true,value = "select reportingperson,reportingpersonemail,reportingpersoncode from employee a where a.orgid=?1 and a.employeecode=?2 ")
	Set<Object[]> getReportingPerson(Long orgId, String employeeCode);

	PermissionRequestVO findByOrgIdAndIdAndEmployeeCode(Long orgId, Long id, String employeeCode);

	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "    a.permissionrequestid,\r\n"
			+ "    a.branch,\r\n"
			+ "    a.branchcode,\r\n"
			+ "    a.date,\r\n"
			+ "    a.fromtime,\r\n"
			+ "    a.notes,\r\n"
			+ "    a.notify,\r\n"
			+ "    a.orgid,\r\n"
			+ "    a.screencode,\r\n"
			+ "    a.screenname,\r\n"
			+ "    a.totime,\r\n"
			+ "    a.totalhours,\r\n"
			+ "    a.employeecode,\r\n"
			+ "    a.employeename,\r\n"
			+ "    a.notifycode,\r\n"
			+ "    b.email\r\n"
			+ "FROM \r\n"
			+ "    permissionrequest a\r\n"
			+ "INNER JOIN \r\n"
			+ "    employee b \r\n"
			+ "ON \r\n"
			+ "    a.employeecode = b.employeecode\r\n"
			+ "WHERE \r\n"
			+ "    a.orgid = ?1 \r\n"
			+ "    AND a.branchcode = ?2 \r\n"
			+ "    AND a.notifycode = ?3 \r\n"
			+ "    AND a.approvestatus = 'Pending' \r\n"
			+ "")
	Set<Object[]> getPendingPermissionRequest(Long orgId, String branchCode, String employeeCode);

	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "    a.permissionrequestid,\r\n"
			+ "    a.branch,\r\n"
			+ "    a.branchcode,\r\n"
			+ "    a.date,\r\n"
			+ "    a.fromtime,\r\n"
			+ "    a.notes,\r\n"
			+ "    a.notify,\r\n"
			+ "    a.orgid,\r\n"
			+ "    a.screencode,\r\n"
			+ "    a.screenname,\r\n"
			+ "    a.totime,\r\n"
			+ "    a.totalhours,\r\n"
			+ "    a.employeecode,\r\n"
			+ "    a.employeename,\r\n"
			+ "    a.notifycode,\r\n"
			+ "    b.email\r\n"
			+ "FROM \r\n"
			+ "    permissionrequest a\r\n"
			+ "INNER JOIN \r\n"
			+ "    employee b \r\n"
			+ "ON \r\n"
			+ "    a.employeecode = b.employeecode\r\n"
			+ "WHERE \r\n"
			+ "    a.orgid = ?1 \r\n"
			+ "    AND a.branchcode = ?2 \r\n"
			+ "    AND a.notifycode = ?3 \r\n"
			+ "    AND a.approvestatus = 'APPROVED' \r\n"
			+ "")
	Set<Object[]> getApprovedPermissionRequestforTeam(Long orgId, String branchCode, String reportingPersonCode);

	
	
}
