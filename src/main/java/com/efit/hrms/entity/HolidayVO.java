package com.efit.hrms.entity;

import java.time.LocalDate;
import java.util.Date;

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
@Table(name = "holidays")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "holidaysgen")
	@SequenceGenerator(name = "holidaysgen", sequenceName = "holidaysseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "holidaysid")
	private Long id;
	@Column(name = "orgid")
	private long orgId;
//	@Column(name = "branchid")
//	private String branchId;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branchname")
	private String branchName;
	@Column(name = "department")
	private String department;
	@Column(name = "holidaydate")
	private LocalDate holidayDate;
	@Column(name = "day")
	private String day;
	@Column(name = "festival")
	private String festival;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "active")
	private boolean active=true;
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "holidaysimage", columnDefinition = "LONGBLOB")
	private byte[] holidaysImage;
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
