package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignationDTO {
	private Long id;
	private String designationName;
	private String designationCode;
	private boolean active;
	private Long orgId;
	private String createdBy;

}