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
@Table(name = "projectmaster")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMasterVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectmastergen")
	@SequenceGenerator(name = "projectmastergen", sequenceName = "projectmasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "projectmasterid")
	private Long id;

	@Column(name = "projectname")
	private String projectName;
	@Column(name = "projectcode")
	private String projectCode;
	@Column(name = "description")
	private String description;
	@Column(name = "active")
	private boolean active ;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel = false;

	@Column(name = "screencode", length = 5)
	private String screenCode = "PM";

	@Column(name = "screenname", length = 25)
	private String screenName = "PROJECT MASTER";

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
