package com.efit.hrms.exception;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.GroupDetailsVO;

@Repository
public interface GroupDetailsRepo extends JpaRepository<GroupDetailsVO, Long>{

}
