package com.efit.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "advance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advancegen")
	@SequenceGenerator(name = "advancegen", sequenceName = "advanceseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "advanceid")
	private Long id;

	@Column(name = "requestdate")
	private LocalDate requestDate;

	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;

	@Column(name = "department")
	private String department;
	@Column(name = "designation")
	private String designation;
	@Column(name = "reasonforadvance")
	private String reasonForAdvance;
	
	@Column(name = "advanceamount", precision = 10, scale = 2)
	private BigDecimal advanceAmount;
	@Column(name = "loanbalance", precision = 10, scale = 2)
	private BigDecimal loanBalance;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "approve")
	private boolean approve;

	@Column(name = "active")
	private boolean active = true;
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
	@Column(name = "finyear")
	private String finYear;
	
	@Lob
	@Column(name = "attachment", columnDefinition = "LONGBLOB")
	private byte[] attachment;


//	
//
//	@Column(name = "screencode", length = 5)
//	private String screenCode = "LP";
//
//	@Column(name = "screenname", length = 25)
//	private String screenName = "LEAVE PROCESS";

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
