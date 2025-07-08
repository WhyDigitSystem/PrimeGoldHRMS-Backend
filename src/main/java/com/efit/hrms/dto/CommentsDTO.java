package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDTO {

private Long id;
	
	private String comments;
	
	private String createdBy;
	private String userName;
	private Long orgId;
	private Long ticketId;
	
//	private String status;
	
}

