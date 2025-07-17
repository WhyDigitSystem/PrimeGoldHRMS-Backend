package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.GroupSalaryEarningsVO;
import com.efit.hrms.entity.GroupSalaryStructureVO;

@Repository
public interface GroupSalaryEarningsRepo extends JpaRepository<GroupSalaryEarningsVO, Long>{


	List<GroupSalaryEarningsVO> findByGroupSalaryStructureVO(GroupSalaryStructureVO groupSalaryStructureVO);

}
