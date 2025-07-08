package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supervisor1FeedBackDTO {

	private Long id;
	private String code;
	private String name;
	private String elgiblityOfPromption;
	private String elgiblityOfIncrement;
	private int score;
	private String adheranceOfEmployee;
	private String hrRemarks;
	private Long orgId;
	private String createdBy;
	private Long finYear;

	
}
