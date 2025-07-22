
package com.efit.hrms.dto;

import java.time.LocalDate;
import java.util.List;

import com.efit.hrms.entity.OverTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

	private Long id;
	private String companyCode;
	private String companyName;
	private String country;
	private String currency;
	// private String mainCurrency;
	private String address;
	private String zip;
	private String city;
	private String state;
	private String phone;
	private String leaveCreditControl;
	private String leavePolicy;
	private LocalDate autoCreditDate;
	private String email;
	// private String webSite;
	// private String note;
	// private String userId;
	private String password;
	private String createdBy;
	private String updatedBy;
	private boolean cancel;
	private boolean active;
	private String ceo;
	private boolean gstRegistered;
	// private int role;
	private String panNo;
	private String gstIn;
	private String employeeName;
	private String employeeCode;
	private byte[] companyLogo;
	private String shiftIn;
	private String shiftOut;
	// In CompanyDTO.java
	private List<String> attendanceMode;

	private OverTime otFlag;
	private String otType;

	private Double latitude;
	private Double longitude;
	private boolean hybrid;
	private String locationAddress;

	private List<CompanyWeekOffDTO> companyWeekOffDTO;

}
