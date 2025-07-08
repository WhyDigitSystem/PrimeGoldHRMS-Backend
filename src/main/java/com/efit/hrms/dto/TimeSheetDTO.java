package com.efit.hrms.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetDTO {

	private Long id;
	private String employeeName;
	private String employeeCode;
	private LocalDate date;
	private boolean active ;
	private String createdBy;
	private Long orgId;
	private String branchCode;
	private String branch;
	
	List<TimeSheetDetailsDTO> timeSheetDetailsDTO;
	
}
