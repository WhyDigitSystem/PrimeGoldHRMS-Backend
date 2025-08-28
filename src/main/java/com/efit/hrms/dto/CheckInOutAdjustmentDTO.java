package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInOutAdjustmentDTO {

	private String entryIn;
	private String entryOut;
	private String reportingPersonMail;
	private String date;
	private String empCode;
	private String empName;
	private Long orgId;
	private String branch;
	private String email;
	private String branchCode;

	private String notify;
	private String notifyCode;
	private String notifyEmail;
}
