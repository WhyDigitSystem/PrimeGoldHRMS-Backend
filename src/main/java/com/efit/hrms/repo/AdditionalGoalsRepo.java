package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AdditionalGoalsVO;

@Repository
public interface AdditionalGoalsRepo extends JpaRepository<AdditionalGoalsVO, Long>{

	@Query(nativeQuery = true,value = "select * from additionalgoals where orgid=?1")
	List<AdditionalGoalsVO> getAdditionalGoals(Long orgId);

	@Query(nativeQuery = true,value = "select k.kradescription,k1.kpidescription from  kpikradetails k join kpi k1 on k.kpiid=k1.kpiid join kpikra k2 on k2.kpikraid=k1.kpikraid\r\n"
			+ "join employee e on e.orgid=k2.orgid\r\n"
			+ "where k2.branchcode= ?3 and e.designation= ?4 and k2.orgid= ?1 and k2.finyear= ?2")
	Set<Object[]> getAdditionalGoals(Long orgId, String finYear, String branchCode, String designation);

}
