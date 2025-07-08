package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMasterDTO {

	private Long id;
	private String role;
	private boolean active;
	private Long orgId;
	private String createdBy;
	
}
