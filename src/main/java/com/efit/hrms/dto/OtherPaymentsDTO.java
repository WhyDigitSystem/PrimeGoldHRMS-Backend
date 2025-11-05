package com.efit.hrms.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

import com.efit.hrms.entity.OtherPaymentsVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherPaymentsDTO {

	private Long id;

	private String employeeCode;
	private String employeeName;
	private BigDecimal amount;
	private BigDecimal allowance;

	private String branch;
	private String branchCode;
	private Long month;
	private Long year;
	private String createdBy;
	private Long orgId;
	private boolean active;

}
