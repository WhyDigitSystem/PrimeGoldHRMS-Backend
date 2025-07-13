package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="employeecodeconfiguration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCodeConfigVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "employeecodeconfigurationgen")
	@SequenceGenerator(name = "employeecodeconfigurationgen", sequenceName = "employeecodeconfigurationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "employeecodeconfigurationid")
	private Long id;
	
	@Column(name="orgid")
    private Long orgId;
	
	@Column(name="company")
	private String company;
	
	@Column(name="companycode")
	private int companyCode;
	
	@Column(name="departmentcode")
	private int departmentCode;
	
	@Column(name="branchcode")
	private int branchCode;
	
	@Column(name="year")
	private int year;
	
	@Column(name="seq")
	private int seq;
	
	@Column(name="seqdigit")
	private int seqDigit;

    @Column(name="codepattern",nullable = false)
    private String codePattern; // e.g. ${companyCode}-${department}-${seq}

    @Column(name="lastseq",nullable = false)
    private int lastSeq = 1;

}
