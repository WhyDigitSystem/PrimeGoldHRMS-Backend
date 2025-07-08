package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherDeductionsDTO {

//	private Long id;
	private String section;
	private String deductions;
	private Long maxLimit;
	private Long declarationId;

	private String declaration;
	private String proof;
	private String status;
//	private byte[] proofImage;

}
