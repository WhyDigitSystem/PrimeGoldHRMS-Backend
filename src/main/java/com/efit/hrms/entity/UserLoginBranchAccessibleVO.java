package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userbranchaccess")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginBranchAccessibleVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "userloginbranchaccessiblegen")
	@SequenceGenerator(name = "userloginbranchaccessiblegen",sequenceName = "userloginbranchaccessibleseq",initialValue = 1000000001,allocationSize = 1)
	@Column(name="userloginbranchaccessibleid")
	private long id;
	
	@Column(name="branch")
	private String branch;
	@Column(name="branchcode")
	private String branchcode;
	
	@JsonBackReference
	@ManyToOne
    @JoinColumn(name = "usersid")
    private UserVO userVO;
	
//	@Embedded
//	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}