package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupSalaryStructureDTO {

	private Long id;

	private String groupName;
	private String createdBy;
	private Long orgId;
	private boolean active;
	
	private String branchCode;
	private String branchName;
	private String finYear;
	
private List<GroupSalaryEarningsDTO> GroupSalaryEarningsDTO;
private List<GroupSalaryDeductionsDTO> GroupSalaryDeductionsDTO;

	
	
}
