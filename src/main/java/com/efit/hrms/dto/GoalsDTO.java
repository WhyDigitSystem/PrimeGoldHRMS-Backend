package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalsDTO {

	private Long id;
	private Long appraisalId;
	private String department;
	private Long orgId;
	private String createdBy;
	private Long finYear;
	private boolean active;

	private List<GoalsDetailsDTO> goalsDetailsDTO;

}
