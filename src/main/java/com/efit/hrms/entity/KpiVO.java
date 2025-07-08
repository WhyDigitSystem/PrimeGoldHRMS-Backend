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
@Table(name = "kpi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiVO {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kpigen")
	@SequenceGenerator(name = "kpigen", sequenceName = "kpiseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "kpi1id")
	private Long id;
	
	@Column(name = "kpiid")
	private Long kpiId;
	@Column(name = "kpidescription")
	private String kpiDescription;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="kpikraid")
	private KpiKraVO kpiKraVO;
}
