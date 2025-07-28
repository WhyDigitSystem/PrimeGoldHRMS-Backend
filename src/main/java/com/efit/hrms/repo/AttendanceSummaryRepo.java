package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AttendanceSummaryVO;

@Repository
public interface AttendanceSummaryRepo extends JpaRepository<AttendanceSummaryVO, Long>{

}
