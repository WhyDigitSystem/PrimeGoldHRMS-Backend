package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.PraiseVO;

@Repository
public interface PraiseRepo  extends JpaRepository<PraiseVO, Long >{

	List<PraiseVO> findByOrgIdAndCircularIdAndUserNameIgnoreCase(long orgId, Long circularId, String userName);

	@Query(nativeQuery = true,value = "select Count(*) from `efit-hrms`.praise where liked ='Yes' and orgid= ?2  and circularid = ?1 ")
	Set<Object[]> findCountOfPraiseByOrgIdAndCircularId(Long circularId, Long orgid);

}
