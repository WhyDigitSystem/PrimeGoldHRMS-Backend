package com.efit.hrms.entity;

import javax.persistence.Column;
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
@Table(name = "supervisor1feedBack")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supervisor1FeedBackVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supervisor1feedBackgen")
	@SequenceGenerator(name = "supervisor1feedBackgen", sequenceName = "supervisor1feedBackseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "supervisor1feedBackid")
	private Long id;

	private String code;

	private String name;
	@Column(name = "elgiblityofpromption")
	private String elgiblityOfPromption;
	@Column(name = "elgiblityofincrement")
	private String elgiblityOfIncrement;

	private int score;
	@Column(name = "adheranceofemployee")
	private String adheranceOfEmployee;
	@Column(name = "promotionstatus")
	private String promotionStatus;
	@Column(name = "hrremarks")
	private String hrRemarks;

	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;

	@Column(name = "finyear")
	private Long finYear;

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

	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
