package com.efit.hrms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppraisalPeriodDTO {
	
	private Long id;

	private String type;

	private Long appraisalId;

	private LocalDate effectiveForm;

	private LocalDate effectiveTo;

	private boolean active;

	private Long orgId;

	private String createdBy;
	
	private Long finYear;

}
