package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CircularVO;
@Repository
public interface CircularRepo extends JpaRepository<CircularVO,Long>  {

	
	@Query(nativeQuery = true,value = "SELECT * FROM circular a WHERE a.orgid = ?1 AND a.branchcode = ?2 AND a.department = ?3  and a.type=?4  AND a.expiresdate >= CURRENT_DATE order by circularid desc")
	List<CircularVO> getAllCircularsByOrgId(Long orgId, String branchCode, String department,String type);

	@Query(nativeQuery = true,value = "select * from circular a where a.circularid=?1 ")
	CircularVO getCircularsById(Long id);
}
