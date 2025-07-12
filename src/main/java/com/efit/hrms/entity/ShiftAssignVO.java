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
@Table(name = "shiftassign")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftAssignVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shiftassigngen")
	@SequenceGenerator(name = "shiftassigngen", sequenceName = "shiftassignseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "shiftassignid")
	private Long id;

	@Column(name = "shifttype")
	private String shiftType;
	@Column(name = "description")
	private String description;
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
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "active")
	private boolean active;
	@Column(name = "remarks")
	private String remarks;
	@Column(name = "screencode", length = 5)
	private String screenCode = "SA";
	@Column(name = "screenname", length = 25)
	private String screenName = "SHIFT ASSIGN";

	
	
    @OneToMany(mappedBy = "shiftAssignVO",cascade = CascadeType.ALL)
   	@JsonManagedReference
   	private List<ShiftAssignDetailsVO> shiftAssignDetailsVO;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
}
