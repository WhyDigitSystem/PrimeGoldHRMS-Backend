package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
	private Long id;

	private String cityCode;
	private String country;
	private String cityName;
	private String state;
	private boolean active;
	private String createdBy;
	private Long orgId;
	private boolean cancel;
}
