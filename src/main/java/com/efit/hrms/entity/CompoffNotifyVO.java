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
@Table(name = "compoffnotify")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompoffNotifyVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compoffnotifygen")
	@SequenceGenerator(name = "compoffnotifygen", sequenceName = "compoffnotifyseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "compoffnotifyid")
	private Long id;
	
	@Column(name = "notify2")
	private String notify2;  

	@Column(name = "notify2email")
	private String notify2Email;  

	@Column(name = "notify2code")
	private String notify2Code;  
	
	@ManyToOne
	@JoinColumn(name = "compensatoryoffid")
	@JsonBackReference
	private CompensatoryOffVO compensatoryOffVO;



}