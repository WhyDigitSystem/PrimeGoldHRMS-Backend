package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CheckInOutBiometricVO;

@Repository
public interface CheckInOutBiometricRepo extends JpaRepository<CheckInOutBiometricVO, Long>{

//	Optional<CheckInOutBiometricVO> findTopByEmpCodeAndOrgIdAndBranchOrderByIdDesc(String empcode, long orgId, String branch);

}
