package com.efit.hrms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompensatoryOffDTO {
	private Long id;
	private String leaveType;
	private String leaveCode;
	private LocalDate compOffDate;
	private String compOffDay;
	private String assignedBy;
	private BigDecimal totalDays;
	private String notes;
	private String notify;
	private String notifyCode;
	private String notifyEmail;
	
	private String employeeName;
	private String employeeCode;
	private String department;
	private String designation;
	
	private String branchCode;
	private String branch;
	private String createdBy;
	private Long orgId;
	
    List<CompoffNotifyDTO> compoffNotifyDTO;

}
