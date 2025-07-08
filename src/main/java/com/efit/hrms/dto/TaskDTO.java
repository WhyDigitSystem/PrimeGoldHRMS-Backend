package com.efit.hrms.dto;

import java.time.LocalDate;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

	private Long id;
	private long orgId;
	private String branchCode;
	private String branchName;
	private String department;

	private String userName;
	
	private String taskTitle;
	private String taskDescription;
	private String category;
	private String priority;
	private String assignedBy;
	private String assignedTo;
	private String status;
	private LocalDate dueDate;
	private String remarks;
	
	private String finYear;
	private String createdBy;
	private String updatedBy;
}
