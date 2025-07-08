package com.efit.hrms.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.efit.hrms.entity.DeclarationDateVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeclarationDateDTO {

	private Long id;
	private String investmentDate;
	private String proofSubmissionDate;
	private boolean active;
	private Long orgId;
	private String createdBy;
	private String finYear;
	private String branch;
	private String branchCode;	
	
}
