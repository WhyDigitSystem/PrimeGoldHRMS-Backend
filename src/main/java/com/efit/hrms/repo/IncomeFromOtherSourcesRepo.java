package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.IncomeFromOtherSourcesVO;

@Repository
public interface IncomeFromOtherSourcesRepo  extends JpaRepository<IncomeFromOtherSourcesVO, Long>{

}
