package com.efit.hrms.repo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.CurrencyVO;
import com.efit.hrms.entity.EmployeeVO;

public interface EmployeeRepo extends JpaRepository<EmployeeVO,Long>{

//	@Query(value = "SELECT e.employeeCode , e.employeeName FROM EmployeeVO e WHERE e.orgId=?1 and branchCode=?2 ")
//	List<EmployeeVO> findAllEmployeeByOrgId(Long orgId, String branchCode);

	@Query(value = "SELECT \r\n"
			+ "    e.employeeid AS employeeId,\r\n"
			+ "    e.alternativemobileno AS alternativeMobileNo,\r\n"
			+ "    e.aadharno AS aadharNo,\r\n"
			+ "    e.accountno AS accountNo,\r\n"
			+ "    e.active AS active,\r\n"
			+ "    e.bloodgroup AS bloodGroup,\r\n"
			+ "    e.branch AS branch,\r\n"
			+ "    e.branchcode AS branchCode,\r\n"
			+ "    e.cancel AS cancel,\r\n"
			+ "    e.cancelremarks AS cancelRemarks,\r\n"
			+ "    e.createdon AS createdOn,\r\n"
			+ "    e.modifiedon AS modifiedOn,\r\n"
			+ "    e.createdby AS createdBy,\r\n"
			+ "    e.dateofbirth AS dateOfBirth,\r\n"
			+ "    e.department AS department,\r\n"
			+ "    e.designation AS designation,\r\n"
			+ "    e.email AS email,\r\n"
			+ "    e.employeeaddress AS employeeAddress,\r\n"
			+ "    e.employeecode AS employeeCode,\r\n"
			+ "    e.employee AS employee,\r\n"
			+ "    e.gender AS gender,\r\n"
			+ "    e.grade AS grade,\r\n"
			+ "    e.ifsccode AS ifscCode,\r\n"
			+ "    e.joiningdate AS joiningDate,\r\n"
			+ "    e.mobileno AS mobileNo,\r\n"
			+ "    e.orgid AS orgId,\r\n"
			+ "    e.panno AS panNo,\r\n"
			+ "    e.profileimage AS profileImage,\r\n"
			+ "    e.reportingrole AS reportingRole,\r\n"
			+ "    e.reportingperson AS reportingPerson,\r\n"
			+ "    e.reportingpersonemail AS reportingPersonEmail,\r\n"
			+ "    e.resigndate AS resignDate,\r\n"
			+ "    e.team AS team,\r\n"
			+ "    e.modifiedby AS modifiedBy,\r\n"
			+ "    e.reportingpersoncode AS reportingPersonCode,\r\n"
			+ "    e.uanno AS uanNo,\r\n"
			+ "    e.bankname AS bankName,"
			+ "    e.employee_type AS employeeType,\r\n"
			+ "    e.esiflag AS esiFlag,\r\n"
			+ "    e.esipercentage AS esiPercentage,\r\n"
			+ "    e.pfflag AS pfFlag,\r\n"
			+ "    e.pfpercentage AS pfPercentage,\r\n"
			+ "    c.companyname AS companyName,\r\n"
			+ "    c.companycode AS companyCode\r\n"
			+ "FROM employee e\r\n"
			+ "JOIN company c ON e.orgid = c.companyid\r\n"
			+ "WHERE e.orgid = ?1 ORDER BY e.employee ASC \r\n"
			+ "", nativeQuery = true)
	List<Map<String, Object>> getEmployeesWithCompanyInfoByOrgId(Long orgId);

	boolean existsByEmployeeCodeAndOrgId(String employeeCode, Long orgId);
	

	@Query(nativeQuery = true,value = "SELECT departmentname FROM department  WHERE orgid=?1 and active=1")
	Set<Object[]> findDepartmentNameForEmployee(Long orgId);

	@Query(nativeQuery = true,value = "SELECT designationname,designationcode FROM designation  WHERE orgid=?1 and active=1")
	Set<Object[]> findDesignationNameForEmployee(Long orgId);
	

    
	
	@Query(value ="select * FROM currency",nativeQuery =true)
	List<CurrencyVO> getAllEmp();

	@Query(value ="select e.employeecode,e.employee,e.dateofbirth,profileimage from employee e where orgid=?1 and active=1 order by e.dateofbirth",nativeQuery =true)
	Set<Object[]> getEmpDob(Long orgId);

	@Query(nativeQuery = true,value = "SELECT employee AS employeename, designation ,email ,employeecode\r\n"
			+ "FROM employee  \r\n"
			+ "WHERE orgid = ?1  and branchcode=?2 AND employeecode NOT IN (?3)  \r\n"
			+ "\r\n"
			+ "AND (trim(designation) LIKE '%MANAGER%' OR trim(designation) LIKE '%TEAM LEAD%' OR trim(designation) LIKE '%CEO%' OR trim(designation) LIKE '%HR%' OR trim(designation) LIKE '%MANAGING DIRECTOR%')" )
	Set<Object[]> findReportingNameForEmployee(Long orgId, String branchCode, String employeeCode);
	
//	@Query(nativeQuery = true,value="select concat(format,lpad(last_number,5,0)) AS docid from sequence_tracker where company_id=?1 and year=?2 and branch_code=?3 and Department_code=?4 and company_code=?5;\r\n")
//	String getEmployeeDocId(Long orgId, String finYear, String branchCode, String departmentCode, String companyCode);
	
//	@Query(nativeQuery = true, value = 
//		    "SELECT CONCAT(a1.format, LPAD(a.lastnumber + 1, 5, '0')) AS docid " +
//		    "FROM sequencetracker a " +
//		    "JOIN sequenceconfig a1 ON a.companyid = a1.companyid " +
//		    "WHERE a.companyid = ?1")
//		String getEmployeeDocId(Long companyId);
//
//	@Modifying
//	@Query(nativeQuery = true, value = 
//	    "UPDATE sequencetracker " +
//	    "SET lastnumber = lastnumber + 1 " +
//	    "WHERE companyid = ?1")
//	void updateLastNumber(Long companyId);
	
	@Query(nativeQuery = true,value = "select * from employee a where a.orgid=?1 and active=1  ")
	List<EmployeeVO> getAllEmployeeByActive(Long orgId);

	List<EmployeeVO> findByOrgId(Long companyId);
	
	
	
	
	
	@Query(nativeQuery = true,value ="SELECT \r\n"
			+ "    employeeid, \r\n"
			+ "    department, \r\n"
			+ "    designation,  \r\n"
			+ "    employeecode, \r\n"
			+ "    employee, \r\n"
			+ "    gender, \r\n"
			+ "    orgid, \r\n"
			+ "    TIMESTAMPDIFF(YEAR, joiningdate, CURDATE()) AS noofyears,"
			+ "    profileimage\r\n"
			+ "FROM employee\r\n"
			+ "WHERE \r\n"
			+ "    MONTH(joiningdate) = MONTH(CURDATE()) \r\n"
			+ "    AND DAY(joiningdate) = DAY(CURDATE())\r\n"
			+ "")
	Set<Object[]> findWorkaniversaryByOrgId(Long orgid);

	
	@Query(nativeQuery = true,value ="SELECT employeeid, department, designation,  employeecode, employee, gender,orgid,profileimage \r\n"
			+ "FROM employee\r\n"
			+ "WHERE MONTH(joiningdate) = MONTH(CURDATE()) \r\n"
			+ "AND YEAR(joiningdate) = YEAR(CURDATE())")
	Set<Object[]> findNewJoinieDtailsByOrgId(Long orgid);

	@Query(value = "SELECT * FROM employee e WHERE e.orgId = ?1 and employeecode=?2 and active=1", nativeQuery = true)
	List<EmployeeVO> getAllEmployeeByOrgIdAndEmployeeCode(Long orgId, String employeeCode);

	EmployeeVO findByEmployeeCode(String employeecode);

	boolean existsByEmployeeCode(String employeeCode);

	boolean existsByEmployeeName(String employeeName);

	boolean existsByEmail(String email);


	@Query(value = "SELECT * FROM employee WHERE orgid = ?1 AND employeecode = ?2 AND branchcode = ?3", nativeQuery = true)
	EmployeeVO getPfAmountAndEsiAmountByEmployee(Long orgId, String employeeCode, String branchCode);
//







}
