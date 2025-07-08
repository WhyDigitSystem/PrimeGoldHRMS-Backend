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
@Table(name = "kpikradetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiKraDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kpikradetailsgen")
	@SequenceGenerator(name = "kpikradetailsgen", sequenceName = "kpikradetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "kpikradetailsid")
	private Long id;

	@Column(name="kraid")
	private Long kraId;
	@Column(name="kradescription")
	private String kraDescription;
	private String ro;
	@Column(name="kpiid")
	private Long kpiId;
	@Column(name="kpidescription")
	private String kpiDescription;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="kpikraid")
	private KpiKraVO kpiKraVO;

}
