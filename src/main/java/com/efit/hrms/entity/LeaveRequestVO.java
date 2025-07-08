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
@Table(name = "leaverequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leaverequestgen")
	@SequenceGenerator(name = "leaverequestgen", sequenceName = "leaverequestseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "leaverequestid")
	private Long id;
	
	@Column(name = "leavetype")
	private String leaveType;
	@Column(name = "leavecode")
	private String leaveCode;
	@Column(name = "fromdate")
	private LocalDate fromDate;
	@Column(name = "todate")
	private LocalDate toDate;
	@Column(name = "selectleave")
	private String selectLeave;
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
	
	@Column(name = "compoffdate")
	private LocalDate compOffDate;
	
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "department")
	private String department;
	@Column(name = "designation")
	private String designation;
	@Column(name = "approvestatus")
	private String approveStatus;
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
	
	@Column(name = "screencode", length = 5)
	private String screenCode = "LRQ";

	@Column(name = "screenname", length = 25)
	private String screenName = "LEAVE REQUEST";
	
	  @OneToMany(mappedBy = "leaveRequestVO",cascade = CascadeType.ALL)
	   	@JsonManagedReference
	   	private List<LeaveRequestNotifyVO> leaveRequestNotifyVO;
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
	
	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}


}
