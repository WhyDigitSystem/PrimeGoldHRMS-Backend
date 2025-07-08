package com.efit.hrms.dto;

import java.time.LocalDate;

import javax.persistence.Column;

import com.efit.hrms.entity.WorkFromHomeVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkFromHomeDTO {

	private Long id;
	private LocalDate wfhDate;
	private String reason;
	private String workAccomplished;
	private String reportingManager;
	private String reportingManagerCode;
	private String reportingManagerEmail;
	private String departmentHead;
	private String departmentHeadCode;
	private String departmentHeadEmail;
	
	private String employeeName;
	private String employeeCode;
	private String employeeEmail;
	private String createdBy;
	private String branch;
	private String branchCode;
	private Long orgId;
	private String finYear;

}
