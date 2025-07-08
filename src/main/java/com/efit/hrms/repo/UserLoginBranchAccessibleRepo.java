package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efit.hrms.entity.UserLoginBranchAccessibleVO;


public interface UserLoginBranchAccessibleRepo extends JpaRepository<UserLoginBranchAccessibleVO, Long> {

}
