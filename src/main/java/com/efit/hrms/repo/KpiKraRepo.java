package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.KpiKraVO;

@Repository
public interface KpiKraRepo extends JpaRepository<KpiKraVO, Long> {

	@Query(value="select * from kpikra where orgid=?1",nativeQuery = true )
	List<KpiKraVO> getKpiKra(Long orgId);

}
