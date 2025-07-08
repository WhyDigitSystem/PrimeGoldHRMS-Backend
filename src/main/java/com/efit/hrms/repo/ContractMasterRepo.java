package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.ContractMasterVO;

@Repository
public interface ContractMasterRepo extends JpaRepository<ContractMasterVO, Long>{

	@Query( value = "SELECT * FROM contractmaster WHERE orgid =?1",nativeQuery = true)
	List<ContractMasterVO> getAllContractMasterByOrgId(Long orgId);

	@Query( value = "SELECT * FROM contractmaster WHERE contractmasterid =?1",nativeQuery = true)
	ContractMasterVO getContractMasterById(Long id);

}
