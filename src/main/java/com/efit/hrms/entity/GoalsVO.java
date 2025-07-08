package com.efit.hrms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
@Table(name = "goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalsVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goalsgen")
	@SequenceGenerator(name = "goalsgen", sequenceName = "goalsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "goalsid")
	private Long id;
	
	@Column(name = "appraisalid")
	private Long appraisalId;
	
	private String department;
	
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	
	@Column(name="finyear")
	private Long finYear;

	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	
	@OneToMany(mappedBy = "goalsVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<GoalsDetailsVO> goalsDetailsVO;
	
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
