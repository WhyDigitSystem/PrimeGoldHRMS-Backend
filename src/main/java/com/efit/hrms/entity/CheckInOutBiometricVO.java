package com.efit.hrms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "checkinoutbiometric")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInOutBiometricVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checkinoutbiometricgen")
	@SequenceGenerator(name = "checkinoutbiometricgen", sequenceName = "checkinoutbiometricseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "checkinoutbiometricid")
	private Long id;
	@Column(name = "empcode")
	private String empCode;
	@Column(name = "empname")
	private String empName;
//	@Column(name = "companycode")
//	private String companyCode;
	@Column(name = "branch")
	private String branch;
	@Column(name = "branchcode")
	private String branchCode;	
	@Column(name = "finyear")
	private String finyear;
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
	private String attendanceMode="BIOMETRIC";
	@Column(name = "screencode", length = 5)
	private String screenCode = "CIOB";

	@Column(name = "screenname", length = 25)
	private String screenName = "CHECKINOUTBIOMETRIC";
	@Column(name = "createdby")
	private String createdBy;
	
	@Column(name = "email")
	private String email;

	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
	
	

}
