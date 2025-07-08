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
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "timesheet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheetgen")
	@SequenceGenerator(name = "timesheetgen", sequenceName = "timesheetseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "timesheetid")
	private Long id;

	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "date")
	private LocalDate date;
	@Column(name = "totalhours")
	private Double totalhours;

	@Column(name = "active")
	private boolean active;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel = false;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branch")
	private String branch;

	@Column(name = "screencode", length = 5)
	private String screenCode = "TS";

	@Column(name = "screenname", length = 25)
	private String screenName = "TIME SHEET";

	@OneToMany(mappedBy = "timeSheetVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<TimeSheetDetailsVO> timeSheetDetailsVO;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
