package com.efit.hrms.dto;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInOutBiometricDTO {

	private Long id;
	private String empCode;
	private String empName;
	private String branch;
	private String branchCode;	
	private String finyear;
	private boolean status;
	private long orgId;
	private String email;
}
