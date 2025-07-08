package com.efit.hrms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRoleAccessDTO {
	private String role;
	private Long roleId;
	private LocalDate startDate=LocalDate.now();
	private LocalDate endDate=LocalDate.now();
	


}
