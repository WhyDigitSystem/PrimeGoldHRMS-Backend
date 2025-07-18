package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AttendanceProcessVO;

@Repository
public interface AttendanceProcessRepo extends JpaRepository<AttendanceProcessVO, Long>{

}
