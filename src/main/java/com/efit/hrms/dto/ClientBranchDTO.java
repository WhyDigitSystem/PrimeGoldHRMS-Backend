package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientBranchDTO {
	private Long id;
	private String branchCode;
	private String branch;
//	private Long orgId;

}
