package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.PreGoalsDetailsVO;
import com.efit.hrms.entity.PreGoalsVO;

@Repository
public interface PreGoalsDetailsRepo extends JpaRepository<PreGoalsDetailsVO, Long>{

	List<PreGoalsDetailsVO> findByPreGoalsVO(PreGoalsVO preGoalsVO);

}
