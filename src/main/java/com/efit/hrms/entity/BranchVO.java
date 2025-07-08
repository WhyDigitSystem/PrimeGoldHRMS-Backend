package com.efit.hrms.entity;

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
@Table(name = "branch")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branchgen")
	@SequenceGenerator(name = "branchgen", sequenceName = "brancseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "branchid")
	private Long id;

	@Column(name = "company")
	private String company;
	@Column(name = "branch")
	private String branch;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "addressline1")
	private String addressLine1;
//	@Column(name = "addressline2")
//	private String addressLine2;
//	@Column(name = "panno")
//	private String pan;
	@Column(name = "contactperson")
	private String contactPerson;	
	@Column(name = "gstin")
	private String gstIn;
	@Column(name = "phone")
	private String phone;
	@Column(name = "state")
	private String state;
	@Column(name = "city")
	private String city;
	@Column(name = "pincode")
	private String pinCode;
	@Column(name = "country")
	private String country;
//	@Column(name = "stateno")
//	private String stateNo;
//	@Column(name = "statecode")
//	private String stateCode;
//	@Column(name = "region")
//	private String region;
	@Column(name = "email")
	private String email;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "active")
	private boolean active;

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
