package com.efit.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companygen")
	@SequenceGenerator(name = "companygen", sequenceName = "companyseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "companyid")
	private Long id;

	@Column(name = "companycode")
	private String companyCode;
	@Column(name = "companyname")
	private String companyName;
	@Column(name = "country")
	private String country;
	@Column(name = "currency")
	private String currency;
	//@Column(name = "maincurrency")
	//private String mainCurrency;
	@Column(name = "address")
	private String address;
	@Column(name = "zipcode")
	private String zip;
	@Column(name = "city")
	private String city;
	@Column(name = "state")
	private String state;
	@Column(name = "phone")
	private String phone;
	@Column(name = "leavecreditcontrol")
	private String leaveCreditControl;
	@Column(name = "leavepolicy")
	private String leavePolicy;
	@Column(name = "autocreditdate ")
	private LocalDate autoCreditDate;
	@Column(name = "email")
	private String email;
//	@Column(name = "website")
//	private String webSite;
	//@Column(name = "notes")
	//private String note;
//	@Column(name = "userd")
//	private String userId;
	@Column(name = "active")
	private boolean active;
//	@Column(unique = true)
//	private String dupchk;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;
	//private int role;
	private String ceo;
	@Column(name = "gstregistered")
	private boolean gstRegistered;
	@Column(name = "panno")
	private String panNo;
	@Column(name = "gstin")
	private String gstIn;
	@Column(name = "employeename")
	private String employeeName;
	@Column(name = "employeecode")
	private String employeeCode;
	@Column(name = "attendancemode")
	private String attendanceMode;
	
	@Column(name = "shiftin")
	private String shiftIn; 
	@Column(name = "shiftout")
	private String shiftOut;
	
	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "hybrid")
	private boolean hybrid;
	
	@Column(name = "otflag")
	private OverTime otFlag;
	
	@Column(name = "ottype")
	private String otType;
	
	@Column(name = "shifthours")
	private BigDecimal shiftHours;
	
	@Column(name = "oteligiblehours")
	private BigDecimal otEligibleHours;
	
	@Column(name = "otpolicy")
	private String otPolicy;

	@Column(name = "locationaddress")
	private String locationAddress;

	
	@Column(name = "companylogo", columnDefinition = "LONGBLOB")
	private byte[] companyLogo;
	
	@OneToMany(mappedBy = "companyVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<CompanyWeekOffVO>companyWeekOffVO;
	
	

	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}
	
	@JsonGetter("gstregistered")
	public String getGstRegistered() {
		return gstRegistered ?  "Active" : "In-Active";
	}

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}