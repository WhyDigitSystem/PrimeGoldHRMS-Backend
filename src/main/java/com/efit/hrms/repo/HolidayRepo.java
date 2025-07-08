package com.efit.hrms.repo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.HolidayVO;

@Repository
public interface HolidayRepo extends JpaRepository<HolidayVO, Long>{

	@Query(nativeQuery = true,value = "select * from holidays a where a.orgid=?1 ")
	List<HolidayVO> getAllHolidayByOrgId(Long orgId);

	@Query(nativeQuery = true,value = "select * from holidays a where a.holidaysid=?1 ")
	HolidayVO getHolidayById(Long id);

	//boolean existsByOrgIdAndHolidayDateIgnoreCase(Long orgId, Date holidayDate);
	
	boolean existsByOrgIdAndHolidayDate(Long orgId, LocalDate holidayDate);

	List<HolidayVO> findByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "SELECT * FROM holidays a WHERE a.holidaydate BETWEEN ?1 AND ?2 AND a.orgid = ?3 AND a.branchcode = ?4")
	List<HolidayVO> findByFromDateAndToDateAndOrgIdAndBranchCode(String fromDate, String toDate, Long orgId, String branchCode);

	@Query(nativeQuery = true, value = "SELECT a.holidaydate,a.festival FROM holidays a WHERE a.holidaydate BETWEEN ?2 AND ?3 AND a.orgid = ?1 AND a.branchcode = ?4")
	Set<Object[]> getHolidaysForTimeSheetReport(Long orgId, LocalDate fromDate, LocalDate toDate, String branchCode);


}
