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
@Table(name = "appraiseedetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraiseeDetailsVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appraiseedetailsgen")
	@SequenceGenerator(name = "appraiseedetailsgen", sequenceName = "appraiseedetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "appraiseedetailsid")
	private Long id;
	
	
	private String area;
	private String goals;
	@Column(name="keyperformanceindicator")
	private String keyPerformanceIndicator;
	@Column(name = "remarks")
	private String reMarks;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="appraiseeid")
	private AppraiseeVO appraiseeVO;

}
