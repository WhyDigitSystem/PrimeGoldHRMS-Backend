package com.efit.hrms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialYearDTO {

	private Long id;
	private int finYear;
	private Long finYearId;
	private String finYearIdentifier;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean currentFinYear;
	private boolean closed;
	private Long orgId;
	private String createdBy;
	private boolean active;

}
