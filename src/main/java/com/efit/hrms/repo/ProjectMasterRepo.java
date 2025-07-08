package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.ProjectMasterVO;

@Repository
public interface ProjectMasterRepo extends JpaRepository<ProjectMasterVO, Long>{

	boolean existsByProjectNameAndOrgId(String projectName, Long orgId);

	@Query(nativeQuery = true, value = "select * from  projectmaster where orgid=?1 ")
	List<ProjectMasterVO> getProjectMasterByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from  projectmaster where projectmasterid=?1 ")
	ProjectMasterVO getProjectMasterById(Long id);

}
