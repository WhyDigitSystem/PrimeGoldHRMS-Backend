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
@Table(name = "otherpayments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherPaymentsVO {

	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otherpaymentsgen")
	@SequenceGenerator(name = "otherpaymentsgen", sequenceName = "otherpaymentsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "otherpaymentsid")
	private Long id;
	
	@Column(name = "employeecode",length = 30)
	private String employeeCode;
	@Column(name = "employee",length = 150)
	private String employeeName;
	@Column(name = "amount",length = 150)
	private BigDecimal amount;
	
	
	@Column(name = "branch",length = 30)
	private String branch;
	@Column(name = "branchcode",length = 30)
	private String branchCode;
	
	
	@Column(name = "createdby",length = 30)
	private String createdBy;
	@Column(name = "modifiedby",length = 30)
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel=false;
	@Column(name = "cancelremarks",length = 150)
	private String cancelRemark;
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "month",length = 30)
	private Long month;
	@Column(name = "year",length = 50)
	private Long year;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
