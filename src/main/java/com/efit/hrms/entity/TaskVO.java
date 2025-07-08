package com.efit.hrms.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskgen")
	@SequenceGenerator(name = "taskgen", sequenceName = "taskseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "taskid")
	private Long id;
	@Column(name = "orgid")
	private long orgId;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branchname")
	private String branchName;
	@Column(name = "department")
	private String department;
	

	@Column(name = "username")
	private String userName;
	
	@Column(name = "tasktitle")
	private String taskTitle;
	@Column(name = "taskdescription", length = 1000)
	private String taskDescription;
	@Column(name = "category")
	private String category;
	@Column(name = "priority")
	private String priority;
	@Column(name = "assignedby")
	private String assignedBy;
	@Column(name = "assignedto")
	private String assignedTo;
	@Column(name = "status")
	private String status;
	@Column(name = "duedate")
	private LocalDate dueDate;
	@Column(name = "remarks",length = 500)
	private String remarks;
	
	@Column(name = "finyear", length = 10)
	private String finYear;
	@Column(name = "screencode", length = 30)
	private String screenCode = "TSK";
	@Column(name = "screenname", length = 30)
	private String screenName = "Task";
	@Column(name = "active")
	private boolean active;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;

}
