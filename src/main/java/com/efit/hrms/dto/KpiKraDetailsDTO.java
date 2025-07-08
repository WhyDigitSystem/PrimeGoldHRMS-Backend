package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KpiKraDetailsDTO {
	
	private Long kraId;
	private String kraDescription;
	private String ro;
	private Long kpiId;
	private String kpiDescription;

}
