package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "praise")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PraiseVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "praisegen")
	@SequenceGenerator(name = "praisegen", sequenceName = "praiseseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "praiseid")
	private Long id;
	@Column(name = "orgid")
	private long orgId;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branchname")
	private String branchName;
	@Column(name = "department")
	private String department;

	@Column(name = "circularid")
	private Long circularId;
	@Column(name = "username")
	private String userName;
	@Column (name= "liked")
	private String liked;
	

	
	@Column(name = "screencode", length = 30)
	private String screenCode = "PRS";
	@Column(name = "screenname", length = 30)
	private String screenName = "praise";
	@Column(name = "active")
	private boolean active;
	@Column(name = "circulartopic")
	private String circularTopic;

}
