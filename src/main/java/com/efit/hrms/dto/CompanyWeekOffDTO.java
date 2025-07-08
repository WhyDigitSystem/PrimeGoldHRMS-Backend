package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyWeekOffDTO {

	private String weekOffDays;
	private List<Integer> weekNumbers; // e.g., [1, 3], or [-1] for ALL

//	private String orgId;

}
