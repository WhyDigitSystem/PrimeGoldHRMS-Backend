package com.efit.hrms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO {

	private Long id;
	
	
	private String leaveType;
	
	private String leaveCode;

	
	private LocalDate fromDate;
	
	private LocalDate toDate;
	
	private String selectLeave;
	
	private BigDecimal totalDays;
	
	private String notes;
	
	private String notify;
	private String notifyCode;
	private String notifyEmail;
	
	private String employeeName;
	private String employeeCode;

	
	private String department;
	
	private String designation;
	
	private LocalDate compOffDate;

	private String branchCode;
	private String branch;
//	private String finYear;

	private String createdBy;
	
	private String updatedBy;
	
	private Long orgId;
	
	private boolean cancel;
	
	private String cancelRemarks;

	List<LeaveRequestNotifyDTO> leaveRequestNotifyDTO;
}
