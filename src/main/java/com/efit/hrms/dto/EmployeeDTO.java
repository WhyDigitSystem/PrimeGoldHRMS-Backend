package com.efit.hrms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;

import com.efit.hrms.entity.OverTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

	private Long id;
    private String employeeCode;
    private String employeeName;
	private String employeeType;
	private String employeeAddress;
    private String gender;
    private String branch;
    private String email;
    private String branchCode;
//	private String finYear;
    private String bloodGroup;
    private long mobileNo;
    private long alternativeMobileNo;
    private long aadharNo;
    private String panNo;
    private String accountNo;
	private String bankName;
    private String ifscCode;
    private String department;
    private String grade;
    private String team;
    private String reportingPerson;
    private String reportingPersonEmail;
	private String reportingPersonCode;
	private Long uanNo;
	private String flagValue;
	private boolean flag;
	private boolean weekoffOtEligible;

	
	private String contractor;
	private String contactPerson;
	private String contactNumber;
	private String contactEmail;

//    private String reportingName;
    private String reportingRole;
//    private String role; 
    private LocalDate resignDate;
    private String designation;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    private String createdBy;
    private Long orgId;
    private boolean active;
	private String bioId;
	private LocalDate payslipEffectiveDate;


	private OverTime otFlag;

    private boolean pfFlag;
	private boolean esiFlag;
	private BigDecimal pfPercentage;
	private BigDecimal esiPercentage;
    
	private List<EmployeeLeaveDTO> employeeLeaveDTO;

	
	public OverTime getOtFlag() {
	    return otFlag;
	}

	public void setOtFlag(OverTime otFlag) {
	    this.otFlag = otFlag;
	}

}
