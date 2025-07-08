
package com.efit.hrms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;
import com.efit.hrms.dto.Role;
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersgen")
	@SequenceGenerator(name = "usersgen", sequenceName = "usersseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "userid")
	private Long id;

	@Column(name = "username")
	private String userName;
	@Column(name = "password")
	private String password;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "nickname")
	private String nickName;
	@Column(name = "email")
	private String email;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "mobileno")
	private String mobileNo;
	@Column(name = "usertype")
	private String userType;
	@Column(name = "branch")
	private String branch;
	@Column(name = "branchcode")
	private String branchcode;
	@Column(name = "department")
	private String department;

	@Column(name = "companyname")
	private String companyName;
	@Column(name = "designation")
	private String designation;
	@Column(name = "loginstatus")
	private boolean loginStatus;
	@Column(name = "active")
	private boolean active;
	@Column(name = "createdby")
	private String createdby;
	@Column(name = "modifiedby")
	private String updatedby;
	@Column(name = "role")
	private Role role;
	@Column(name = "allindiaaccess")
	private boolean allIndiaAcces;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

	@OneToMany(mappedBy = "userVO", cascade = CascadeType.ALL)
	private List<UserLoginRolesVO> roleAccessVO;

//	@OneToMany(mappedBy = "userVO", cascade = CascadeType.ALL)
//	private List<UserLoginClientAccessVO> clientAccessVO;

	@OneToMany(mappedBy = "userVO", cascade = CascadeType.ALL)
	private List<UserLoginBranchAccessibleVO> branchAccessibleVO;
	
	private Date accountRemovedDate;
	
	@ManyToOne
	@JoinColumn(name="companyid")
	private CompanyVO companyVO;
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
