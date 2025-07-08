package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinScreenDTO {

	private Long id;
	private String screenName;
	private String screenCode;
	private String createdBy;
	private boolean active;
}
