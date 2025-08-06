package com.efit.hrms.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
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
@Table(name = "attendancesummary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSummaryVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendancesummarygen")
	@SequenceGenerator(name = "attendancesummarygen", sequenceName = "attendancesummaryseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "attendancesummaryid")
	private Long id;
	@Column(name = "empcode")
	private String empCode;
	@Column(name = "empname")
	private String empName;
	@Column(name = "branch")
	private String branch;
	@Column(name = "branchcode")
	private String branchCode;	
	@Column(name = "department")
	private String department;
	@Column(name = "finyear")
	private String finyear;
	@Column(name = "month")
	private int month;
	@Column(name = "orgid")
	private long orgId;
	@Column(name = "totaldays")
	private int totalDays;
	@Column(name = "holidays")
	private int holidays;
	@Column(name = "weekoff")
	private int weekoff;
	@Column(name = "leaves")
	private BigDecimal leaves;
	@Column(name = "lop")
	private BigDecimal lop;
	@Column(name = "absent")
	private BigDecimal absent;
	@Column(name = "present")
	private BigDecimal present;
	@Column(name = "salarydays")
	private BigDecimal salarydays;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedBy")
	private String updatedBy;
	
	@Column(name = "othours")
	private Long otHours;
	
	@Column(name = "approvestatus")
	private String approveStatus;
	@Column(name = "approveby")
	private String approveBy;
	@Column(name = "approveon")
	private String approveOn;
	
	
	@Column(name = "screencode", length = 5)
	private String screenCode = "AS";

	@Column(name = "screenname", length = 25)
	private String screenName = "ATTENDANCE SUMMARY";


	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}

