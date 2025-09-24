package com.efit.hrms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

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

	private String employeeName;
	private String employeeCode;
	private BigDecimal totalCompanyWorkingDays;
	private BigDecimal totalLeave;
	private BigDecimal lopLeave;
	private BigDecimal empTotalWorkingDays;
	private BigDecimal empSalaryDays;
	private BigDecimal grossPay;
	private BigDecimal netPay;
	private BigDecimal otHours;
	private BigDecimal BankOtAmount;
	private BigDecimal cashOtAmount;
	private BigDecimal bankAmount;
	private BigDecimal cashAmount;
	private String approvedStatus;
	private String approveBy;
	private String approveOn;
	
	private BigDecimal advanceDeduction;
//	private BigDecimal salary;
	
//	@Column(name = "requestdate")
//	private LocalDate requestDate;
//	@Column(name = "loanbalance")
//	private BigDecimal loanBalance;
	
	private String createdBy;
	private String branch;
	private String branchCode;
	private Long orgId;
}
