package com.efit.hrms.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSummaryDTO {

	private Long id;
	private String empCode;
	private String empName;
	private String branch;
	private String branchCode;	
	private String department;
	private String finyear;
	private int month;
	private long orgId;
	private int totalDays;
	private int holidays;
	private int weekoff;
	private BigDecimal leaves;
	private BigDecimal lop;
	private BigDecimal absent;
	private BigDecimal present;
	private BigDecimal salarydays;
	private String createdBy;
	private Long otHours;
	private BigDecimal bankOtAmount;
	private BigDecimal cashOtAmount;

	
	private String approveStatus;
	private String approveBy;
	private String approveOn;
	
}
