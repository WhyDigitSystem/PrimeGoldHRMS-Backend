package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.HousePropertyVO;

@Repository
public interface HousePropertyRepo extends JpaRepository<HousePropertyVO, Long>{

}
