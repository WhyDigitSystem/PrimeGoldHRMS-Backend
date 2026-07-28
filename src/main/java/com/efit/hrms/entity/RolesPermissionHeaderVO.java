package com.efit.hrms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rolepermissionheader")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermissionHeaderVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolepermissionheadergen")
	@SequenceGenerator(name = "rolepermissionheadergen", sequenceName = "rolepermissionheaderseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "rolepermissionheaderid")
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
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
	
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}
	
	@OneToMany(mappedBy ="rolesPermissionHeaderVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<RolesPermissionVO> rolesPermissionVO;
	
}
