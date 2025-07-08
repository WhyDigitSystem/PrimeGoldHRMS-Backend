package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CompensatoryOffVO;
import com.efit.hrms.entity.CompoffNotifyVO;

@Repository
public interface CompoffNotifyRepo extends JpaRepository<CompoffNotifyVO, Long>{

	List<CompoffNotifyVO> findByCompensatoryOffVO(CompensatoryOffVO vo);

}
