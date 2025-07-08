package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sequencetracker")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SequenceTrackerVO {
	
	
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequencetrackergen")
	@SequenceGenerator(name = "sequencetrackergen", sequenceName = "sequencetrackerseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "sequencetrackerid", columnDefinition = "BIGINT DEFAULT 0")
    private Long id;
    @Column(name = "companyid")
    private Long companyId;
    @Column(name = "companycode")
    private String companyCode;
    @Column(name = "branchcode")
    private String branchCode;
    @Column(name = "departmentcode")
    private String departmentCode;
    @Column(name = "lastnumber")
    private Integer lastNumber;
    @Column(name = "year")
    private Integer year;

}
