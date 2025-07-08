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
@Table(name = "selfgoalsdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelfGoalsDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "selfgoalsdetailssgen")
	@SequenceGenerator(name = "selfgoalsdetailsgen", sequenceName = "selfgoalsdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "selfgoalsdetailsid")
	private Long id;
	
	
	private String area;
	@Column(name="keyperformanceindicator")
	private String keyPerformanceIndicator;
	private String goals;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="selfgoalsid")
	private SelfGoalsVO selfGoalsVO;
	
}
