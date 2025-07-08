package com.efit.hrms.entity;

import java.util.Optional;

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
@Table(name = "incomefromothersources")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeFromOtherSourcesVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incomefromothersourcesgen")
	@SequenceGenerator(name = "incomefromothersourcesgen", sequenceName = "incomefromothersourcesseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "incomefromothersourcesid")
	private Long id;
	
	@Column(name = "proofimage", columnDefinition = "LONGBLOB")
	private byte[] proofImage;
	
	

	@ManyToOne
	@JoinColumn(name = "declarationid")
	@JsonBackReference
	private DeclarationVO declarationVO;


}
