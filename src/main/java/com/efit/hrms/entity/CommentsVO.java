package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentsgen")
	@SequenceGenerator(name = "commentsgen", sequenceName = "commentsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "commentsid")
	private Long id;
	
	private String comments;
	
	@Column(name="createdby")
	private String createdBy;
	@Column(name="modifiedy")
	private String updatedBy;
	@Column(name="username")
	private String userName;
	@Column(name = "orgid")
	private Long orgId;
	
	private String status;
	@Column(name = "ticketid")
	private Long ticketId;
	
	@Column(name="notificationflag")
	private Boolean notificationFlag=false;
	
	//private boolean cancel;
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
}

