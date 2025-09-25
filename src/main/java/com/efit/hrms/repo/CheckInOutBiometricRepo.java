package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CheckInOutBiometricVO;

@Repository
public interface CheckInOutBiometricRepo extends JpaRepository<CheckInOutBiometricVO, Long>{


	@Query("SELECT c.empCode, c.checkInDate, c.entryTime " +
		       "FROM CheckInOutBiometricVO c " +
		       "WHERE c.checkInDate BETWEEN :fromDate AND :toDate " +
		       "AND c.orgId = :orgId " +
		       "AND c.branchCode = :branchCode")
		List<Object[]> findKeysByDateRange(@Param("fromDate") LocalDate fromDate,
		                                   @Param("toDate") LocalDate toDate,
		                                   @Param("orgId") Long orgId,
		                                   @Param("branchCode") String branchCode);


//	boolean existsByEmpCodeAndCheckInDateAndEntryTime(String employeeCode, LocalDate inDate, LocalTime inTime);



//	Optional<CheckInOutBiometricVO> findTopByEmpCodeAndOrgIdAndBranchOrderByIdDesc(String empcode, long orgId, String branch);

}
