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
@Table(name = "finscreen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinScreenVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "finscreengen")
	@SequenceGenerator(name = "finscreengen", sequenceName = "finscreenseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "finscreenid")
	private Long id;
	@Column(name = "screenname")
	private String screenName;
	@Column(name = "screencode")
	private String screenCode;
	@Column(name = "active")
	private boolean active;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
