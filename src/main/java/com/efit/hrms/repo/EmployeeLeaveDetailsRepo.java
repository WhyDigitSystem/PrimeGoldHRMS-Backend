package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.EmployeeLeaveDetailsVO;

@Repository
public interface EmployeeLeaveDetailsRepo extends JpaRepository<EmployeeLeaveDetailsVO, Long>{

}
