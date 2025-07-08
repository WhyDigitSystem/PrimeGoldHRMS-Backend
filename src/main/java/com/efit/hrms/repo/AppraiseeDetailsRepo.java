package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AppraiseeDetailsVO;
import com.efit.hrms.entity.AppraiseeVO;
@Repository
public interface AppraiseeDetailsRepo extends JpaRepository<AppraiseeDetailsVO, Long>{

	List<AppraiseeDetailsVO> findByAppraiseeVO(AppraiseeVO appraiseeVO);

}
