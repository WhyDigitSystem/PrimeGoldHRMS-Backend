package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesPermissionHeaderDTO {

	private Long id;
	private String role;
	private boolean active;
	private boolean cancel;
	private Long orgId;
	private String createdBy;
	private String updatedBy;
	private String cancelRemarks;
	
	private List<RolesPermissionDTO> rolesPermissionDTO;
	
}
