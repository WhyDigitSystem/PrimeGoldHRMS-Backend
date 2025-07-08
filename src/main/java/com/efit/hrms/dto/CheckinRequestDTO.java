package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckinRequestDTO {

	private String entryTime;
	private String reportingPersonMail;
	private String date;
	private String empCode;
	private String empName;
	private long orgId;
	private String branch;
	
	private String notify;
	private String notifyCode;
	private String notifyEmail;




}


	

	
