package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

	private Long id;

	private String group;
	private String createdBy;
	private Long orgId;
	private String cancelRemark;
	private boolean active;
	private String finYear;
	
	private List<GroupDetailsDTO> groupDetailsDTO;
	
}
