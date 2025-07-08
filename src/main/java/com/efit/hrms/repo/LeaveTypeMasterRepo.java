package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.LeaveTypeMasterVO;

@Repository
public interface LeaveTypeMasterRepo extends JpaRepository<LeaveTypeMasterVO, Long> {

	boolean existsByLeaveTypeAndOrgId(String leaveType, Long orgId);

	boolean existsByLeaveCodeAndOrgId(String leaveCode, Long orgId);

	@Query(value = "SELECT * FROM leavetypemaster where orgid=?1", nativeQuery = true)
	List<LeaveTypeMasterVO> getAllLeaveDetailsByOrgId(Long orgId);

	@Query(value = "select l.leavetype,l.leavecode from leavedetails l where orgid=?1 and active=1 group by l.leavetype,l.leavecode", nativeQuery = true)
	Set<Object[]> findLeaveCode(Long orgId);

}
