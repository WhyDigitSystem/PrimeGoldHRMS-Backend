package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AemployeeLeaveVO;

@Repository
public interface AemployeeLeaveRepo extends JpaRepository<AemployeeLeaveVO, Long>{

}
