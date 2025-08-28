package com.efit.hrms.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shiftassigndetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftAssignDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shiftassigndetailsgen")
	@SequenceGenerator(name = "shiftassigndetailsgen", sequenceName = "shiftassigndetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "shiftassigndetailsid")
	private Long id;
	
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "shifttype")
	private String shiftType;
	@Column(name = "shiftcode")
	private String shiftCode;
	@Column(name = "intime")
	private String inTime;
	@Column(name = "outtime")
	private String outTime;
	@Column(name = "department")
	private String department;

	@Column(name = "type")
	private String type;
	@Column(name = "contractor")
	private String contractor;
	@Column(name = "contactperson")
	private String contactPerson;
	@Column(name = "contactnumber")
	private String contactNumber;
	@Column(name = "contactemail")
	private String contactEmail;
	
	@Column(name = "hours")
	private String hours;
	@Column(name = "effectivefrom")
	private LocalDate effectiveFrom;
	@Column(name = "effectiveto")
	private LocalDate effectiveTo;
	
	@Column(name = "active")
	private boolean active;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
	
	@ManyToOne
	@JoinColumn(name = "shiftassignid")
	@JsonBackReference
	private ShiftAssignVO shiftAssignVO;
	
}
