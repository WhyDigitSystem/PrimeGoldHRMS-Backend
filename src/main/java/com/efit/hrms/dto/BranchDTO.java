package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO {

	private Long id;
	private String company;
	
	private String branch;
	
	private String branchCode;
	
	private Long orgId;
	
	private String addressLine1;
	
//	private String addressLine2;

//	private String pan;

	private String gstIn;
	
	private String contactPerson;	
	
	private String email;

	private String phone;
	
	private String state;

	private String city;

	private String pinCode;
	
	private String country;

//	private String stateNo;
//
//	private String stateCode;
//
//	private String region;
//
//	private String lccurrency;

	private boolean cancel;
	
	private String cancelRemarks;

	private String createdBy;

	private boolean active;
	
	private String userid;
}
