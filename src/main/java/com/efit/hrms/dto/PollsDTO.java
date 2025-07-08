package com.efit.hrms.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollsDTO {
	private Long id;

	private long orgId;

	private String branchCode;

	private String branchName;

	private String department;
	
	private LocalDate expiresDate;

	private String question;

	private String multiSelect;

	private Long maxSelection;
	private String type;

	

	private String createdBy;



	
	private List<PollDetailsDTO> pollDetailsDTO;
}

