package com.efit.hrms.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginBranchAccessDTO {
	
	private String branch;
	private String branchCode;
	

}