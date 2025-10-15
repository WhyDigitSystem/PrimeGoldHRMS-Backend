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

	
	DepartmentVO findByOrgIdAndDepartmentName(Long orgId, String department);

	@Query(nativeQuery = true,value = "select DISTINCT d.* from department d join employee e on e.department=d.departmentname where d.orgid=?1  AND (?2 = 'ALL' OR e.employeecode = ?2)")
	List<DepartmentVO> getDepartmentForEmployeeCode(Long orgId, String employeeCode);



}
