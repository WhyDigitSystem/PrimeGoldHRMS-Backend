package com.efit.hrms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceDTO {
	private Long id;
	private LocalDate requestDate;
	private String employeeName;
	private String employeeCode;
	private String department;
	private String designation;
	private String reasonForAdvance;
	private BigDecimal advanceAmount;
	private BigDecimal loanBalance;
	private String remarks;
	private boolean approve;
	private boolean active;
	private String createdBy;
	private int dueMonth;

	private Long orgId;
	private boolean cancel;
	private String branchCode;
	private String branch;
	private String finYear;

}
