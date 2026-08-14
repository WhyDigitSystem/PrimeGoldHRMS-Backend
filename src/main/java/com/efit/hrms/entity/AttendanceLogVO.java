package com.efit.hrms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendancelog")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttendanceLogVO {

    @Id
    @Column(name = "attendancelogid")
    @JsonProperty("AttendanceLogId")
    private Long attendanceLogId;

    @Column(name = "employeename")
    @JsonProperty("EmployeeName")
    private String employeeName;

    @Column(name = "employeecode")
    @JsonProperty("EmployeeCode")
    private String employeeCode;

    @Column(name = "employeecodeindevice")
    @JsonProperty("EmployeeCodeInDevice")
    private String employeeCodeInDevice;

    @Column(name = "attendancedate")
    @JsonProperty("AttendanceDate")
    private String attendanceDate;

    @Column(name = "intime")
    @JsonProperty("InTime")
    private String inTime;

    @Column(name = "outtime")
    @JsonProperty("OutTime")
    private String outTime;

    @Column(name = "attendancestatus")
    @JsonProperty("AttendanceStatus")
    private String attendanceStatus;

    @Column(name = "attendancestatuscode")
    @JsonProperty("AttendanceStatusCode")
    private String attendanceStatusCode;

    @Column(name = "workdurationminutes")
    @JsonProperty("WorkDurationMinutes")
    private String workDurationMinutes;

    @Column(name = "shiftname")
    @JsonProperty("ShiftName")
    private String shiftName;

    @Column(name = "shiftbegintime")
    @JsonProperty("ShiftBeginTime")
    private String shiftBeginTime;

    @Column(name = "shiftendtime")
    @JsonProperty("ShiftEndTime")
    private String shiftEndTime;

    @Column(name = "companyname")
    @JsonProperty("CompanyName")
    private String companyName;

    @Column(name = "categoryname")
    @JsonProperty("CategoryName")
    private String categoryName;

    @Column(name = "departmentname")
    @JsonProperty("DepartmentName")
    private String departmentName;

    @Column(name = "designation")
    @JsonProperty("Designation")
    private String designation;

    @Column(name = "indevice")
    @JsonProperty("InDevice")
    private String inDevice;

    @Column(name = "outdevice")
    @JsonProperty("OutDevice")
    private String outDevice;

    @Column(name = "lateby")
    @JsonProperty("LateBy")
    private String lateBy;

    @Column(name = "earlyby")
    @JsonProperty("EarlyBy")
    private String earlyBy;

    @Column(name = "leavetype")
    @JsonProperty("LeaveType")
    private String leaveType;

    @Column(name = "leaveremarks")
    @JsonProperty("LeaveRemarks")
    private String leaveRemarks;

    @Column(name = "overtime")
    @JsonProperty("OverTime")
    private String overTime;

    @Column(name = "punchrecords", length = 5000)
    @JsonProperty("PunchRecords")
    private String punchRecords;

    @Column(name = "status")
    @JsonProperty("Status")
    private String status;

}
