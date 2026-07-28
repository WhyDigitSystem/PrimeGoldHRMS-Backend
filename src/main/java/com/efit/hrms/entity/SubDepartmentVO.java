package com.efit.hrms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="subdepartment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubDepartmentVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subdepartmentgen")
	@SequenceGenerator(name = "subdepartmentgen", sequenceName = "subdepartmentseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "subdepartmentid")
	private Long id;
	@Column(name = "departmentname")
	private String departmentName;
	@Column(name = "shortname")
	private String shortName;
	@Column(name = "active")
	private boolean active= true;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "maindepartmentid")
	private MainDepartmentVO mainDepartmentVO;

}
