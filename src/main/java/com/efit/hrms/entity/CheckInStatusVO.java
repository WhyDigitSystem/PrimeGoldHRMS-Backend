package com.efit.hrms.entity;

import java.time.LocalDateTime;

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
@Table(name="checkinstatus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInStatusVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checkinstatusgen")
	@SequenceGenerator(name = "checkinstatusgen", sequenceName = "checkinstatusseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "checkinstatusid")
	private Long id;
	@Column(name = "empcode")
	private String empcode;
	@Column(name = "empname")
	private String empName;
	@Column(name = "status")
	private String status;
	@Column(name = "branch")
	private String branch;
	@Column(name = "orgid")
	private long orgId;

	
}
