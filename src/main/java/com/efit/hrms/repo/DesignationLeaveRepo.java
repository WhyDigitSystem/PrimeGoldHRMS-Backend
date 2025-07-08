package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.DesignationLeaveVO;

@Repository
public interface DesignationLeaveRepo extends JpaRepository<DesignationLeaveVO, Long>{

	@Query(nativeQuery = true, value = "select * from  designationleave where designationleaveid=?1 ")
	DesignationLeaveVO getDesignationLeaveById(Long id);

	@Query(nativeQuery = true, value = "select * from  designationleave where orgid=?1 ")
	List<DesignationLeaveVO> getDesignationLeaveByOrgId(Long orgId);

	@Query(nativeQuery = true, value = " SELECT \r\n"
			+ "    a.leavetype,\r\n"
			+ "    a.leavecode,\r\n"
			+ "    a.totalleave,\r\n"
			+ "    a.designationcode,\r\n"
			+ "    a.designation\r\n"
			+ "FROM designationleave a\r\n"
			+ "JOIN leavetype b \r\n"
			+ "    ON a.leavetype = b.leavetype"
			+ "    AND a.orgid = b.orgid  \r\n"
			+ "\r\n"
			+ "WHERE a.orgid = ?1 \r\n"
			+ "    AND a.designationcode = ?2 \r\n"
			+ "    AND (b.leaveapplicable = ?3 OR b.leaveapplicable = 'ALL') ")
	Set<Object[]> getLeaveDetailsFromDesignationLeave(Long orgId, String designationCode, String leaveApplicable);

	boolean existsByDesignationAndLeaveTypeAndOrgId(String designation, String leaveType, Long orgId);



}
