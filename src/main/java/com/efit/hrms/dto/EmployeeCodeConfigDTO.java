package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCodeConfigDTO {
	
	private Long id;
    private Long orgId;
	private String company;
	private int companyCode;
	private int departmentCode;
	private int branchCode;
	private int year;
	private int seq;
	private int seqDigit;
    private String codePattern;

}
