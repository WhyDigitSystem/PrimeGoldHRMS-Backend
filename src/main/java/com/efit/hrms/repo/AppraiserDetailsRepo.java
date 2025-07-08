package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AppraiserDetailsVO;
import com.efit.hrms.entity.AppraiserVO;

@Repository
public interface AppraiserDetailsRepo extends JpaRepository<AppraiserDetailsVO, Long>{

	List<AppraiserDetailsVO> findByAppraiserVO(AppraiserVO appraiserVO);

}
