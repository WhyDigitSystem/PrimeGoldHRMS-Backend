package com.efit.hrms.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveProcessDTO {

	private Long id;
	private String employeeName;
	private String employeeCode;
	private BigDecimal totalCompanyWorkingDays;
	private BigDecimal totalLeave;
	private BigDecimal lopLeave;
	private BigDecimal empTotalWorkingDays;
	private long month;
	private String year;
	@Column(name = "branchcode")
	private String branchCode;
	private String branch;
	

	private BigDecimal empSalaryDays;

	private boolean active ;
	private String createdBy;
	private Long orgId;
}
