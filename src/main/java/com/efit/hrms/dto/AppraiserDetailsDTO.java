package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppraiserDetailsDTO {

	private String area;
	private String goals;
	private String keyPerformanceIndicator;
	private String reMarks;
	private int score;

	private String input;

}
