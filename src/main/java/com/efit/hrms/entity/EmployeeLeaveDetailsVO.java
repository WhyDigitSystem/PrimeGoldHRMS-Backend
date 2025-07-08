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
@Table(name = "employeeleavedetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLeaveDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeleavedetailsgen")
	@SequenceGenerator(name = "employeeleavedetailsgen", sequenceName = "employeeleavedetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "employeeleavedetailsid")
	private Long id;
	@Column(name ="leavetype",length =30)
	private String leaveType;
	@Column(name ="leavecode",length = 15)
	private String leaveCode;
	@Column(name ="noofdays")
	private String noOfDays;
	@Column(name ="effctive",length =30)
	private String effctive;	
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="employeeid")
	private EmployeeVO employeeVO;
	
}
