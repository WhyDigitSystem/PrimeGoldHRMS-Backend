package com.efit.hrms.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AttendanceDailyVO;

@Repository
public interface AttendanceDailyRepo extends JpaRepository<AttendanceDailyVO, Long>{

//	AttendanceDailyVO findByEmpCodeAndCheckInDateAndOrgIdAndBranchAndStatus(String empcode, LocalDate today, long orgId,
//			String branch, String string);

	AttendanceDailyVO findByEmpCodeAndCheckInDateAndOrgIdAndBranch(String empcode, LocalDate today, long orgId,
			String branch);

}
