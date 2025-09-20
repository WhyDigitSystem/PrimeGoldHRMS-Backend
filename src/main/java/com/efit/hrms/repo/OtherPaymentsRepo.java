package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.OtherPaymentsVO;

@Repository
public interface OtherPaymentsRepo  extends JpaRepository<OtherPaymentsVO, Long>{

}
