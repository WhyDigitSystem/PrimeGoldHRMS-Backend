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
@Table(name = "weightage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeightageVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weightagegen")
	@SequenceGenerator(name = "weightagegen", sequenceName = "weightageseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "weightageid")
	private Long id;

	private String level;
	@Column(name = "businessoperations")
	private int businessOperations;
	@Column(name = "valuecreation")
	private int valueCreation;
	@Column(name = "peopleengagement")
	private int peopleEngagement;

	private String remarks;
	@Column(name = "invlid")
	private int invlId;
	
	@Column(name="finyear")
	private Long finYear;

	private boolean active;
	private boolean cancel;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;

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
