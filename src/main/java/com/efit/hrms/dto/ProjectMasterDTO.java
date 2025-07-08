package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMasterDTO {

	private Long id;
	private String projectName;
	private String projectCode;
	private String description;
	private boolean active ;
	private String createdBy;
	private Long orgId;
}
