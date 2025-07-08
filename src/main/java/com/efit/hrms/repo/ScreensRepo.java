package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efit.hrms.entity.ResponsibilityVO;
import com.efit.hrms.entity.ScreensVO;

public interface ScreensRepo extends JpaRepository<ScreensVO, Long> {

	List<ScreensVO> findByResponsibilityVO(ResponsibilityVO responsibilityVO);

}
