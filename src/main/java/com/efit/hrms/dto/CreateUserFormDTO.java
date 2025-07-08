package com.efit.hrms.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserFormDTO {

	private Long userId;
	private String userName;
	private String password;
	private String userType;
	private boolean allIndiaAccess;
	private String employeeCode;
	private String employeeName;
	private String email;
    private String reportingTO;
	private String location;
	private boolean isActive;
	private LocalDate deactivatedOn;
	private Long orgId;

	private List<UserRoleDTO> userRoleDTO;
	private List<BranchAccessDTO> branchAccessDTO;

}