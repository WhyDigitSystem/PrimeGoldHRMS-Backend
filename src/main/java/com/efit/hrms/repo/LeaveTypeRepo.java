package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.LeaveTypeVO;

public interface LeaveTypeRepo extends JpaRepository<LeaveTypeVO, Long>
{
	@Query(nativeQuery = true, value = "select * from  leavetype where orgid=?1 ")
	List<LeaveTypeVO> getAllLeaveTypeByOrgId(Long orgId);
	
	@Query(nativeQuery = true, value = "select * from leavetype where leavetypeid=?1")
	LeaveTypeVO getLeaveTypeById(Long id);

	boolean existsByLeaveTypeAndOrgId(String leaveType, Long orgId);

	boolean existsByLeaveCodeAndOrgId(String leaveCode, Long orgId);

	@Query(nativeQuery = true, value = "select * from leavetype where orgid=?1 and carryforward=1")
	List<LeaveTypeVO> findByOrgId(Long companyId);
 
	
}
