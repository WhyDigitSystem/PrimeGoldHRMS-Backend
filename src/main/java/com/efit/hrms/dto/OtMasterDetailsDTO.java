package com.efit.hrms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtMasterDetailsDTO {

	private String slab;
	private int minHours;
	private int maxHours;
	private String otrate;
	private LocalDate effectiveFrom;
	private LocalDate effectiveTo;
	private boolean applicable;
	

}
