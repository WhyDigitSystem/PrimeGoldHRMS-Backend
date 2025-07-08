package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.WeightageVO;

@Repository
public interface WeightageRepo extends JpaRepository<WeightageVO, Long>{

	@Query(nativeQuery = true,value = "select * from weightage where orgid=?1")
	List<WeightageVO> getWeightage(Long orgId);

}
