package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeDTO {

	private Long id;

	private int rangeFrom;
	private int rangeTo;
	private String indications;
	private boolean active;
	private Long orgId;
	private String createdBy;
	private Long finYear;
	private String grade;

	private String score;
}
