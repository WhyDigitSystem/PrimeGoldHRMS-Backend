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
@Table(name = "designation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignationVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "designationgen")
	@SequenceGenerator(name = "designationgen", sequenceName = "designationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "designationid")
	private Long id;
    
    @Column(name = "designationname", length = 100)
	private String designationName;

    @Column(name = "designationcode", length = 75)
    private String designationCode;
    
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
	private String screenCode = "DES";

	@Column(name = "screenname", length = 25)
	private String screenName = "DESIGNATION";
    
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
    
    @Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}


