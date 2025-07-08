package com.efit.hrms.dto;

import java.time.LocalDate;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelRequestDTO {

	private Long id;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String travelReason;
	private String modeOfTravel;
	private String approvingAuthorities;
	private String approvingAuthoritiesCode;
	private String approvingAuthoritiesEmail;
	private String employeeName;
	private String employeeCode;
	private String createdBy;
	private String branch;
	private String branchCode;
	private Long orgId;
	private String finYear;
}
