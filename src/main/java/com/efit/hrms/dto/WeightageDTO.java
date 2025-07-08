package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightageDTO {

	private Long id;

	private String level;

	private int businessOperations;

	private int valueCreation;

	private int peopleEngagement;

	private String remarks;
	
	private Long finYear;

	private int invlId;

	private boolean active;
	private Long orgId;
	private String createdBy;

}
