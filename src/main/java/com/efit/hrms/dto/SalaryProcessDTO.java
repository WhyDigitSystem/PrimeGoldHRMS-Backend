package com.efit.hrms.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryProcessDTO {

	private Long id;
	private Long month;
	private String year;
//	private LocalDate date;
	private String employeeName;
	private String employeeCode;
	private BigDecimal totalCompanyWorkingDays;
	private BigDecimal totalLeave;
	private BigDecimal lopLeave;
	private BigDecimal empTotalWorkingDays;
	private BigDecimal empSalaryDays;
	private BigDecimal grossPay;
	private BigDecimal netPay;
	private BigDecimal payOnHand;
	private String approvedStatus;
	private BigDecimal otHours;
	private BigDecimal otAmount;
	
	
	private String createdBy;
	private String branch;
	private String branchCode;
	private Long orgId;
}
