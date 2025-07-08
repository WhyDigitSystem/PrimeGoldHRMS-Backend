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
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMasterVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolegen")
	@SequenceGenerator(name = "rolegen", sequenceName = "roleseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "roleid")
	private Long id;
	@Column(name = "role")
	private String role;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	
	
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
