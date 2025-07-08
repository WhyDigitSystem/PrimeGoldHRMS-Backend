package com.efit.hrms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
	private Long id;

	private String customerName;
	private Long orgId;
	private String customerShortName;
	private String panNo;
	private String contactPerson;
	private String mobileNumber;
	private String gstRegistration;
	private String emailId;
	private String groupOf;
	private String tanNo;
	private String address1;
	private String address2;
	private String gstNo;
	private String city;
	private String state;
	private String country;
	private String cancelRemarks;
	private String createdBy;
	private String updatedBy;
	private boolean active;
	private boolean cancel;

	private List<ClientDTO> clientDTO;
	
	private List<ClientBranchDTO>clientBranchDTO;

}
