package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermissionDTO {
	
	
	private String screenId;
	
    private String screenName;
    

	private boolean canRead;
	
	private boolean canWrite;
	
	private boolean canDelete;
	
	
}

