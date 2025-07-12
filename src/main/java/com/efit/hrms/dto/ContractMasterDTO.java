package com.efit.hrms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractMasterDTO {

	private Long id;
	private String contractorCode;
	private String contractor;
	private String contactPerson;
	private String contactNumber;
	private LocalDate startDate;
	private LocalDate endDate;
	private String address;
	private String status;
	private String panNo;
	private String gst;
	
	
	private long orgId;
	private String branchCode;
	private String branch;
	private String finYear;
	private boolean active;

	private String createdBy;
	
	
}
