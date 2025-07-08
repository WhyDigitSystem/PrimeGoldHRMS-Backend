package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.PollDetailsVO;
import com.efit.hrms.entity.PollVoteVO;
import com.efit.hrms.entity.PollsVO;

@Repository
public interface PollDetailsRepo extends JpaRepository<PollDetailsVO, Long>{

	List<PollDetailsVO> findByPollsVO(PollsVO pollsVO);

	
}
