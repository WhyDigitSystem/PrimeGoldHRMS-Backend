package com.efit.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeegen")
	@SequenceGenerator(name = "employeegen", sequenceName = "employeeseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "employeeid")
	private Long id;

	@Column(name = "employeecode",length = 30)
	private String employeeCode;
	@Column(name = "employee",length = 150)
	private String employeeName;
	@Column(name = "type",length = 150)
	private String employeeType;
	@Column(name = "employeeaddress",length = 150)
	private String employeeAddress;
	@Column(name = "gender",length = 30)
	private String gender;
	@Column(name = "branch",length = 50)
	private String branch;
	@Column(name = "email",length = 150)
	private String email;
	@Column(name = "branchcode",length = 30)
	private String branchCode;
//	@Column(name = "finyear",length = 10)
//	private String finYear;
	@Column(name = "bloodgroup",length = 5)
	private String bloodGroup;
	@Column(name = "mobileno",length = 10)
	private Long mobileNo;
	@Column(name = "alternativemobileno",length = 10)
	private Long AlternativeMobileNo;
	@Column(name = "aadharno",length = 12)
	private Long aadharNo;
	@Column(name = "panno",length = 10)
	private String panNo;
	@Column(name = "accountno",length = 18)
	private String accountNo;
	@Column(name = "bankname",length = 150)
	private String bankName;
	@Column(name = "ifsccode",length = 12)
	private String ifscCode;
	@Column(name = "department")
	private String department;
	@Column(name = "grade",length = 12)
	private String grade;
	@Column(name = "team",length = 50)
	private String team;
	@Column(name = "uanno",length = 50)
	private Long uanNo;
	@Column(name = "reportingperson",length = 150)
	private String reportingPerson;
	@Column(name = "reportingpersonemail",length = 150)
	private String reportingPersonEmail;
	@Column(name = "reportingpersoncode",length = 150)
	private String reportingPersonCode;
//	@Column(name = "reportingname",length = 150)
//	private String reportingName;
	@Column(name = "reportingrole",length = 100)
	private String reportingRole;
//	@Column(name = "role",length = 50)
//	private String role;
	@Column(name = "resigndate")
	private LocalDate resignDate;
	@Column(name = "designation")
	private String designation;
	@Column(name = "dateofbirth")
	private LocalDate dateOfBirth;
	@Column(name = "joiningdate")
	private LocalDate joiningDate;
	@Column(name = "createdby",length = 30)
	private String createdBy;
	@Column(name = "modifiedby",length = 30)
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks",length = 150)
	private String cancelRemark;
	@Column(name = "active")
	private boolean active;
	@Column(name = "payslipeffectivedate")
	private LocalDate payslipEffectiveDate;
	
	@Column(name = "contractor",length = 30)
	private String contractor;
	@Column(name = "contactperson",length = 150)
	private String contactPerson;
	@Column(name = "contactnumber",length = 150)
	private String contactNumber;
	@Column(name = "contactgmail",length = 150)
	private String contactEmail;
	
	@Column(name = "pfflag")
	private boolean pfFlag;
	@Column(name = "esiflag")
	private boolean esiFlag;
	@Column(name = "pfpercentage")
	private BigDecimal pfPercentage;
	@Column(name = "esipercentage")
	private BigDecimal esiPercentage;
	@Column(name = "flagvalue")
	private String flagValue;
	@Column(name = "flag")
	private boolean flag;
	
	@Column(name = "otflag")
	private OverTime otFlag;
	
	@Column(name = "bioid")
	private String bioId;
	
//	@Column(name = "weekoffoteligible")
//	private boolean weekoffOtEligible;
	
	@Column(name = "category")
	private Category category;
	
	@Column(name = "profileimage", columnDefinition = "LONGBLOB")
	private byte[] profileImage;

//	@Column(name="docid")
//	private String docid;
	

//	@Column(name = "salesFlag")
//	private boolean salesFlag;
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}

	
	public OverTime getOtFlag() {
	    return otFlag;
	}
	
	public Category getCategory() {
	    return category;
	}

	public void setOtFlag(OverTime otFlag) {
	    this.otFlag = otFlag;
	}
	
	@OneToMany(mappedBy = "employeeVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<EmployeeLeaveVO> employeeLeaveVO;
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
