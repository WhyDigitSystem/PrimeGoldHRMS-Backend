package com.efit.hrms.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
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
@Table(name = "attendancedaily")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDailyVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendancedailygen")
	@SequenceGenerator(name = "attendancedailygen", sequenceName = "attendancedailyseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "attendancedailyid")
	private Long id;
	@Column(name = "empcode")
	private String empCode;
	@Column(name = "empname")
	private String empName;
	@Column(name = "branch")
	private String branch;
	@Column(name = "branchcode")
	private String branchCode;	
	@Column(name = "finyear")
	private String finyear;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "checkindate")
	private LocalDate checkInDate;
	@Column(name = "checkoutdate")
	private LocalDate checkOutDate;
	@Column(name = "intime")
	private LocalTime inTime;
	@Column(name = "outtime")
	private LocalTime outTime;
	@Column(name = "orgid")
	private long orgId;
	@Column(name = "grosshours")
	private long grossHours;
	@Column(name = "effectivehours")
	private long effectiveHours;
	
	@Column(name = "attendancemode")
	private String attendanceMode;
	@Column(name = "screencode", length = 5)
	private String screenCode = "Ad";

	@Column(name = "screenname", length = 25)
	private String screenName = "ATTENDANCE DAILY";


	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
