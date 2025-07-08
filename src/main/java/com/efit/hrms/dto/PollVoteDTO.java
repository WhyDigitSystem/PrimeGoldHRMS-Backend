package com.efit.hrms.dto;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollVoteDTO {
	

	private long orgId;

	private String branchCode;

	private String branchName;

	private String department;


	private long pollId;

	private String question;

	private String userName;

	private String options;
	

	

}
