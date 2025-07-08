package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLeaveDetailsDTO {
	
	private Long id;
	private String leaveType;
	private String leaveCode;
	private String noOfDays;
	private String effctive;

}
