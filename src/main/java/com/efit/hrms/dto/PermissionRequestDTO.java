package com.efit.hrms.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequestDTO {
private Long id;

	private LocalDate date;

	private String fromTime;

	private String toTime;

	private long totalHours;

	private String notes;

	private String notify;
	private String notifyCode;

	
	private String createdBy;
		
	private Long orgId;
	
    private String branch;
    private String branchCode;
	private String employeeName;
	private String employeeCode;
	private String employeeEmail;

	
	List<PermissionRequestNotifyDTO> permissionRequestNotifyDTO;
	

}
