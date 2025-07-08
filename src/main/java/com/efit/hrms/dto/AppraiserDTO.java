package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppraiserDTO {
	
	private Long id;

	private String appraisalId;

	private String empName;

	private String empCode;

	private String department;

	private String supName;

	private String supCode;

	private String finYear;

	private String createdBy;

	private boolean active;

	private String branch;

	private Long orgId;
	
	private List<AppraiserDetailsDTO> appraiserDetailsDTO;

}
