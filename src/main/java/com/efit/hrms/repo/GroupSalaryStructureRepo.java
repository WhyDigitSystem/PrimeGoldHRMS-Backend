package com.efit.hrms.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.GroupSalaryStructureVO;

@Repository
public interface GroupSalaryStructureRepo extends JpaRepository<GroupSalaryStructureVO,Long>{

	@Query(nativeQuery = true, value = "select * from groupsalarystructure where orgid=?1")
	List<GroupSalaryStructureVO> getGroupSalaryStructureByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from groupsalarystructure where groupsalarystructureid=?1")
	Optional<GroupSalaryStructureVO> getGroupSalaryStructureById(Long id);

}
