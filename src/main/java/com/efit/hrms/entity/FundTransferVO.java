
package com.efit.hrms.entity;

import java.math.BigDecimal;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fundtransfer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundTransferVO {
	@Id
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fundtransfergen")
	@SequenceGenerator(name = "fundtransfergen", sequenceName = "fundtransferseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "fundtransferid")
	private Long id;
	@Column(name = "branch",length = 25)
	private String branch;
	@Column(name = "docid")
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate=LocalDate.now();
	@Column(name = "currency",length =10)
	private String currency;
	@Column(name = "exrate",precision =10,scale =2)
	private String exRate;
	@Column(name = "amount",precision =10,scale =2)
	private BigDecimal amount;
	@Column(name = "active")
	private boolean active;
	@Column(name = "orgid",length = 50)
	private Long orgId;
	@Column(name = "createdby",length = 25)
	private String createdBy;
	@Column(name = "modifiedby",length = 25)
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks",length = 50)
	private String cancelRemarks;
	@Column(name = "branchcode",length =10)
	private String branchCode;
	@Column(name = "finyear",length =10)
	private String finYear;
	@Column(name = "Screencode",length =10)
	private String ScreenCode="FT";
	@Column(name = "Screenname",length =15)
	private String ScreenName="FUND TRANSFER";
	@Column(name = "ipno",length =15)
	private String ipNo;
	@Column(name = "latitude",length =100)
	private String latitude;
	@Column(name = "mode",length =25)
	private String mode;
	@Column(name = "docno",length =50)
	private String docNo;
	@Column(name = "corpaccount",length =50)
	private String corpAccount;
	@Column(name = "transferto",length =50)
	private String transferTo;
	@Column(name = "branchacc",length =50)
	private String branchAcc;
	@Column(name = "narration",length =150)
	private String narration;
	@Column(name = "amtbase",precision =10,scale =2)
	private BigDecimal amtBase;
 
	


	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
