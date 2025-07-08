package com.efit.hrms.dto;

import javax.persistence.Column;

import com.efit.hrms.entity.DeclarationVO;
import com.efit.hrms.entity.TaxSavingAllowancesVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxSavingAllowancesDTO {

	private Long declarationId;
	private String section;

	private String deductionsName;
	private Long maxexemptionLimit;
	
	private String declaration;
	private String proof;
	private String status;
	
	
}
