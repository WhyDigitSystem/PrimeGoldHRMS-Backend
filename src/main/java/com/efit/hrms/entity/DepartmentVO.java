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
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentVO {
	
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departmentgen")
		@SequenceGenerator(name = "departmentgen", sequenceName = "departmentseq", initialValue = 1000000001, allocationSize = 1)
		@Column(name = "departmentid")
		private Long id;
	    
	    @Column(name = "departmentname", length = 100)
		private String departmentName;

	    @Column(name = "departmentcode", length = 75)
	    private String departmentCode;
	    
		@Column(name = "active")
		private boolean active;
		@Column(name = "createdby")
		private String createdBy;
		@Column(name = "modifiedby")
		private String updatedBy;
		@Column(name = "orgid")
		private Long orgId;
		@Column(name = "cancel")
		private boolean cancel;
		
		@Column(name = "screencode", length = 5)
		private String screenCode = "DEP";

		@Column(name = "screenname", length = 25)
		private String screenName = "DEPARTMENT";
	    
		@JsonGetter("active")
		public String getActive() {
			return active ? "Active" : "In-Active";
		}
	    
	    @Embedded
		@Builder.Default
		private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	}
