package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.RoleMasterVO;

public interface RoleRepo extends JpaRepository<RoleMasterVO, Long> {

	@Query(nativeQuery = true,value = "select * from role where roleid=?1")
	List<RoleMasterVO> findRoleById(Long id);

	@Query(nativeQuery = true,value = "select * from role where orgid=?1")
	List<RoleMasterVO> findRoleByOrgId(Long orgId);

	@Query(nativeQuery = true,value = "select * from role where active=1")
	List<RoleMasterVO> findRoleByActive();

	boolean existsByRoleAndOrgId(String role, Long orgId);




}
