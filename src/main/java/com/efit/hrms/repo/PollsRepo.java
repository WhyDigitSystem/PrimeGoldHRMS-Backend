package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.HolidayVO;
import com.efit.hrms.entity.PollsVO;

@Repository
public interface PollsRepo extends JpaRepository<PollsVO, Long>{
	
	@Query(nativeQuery = true,value = "select * from polls where orgid=?1 and branchcode=?2  AND department = ?3 and type=?4  AND expiresdate >= CURRENT_DATE order by pollsid desc")
	List<PollsVO> getAllPollsByOrgId(Long orgId, String branchCode, String department, String type);

	@Query(nativeQuery = true,value = "select * from polls where pollsid=?1 ")
	PollsVO getPollsById(Long id);

}
