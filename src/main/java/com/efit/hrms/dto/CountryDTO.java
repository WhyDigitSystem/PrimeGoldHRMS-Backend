package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {

	private Long id;

	private String countryName;
	private String countryCode;
	private boolean active;
	private Long orgId;
//	private String userId;
//	private String dupchk;
	private String createdBy;
//	private String updatedBy;
	private boolean cancel;
}
