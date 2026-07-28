package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CheckInOutUploadVO;

@Repository
public interface CheckInOutUploadRepo extends JpaRepository<CheckInOutUploadVO, Long>{

}
