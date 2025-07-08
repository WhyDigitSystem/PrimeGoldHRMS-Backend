package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CommentsVO;

@Repository
public interface CommentsRepo extends JpaRepository<CommentsVO, Long>{
   
	@Query(nativeQuery =true,value ="select * from comments where ticketid=?1 and orgid=?2 ORDER BY commentsid desc")
	List<CommentsVO> getComments(Long ticketId, Long orgId);

	@Query(nativeQuery =true,value = "select * from comments c where c.ticketid=?1")
	List<CommentsVO> findByTicketId(Long ticketId);

	@Query(nativeQuery =true,value = "select * from comments where username=?1")
	List<CommentsVO> findByUserName(String userName);

	@Query(nativeQuery =true,value ="select * from comments where ticketid=?1 and orgid=?2 and notificationflag=1")
	List<CommentsVO> findByTicketIdAndorgId(Long ticketId, Long orgId);

}

