package com.efit.hrms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftAssignDetailsDTO {

	private String employeeCode;
	private String employeeName;
	private String shiftType;
	private String shiftCode;

	private String inTime;
	private String outTime;
	private String department;

	private String hours;
	private LocalDate effectiveFrom;
	private LocalDate effectiveTo;

}
