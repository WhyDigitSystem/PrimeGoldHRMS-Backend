package com.efit.hrms.entity;

import java.time.LocalDate;

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
@Table(name = "otmasterdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtMasterDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otmasterdetailsgen")
	@SequenceGenerator(name = "otmasterdetailsgen", sequenceName = "otmasterdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "otmasterdetailsid")
	private Long id;
	@Column(name = "slab")
	private String slab;
	@Column(name = "minhours")
	private int minHours;
	@Column(name = "maxhours")
	private int maxHours;
	@Column(name = "otrate")
	private String otrate;
	@Column(name = "effectivefrom")
	private LocalDate effectiveFrom;
	@Column(name = "effectiveto")
	private LocalDate effectiveTo;
	@Column(name = "applicable")
	private boolean applicable;
	

	@ManyToOne
	@JoinColumn(name = "otmasterid", columnDefinition = "BIGINT DEFAULT 0")
	@JsonBackReference
	private OtMasterVO otMasterVO;
}
