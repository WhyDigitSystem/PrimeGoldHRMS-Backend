package com.efit.hrms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appraisee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraiseeVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appraiseegen")
	@SequenceGenerator(name = "appraiseegen", sequenceName = "appraiseeseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "appraiseeid")
	private Long id;

	private String name;

	private String code;

	private String designation;

	private String department;

	private String branch;
	@Column(name = "reportinghead")
	private String reportingHead;
	@Column(name = "reportingheadcode")
	private String reportingHeadCode;
	@Column(name = "reportingheaddesignation")
	private String reportingHeadDesignation;

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

	@OneToMany(mappedBy = "appraiseeVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<AppraiseeDetailsVO> appraiseeDetailsVO;
	
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
