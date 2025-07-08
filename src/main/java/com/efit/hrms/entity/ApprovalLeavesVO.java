package com.efit.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "approvalleaves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalLeavesVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approvalleavesgen")
	@SequenceGenerator(name = "approvalleavesgen", sequenceName = "approvalleavesseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "approvalleavesid")
	private Long id;
	
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "leavetype")
	private String leaveType;
	@Column(name = "leavedate")
	private LocalDate leaveDate;
	@Column(name = "totalleave")
	private BigDecimal totalLeave;
	@Column(name = "approvestatus")
	private String approveStatus;
	@Column(name = "approveby")
	private String approveBy;
	@Column(name = "orgid")
	private Long orgId;
	
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branch")
	private String branch;
//	@Column(name = "finyear")
//	private String finYear;
//	@Column(name = "lpstatus")
//	private String lpstatus;
	
	
	
	
}
