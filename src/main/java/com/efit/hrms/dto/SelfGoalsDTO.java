package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfGoalsDTO {

	private Long id;
	private String appraisalId;
	private String code;
	private String supervisorCode;
	private String name;
	private String supervisorName;
	private Long orgId;
	private String createdBy;
	private boolean active;
	private String finYear;
	
	private List<SelfGoalsDetailsDTO> selfGoalsDetailsDTO;
	
}
