package com.efit.hrms.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CompanyVO;
import com.efit.hrms.entity.LeaveTypeVO;

@Repository
public interface CompanyRepo extends JpaRepository<CompanyVO, Long> {

	boolean existsByCompanyCodeAndId(String companyCode, Long id);

	boolean existsByCompanyNameAndId(String companyName, Long id);

	//boolean existsByEmployeeCodeAndId(String employeeCode, Long id);

	//boolean existsByEmailAndId(String email, Long id);

	boolean existsByPhoneAndId(String phone, Long id);

	//boolean existsByCompanyCodeAndCompanyNameAndEmployeeCodeAndEmailAndPhoneAndId(String companyCode,String companyName, String phone, Long id);

	@Query(nativeQuery = true, value = "select * from company  where companyid=?1")
	List<CompanyVO> findByCompany(Long companyid);


//	boolean existsByCompanyCodeAndCompanyNameAndPhoneAndId(String companyCode, String companyName, String phone,
//			Long id);

	boolean existsByEmailAndId(String email, Long id);

//	boolean existsByCompanyCodeAndCompanyNameAndPhoneAndEmail(String companyCode, String companyName, String email,
//			String phone);

	boolean existsByCompanyName(String companyName);

	boolean existsByCompanyCode(String companyCode);

	boolean existsByPhone(String phone);

	boolean existsByEmail(String email);

//	CompanyVO findByCompanyId(Long orgId);





	



	//Sanveg leave
//	@Query(nativeQuery = true, value = "SELECT l.employeecode, l.employeename, l.leavecode, l.leavetype, l.fromdate, l.todate, "
//	        + "GROUP_CONCAT(DISTINCT cw.weekoffdays) AS weekoffdays, c.leavepolicy, "
//	        + "(DATEDIFF(l.todate, l.fromdate) + 1 - "
//	        + "    (SELECT COUNT(*) FROM "
//	        + "        (SELECT DISTINCT DATE_ADD(l.fromdate, INTERVAL t.n DAY) AS leave_date "
//	        + "         FROM (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 "
//	        + "               UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 "
//	        + "               UNION ALL SELECT 8 UNION ALL SELECT 9) t "
//	        + "         WHERE DATE_ADD(l.fromdate, INTERVAL t.n DAY) BETWEEN l.fromdate AND l.todate"
//	        + "        ) temp "
//	        + "    JOIN companyweekoff cw ON FIND_IN_SET(DAYNAME(temp.leave_date), cw.weekoffdays) "
//	        + "    WHERE cw.companyid = :orgId) " // Use named parameter
//	        + ") AS totalleave "
//	        + "FROM leaverequest l "
//	        + "JOIN company c ON l.orgid = c.companyid "
//	        + "LEFT JOIN companyweekoff cw ON c.companyid = cw.companyid "
//	        + "WHERE l.todate >= l.fromdate AND l.orgid = :orgId " // Use named parameter
//	        + "GROUP BY l.employeecode, l.employeename, l.leavecode, l.leavetype, "
//	        + "l.fromdate, l.todate, c.leavepolicy, l.orgid")
//	List<CompanyVO> findByOrgId(@Param("orgId") Long orgId);


	

	
	

}
