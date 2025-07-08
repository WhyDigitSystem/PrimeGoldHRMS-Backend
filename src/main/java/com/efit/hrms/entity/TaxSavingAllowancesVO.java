package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taxsavingallowances")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxSavingAllowancesVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taxsavingallowancesgen")
	@SequenceGenerator(name = "taxsavingallowancesgen", sequenceName = "taxsavingallowancesseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "taxsavingallowancesid")
	private Long id;
	@Column(name = "section")
	private String section;


	@Column(name = "deductionsname")
	private String deductionsName;
	@Column(name = "maxexemptionlimit")
	private Long maxexemptionLimit;
	
	@Column(name = "declaration")
	private String declaration;
	@Column(name = "proof")
	private String proof;
	@Column(name = "status")
	private String status;
	
	
	@Column(name = "proofimage", columnDefinition = "LONGBLOB")
	private byte[] proofImage;
	
	

	@ManyToOne
	@JoinColumn(name = "declarationid")
	@JsonBackReference
	private DeclarationVO declarationVO;
	
}


