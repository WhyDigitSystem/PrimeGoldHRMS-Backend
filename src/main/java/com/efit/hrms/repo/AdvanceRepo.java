package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AdvanceVO;

@Repository
public interface AdvanceRepo extends JpaRepository<AdvanceVO, Long> {
	
	@Query(nativeQuery = true, value = "select * from advance a where a.orgid=?1 and a.branchcode=?2 ")
	List<AdvanceVO> getAllAdvanceByOrgId(Long orgId,String branchCode);

	@Query(nativeQuery = true, value = "select * from advance a where a.advanceid=?1 ")
	AdvanceVO getAdvanceById(Long id);

	@Query(nativeQuery = true,value = "select employee,employeecode,department,designation from employee where orgid=?1 and active= 1")
	Set<Object[]> findEmployeeDetails(Long orgId);
}
