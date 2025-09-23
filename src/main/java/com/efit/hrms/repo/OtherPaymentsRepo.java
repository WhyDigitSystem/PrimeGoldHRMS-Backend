package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.OtherPaymentsVO;

@Repository
public interface OtherPaymentsRepo  extends JpaRepository<OtherPaymentsVO, Long>{

	@Query( value = "SELECT * FROM otherpayments WHERE orgid =?1 and month=?2 and year=?3 ",nativeQuery = true)
	List<OtherPaymentsVO> getAllOtherPaymentsByOrgId(Long orgId, Long month, Long year);

}
