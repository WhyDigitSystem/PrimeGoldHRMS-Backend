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
@Table(name = "groupdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groupdetailsgen")
	@SequenceGenerator(name = "groupdetailsgen", sequenceName = "groupdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "groupdetailsid")
	private Long id;
	
	private String code;
	
	private String name;
	
	private String department;
	
	@ManyToOne
	@JoinColumn(name="groupmasterid")
	@JsonBackReference
	private GroupVO groupVO;
	
	
}
