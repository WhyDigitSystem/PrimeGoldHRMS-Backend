package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.dto.PermissionRequestDTO;
import com.efit.hrms.entity.PermissionRequestNotifyVO;

@Repository
public interface PermissionRequestNotifyRepo extends JpaRepository<PermissionRequestNotifyVO, Long>{

	List<PermissionRequestNotifyVO> findByPermissionRequestVO(PermissionRequestDTO permissionRequestDTO);

}
