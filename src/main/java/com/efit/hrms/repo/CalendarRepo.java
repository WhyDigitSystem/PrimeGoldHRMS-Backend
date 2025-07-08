package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CalendarVO;

@Repository
public interface CalendarRepo extends JpaRepository<CalendarVO, Long>{

	@Query(nativeQuery = true, value = "SELECT * FROM calendar a WHERE a.orgid = ?1 AND a.branchcode = ?2 AND a.empcode = ?3 ORDER BY a.eventdate ASC, a.fromtime ASC")
	List<CalendarVO> getAllCalendarByOrgId(Long orgId, String branchCode, String empCode);

	@Query(nativeQuery = true,value = "select * from calendar a where a.calendarid=?1")
	CalendarVO getCalendarById(Long id);

	@Query(nativeQuery = true,value = "SELECT *\r\n"
			+ "FROM calendar a\r\n"
			+ "WHERE a.eventdate = CURDATE()\r\n"
			+ "  AND  CURTIME() BETWEEN  (CAST(a.fromtime AS TIME) - INTERVAL 10 MINUTE) \r\n"
			+ "  AND CAST(a.fromtime AS TIME) and a.orgid=?1 and a.branchcode=?2 and a.empcode=?3 \r\n"
			+ "  union\r\n"
			+ "SELECT *\r\n"
			+ "FROM calendar a\r\n"
			+ "WHERE a.eventdate = CURDATE()\r\n"
			+ "  AND TIME_FORMAT(CAST(a.fromtime AS TIME), '%H:%i') = TIME_FORMAT(CURTIME(), '%H:%i') \r\n"
			+ "  and a.orgid=?1 and a.branchcode=?2 and a.empcode=?3 ")
	List<CalendarVO> getCalendarNotificationByOrgId(Long orgId, String branchCode, String empCode);

}
