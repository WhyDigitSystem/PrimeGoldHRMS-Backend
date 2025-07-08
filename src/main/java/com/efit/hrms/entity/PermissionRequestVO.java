package com.efit.hrms.entity;

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
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissionrequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequestVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissionrequestgen")
	@SequenceGenerator(name = "permissionrequestgen", sequenceName = "permissionrequestseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "permissionrequestid")
	private Long id;
	@Column(name = "date")
	private LocalDate date;
	@Column(name = "fromtime")
	private String fromTime;
	@Column(name = "totime")
	private String toTime;
	@Column(name = "totalhours")
	private long totalHours;
	@Column(name = "notes")
	private String notes;
	@Column(name = "notify")
	private String notify;
	@Column(name = "notifycode")
	private String notifyCode;
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "employeeemail")
	private String employeeEmail;

	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "active")
	private boolean active = true;
	@Column(name = "cancelremarks")
	private String cancelRemarks;

	@Column(name = "approvestatus")
	private String approveStatus;
	@Column(name = "approveby")
	private String approveBy;
	@Column(name = "approveon")
	private String approveOn;

	@Column(name = "branch", length = 100)
	private String branch;
	@Column(name = "branchcode", length = 20)
	private String branchCode;

	@Column(name = "screencode", length = 5)
	private String screenCode = "PRQ";

	@Column(name = "screenname", length = 25)
	private String screenName = "PERMISSION REQUEST";

	@OneToMany(mappedBy = "permissionRequestVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PermissionRequestNotifyVO> permissionRequestNotifyVO;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
