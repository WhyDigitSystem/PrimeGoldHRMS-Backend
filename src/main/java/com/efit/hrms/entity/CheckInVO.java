package com.efit.hrms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "checkin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checkingen")
	@SequenceGenerator(name = "checkingen", sequenceName = "checkinseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "checkinid")
	private Long id;
	@Column(name = "empcode")
	private String empCode;
	@Column(name = "empname")
	private String empName;
//	@Column(name = "companycode")
//	private String companyCode;
	@Column(name = "branch")
	private String branch;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "checkindate")
	private LocalDate checkInDate = LocalDate.now();
	@Column(name = "entrytime")
	private LocalTime entryTime = LocalTime.now();
	@Column(name = "status")
	private String status;
	@Column(name = "orgid")
	private long orgId;
	@Column(name = "attendancemode")
	private String attendanceMode;
	@Column(name = "screencode", length = 5)
	private String screenCode = "CIO";

	@Column(name = "screenname", length = 25)
	private String screenName = "CHECKINOUT";

	@Column(name = "approvalstatus")
	private String approvalStatus;
	@Column(name = "approveby")
	private String approveBy;
	@Column(name = "approveon")
	private String approveOn;
	
	
	@Column(name = "notify")
	private String notify;
	@Column(name = "notifycode")
	private String notifyCode;
	@Column(name = "notifyemail")
	private String notifyEmail;
	@Column(name = "email")
	private String email;

	@Column(name = "latitude")
	private Double latitude;
	@Column(name = "longitude")
	private Double longitude;
	@Column(name = "wfh")
	private String workFromHome;
	@Column(name = "locationaddress")
	private String locationAddress;
	@Column(name = "createdon")
	private LocalDateTime createdOn;
}
