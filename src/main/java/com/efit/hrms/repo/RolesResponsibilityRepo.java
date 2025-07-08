package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efit.hrms.entity.RolesResponsibilityVO;
import com.efit.hrms.entity.RolesVO;


public interface RolesResponsibilityRepo extends JpaRepository<RolesResponsibilityVO, Long>{

	List<RolesResponsibilityVO> findByRolesVO(RolesVO rolesVO);

}
