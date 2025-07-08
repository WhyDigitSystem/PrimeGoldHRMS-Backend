package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.UserActionVO;



@Repository
public interface UserActionRepo extends JpaRepository<UserActionVO, Long>{

}
