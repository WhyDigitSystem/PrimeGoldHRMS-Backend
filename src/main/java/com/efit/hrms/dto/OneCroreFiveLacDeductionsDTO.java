package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneCroreFiveLacDeductionsDTO {

	private String section;
	private String deductions;
	private Long maxLimit;
	
	private String declaration;
	private String status;
	private Long declarationId;

//	private byte[] proofImage;
}
