package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AemployeeVO;

@Repository
public interface AemployeeRepo extends JpaRepository<AemployeeVO, Long>{

}
