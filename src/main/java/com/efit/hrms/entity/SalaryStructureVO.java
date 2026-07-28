package com.efit.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "salarystructure")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryStructureVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salarystructuregen")
	@SequenceGenerator(name = "salarystructuregen", sequenceName = "salarystructureseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "salarystructureid")
	private Long id;
	@Column(name = "employeename", length = 100)
	private String employeeName;
	@Column(name = "employeecode", length = 100)
	private String employeeCode;
	@Column(name = "dateofbirth")
	private LocalDate dateOfBirth;
	@Column(name = "grade")
	private String grade;
	@Column(name = "department", length = 100)
	private String department;
	@Column(name = "panno", length = 10)
	private String panNo;
	@Column(name = "bankaccountno", length = 20)
	private String bankAccountNo;
	@Column(name = "dateofjoining")
	private LocalDate dateOfJoining;
	@Column(name = "designation", length = 255)
	private String designation;
	@Column(name = "amount")
	private BigDecimal amount;
	@Column(name = "sumofearning")
	private BigDecimal sumOfEarning;
	@Column(name = "sumofdetection")
	private BigDecimal sumOfDetection;
	@Column(name = "date", nullable = false)
	private LocalDate date = LocalDate.now();
	
	@Column(name = "pfpercentage")
	private BigDecimal pfPercentage;
	@Column(name = "esipercentage")
	private BigDecimal esiPercentage;
	
	@Column(name = "active")
	private boolean active = true;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;

    @Column(name = "branch", length = 100)
    private String branch;
    @Column(name = "branchcode", length = 20)
    private String branchCode;
    @Column(name = "cancelremarks", length = 50)
    private String cancelRemarks;
//    @Column(name = "finyear", length = 5)
//    private String finYear;
    @Column(name = "screencode", length = 5)
    private String screenCode = "SS";
    @Column(name = "screenname", length = 25)
    private String screenName = "SALARY STRUCTURE";
    
    @OneToMany(mappedBy = "salaryStructureVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<SalaryEarningDetailsVO> salaryEarningDetailsVO;
    
    @OneToMany(mappedBy = "salaryStructureVO",cascade = CascadeType.ALL)
   	@JsonManagedReference
   	private List<SalaryDetectionDetailsVO> salaryDetectionDetailsVO;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}

	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}