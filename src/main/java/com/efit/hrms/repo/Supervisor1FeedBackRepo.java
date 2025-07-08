package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.Supervisor1FeedBackVO;

@Repository
public interface Supervisor1FeedBackRepo extends JpaRepository<Supervisor1FeedBackVO, Long>{

	@Query(nativeQuery = true,value = "select * from supervisor1feedBack where orgid=?1")
	List<Supervisor1FeedBackVO> getSupervisor1FeedBack(Long orgId);

}
