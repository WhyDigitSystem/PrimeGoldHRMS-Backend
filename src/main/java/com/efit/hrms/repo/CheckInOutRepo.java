package com.efit.hrms.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CheckInOutVO;

@Repository
public interface CheckInOutRepo extends JpaRepository<CheckInOutVO, Long>{

	Optional<CheckInOutVO> findTopByEmpCodeAndOrgIdAndBranchOrderByIdDesc(String empcode, long orgId, String branch);

}
