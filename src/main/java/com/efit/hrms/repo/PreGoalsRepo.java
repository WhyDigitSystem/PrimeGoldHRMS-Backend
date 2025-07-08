package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.PreGoalsVO;

@Repository
public interface PreGoalsRepo extends JpaRepository<PreGoalsVO, Long>{

	@Query(nativeQuery = true,value="select * from pregoals where orgid=?1")
	List<PreGoalsVO> getPreGoals(Long orgId);
	
	@Query(nativeQuery = true, value = "select s.code as empcode,s.name as empname,s.createdon as submittedon,a.supcode,a.supname,a.createdon as approvedon\r\n"
			+ "from selfgoals s join appraiser a on s.code=a.empcode\r\n"
			+ " and s.orgid= ?1  and a.finyear= ?2 and a.supcode= ?3 and s.code in(select empcode from appraiser)")
	Set<Object[]> getPreGoals(Long orgId, String finYear, String supCode);

}
