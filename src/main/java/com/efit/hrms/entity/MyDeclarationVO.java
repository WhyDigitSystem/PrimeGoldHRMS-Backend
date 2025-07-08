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
@Table(name = "mydeclaration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyDeclarationVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mydeclarationgen")
	@SequenceGenerator(name = "mydeclarationgen", sequenceName = "mydeclarationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "mydeclarationid")
	private Long id;
	@Column(name = "declaration")
	private String declaration;

	@Column(name = "count")
	private Long count;
	@Column(name = "declared")
	private Long declared;
	
	@Column(name = "proof")
	private boolean proof;
	@Column(name = "rejected")
	private Long rejected;
	@Column(name = "accepted")
	private Long accepted;
	
	@Column(name = "proofimage", columnDefinition = "LONGBLOB")
	private byte[] proofImage;

	@ManyToOne
	@JoinColumn(name = "declarationid")
	@JsonBackReference
	private DeclarationVO declarationVO;
	
}
