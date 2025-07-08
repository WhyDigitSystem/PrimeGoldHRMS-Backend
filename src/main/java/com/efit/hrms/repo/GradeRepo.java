package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.GradeVO;

@Repository
public interface GradeRepo extends JpaRepository<GradeVO, Long>{

	@Query(nativeQuery = true,value = "select * from grade where orgid=?1")
	List<GradeVO> getGrade(Long orgId);

}
