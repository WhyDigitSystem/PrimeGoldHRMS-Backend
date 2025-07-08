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
@Table(name = "compensatoryoff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompensatoryOffVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compensatoryoffgen")
	@SequenceGenerator(name = "compensatoryoffgen", sequenceName = "compensatoryoffseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "compensatoryoffid")
	private Long id;
	
	@Column(name = "leavetype")
	private String leaveType;
	@Column(name = "leavecode")
	private String leaveCode;
	@Column(name = "compoffdate")
	private LocalDate compOffDate;
	@Column(name = "compoffday")
	private String compOffDay;
	@Column(name = "assignedby")
	private String assignedBy;
	@Column(name = "totaldays")
	private BigDecimal totalDays;
	@Column(name = "notes")
	private String notes;
	@Column(name = "notify")
	private String notify;
	@Column(name = "notifycode")
	private String notifyCode;
	@Column(name = "notifyemail")
	private String notifyEmail;
	
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "department")
	private String department;
	@Column(name = "designation")
	private String designation;
	@Column(name = "approvalstatus")
	private String approvalStatus;
	@Column(name = "approveby")
	private String approveBy;
	@Column(name = "approveon")
	private String approveOn;
	
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branch")
	private String branch;
//	@Column(name = "finyear")
//	private String finYear;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	
	@Column(name = "screencode", length = 10)
	private String screenCode = "Compoff";

	@Column(name = "screenname", length = 25)
	private String screenName = "COMPENSATORY OFF";
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
    @OneToMany(mappedBy = "compensatoryOffVO",cascade = CascadeType.ALL)
   	@JsonManagedReference
   	private List<CompoffNotifyVO> compoffNotifyVO;
	
	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}


}