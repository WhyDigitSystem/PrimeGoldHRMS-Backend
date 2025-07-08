package com.efit.hrms.dto;

import java.time.LocalDate;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDTO {
	
	private Long id;

	private long orgId;
	private String branchCode;
	private String branchName;
	private String department;
	private String topic;
	private String announcement;
	private String createdBy;
	private LocalDate expiresDate;


}
