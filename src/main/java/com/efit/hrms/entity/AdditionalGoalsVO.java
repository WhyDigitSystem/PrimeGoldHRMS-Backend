package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "additionalgoals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalGoalsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "additionalgoalsgen")
	@SequenceGenerator(name = "additionalgoalsgen", sequenceName = "additionalgoalsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "additionalgoalsid")
	private Long id;

	@Column(name="areaofimportance")
	private String areaOfImportance;
	@Column(name="keyperformanceindicator")
	private String keyPerformanceIndicator;
	@Column(name="performanceindicator")
	private String performanceIndicator;

	private String goal;

	private String remarks;

	@Column(name = "finyear")
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
}