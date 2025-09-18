package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "devicelog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLogVO {
	
	@Id
	@Column(name = "devicelogid")
    @JsonProperty("DeviceLogId")
    private Long deviceLogId;

    @Column(name = "deviceid")
    @JsonProperty("DeviceId")
    private String deviceId;

    @Column(name = "userid")
    @JsonProperty("UserId")
    private String userId;

    @Column(name = "employeecode")
    @JsonProperty("EmployeeCode")
    private String employeeCode;

    @Column(name = "employeename")
    @JsonProperty("EmployeeName")
    private String employeeName;

    @Column(name = "logdate")
    @JsonProperty("LogDate")
    private String logDate;

    @Column(name = "downloaddate")
    @JsonProperty("DownloadDate")
    private String downloadDate;

    @Column(name = "attendancedate")
    @JsonProperty("AttendanceDate")
    private String attendanceDate;

    @Column(name = "attendancetime")
    @JsonProperty("AttendanceTime")
    private String attendanceTime;

    @Column(name = "location")
    @JsonProperty("Location")
    private String location;

    @Column(name = "direction")
    @JsonProperty("Direction")
    private String direction;

    @Column(name = "attdirection")
    @JsonProperty("AttDirection")
    private String attDirection;

    @Column(name = "devicename")
    @JsonProperty("DeviceName")
    private String deviceName;

    @Column(name = "serialnumber")
    @JsonProperty("SerialNumber")
    private String serialNumber;

    @Column(name = "ipaddress")
    @JsonProperty("IpAddress")
    private String ipAddress;

    @Column(name = "dateofjoining")
    @JsonProperty("DateOfJoining")
    private String dateOfJoining;

    @Column(name = "status")
    @JsonProperty("Status")
    private String status;

}
