package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTOnew {
	
    private String branch;
    private String branchCode;
    private String department;
    private Long orgId;
}
