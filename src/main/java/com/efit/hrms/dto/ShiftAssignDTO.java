package com.efit.hrms.dto;

import java.util.List;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftAssignDTO {

	private Long id;
	private String shiftType;
	private String shiftCode;
	private String description;
	private long orgId;
	private String branchCode;
	private String branch;
	private String finYear;
	private String department;

	private String type;
	private String contractor;
	private String contactPerson;
	private String contactNumber;
	private String contactEmail;


	private String createdBy;
	private boolean active;

	List<ShiftAssignDetailsDTO> shiftAssignDetailsDTO;

	
}
