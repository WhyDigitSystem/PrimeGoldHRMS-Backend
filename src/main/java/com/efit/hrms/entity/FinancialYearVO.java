package com.efit.hrms.entity;

import java.time.LocalDate;

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
@Table(name = "financialyear")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialYearVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "financialyearegen")
	@SequenceGenerator(name = "financialyearegen", sequenceName = "financialyearseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "finyearid")
	private Long id;
	@Column(name = "financialyearid")
	private Long finYearId;
	@Column(name = "finyear")
	private int finYear;
	@Column(name = "finyearidentifier")
	private String finYearIdentifier;
	@Column(name = "startdate")
	private LocalDate startDate;
	@Column(name = "enddate")
	private LocalDate endDate;
	@Column(name = "currentfinyear")
	private boolean currentFinYear;
	@Column(name = "closed")
	private boolean closed;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	private boolean active;

	@JsonGetter("active")
    public String getActive() {
        return active ? "Active" : "In-Active";  // If 'active' is true, return "Active", otherwise "In-Active"
    }

    @JsonGetter("closed")
    public String getClosed() {
        return closed ? "Yes" : "No";  // If 'cancel' is true, return "Yes", otherwise "No"
    }

    @JsonGetter("currentFinYear")
    public String getCurrentFinYear() {
        return currentFinYear ? "Yes" : "No";  // If 'currentFinYear' is true, return "Yes", otherwise "No"
    }

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}