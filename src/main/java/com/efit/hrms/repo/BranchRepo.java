package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.BranchVO;

@Repository
public interface BranchRepo extends JpaRepository<BranchVO, Long> {

	@Query(value = "SELECT a from BranchVO a where a.orgId=?1 ")
	List<BranchVO> findAll(Long orgid);

	boolean existsByBranchAndOrgId(String branch, Long orgId);

	boolean existsByBranchCodeAndOrgId(String branchCode, Long orgId);

	@Query(nativeQuery = true,value="select branch from branch where orgid=?1")
	Set<Object[]> findBranchForBrsOpening(Long orgId);

	BranchVO findByOrgIdAndBranch(Long orgId, String branch);

}
