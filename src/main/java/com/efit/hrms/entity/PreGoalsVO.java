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
@Table(name = "pregoals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreGoalsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pregoalsgen")
	@SequenceGenerator(name = "pregoalsgen", sequenceName = "pregoalsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "pregoalsid")
	private Long id;

	@Column(name="appraisalid")
	private String appraisalId;
	@Column(name = "code")
	private String code;
	@Column(name = "supervisorcode")
	private String supervisorCode;
	@Column(name = "name")
	private String name;
	@Column(name = "supervisorname")
	private String supervisorName;
	
	@Column(name="finyear")
	private Long finYear;

	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	
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
	
	@OneToMany(mappedBy ="preGoalsVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PreGoalsDetailsVO> PreGoalsDetailsVO;
	

}
