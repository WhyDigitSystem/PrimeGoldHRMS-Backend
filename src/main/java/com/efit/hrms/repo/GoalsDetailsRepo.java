package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.GoalsDetailsVO;
import com.efit.hrms.entity.GoalsVO;
@Repository
public interface GoalsDetailsRepo extends JpaRepository<GoalsDetailsVO, Long>{

	List<GoalsDetailsVO> findByGoalsVO(GoalsVO goalsVO);

}

