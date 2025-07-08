package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDTO {

	private Long id;

	private String input;

	private int score;
	private Long finYear;
	private boolean active;
	private String createdBy;
	private Long orgId;

}
