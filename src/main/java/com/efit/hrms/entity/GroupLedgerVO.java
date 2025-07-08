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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "groupledger")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupLedgerVO {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "groupledgergen")
	@SequenceGenerator(name = "groupledgergen", sequenceName = "groupledgerseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "accountcode")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "groupname", length = 150)
	private String groupName;
	@Column(name = "gsttaxflag", length = 50)
	private String gstTaxFlag;
	@Column(name = "coalist", length = 50)
	private String coaList;
	@Column(name = "accountgroupname", length = 150)
	private String accountGroupName;
	@Column(name = "type", length = 50)
	private String type;
	@Column(name = "interbranchac")
	private boolean interBranchAc;
	@Column(name = "controllac")
	private boolean controllAc;
	@Column(name = "category", length = 50)
	private String category;
	@Column(name = "currency", length = 50)
	private String currency;
	@Column(name = "createdby", length = 50)
	private String createdBy;
	@Column(name = "modifiedby", length = 50)
	private String updatedBy;
	@Column(name = "cancelremarks", length = 150)
	private String cancelRemarks;
	private boolean cancel;
	private boolean active;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
