package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.OtMasterVO;

@Repository
public interface OtMasterRepo extends JpaRepository<OtMasterVO, Long>{

	@Query( value = "SELECT * FROM otmaster WHERE orgid=?1",nativeQuery = true)
	List<OtMasterVO> getAllOtMasterByOrgId(Long orgId);

	@Query( value = "SELECT * FROM otmaster WHERE otmasterid=?1",nativeQuery = true)
	OtMasterVO getOtMasterById(Long id);

	@Query(value = "SELECT e.employeecode, e.employee " +
            "FROM employee e " +
            "WHERE e.orgid = ?1 " +
            "  AND (?2 = 'ALL' OR e.branch = ?2) " +
            "  AND (?3 = 'ALL' OR e.department = ?3) " +
            "  AND ( ?4 = 'ALL' " +
            "        OR (?4 = 'EMPLOYEE' AND e.type = 'EMPLOYEE') " +
            "        OR (?4 = 'CONTRACTOR' AND e.type = 'CONTRACTOR' AND (?5 IS NULL OR e.contractor = ?5)) " +
            "      )", nativeQuery = true)
List<Object[]> getEmployeeNameForApprovalOtProcess(Long orgId, String branch, String department, String type, String contractor);


}
