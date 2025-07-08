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
@Table(name = "declaration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeclarationVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "declarationgen")
	@SequenceGenerator(name = "declarationgen", sequenceName = "declarationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "declarationid")
	private Long id;
	@Column(name = "orgid")
	private long orgId;

	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;

	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branch")
	private String branch;
	@Column(name = "department")
	private String department;
	@Column(name = "finyear")
	private String finYear;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "screencode", length = 30)
	private String screenCode = "DL";
	@Column(name = "screenname", length = 30)
	private String screenName = "declaration";
	@Column(name = "active")
	private boolean active = true;
//
//	@OneToMany(mappedBy = "declarationVO", cascade = CascadeType.ALL)
//	@JsonManagedReference
//	private List<MyDeclarationVO> myDeclarationVO;
//	
	
	@OneToMany(mappedBy = "declarationVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<OneCroreFiveLacDeductionsVO> oneCroreFiveLacDeductionsVO;
	
	@OneToMany(mappedBy = "declarationVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<OtherDeductionsVO> otherDeductionsVO;
	
	
	@OneToMany(mappedBy = "declarationVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<TaxSavingAllowancesVO> taxSavingAllowancesVO;
	
	@OneToMany(mappedBy = "declarationVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<HousePropertyVO> housePropertyVO;
	
	@OneToMany(mappedBy = "declarationVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<IncomeFromOtherSourcesVO> incomeFromOtherSourcesVO;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";

	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
