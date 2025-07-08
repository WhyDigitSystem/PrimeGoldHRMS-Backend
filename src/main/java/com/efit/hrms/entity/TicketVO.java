package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketgen")
	@SequenceGenerator(name = "ticketgen", sequenceName = "ticketseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "ticketid")
	private Long id;
	
	private String subject;
	
	private String description;
	
	@Column(name="createdby")
	private String createdBy;
	@Column(name="modifiedy")
	private String updatedBy;
	@Column(name="username")
	private String userName;
	@Column(name = "orgid")
	private Long orgId;
	
	private String status;

	@Column(name="statusflag")
	private Boolean statusFlag=true;
	
	@Column(name="notificationflag")
	private Boolean notificationFlag=false;
	
	private boolean cancel;
	
	@Lob
	@Column(name = "screenshot", columnDefinition = "LONGBLOB")
	private byte[] screenShot;
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}

