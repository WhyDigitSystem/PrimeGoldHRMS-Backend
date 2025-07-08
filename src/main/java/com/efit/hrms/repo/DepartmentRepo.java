package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.DepartmentVO;

@Repository
public interface DepartmentRepo extends JpaRepository<DepartmentVO, Long>{

	@Query(nativeQuery = true,value = "select * from department where orgid=?1")
	List<DepartmentVO> findDepartmentByOrgId(Long orgId);

	boolean existsByDepartmentNameAndOrgId(String departmentName, Long orgId);

	boolean existsByDepartmentNameAndDepartmentCodeAndOrgId(String departmentName, String departmentCode, Long orgId);

	boolean existsByDepartmentCodeAndOrgId(String departmentCode, Long orgId);


}
