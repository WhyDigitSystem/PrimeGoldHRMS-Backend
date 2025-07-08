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
@Table(name = "appraiser")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraiserVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appraisergen")
	@SequenceGenerator(name = "appraisergen", sequenceName = "appraiserseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "appraiserid")
	private Long id;

	@Column(name="appraisalid")
	private String appraisalId;
	@Column(name="empname")
	private String empName;
	@Column(name="empcode")
	private String empCode;
	private String department;
	@Column(name="supname")
	private String supName;
	@Column(name="supcode")
	private String supCode;
	@Column(name="finyear")
	private String finYear;
	@Column(name="createdby")
	private String createdBy;
	@Column(name="modifiedby")
	private String updatedBy;
	private boolean active;
	private boolean cancel;
	private String branch;
	@Column(name="orgid")
	private Long orgId;
	
	@OneToMany(mappedBy = "appraiserVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<AppraiserDetailsVO> appraiserDetailsVO;
	
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
