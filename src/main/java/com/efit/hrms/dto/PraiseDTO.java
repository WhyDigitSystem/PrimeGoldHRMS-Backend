package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PraiseDTO {
	
	private Long id;
	private long orgId;
	private String branchCode;
	private String branchName;
	private String department;
	private Long circularId;
	private String userName;
	private String liked;
	

}
