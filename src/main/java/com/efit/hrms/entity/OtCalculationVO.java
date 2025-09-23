package com.efit.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
@Table(name = "otcalculation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtCalculationVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otcalculationgen")
	@SequenceGenerator(name = "otcalculationgen", sequenceName = "otcalculationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "otcalculationid")
    private Long id;

    @Column(name = "empcode", nullable = false)
    private String empcode;

    @Column(name = "empname")
    private String empname;

    @Column(name = "checkindate")
    private LocalDate checkindate;

    @Column(name = "intime")
    private LocalTime intime;

    @Column(name = "outtime")
    private LocalTime outtime;

    @Column(name = "othours")
    private int othours;

//    @Column(name = "otamount", precision = 10, scale = 2)
//    private BigDecimal otamount;
    
    
    @Column(name = "bankotamount", precision = 10, scale = 2)
    private BigDecimal bankOtAmount;
    
    @Column(name = "cashotamount", precision = 10, scale = 2)
    private BigDecimal cashOtAmount;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "ottype")
    private String ottype;

    @Column(name = "otcategory")
    private String otcategory;
    
    @Column(name = "companyotpolicy")
    private String companyOtPolicy;
    
    @Column(name = "status")
    private String status;

    @Column(name = "createdon")
    private LocalDateTime createdon;
    
    @Column(name = "orgid")
    private Long orgId;
    
    @Column(name = "approveby")
	private String approveBy;
	@Column(name = "approveon")
	private String approveOn;

    // Getters and Setters
}
