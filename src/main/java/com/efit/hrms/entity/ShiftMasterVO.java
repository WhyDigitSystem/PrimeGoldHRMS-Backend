package com.efit.hrms.entity;

import java.math.BigDecimal;

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
@Table(name = "shiftmaster")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftMasterVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shiftmastergen")
	@SequenceGenerator(name = "shiftmastergen", sequenceName = "shiftmasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "shiftmasterid")
	private Long id;
	@Column(name = "shiftcode")
	private String shiftCode;
	@Column(name = "shift")
	private String shift;
	@Column(name = "intime")
	private String inTime;
	@Column(name = "outtime")
	private String outTime;
	@Column(name = "breaktime")
	private String breakTime;
	@Column(name = "gracetime")
	private String graceTime;
	@Column(name = "nightshift")
	private boolean nightShift;

	@Column(name = "orgid")
	private long orgId;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branch")
	private String branch;
	@Column(name = "finyear")
	private String finYear;

	@Column(name = "halfdayhours")
	private BigDecimal halfDayHours;
	@Column(name = "fulldayhours")
	private BigDecimal fullDayHours;

	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "active")
	private boolean active;
	@Column(name = "remarks")
	private String remarks;
	@Column(name = "screencode", length = 5)
	private String screenCode = "SM";
	@Column(name = "screenname", length = 25)
	private String screenName = "SHIFT MASTER";

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
}
