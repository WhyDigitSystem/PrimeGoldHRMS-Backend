package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AttendanceLogVO;

@Repository
public interface AttendanceLogRepo extends JpaRepository<AttendanceLogVO, Long> {

}
