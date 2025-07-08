package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.KpiKraDetailsVO;
import com.efit.hrms.entity.KpiKraVO;

@Repository
public interface KpiKraDetailsRepo extends JpaRepository<KpiKraDetailsVO,Long>{

	List<KpiKraDetailsVO> findByKpiKraVO(KpiKraVO kpiKraVO);

}
