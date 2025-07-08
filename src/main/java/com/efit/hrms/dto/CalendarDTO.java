package com.efit.hrms.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDTO {
	private Long id;
	private long orgId;
	private   String eventTitle;
	private LocalDate date;
	private   String eventType;
	private String description;
	private String empName;
	private String empCode;
	private String fromTime;
	private String toTime;

	private String branchCode;
	private String branchName;
	private String department;
	private String createdBy;
	

}
