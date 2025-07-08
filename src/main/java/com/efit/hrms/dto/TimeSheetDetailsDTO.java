package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetDetailsDTO {

	private String projectName;
	private String fromTime;
	private String toTime;
	private String description;
	
}
