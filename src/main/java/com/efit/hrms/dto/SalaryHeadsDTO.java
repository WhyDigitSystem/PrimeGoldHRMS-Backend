package com.efit.hrms.dto;

import javax.persistence.Column;

import com.efit.hrms.entity.SalaryHeadsVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryHeadsDTO {

	private Long id;
	private String heading;
	private String code;
	private String category;
	private String type;
	private boolean active;
	private String createdBy;
	private Long orgId;

	private String branch;
	private String branchCode;
//	private String finYear;
}
