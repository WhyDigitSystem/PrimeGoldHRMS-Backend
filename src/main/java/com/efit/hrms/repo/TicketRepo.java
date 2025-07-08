package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.TicketVO;

@Repository
public interface TicketRepo extends JpaRepository<TicketVO, Long> {

	@Query(nativeQuery = true,value="select * from ticket where username=?1 and orgid=?2 ORDER BY ticketid desc")
	List<TicketVO> findByUserName(String userName, Long orgId);

	@Query(nativeQuery = true,value="select * from ticket where orgid=?1 ORDER BY ticketid desc")
	List<TicketVO> getByOrgId(Long orgId);

	@Query(value ="select * from ticket where ticketid=?1 and orgid=?2",nativeQuery =true)
	TicketVO findByTicketIdAndorgId(Long ticketId, Long orgId);

	@Query(value = "SELECT " +
            "  (@row_number \\:= @row_number + 1) AS id, " +
            "  t.ticketid, " +
            "  t.createdby, " +
            "  t.description, " +
            "  t.orgid, " +
            "  t.status, " +
            "  t.subject, " +
            "  t.username " +
            "FROM " +
            "  (SELECT @row_number \\:= 0) AS r, " +
            "  ticket t " +
            "WHERE " +
            "  t.orgid = ?1 AND t.statusflag = 1", nativeQuery = true)
    Set <Object[]> getTicketNotification(Long orgId);


	@Query(value ="select * from  ticket t where t.username=?1 and t.orgid=?2 and t.ticketid=?3",nativeQuery =true)
	TicketVO findByUserNameAndOrgIdAndTicketId(String userName, Long orgId, Long ticketId);
	
	@Query(value ="select * from  ticket t where  t.orgid=?1",nativeQuery =true)
	List<TicketVO> findByOrgId(Long orgId);

	@Query(nativeQuery =true,value = "  SELECT " 
			+ "  (@row_number \\:= @row_number + 1) AS id, " 
			+ "  t.ticketid,\r\n"
			+ "  t.modifiedy,\r\n"
			+ "  t.status,\r\n"
			+ "  t.orgid,\r\n"
			+ "  c.comments\r\n"
			+ "FROM \r\n"
			+ "  (SELECT @row_number \\:= 0) AS r, "
			+ "  ticket t\r\n"
			+ "JOIN \r\n"
			+ "  comments c ON t.ticketid = c.ticketid\r\n"
			+ "WHERE \r\n"
			+ "  t.notificationflag = 1 \r\n"
			+ "  AND c.notificationflag = 1 \r\n"
			+ "  AND t.username = ?1")
	Set<Object[]> getNotificationFromUser(String userName);

	@Query(nativeQuery =true,value = "select * from ticket where username=?1")
	List<TicketVO> findByUserName(String userName);

	

}
