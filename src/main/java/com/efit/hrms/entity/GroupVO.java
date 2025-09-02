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
@Table(name = "groupmaster")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groupmastergen")
	@SequenceGenerator(name = "groupmastergen", sequenceName = "groupmasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "groupmasterid")
	private Long id;

	@Column(name = "groupname")
	private String groupName;
	
	@Column(name = "department")
	private String department;
	@Column(name = "type")
	private String type;
	@Column(name = "contractor")
	private String contractor;
	
	@Column(name = "createdby",length = 30)
	private String createdBy;
	@Column(name = "modifiedby",length = 30)
	private String updatedBy;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks",length = 150)
	private String cancelRemark;
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branchname")
	private String branchName;
	@Column(name="finyear")
	private String finYear;
	
	@OneToMany(mappedBy = "groupVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<GroupDetailsVO> groupDetailsVO;
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
