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
@Table(name = "pregoalsdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreGoalsDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pregoalsdetailsgen")
	@SequenceGenerator(name = "pregoalsdetailsgen", sequenceName = "pregoalsdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "pregoalsdetailsid")
	private Long id;
	
	
	private String area;
	@Column(name="keyperformanceindicator")
	private String keyPerformanceIndicator;
	private String goals;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="preGoalsid")
	private PreGoalsVO preGoalsVO;
	
}
