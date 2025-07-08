package com.efit.hrms.entity;

import java.math.BigDecimal;

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
@Table(name = "leavebalance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBalanceVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leavebalancegen")
	@SequenceGenerator(name = "leavebalancegen", sequenceName = "leavebalanceseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "leavebalanceid")
	private Long id;

	@Column(name = "employeecode",length = 30)
	private String employeeCode;
	@Column(name = "employee",length = 150)
	private String employeeName;
	@Column(name = "leavetype")
	private String leaveType;
	@Column(name = "leavecode")
	private String leaveCode;
	@Column(name = "totalleave")
	private BigDecimal totalLeave;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "leavestatus")
	private String leaveStatus;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branch")
	private String branch;
//	@Column(name = "finyear")
//	private String finYear;
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
