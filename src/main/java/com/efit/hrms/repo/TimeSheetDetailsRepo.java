package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.TimeSheetDetailsVO;
import com.efit.hrms.entity.TimeSheetVO;

@Repository
public interface TimeSheetDetailsRepo extends JpaRepository< TimeSheetDetailsVO, Long>{

	List<TimeSheetDetailsVO> findByTimeSheetVO(TimeSheetVO timeSheetVO);
	
	@Query(value = "SELECT a.totalWorkingHours FROM attendancetime a WHERE a.empcode = :empCode AND a.entrydate = :entryDate", nativeQuery = true)
	String findTotalWorkingHoursByEmpcodeAndDate(@Param("empCode") String employeeCode, @Param("entryDate") LocalDate date);
}
