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
@Table(name = "appraisalperiod")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalPeriodVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appraisalperiodgen")
	@SequenceGenerator(name = "appraisalperiodgen", sequenceName = "appraisalperiodseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "appraisalperiodid")
	private Long id;

	@Column(name = "finyear")
	private Long finYear;
	@Column(name = "type")
	private String type;
	@Column(name = "appraisalid")
	private Long appraisalId;
	@Column(name = "effectiveform")
	private LocalDate effectiveForm;
	@Column(name = "effectiveto")
	private LocalDate effectiveTo;
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
