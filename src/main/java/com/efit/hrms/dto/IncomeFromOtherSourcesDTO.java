package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeFromOtherSourcesDTO {

	private Long declarationId;
	private byte[] proofImage;
}
