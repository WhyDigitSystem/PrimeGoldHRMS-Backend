package com.efit.hrms.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.GroupLedgerVO;

public interface GroupLedgerRepo extends JpaRepository<GroupLedgerVO, Long>{
	
@Query(nativeQuery =true,value = "select * from groupledger where orgid=?1")
	List<GroupLedgerVO> getAllGroupLedgerByOrgId(Long orgId);

@Query(nativeQuery =true,value = "select * from groupledger where accountcode=?1")
List<GroupLedgerVO> getAllGroupLedgerById(Long id);

@Query(nativeQuery = true,value = "select * from groupledger where active=1")
List<GroupLedgerVO> findGroupLedgerByActive();

boolean existsByAccountGroupNameAndOrgId(String accountGroupName, Long orgId);

@Query(nativeQuery = true, value = "select accountgroupname from groupledger where orgid=?1 and type='GROUP' and active=1 group by accountgroupname")
Set<Object[]> getGroupDetails(Long orgId);


}
