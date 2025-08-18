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
@Table(name = "country")	
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countrygen")
	@SequenceGenerator(name = "countrygen", sequenceName = "countryseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "countryid")
	private Long id;
	@Column(name = "country")
	private String countryName;
	@Column(name = "countrycode")
	private String countryCode;
	@Column(name = "active")
	private boolean active= true;
	@Column(name = "orgid")
	private Long orgId;
//	@Column(name = "userid")
//	private String userId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancel")
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
