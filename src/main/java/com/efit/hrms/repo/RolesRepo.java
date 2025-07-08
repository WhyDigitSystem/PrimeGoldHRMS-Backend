package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.RolesVO;

public interface RolesRepo extends JpaRepository<com.efit.hrms.entity.RolesVO, Long> {

	boolean existsByRoleAndOrgId(String role, Long orgId);

	@Query(nativeQuery = true, value ="select role,active from Roles a where a.active=1 and a.orgId=?1")
	Set<Object[]> getAllActiveRolesByOrgId(Long orgId);
	
	@Query(nativeQuery = true, value = "select * from roles where rolesid=?1")
	RolesVO getRolesById(Long id);

	@Query(nativeQuery = true, value =  "select * from Roles a where  a.orgId=?1")
	List<RolesVO> findRolesByOrgId(Long orgId);



}
