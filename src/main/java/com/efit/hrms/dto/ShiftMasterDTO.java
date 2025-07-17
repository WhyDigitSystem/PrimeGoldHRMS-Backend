package com.efit.hrms.dto;

import java.time.LocalTime;

import javax.persistence.Column;

import com.efit.hrms.entity.ShiftMasterVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftMasterDTO {

	
	private Long id;
	private String shiftCode;
	private String shift;
	private String inTime;
	private String outTime;
	private String breakTime;
	private String graceTime;
	private boolean nightShift;
	
	private long orgId;
	private String branchCode;
	private String branch;
	private String finYear;
	
	private String createdBy;
	private boolean active;
	
}
