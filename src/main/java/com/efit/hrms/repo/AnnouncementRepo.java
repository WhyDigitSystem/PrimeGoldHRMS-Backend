package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.AnnouncementVO;

@Repository
public interface AnnouncementRepo extends JpaRepository<AnnouncementVO, Long> {
	
	@Query(nativeQuery = true, value = "select * from announcement where orgid=?1 and branchcode=?2 AND expiresdate >= CURRENT_DATE order by announcementid desc")
	List<AnnouncementVO> GetAnnouncementsByOrgId(Long orgId, String branchCode);

	@Query(nativeQuery = true, value = "select * from announcement  where announcementid=?1")
	AnnouncementVO getAnnouncementById(Long id);

} 
