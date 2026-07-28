package com.efit.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salaryprocess")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryProcessVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salaryprocessgen")
	@SequenceGenerator(name = "salaryprocessgen", sequenceName = "salaryprocessseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "salaryprocessid")
	private Long id;

	@Column(name = "month")
	private Long month;
	@Column(name = "year")
	private String year;
	
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "totalcompanyworkingdays")
	private BigDecimal totalCompanyWorkingDays;
	@Column(name = "totalleave")
	private BigDecimal totalLeave;
	@Column(name = "lopleave")
	private BigDecimal lopLeave;
	@Column(name = "emptotalworkingdays")
	private BigDecimal empTotalWorkingDays;
	@Column(name = "empsalarydays")
	private BigDecimal empSalaryDays;
//	@Column(name = "grosspay")
//	private BigDecimal grossPay;
//	@Column(name = "netpay")
//	private BigDecimal netPay;
	@Column(name = "othours")
	private BigDecimal otHours;
	@Column(name = "bankotamount")
	private BigDecimal BankOtAmount;
	@Column(name = "cashotamount")
	private BigDecimal cashOtAmount;
	@Column(name = "bankamount")
	private BigDecimal bankAmount;
	@Column(name = "cashamount")
	private BigDecimal cashAmount;
	@Column(name = "approvedstatus")
	private String approvedStatus;
	@Column(name = "approveby")
	private String approveBy;
	@Column(name = "approveon")
	private String approveOn;

	@Column(name = "totalearnings")
	private BigDecimal totalEarnings;
	@Column(name = "totaldeductions")
	private BigDecimal totalDeductions;
	
	@Column(name = "pfamount")
	private BigDecimal pfAmount;
	@Column(name = "esiamount")
	private BigDecimal esiAmount;
	
	@Column(name = "ptamount")
	private BigDecimal ptAmount;
	@Column(name = "tdsamount")
	private BigDecimal tdsAmount;
	
	@Column(name = "fixedwages")
	private BigDecimal fixedWages;
	
	@Column(name = "cashadvance")
	private BigDecimal cashAdvance;
	@Column(name = "bankadvance")
	private BigDecimal bankAdvance;
	@Column(name = "earnings")
	private BigDecimal earnings;
//	@Column(name = "advancededuction")
//	private BigDecimal advanceDeduction;
//	@Column(name = "salaryamount")
//	private BigDecimal salary;
	
//	@Column(name = "requestdate")
//	private LocalDate requestDate;
//	@Column(name = "loanbalance")
//	private BigDecimal loanBalance;
	
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	
	@Column(name = "screencode", length = 30)
	private String screenCode = "SP";
	@Column(name = "screenname", length = 30)
	private String screenName = "SALARYPROCESS";
	@Column(name = "branch",length = 30)
	private String branch;
	@Column(name = "branchcode",length = 10)
	private String branchCode;
	@Column(name = "active")
	private boolean active = true;
	@Column(name = "orgid")
	private Long orgId;
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
				
	}
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}

