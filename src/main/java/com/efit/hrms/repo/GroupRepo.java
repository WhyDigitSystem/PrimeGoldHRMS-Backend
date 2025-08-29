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

	
	@Query(value = "SELECT e.employeecode, e.employee,e.department" +
            " FROM employee e " +
            "WHERE e.orgid = ?1 " +
            "  AND (?2 = 'ALL' OR e.branch = ?2) " +
            "  AND (?3 = 'ALL' OR e.department = ?3) " +
            "  AND ( ?4 = 'ALL' " +
            "        OR (?4 = 'EMPLOYEE' AND e.type = 'EMPLOYEE') " +
            "        OR (?4 = 'CONTRACTOR' AND e.type = 'CONTRACTOR' AND (?5 IS NULL OR e.contractor = ?5)) " +
            "      )", nativeQuery = true)
	List<Object[]> getEmployeeNameForGroupMaster(Long orgId, String branch, String department, String type,
			String contractor);

}
