package com.efit.hrms.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadCheckInDTO {


	private String branch;
	private String checkInDate;
	private String entryTime;

	private String empCode;
	private String empName;
	private long orgId;
	private String status;
}
