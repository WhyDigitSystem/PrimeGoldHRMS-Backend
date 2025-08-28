package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.RolesPermissionHeaderVO;

@Repository
public interface RolesPermissionHeaderRepo extends JpaRepository<RolesPermissionHeaderVO, Long> {
	
	@Query(value = "select * from rolepermissionheader a where a.active=true and a.orgid=?2 and a.role=?1", nativeQuery = true)
	List<RolesPermissionHeaderVO>  getRolesPermissionHeaderByRoleandOrgid(String role, Long orgId);

	
	
}
