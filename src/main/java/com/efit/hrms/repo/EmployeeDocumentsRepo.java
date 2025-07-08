package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.EmployeeDocumentsVO;

@Repository
public interface EmployeeDocumentsRepo extends JpaRepository<EmployeeDocumentsVO, Long>{

    List<EmployeeDocumentsVO> findByEmployeeCode(String employeeCode);

	@Query(nativeQuery = true, value = "select * from employeedocuments where orgid=?1 and employeecode=?2")
	List<EmployeeDocumentsVO> getEmployeeDocumentsByEmpCodeAndOrgId(Long orgId, String employeeCode);

    
}
