package com.efit.hrms.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;

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
	private String inTime;
	private String outTime;
	
	private String hours;
	private LocalDate effectiveFrom;
	private LocalDate effectiveTo;

}
