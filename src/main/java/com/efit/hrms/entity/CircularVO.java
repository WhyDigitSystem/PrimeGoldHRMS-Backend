package com.efit.hrms.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "circular")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CircularVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "circulargen")
	@SequenceGenerator(name = "circulargen", sequenceName = "circularseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "circularid")
	private Long id;
	@Column(name = "orgid")
	private long orgId;
//		@Column(name = "branchid")
//		private String branchId;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branchname")
	private String branchName;
	@Column(name = "department")
	private String department;
	@Column(name = "type")
	private String type;

	@Column(name = "circularcontent",length = 2000)
	private String circularcontent;

	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
//	@Column(name = "finyear", length = 10)
//	private String finYear;
	@Column(name = "screencode", length = 30)
	private String screenCode = "CIR";
	@Column(name = "screenname", length = 30)
	private String screenName = "circular";
	@Column(name = "active")
	private boolean active=true;
	@Column(name = "circulartopic")
	private String circularTopic;
	@Column(name = "expiresdate")
	private LocalDate expiresDate;

	@Lob
	@Column(name = "postimage", columnDefinition = "LONGBLOB") // Ensure the column is LONGBLOB
	private byte[] postImage;

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";

	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
