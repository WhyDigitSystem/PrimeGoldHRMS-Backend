package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efit.hrms.entity.EmployeeLeaveVO;
import com.efit.hrms.entity.EmployeeVO;

public interface EmployeeLeaveRepo extends JpaRepository<EmployeeLeaveVO, Long> {

	List<EmployeeLeaveVO> findByEmployeeVO(EmployeeVO employeeVO);



	List<EmployeeLeaveVO> findByEmployeeVO_Id(Long id);



	boolean existsByLeaveCodeAndEmployeeVO_EmployeeCodeAndEmployeeVO_OrgId(String leaveCode, String employeeCode,
			Long orgId);



	List<EmployeeLeaveVO> findByEmployeeVO_EmployeeCodeAndEmployeeVO_OrgId(String employeeCode, Long orgId); 


//	List<EmployeeLeaveVO> findByEmployeeVO(Long id);



}
