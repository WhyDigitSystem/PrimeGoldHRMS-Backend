package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalGoalsDTO {

	private Long id;

	private String areaOfImportance;
	
	private String keyPerformanceIndicator;
	
	private String performanceIndicator;

	private String goal;

	//private String remarks;

	private Long finYear;

	//private boolean cancel;
	
	private Long orgId;
	
	private String createdBy;

}
