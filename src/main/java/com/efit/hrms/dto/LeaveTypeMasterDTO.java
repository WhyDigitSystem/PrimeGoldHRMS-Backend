package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeMasterDTO {

	private Long id;
	private String leaveType;
	private String leaveCode;
	private int noOfDays;
	private String effective;
	private Long orgId;
	private String createdBy;
	private String updatedBy;
	private String carryForward;
	private String leaveApplicable;
	
	private boolean active;
	
}
