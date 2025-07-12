package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.GroupVO;

@Repository
public interface GroupRepo extends JpaRepository<GroupVO, Long> {

	boolean existsByGroup(String group);

	@Query(nativeQuery = true, value = "select * from group where orgid=?1")
	List<GroupVO> getGroupByOrgId(Long orgId);

}
