package com.efit.hrms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryStructureDTO {

	private Long id;
	private String employeeName;
	private String employeeCode;
	private LocalDate dateOfBirth;
	private String grade;
	private String department;
	private String panNo;
	private String bankAccountNo;
	private LocalDate dateOfJoining;
	private String designation;
	private BigDecimal amount;
	private BigDecimal sumOfEarning;
	private BigDecimal sumOfDetection;
//	private LocalDate date;


	private String createdBy;
	private Long orgId;

    private String branch;
    private String branchCode;
//    private String finYear;
    
    List<SalaryEarningDetailsDTO> salaryEarningDetailsDTO;
    List<SalaryDetectionDetailsDTO> salaryDetectionDetailsDTO;



}
