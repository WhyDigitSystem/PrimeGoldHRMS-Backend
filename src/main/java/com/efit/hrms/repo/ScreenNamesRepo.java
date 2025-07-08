package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.ScreenNamesVO;

public interface ScreenNamesRepo extends JpaRepository<ScreenNamesVO, Long> {

	
	@Query(nativeQuery = true, value = "select * from screenname where screennameid=?1")
	List<ScreenNamesVO> findFinScreenById(Long id);


	boolean existsByScreenName(String screenName);

	boolean existsByScreenCode(String screenCode);

	@Query(nativeQuery = true, value = "select screencode,screenname from screenname where screencode not in (select screencode from documenttype WHERE ORGID=?1);")
	Set<Object[]> findAllScreenCode(Long orgId);



}
