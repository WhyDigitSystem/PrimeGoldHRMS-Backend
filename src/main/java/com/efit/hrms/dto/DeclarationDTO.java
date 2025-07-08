package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeclarationDTO {

	private Long id;
	private long orgId;
	private String employeeName;
	private String employeeCode;
	private String branchCode;
	private String branch;
	private String department;
	private String createdBy;
	private String finYear;

//	List<MyDeclarationDTO> myDeclarationDTO;
//	List<OtherDeclarationDTO> otherDeclarationDTO;
//	List<OneCroreFiveLacDeductionsDTO> oneCroreFiveLacDeductionsDTO;

	
}
