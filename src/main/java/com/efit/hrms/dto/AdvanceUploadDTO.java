package com.efit.hrms.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

import com.efit.hrms.entity.AdvanceUploadVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceUploadDTO {

	private Long id;
	private String employeeCode;
	private String employeeName;
	private BigDecimal bank;
	private BigDecimal cash;
	private Long month;
	private Long year;

	private String branch;
	private String branchCode;

	private String createdBy;
	private Long orgId;
	@Column(name = "active")
	private boolean active;

}
