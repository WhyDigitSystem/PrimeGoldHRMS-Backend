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
@Table(name = "otherdeductions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherDeductionsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otherdeductionsgen")
	@SequenceGenerator(name = "otherdeductionsgen", sequenceName = "otherdeductionsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "otherdeductionsid")
	private Long id;
	@Column(name = "section")
	private String section;

	@Column(name = "deductions")
	private String deductions;
	@Column(name = "maxlimit")
	private Long maxLimit;
	
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
