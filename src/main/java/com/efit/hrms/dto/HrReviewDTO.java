package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrReviewDTO {

	private Long id;
	private String code;
	private String name;
	private String elgibilityOfPromption;
	private String elgibilityOfIncrement;
	private int score;
	private String adheranceOfEmployeeEngagement;
	private String remrks;
	private Long finYear;
	private Long orgId;
	private String createdBy;
	private String promotionStatus;
}
