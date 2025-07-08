package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.LeaveRequestNotifyVO;
import com.efit.hrms.entity.LeaveRequestVO;

@Repository
public interface LeaveRequestNotifyRepo extends JpaRepository<LeaveRequestNotifyVO, Long>{

	List<LeaveRequestNotifyVO> findByLeaveRequestVO(LeaveRequestVO leaveRequestVO);

}
