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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "state")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "stategen")
	@SequenceGenerator(name = "stategen",sequenceName = "stateseq",initialValue = 1000000001,allocationSize = 1)
	@Column(name="stateid")
	private Long id;
	
	@Column(name="statecode")
	private String stateCode;
	@Column(name="state")
	private String stateName;
	@Column(name="country")
    private String country;
	@Column(name="region")
    private String region;
	@Column(name="statenumber")
	 private String stateNumber;
	@Column(name="active")
    private boolean active =true;
//	@Column(name="userid")
//    private String userId;
    @Column(name="createdby")
    private String createdBy;
    @Column(name="modifiedby")
	private String updatedBy;
    @Column(name="orgid")
	private Long orgId;
    @Column(name="cancel")
	private boolean cancel;

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
