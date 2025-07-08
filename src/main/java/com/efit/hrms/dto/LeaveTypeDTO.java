package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeDTO {
	
	private Long id;

	
	private String leaveType;
	
	private String leaveCode;
	
	private Long orgId;
	
	private String leaveApplicable;
	private boolean carryForward;

	
	private String createdBy;
	
	private String updatedBy;
	
	private boolean active;
	private String branch;
	private String branchCode;
	private String finYear;
	private String salaryDeduction;
	
//	private List<CompanyCarryForwardDTO>companyCarryForwardDTO;



}
