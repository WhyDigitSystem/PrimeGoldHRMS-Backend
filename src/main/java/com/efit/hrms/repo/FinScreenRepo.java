package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.FinScreenVO;

public interface FinScreenRepo extends JpaRepository<FinScreenVO, Long> {

	@Query(nativeQuery = true, value = "select * from finscreen where finscreenid=?1")
	List<FinScreenVO> findFinScreenById(Long id);

	@Query(nativeQuery = true, value = "select * from finscreen where orgid=?1")
	List<FinScreenVO> findFinScreenByOrgId(Long orgId);

	boolean existsByScreenName(String screenName);

	boolean existsByScreenCode(String screenCode);

	@Query(nativeQuery = true, value = "select screencode,screenname from finscreen where screencode not in (select screencode from documenttype where orgid=?1)")
	Set<Object[]> findAllScreenCode(Long orgId);
}
