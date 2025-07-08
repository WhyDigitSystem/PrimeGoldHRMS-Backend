package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.SelfGoalsDetailsVO;
import com.efit.hrms.entity.SelfGoalsVO;

@Repository
public interface SelfGoalsDetailsRepo extends JpaRepository<SelfGoalsDetailsVO, Long>{

	List<SelfGoalsDetailsVO> findBySelfGoalsVO(SelfGoalsVO selfGoalsVO);

}
