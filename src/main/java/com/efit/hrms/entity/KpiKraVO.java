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
@Table(name = "kpikra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiKraVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kpikragen")
	@SequenceGenerator(name = "kpikragen", sequenceName = "kpikraseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "kpikraid")
	private Long id;

	@Column(name = "appraisalid")
	private Long appraisalId;

	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "branchcode")
	private String branchCode;
	
	private String branch;
	
	@Column(name="finyear")
	private String finYear;

	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	
	@OneToMany(mappedBy = "kpiKraVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<KpiKraDetailsVO> kpiKraDetailsVO;
	
	@OneToMany(mappedBy = "kpiKraVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<KpiVO> kpiVO;

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
