
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
@Table(name = "onecrorefivelacdeduction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneCroreFiveLacDeductionsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "onecrorefivelacdeductiongen")
	@SequenceGenerator(name = "onecrorefivelacdeductiongen", sequenceName = "onecrorefivelacdeductionseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "otherdeclarationid")
	private Long id;
	@Column(name = "section")
	private String section;

	@Column(name = "deductions")
	private String deductions;
	@Column(name = "maxlimit")
	private Long maxLimit;
	
	@Column(name = "declaration")
	private String declaration;
	@Column(name = "status")
	private String status;
	
	
	@Column(name = "proofimage", columnDefinition = "LONGBLOB")
	private byte[] proofImage;
	
	

	@ManyToOne
	@JoinColumn(name = "declarationid")
	@JsonBackReference
	private DeclarationVO declarationVO;
	
}
