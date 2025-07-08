package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.PollVoteVO;

@Repository
public interface PollVoteRepo extends JpaRepository<PollVoteVO, Long>{




	@Query(nativeQuery = true, value = "SELECT  branchcode, department, options, orgid, pollid, question FROM `efit-hrms`.pollvote where orgid= ?1  and pollid =?3 and username = ?2")
	Set<Object[]> findPollResultForUser(Long orgId,String userName,Long pollId);
	
	@Query(nativeQuery = true, value = "SELECT options, count(options) as count FROM `efit-hrms`.pollvote where orgid= ?1  and pollid =?2 group by options")
	Set<Object[]> findPollResultForHR(Long orgId,Long pollId);

	List<PollVoteVO> findByOrgIdAndPollIdAndUserNameIgnoreCase(long orgId, long pollId, String userName);
	 

}
