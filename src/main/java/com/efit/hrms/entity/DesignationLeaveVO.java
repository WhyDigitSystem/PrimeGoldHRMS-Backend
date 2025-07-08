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
@Table(name = "designationleave")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignationLeaveVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "designationleavegen")
	@SequenceGenerator(name = "designationleavegen", sequenceName = "designationleaveseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "designationleaveid")
	private Long id;

	@Column(name = "leavetype")
	private String leaveType;
	@Column(name = "leavecode")
	private String leaveCode;
	@Column(name = "totalleave")
	private BigDecimal totalLeave;
	
	@Column(name = "designation", length = 75)
	private String designation;
	@Column(name = "designationcode", length = 75)
	private String designationCode;

	@Column(name = "active")
	private boolean active ;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel = false;

	@Column(name = "screencode", length = 5)
	private String screenCode = "DL";

	@Column(name = "screenname", length = 25)
	private String screenName = "DESIGNATION LEAVE";

	

	@Embedded
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
