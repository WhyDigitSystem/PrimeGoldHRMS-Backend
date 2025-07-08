package com.efit.hrms.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.efit.hrms.entity.GlobalParameterVO;

public interface GlobalParameterRepo extends JpaRepository<GlobalParameterVO, Long> {

	@Query("select a from GlobalParameterVO a where a.orgId=?1 and a.userid=?2")
	Optional<GlobalParameterVO> findGlobalParamByOrgIdAndUserName(Long orgid, String username);

	@Query("select a from GlobalParameterVO a where a.orgId=?1 and a.userid=?2")
	GlobalParameterVO findGlobalParam(Long orgId, String userid);

	@Query(nativeQuery = true, value = "select w.warehouse from warehouse w , warehouseclient wc where wc.warehouseid=w.warehouseid and orgid=?1 and branch=?2 and client =?3")
	Set<Object[]> findWarehouseNameByOrgIdAndBranchAndClient(Long orgid, String branch, String client);

}
