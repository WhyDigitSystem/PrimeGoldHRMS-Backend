package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubDepartmentResponse {
	
	private int present;
    private int absent;
    private int missingPunch;

}
