package com.efit.hrms.entity;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "checkinoutupload")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInOutUploadVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checkinoutuploadgen")
	@SequenceGenerator(name = "checkinoutuploadgen", sequenceName = "checkinoutuploadseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "checkinoutuploadid")
	private Long id;

	@Column(name = "empname")
	private String empname;
	@Column(name = "empcode")
	private String empcode;
	@Column(name = "orgid")
	private long orgId;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branch")
	private String branch;
	@Column(name = "finyear")
	private String finYear;
	@Column(name = "createdby")
	private String createdBy;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "checkindate")
	private LocalDate checkInDate = LocalDate.now();
	@Column(name = "entrytime")
	private LocalTime entryTime = LocalTime.now();
	@Column(name = "status")
	private String status;
	@Column(name = "attendancemode")
	private String attendanceMode="FILES";

	
	@Column(name = "screencode", length = 5)
	private String screenCode = "CIOU";
	@Column(name = "screenname", length = 25)
	private String screenName = "CHECKINOUTUPLOAD";

	

	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
