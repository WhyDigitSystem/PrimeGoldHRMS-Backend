package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.KpiKraVO;
import com.efit.hrms.entity.KpiVO;

@Repository
public interface KpiRepo extends JpaRepository<KpiVO, Long>{

	List<KpiVO> findByKpiKraVO(KpiKraVO kpiKraVO);

}
