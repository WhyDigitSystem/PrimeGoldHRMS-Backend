package com.efit.hrms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CircularDTO {
	
	private Long id;
	
	private String circularcontent;
	private String department;
	private String type;

	private String createdBy;
//	private String finYear;
	private String branchName;
	private String branchCode;
	private Long orgId;
	private String circularTopic;
	private LocalDate expiresDate;

	
//	private byte[] postImage;
	
}
