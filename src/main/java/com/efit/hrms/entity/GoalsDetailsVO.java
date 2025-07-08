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
@Table(name = "goalsdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalsDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goalsdetailsgen")
	@SequenceGenerator(name = "goalsdetailsgen", sequenceName = "goalsdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "goalsdetailsid")

	private Long id;

	private String area;

	private String indicators;

	private String goals;
	
	@ManyToOne
	@JoinColumn(name="goalsid")
	@JsonBackReference
	private GoalsVO goalsVO;

}
