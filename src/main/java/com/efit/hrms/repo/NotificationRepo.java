package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.NotificationVO;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationVO, Long>{

}

