package com.efit.hrms.entity;

import java.math.BigDecimal;
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
	@Table(name = "employeeleave")
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class EmployeeLeaveVO {

		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeleavegen")
		@SequenceGenerator(name = "employeeleavegen", sequenceName = "employeeleaveseq", initialValue = 1000000001, allocationSize = 1)
		@Column(name = "employeeleaveid")
		private Long id;
		
		@Column(name = "leavetype")
		private String leaveType;
		@Column(name = "leavecode")
		private String leaveCode;
		@Column(name = "totalleave")
		private BigDecimal totalLeave;
//		@Column(name = "leaveapplicable")
//		private String leaveApplicable;
//		@Column(name = "effective")
//		private String effective;
//		@Column(name = "carryforward")
//		private String carryForward;
		@Column(name = "effectivefrom")
		private LocalDate effectiveFrom;
		
		
		@ManyToOne
		@JoinColumn(name = "employeeid", columnDefinition = "BIGINT DEFAULT 0")
		@JsonBackReference
		EmployeeVO employeeVO;
	}


