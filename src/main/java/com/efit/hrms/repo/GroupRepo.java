package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.GroupVO;

@Repository
public interface GroupRepo extends JpaRepository<GroupVO, Long> {


	@Query(nativeQuery = true, value = "select * from groupmaster where orgid=?1")
	List<GroupVO> getGroupByOrgId(Long orgId);

	boolean existsByGroupName(String groupName);

	@Query(nativeQuery = true, value = "select * from groupmaster where orgid=?1 and groupname=?2")
	List<GroupVO> getGroupMasterByOrgIdAndGroup(Long orgId, String groupName);

	List<GroupVO> findByGroupNameAndOrgIdAndBranchCode(String groupName, Long orgId, String branchCode);

	
	@Query(value = "SELECT e.employeecode,\r\n"
			+ "       e.employee,\r\n"
			+ "       e.department\r\n"
			+ "FROM employee e\r\n"
			+ "WHERE e.orgid = ?1\r\n"
			+ "  AND (?2 = 'ALL' OR e.branch = ?2)\r\n"
			+ "  AND (?3 = 'ALL' OR e.department = ?3)\r\n"
			+ "  AND (\r\n"
			+ "        ?4 = 'ALL'\r\n"
			+ "        OR (?4 = 'EMPLOYEE' AND e.type = ?4)\r\n"
			+ "        OR (?4 = 'CONTRACTOR' AND e.type = ?4 AND (?5 IS NULL OR e.contractor = ?5)))", nativeQuery = true)
	List<Object[]> getEmployeeNameForGroupMaster(Long orgId, String branch, String department, String type,
			String contractor);
	
	

}
