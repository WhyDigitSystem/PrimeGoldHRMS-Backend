package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtMasterDTO {

	private Long id;
	private String otType;
	private String otCategory;
	
	private long orgId;
	private String branchCode;
	private String branch;
	private String finYear;
	
	private String createdBy;
	private boolean active;

	List<OtMasterDetailsDTO>  otMasterDetailsDTO;
	
	
}
