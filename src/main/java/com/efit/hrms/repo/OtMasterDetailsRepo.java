package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.OtMasterDetailsVO;
import com.efit.hrms.entity.OtMasterVO;
import com.efit.hrms.entity.ShiftAssignDetailsVO;
import com.efit.hrms.entity.ShiftAssignVO;

@Repository
public interface OtMasterDetailsRepo extends JpaRepository<OtMasterDetailsVO, Long>{

	List<OtMasterDetailsVO> findByOtMasterVO(OtMasterVO otMasterVO);


	
}
