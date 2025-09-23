package com.efit.hrms.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
	
	private int present;
    private int absent;
    private int missingPunch;
    private Map<String, SubDepartmentResponse> subDepartments;

}
