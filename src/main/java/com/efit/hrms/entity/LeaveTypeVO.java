package com.efit.hrms.entity;

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
@Table(name = "leavetype")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTypeVO {
	
	
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leavetypegen")
		@SequenceGenerator(name = "leavetypegen", sequenceName = "leavetypeseq", initialValue = 1000000001, allocationSize = 1)
		@Column(name = "leavetypeid")
		private Long id;

		@Column(name = "leavetype")
		private String leaveType;
		@Column(name = "leavecode")
		private String leaveCode;
		@Column(name = "orgid")
		private Long orgId;
		@Column(name = "leaveapplicable")
		private String leaveApplicable;
		@Column(name = "carryforward")
		private boolean carryForward;
		
		@Column(name = "createdby")
		private String createdBy;
		@Column(name = "modifiedby")
		private String updatedBy;
		@Column(name = "finyear",length =10)
		private String finYear;
		@Column(name = "screencode", length = 30)
		private String screenCode = "LVT";
		@Column(name = "screenname", length = 30)
		private String screenName = "LEAVETYPE";
		@Column(name = "branch",length = 30)
		private String branch;
		@Column(name = "branchcode",length = 10)
		private String branchCode;
		@Column(name = "active")
		private boolean active;
		@Column(name = "salarydeduction",length = 10)
		private String salaryDeduction;
		
		
		
		@JsonGetter("active")
		public String getActive() {
			return active ? "Active" : "In-Active";
					
		}
		
		@Embedded
		private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
