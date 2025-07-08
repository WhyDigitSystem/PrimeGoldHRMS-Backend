package com.efit.hrms.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDTO {

	private Long id;
	private String regionCode;
	private String regionName;
	private boolean active;
	private String createdBy;
	private Long orgId;
	private boolean cancel;
}
