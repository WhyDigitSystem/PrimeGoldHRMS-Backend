package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.GroupSalaryDeductionsVO;
import com.efit.hrms.entity.GroupSalaryStructureVO;

@Repository
public interface GroupSalaryDeductionsRepo extends JpaRepository<GroupSalaryDeductionsVO, Long> {

	List<GroupSalaryDeductionsVO> findByGroupSalaryStructureVO(GroupSalaryStructureVO groupSalaryStructureVO);

}
