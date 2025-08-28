package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efit.hrms.entity.AttendanceLogVO;

public interface AttendanceLogRepo extends JpaRepository<AttendanceLogVO, Long> {

}
