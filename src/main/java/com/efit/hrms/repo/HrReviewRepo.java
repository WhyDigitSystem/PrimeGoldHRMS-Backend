package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.HrReviewVO;

@Repository
public interface HrReviewRepo extends JpaRepository<HrReviewVO, Long> {

	@Query(nativeQuery = true, value = "select * from hrreview where orgid=?1")
	List<HrReviewVO> getHrReview(Long orgId);

}
