package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppraiseeDTO {

	private Long id;

	private String name;

	private String code;

	private String designation;

	private String department;

	private String branch;

	private String reportingHead;

	private String reportingHeadCode;

	private String reportingHeadDesignation;

	private Long orgId;

	private String createdBy;

	private Long finYear;

	private boolean active;
	
	private List<AppraiseeDetailsDTO> appraiseeDetailsDTO;

}
