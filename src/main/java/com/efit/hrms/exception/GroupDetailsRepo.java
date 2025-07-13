package com.efit.hrms.exception;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.GroupDetailsVO;
import com.efit.hrms.entity.GroupVO;

@Repository
public interface GroupDetailsRepo extends JpaRepository<GroupDetailsVO, Long>{


	List<GroupDetailsVO> findByGroupVO(GroupVO groupVO);

}
