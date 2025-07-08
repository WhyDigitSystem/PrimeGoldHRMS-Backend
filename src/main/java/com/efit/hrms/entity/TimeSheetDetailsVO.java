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
@Table(name = "timesheetdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheetdetailsgen")
	@SequenceGenerator(name = "timesheetdetailsgen", sequenceName = "timesheetdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "timesheetdetailsid")
	private Long id;

	@Column(name = "projectname")
	private String projectName;
	@Column(name = "fromtime")
	private String fromTime;
	@Column(name = "totime")
	private String toTime;
	@Column(name = "description")
	private String description;
	
	
	@ManyToOne
	@JoinColumn(name = "timesheetid")
	@JsonBackReference
	private TimeSheetVO timeSheetVO;
	
	
}
