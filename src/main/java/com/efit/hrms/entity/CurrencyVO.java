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
@Table(name = "currency")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currencygen")
	@SequenceGenerator(name = "currencygen", sequenceName = "currencyseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "currencyid")
	private Long id;
	@Column(name = "country")
	private String country;
	@Column(name = "currency")
	private String currency;
	@Column(name = "subcurrency")
	private String subCurrency;
	@Column(name = "currencydesc")
	private String currencyDescription;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "active")
	private boolean active =true;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel=true;

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
