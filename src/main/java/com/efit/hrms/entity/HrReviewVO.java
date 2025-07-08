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
@Table(name = "hrreview")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrReviewVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hrreviewgen") 
	@SequenceGenerator(name = "hrreviewgen", sequenceName = "hrreviewseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "hrreviewid")
	private Long id;

	private String code;

	private String name;
	@Column(name = "elgibilityofpromption")
	private String elgibilityOfPromption;
	@Column(name = "elgibilityofincrement")
	private String elgibilityOfIncrement;
	private int score;
	@Column(name = "adheranceofemployeeengagement")
	private String adheranceOfEmployeeEngagement;
	private String remrks;

	@Column(name = "finyear")
	private Long finYear;

	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "promotionstatus")
	private String promotionStatus;

	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;

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
