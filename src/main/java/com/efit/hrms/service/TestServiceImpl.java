package com.efit.hrms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efit.hrms.dto.GroupDTO;
import com.efit.hrms.dto.GroupDetailsDTO;
import com.efit.hrms.entity.GroupDetailsVO;
import com.efit.hrms.entity.GroupVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.GroupRepo;

@Service
public class TestServiceImpl implements TestService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl.class);

	@Autowired
	GroupRepo groupRepo;

	@Transactional
	@Override
	public Map<String, Object> createUpdateGroup(@Valid GroupDTO groupDTO) throws ApplicationException {
		String message;

		GroupVO groupVO = null;

		if (ObjectUtils.isEmpty(groupDTO.getId())) {

			if (groupRepo.existsByGroup(groupDTO.getGroup())) {

				String errorMessage = String.format("This Group: %s Already Exists in This Organization",
						groupDTO.getGroup());
				throw new ApplicationException(errorMessage);

			}

			groupVO = new GroupVO();

			groupVO.setCreatedBy(groupDTO.getCreatedBy());
			groupVO.setUpdatedBy(groupDTO.getCreatedBy());

			message = "Group Creation SuccessFully";

		} else {

			groupVO = groupRepo.findById(groupDTO.getId())
					.orElseThrow(() -> new ApplicationException("PreGoals  not found with id: " + groupDTO.getId()));

			if (!groupVO.getGroup().equals(groupDTO.getGroup())) {

				if (groupRepo.existsByGroup(groupDTO.getGroup())) {

					String errorMessage = String.format("This Group: %s Already Exists in This Organization",
							groupDTO.getGroup());
					throw new ApplicationException(errorMessage);

				}
			}

			groupVO.setUpdatedBy(groupDTO.getCreatedBy());

			message = "Group Updation SuccessFully";
		}

		groupVO = getGroupDTOFormGroupDTO(groupVO, groupDTO);
		groupRepo.save(groupVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("groupVO", groupVO);
		return response;
	}

	private GroupVO getGroupDTOFormGroupDTO(GroupVO groupVO, @Valid GroupDTO groupDTO) throws ApplicationException {

		groupVO.setGroup(groupDTO.getGroup());
		groupVO.setCancelRemark(groupDTO.getCancelRemark());
		groupVO.setFinYear(groupDTO.getFinYear());
		groupVO.setOrgId(groupDTO.getOrgId());
		groupVO.setActive(groupDTO.isActive());

		List<GroupDetailsVO> groupDetailsVOs = new ArrayList<>();

		if (groupVO.getGroupDetailsVO() != null) {

			for (GroupDetailsDTO groupDetailsDTO : groupDTO.getGroupDetailsDTO()) {

				GroupDetailsVO groupDetailsVO = new GroupDetailsVO();

				groupDetailsVO.setCode(groupDetailsDTO.getCode());
				groupDetailsVO.setDepartment(groupDetailsDTO.getDepartment());
				groupDetailsVO.setName(groupDetailsDTO.getName());

				groupDetailsVO.setGroupVO(groupVO);
				groupDetailsVOs.add(groupDetailsVO);

			}

			groupVO.setGroupDetailsVO(groupDetailsVOs);
		} else {

			throw new ApplicationException("GroupDetailsVO Is Null");
		}

		for (GroupDetailsDTO groupDetailsDTO : groupDTO.getGroupDetailsDTO()) {

			GroupDetailsVO groupDetailsVO = new GroupDetailsVO();

			groupDetailsVO.setCode(groupDetailsDTO.getCode());
			groupDetailsVO.setDepartment(groupDetailsDTO.getDepartment());
			groupDetailsVO.setName(groupDetailsDTO.getName());

			groupDetailsVO.setGroupVO(groupVO);
			groupDetailsVOs.add(groupDetailsVO);

		}

		groupVO.setGroupDetailsVO(groupDetailsVOs);

		return groupVO;
	}

	@Override
	public List<GroupVO> getGroupByOrgId(Long orgId) {
		return groupRepo.getGroupByOrgId(orgId);
	}

	@Override
	public Optional<GroupVO> getPreGoalsById(Long id) {
		return groupRepo.findById(id);
	}

}
