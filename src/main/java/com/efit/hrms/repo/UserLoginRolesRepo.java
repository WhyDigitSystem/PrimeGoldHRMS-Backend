package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efit.hrms.entity.UserLoginRolesVO;
import com.efit.hrms.entity.UserVO;


public interface UserLoginRolesRepo extends JpaRepository<UserLoginRolesVO, Long> {

	List<UserLoginRolesVO> findByUserVO(UserVO userVO);

	UserLoginRolesVO findByUserVO_EmployeeCodeAndUserVO_OrgId(String employeeCode, Long orgId);

}