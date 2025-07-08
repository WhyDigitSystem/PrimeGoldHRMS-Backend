package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "globalparam")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalParameterVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "globalparamgen")
	@SequenceGenerator(name = "globalparamgen", sequenceName = "globalparamseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "globalparamid")
	private Long id;

	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "userid")
	private String userid;
	@Column(name = "branch")
	private String branch;
	@Column(name = "branchcode")
	private String branchcode;
	@Column(name = "finyear")
	private String finYear;
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
