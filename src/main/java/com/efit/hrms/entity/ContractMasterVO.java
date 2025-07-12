package com.efit.hrms.entity;

import java.time.LocalDate;

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
@Table(name="contractmaster")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractMasterVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contractmastergen")
	@SequenceGenerator(name = "contractmastergen", sequenceName = "contractmasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "contractmasterid")
	private Long id;
	@Column(name = "contractorcode")
	private String contractorCode;
	@Column(name = "contractor")
	private String contractor;
	@Column(name = "contactperson")
	private String contactPerson;
	@Column(name = "contactnumber")
	private String contactNumber;
	@Column(name = "startdate")
	private LocalDate startDate;
	@Column(name = "enddate")
	private LocalDate endDate;
	@Column(name = "address")
	private String address;
	@Column(name = "status")
	private String status;
	@Column(name = "panno")
	private String panNo;
	@Column(name = "gst")
	private String gst;
	
	
	@Column(name = "orgid")
	private long orgId;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branch")
	private String branch;
	@Column(name = "finyear")
	private String finYear;
	
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "active")
	private boolean active;
	@Column(name = "remarks")
	private String remarks;
	
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
}
}

