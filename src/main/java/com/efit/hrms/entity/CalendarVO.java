package com.efit.hrms.entity;

import java.time.LocalDate;
import java.util.List;

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
@Table(name = "calendar")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calendargen")
	@SequenceGenerator(name = "calendargen", sequenceName = "calendarseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "calendarid")
	private Long id;
	@Column(name = "orgid")
	private long orgId;

	@Column(name = "eventtitle")
	private String eventTitle;
	@Column(name = "eventdate")
	private LocalDate date;
	@Column(name = "eventtype")
	private String eventType;
	@Column(name = "description")
	private String description;
	@Column(name = "empname")
	private String empName;
	@Column(name = "empcode")
	private String empCode;
	@Column(name = "fromtime")
	private String fromTime;
	@Column(name = "totime")
	private String toTime;
	
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branchname")
	private String branchName;
	@Column(name = "department")
	private String department;

	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "screencode", length = 30)
	private String screenCode = "CL";
	@Column(name = "screenname", length = 30)
	private String screenName = "calendar";
	@Column(name = "active")
	private boolean active=true;


	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";

	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}