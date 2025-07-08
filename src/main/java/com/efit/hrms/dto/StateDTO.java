package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateDTO {

private Long id;
	
	private String stateCode;
	private String stateName;
    private String country;
    private String region;
    private String stateNumber;
    private boolean active;
    private String createdBy;
	private Long orgId;
	private boolean cancel;

}