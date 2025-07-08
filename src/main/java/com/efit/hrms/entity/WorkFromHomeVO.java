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
@Table(name = "workfromhome")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkFromHomeVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workfromhomegen")
	@SequenceGenerator(name = "workfromhomegen", sequenceName = "workfromhomeseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "workfromhomeid")
	private Long id;
	
	@Column(name = "wfhdate")
	private LocalDate wfhDate;
	@Column(name = "reason")
	private String reason;
	@Column(name = "workaccomplished")
	private String workAccomplished;
	@Column(name = "reportingmanager")
	private String reportingManager;
	@Column(name = "reportingmanagercode")
	private String reportingManagerCode;
	@Column(name = "reportingmanageremail")
	private String reportingManagerEmail;
	@Column(name = "departmenthead")
	private String departmentHead;
	@Column(name = "departmentheadcode")
	private String departmentHeadCode;
	@Column(name = "departmentheademail")
	private String departmentHeadEmail;
	
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "employeeemail")
	private String employeeEmail;
	
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
	private String screenCode = "WFH";
	@Column(name = "screenname", length = 30)
	private String screenName = "WORKFROMHOME";
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
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
				
	}
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
}
