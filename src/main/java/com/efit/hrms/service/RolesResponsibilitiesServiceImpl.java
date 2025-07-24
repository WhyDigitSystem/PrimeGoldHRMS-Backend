package com.efit.hrms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efit.hrms.dto.RolesPermissionDTO;
import com.efit.hrms.dto.RolesPermissionHeaderDTO;
import com.efit.hrms.entity.RolesPermissionHeaderVO;
import com.efit.hrms.entity.RolesPermissionVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.RolePermissionRepo;
import com.efit.hrms.repo.RolesPermissionHeaderRepo;

@Service
public class RolesResponsibilitiesServiceImpl implements RolesResponsibilitiesService{

	@Autowired
	RolesPermissionHeaderRepo rolesPermissionHeaderRepo;
	
	@Autowired
	RolePermissionRepo rolesPermissionRepo;
	
	@Override
	public Map<String, Object> createUpdateRoleScreenPermission(RolesPermissionHeaderDTO rolesPermissionHeaderDTO)
			throws ApplicationException {

		RolesPermissionHeaderVO rolesPermissionHeaderVO;
		String message;

		if (ObjectUtils.isNotEmpty(rolesPermissionHeaderDTO.getId())) {
			rolesPermissionHeaderVO = rolesPermissionHeaderRepo.findById(rolesPermissionHeaderDTO.getId())
					.orElseThrow(() -> new ApplicationException(
							"This Role Not Found Any Information, Invalid Id: " + rolesPermissionHeaderDTO.getId()));
			rolesPermissionHeaderVO.setUpdatedBy(rolesPermissionHeaderDTO.getCreatedBy());

			message = "Roles Permission Updated Successfully";
		} else {
			rolesPermissionHeaderVO = new RolesPermissionHeaderVO();
			rolesPermissionHeaderVO.setCreatedBy(rolesPermissionHeaderDTO.getCreatedBy());
			rolesPermissionHeaderVO.setUpdatedBy(rolesPermissionHeaderDTO.getCreatedBy());

			message = "Roles Permission Created Successfully";
		}

		createUpdateRolesPermissionHeaderVO(rolesPermissionHeaderDTO, rolesPermissionHeaderVO);
		rolesPermissionHeaderRepo.save(rolesPermissionHeaderVO);

		// rolesPermissionHeaderRepo.save(rolesPermissionHeaderVO);

		Map<String, Object> response = new HashMap<>();
		response.put("rolesPermissionHeaderVO", rolesPermissionHeaderVO);
		response.put("message", message);
		return response;
	}

	private RolesPermissionHeaderVO createUpdateRolesPermissionHeaderVO(RolesPermissionHeaderDTO dto,
			RolesPermissionHeaderVO vo) throws ApplicationException {

		vo.setRole(dto.getRole());
		vo.setActive(dto.isActive());
		vo.setCancel(dto.isCancel());
		vo.setOrgId(dto.getOrgId());
		vo.setCancelRemarks(dto.getCancelRemarks());
		
		
		if(vo.getId()!=null){
			List<RolesPermissionVO> RolesPermissionVOList = rolesPermissionRepo.findByRolesPermissionHeaderVO(vo);
			rolesPermissionRepo.deleteAll(RolesPermissionVOList);
		}
		
		if (ObjectUtils.isNotEmpty(dto.getRolesPermissionDTO())) {
			List<RolesPermissionVO> permissionList = new ArrayList<>();
			for (RolesPermissionDTO permissionDTO : dto.getRolesPermissionDTO()) {
				RolesPermissionVO permissionVO = new RolesPermissionVO();
				permissionVO.setCanDelete(permissionDTO.isCanDelete());
				permissionVO.setCanRead(permissionDTO.isCanRead());
				permissionVO.setCanWrite(permissionDTO.isCanWrite());
				permissionVO.setScreenId(permissionDTO.getScreenId());
				permissionVO.setScreenName(permissionDTO.getScreenName());
				permissionVO.setRolesPermissionHeaderVO(vo);
				permissionList.add(permissionVO);
			}
			vo.setRolesPermissionVO(permissionList);
		}

		return vo;
	}

	
	
@Override
public List<RolesPermissionHeaderVO> getRolesPermissionHeaderByRoleandOrgid(String role, Long orgId) {
	return rolesPermissionHeaderRepo.getRolesPermissionHeaderByRoleandOrgid(role, orgId);
}

	
}
