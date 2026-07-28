package com.efit.hrms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="maindepartment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainDepartmentVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "maindepartmentgen")
	@SequenceGenerator(name = "maindepartmentgen", sequenceName = "maindepartmentseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "maindepartmentid")
	private Long id;
	@Column(name = "departmentname")
	private String departmentName;
	@Column(name = "shortname")
	private String shortName;
	@Column(name = "active")
	private boolean active= true;
	@Column(name = "orgid")
	private Long orgId;
//	@Column(name = "userid")
//	private String userId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
	
	@JsonManagedReference
	@OneToMany(mappedBy = "mainDepartmentVO", cascade = CascadeType.ALL)
	private List<SubDepartmentVO> subDepartmentVO;
	
	

}
