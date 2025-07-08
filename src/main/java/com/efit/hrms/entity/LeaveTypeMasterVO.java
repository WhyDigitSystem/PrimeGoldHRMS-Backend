package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leavetypemaster")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeMasterVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "leavetypemastergen")
	@SequenceGenerator(name = "leavetypemastergen", sequenceName = "leavetypemasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "leavetypemasterid")
	private Long id;
	@Column(name="leavetype")
	private String leaveType;
	@Column(name="leavecode")
	private String leaveCode;
	@Column(name="noofdays")
	private int noOfDays;
	@Column(name="effective")
	private String effective;
	@Column(name="orgid")
	private Long orgId;
	@Column(name="createdby")
	private String createdBy;
	@Column(name="modifiedby")
	private String updatedBy;
	@Column(name="carryforward")
	private String carryForward;
	@Column(name="leaveapplicable")
	private String leaveApplicable;
	
	private boolean active;
	private boolean cancel;
	

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
	
}
