package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pollvote")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollVoteVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pollvotegen")
	@SequenceGenerator(name = "pollvotegen", sequenceName = "pollvoteseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "pollvoteid")
	private Long id;
	@Column(name = "orgid")
	private long orgId;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branchname")
	private String branchName;
	@Column(name = "department")
	private String department;

	@Column(name = "pollid")
	private long pollId;
	@Column(name = "question")
	private String question;
	@Column(name = "username")
	private String userName;
	@Column(name = "options")
	private String options;
	
	
}
