package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AemployeeLeaveVO;

@Repository
public interface AemployeeLeaveRepo extends JpaRepository<AemployeeLeaveVO, Long>{


	List<AemployeeLeaveVO> findByAemployeeVO_EmployeeCodeAndAemployeeVO_OrgId(String employeeCode, Long orgId);



}
