package com.efit.hrms.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDTO {
	private Long id;
	private long orgId;
//	private String branchId;
	private String department;
	private LocalDate holidayDate;
	private String day;
	private String branchCode;
	private String branchName;
	private String festival;
	private String createdBy;
	private byte[] holidaysImage;

}
