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
@Table(name = "travelrequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelRequestVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "travelrequestgen")
	@SequenceGenerator(name = "travelrequestgen", sequenceName = "travelrequestseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "travelrequestid")
	private Long id;
	
	@Column(name = "fromdate")
	private LocalDate fromDate;
	@Column(name = "todate")
	private LocalDate toDate;
	@Column(name = "travelreason")
	private String travelReason;
	@Column(name = "modeoftravel")
	private String modeOfTravel;
	@Column(name = "approvingauthorities")
	private String approvingAuthorities;
	
	@Column(name = "approvingauthoritiescode")
	private String approvingAuthoritiesCode;
	@Column(name = "approvingauthoritiesemail")
	private String approvingAuthoritiesEmail;
	
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	
	@Column(name = "approvestatus")
	private String approveStatus;
	@Column(name = "approveby")
	private String approveBy;
	@Column(name = "approveon")
	private String approveOn;
	
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	
	@Column(name = "screencode", length = 30)
	private String screenCode = "TR";
	@Column(name = "screenname", length = 30)
	private String screenName = "TRAVELREQUEST";
	@Column(name = "branch",length = 30)
	private String branch;
	@Column(name = "branchcode",length = 10)
	private String branchCode;
	@Column(name = "active")
	private boolean active = true;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "finyear")
	private String finYear;
	@Column(name = "employeeemail")
	private String employeeEmail;
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
				
	}
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
