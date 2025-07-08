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
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leaveprocess")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveProcessVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leaveprocessgen")
	@SequenceGenerator(name = "leaveprocessgen", sequenceName = "leaveprocessseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "leaveprocessid")
	private Long id;

	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "totalcompanyworkingdays")
	private BigDecimal totalCompanyWorkingDays;
	@Column(name = "month")
	private long month;
	@Column(name = "year")
	private String year;
	@Column(name = "totalleave")
	private BigDecimal totalLeave;
	@Column(name = "lopleave")
	private BigDecimal lopLeave;
	@Column(name = "emptotalworkingdays")
	private BigDecimal empTotalWorkingDays;
	@Column(name = "empsalarydays")
	private BigDecimal empSalaryDays;
	@Column(name = "approvedstatus")
	private String approvedStatus;

	@Column(name = "active")
	private boolean active=true ;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel = false;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branch")
	private String branch;
	

	@Column(name = "screencode", length = 5)
	private String screenCode = "LP";

	@Column(name = "screenname", length = 25)
	private String screenName = "LEAVE PROCESS";

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
}
