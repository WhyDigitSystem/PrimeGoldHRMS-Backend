package com.efit.hrms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLeaveDTO {
	
	private String leaveCode;
	private String leaveType;
//	private String leaveApplicable;
	private BigDecimal totalLeave;

//	private String effective;
//	
//	private String carryForward;
	private LocalDate effectiveFrom;


	
}
