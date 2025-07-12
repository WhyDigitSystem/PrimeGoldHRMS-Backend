package com.efit.hrms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.efit.hrms.dto.GroupDTO;
import com.efit.hrms.entity.GroupVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface TestService {

	Map<String, Object> createUpdateGroup(@Valid GroupDTO groupDTO) throws ApplicationException;

	List<GroupVO> getGroupByOrgId(Long orgId);

	Optional<GroupVO> getPreGoalsById(Long id);

}
