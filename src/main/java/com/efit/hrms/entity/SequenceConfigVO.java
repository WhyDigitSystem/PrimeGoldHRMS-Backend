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
@Table(name = "sequenceconfig")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SequenceConfigVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceconfiggen")
	@SequenceGenerator(name = "sequenceconfiggen", sequenceName = "sequenceconfigseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "sequenceconfigid", columnDefinition = "BIGINT DEFAULT 0")
    private Long id;
    @Column(name = "companyid", nullable = false)
    private Long companyId;
    @Column(name = "companycode", unique = true, nullable = false)
    private String companyCode;
    @Column(name = "branchcode")
    private String branchCode;
    @Column(name = "departmentcode")
    private String departmentCode;
    @Column(name = "startingfrom")
    private Integer startingFrom;
    @Column(name = "noofdigits")
    private Integer noOfDigits;
    @Column(name = "separators")
    private String separators;
    @Column(name = "format")
    private String format;
	@Column(name="createdby",length =25)
	private String createdBy;
	
	@Column(name="modifiedby",length =25)
	private String updatedBy;
    

}
