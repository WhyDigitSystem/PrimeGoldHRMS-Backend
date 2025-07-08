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
@Table(name = "salaryheads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryHeadsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salaryheadsgen")
	@SequenceGenerator(name = "salaryheadsgen", sequenceName = "salaryheadsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "salaryheadsid")
	private Long id;
	@Column(name = "heading", length = 100)
	private String heading;
	@Column(name = "code", length = 100)
	private String code;
	@Column(name = "category", length = 50)
	private String category;
	@Column(name = "type", length = 50)
	private String type;
	@Column(name = "active")
	private boolean active;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;

    @Column(name = "branch", length = 25)
    private String branch;
    @Column(name = "branchcode", length = 20)
    private String branchCode;
    @Column(name = "cancelremarks", length = 50)
    private String cancelRemarks;
//    @Column(name = "finyear", length = 5)
//    private String finYear;
    @Column(name = "screencode", length = 5)
    private String screenCode = "SH";
    @Column(name = "screenname", length = 25)
    private String screenName = "SALARY HEADS";

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

