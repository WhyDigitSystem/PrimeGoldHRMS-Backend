package com.efit.hrms.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;

import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
	private Long usersId;
	private String userName;
	private String employeeName; 
	private String employeeCode; 
	private String email; 
	
	private String companyName;

	private String userType;
	private Long orgId;
	private String customer;
	private String branch;
	private String branchCode;
	private String department;
	private String designation;
	private boolean loginStatus;
	private boolean active;
	private boolean allIndiaAcces;
	private List<Map<String, Object>> roleVO;
//	private List<Map<String, Object>> responsibilityVO;
//	private List<Map<String, Object>> screensVO;
//	
    @Embedded
    private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
    private Date accountRemovedDate;
    private String token;
    private String tokenId;
	

    @JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
    // Setter method to accept List<Map<String, Object>>
   
   
}
