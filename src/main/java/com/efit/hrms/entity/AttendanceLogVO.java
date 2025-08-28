package com.efit.hrms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="attendancelog")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttendanceLogVO {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendancelogid")
    private Long attendanceLogId;

    @Column(name = "employeename")
    private String employeeName;

    @Column(name = "employeecode")
    private String employeeCode;

    @Column(name = "employeecodeindevice")
    private String employeeCodeInDevice;

    @Column(name = "attendancedate")
    private LocalDate attendanceDate;   // ❗️ Better: use LocalDate

    @Column(name = "intime")
    private LocalDateTime inTime;           // ❗️ Better: use LocalDateTime

    @Column(name = "outtime")
    private LocalDateTime outTime;          // ❗️ Better: use LocalDateTime

    @Column(name = "attendancestatus")
    private String attendanceStatus;

    @Column(name = "attendancestatuscode")
    private String attendanceStatusCode;

    @Column(name = "workdurationminutes")
    private String workDurationMinutes;

    @Column(name = "shiftname")
    private String shiftName;

    @Column(name = "shiftbegintime")
    private String shiftBeginTime;

    @Column(name = "shiftendtime")
    private String shiftEndTime;

    @Column(name = "companyname")
    private String companyName;

    @Column(name = "categoryname")
    private String categoryName;

    @Column(name = "departmentname")
    private String departmentName;

    @Column(name = "designation")
    private String designation;

    @Column(name = "indevice")
    private String inDevice;

    @Column(name = "outdevice")
    private String outDevice;

    @Column(name = "lateby")
    private String lateBy;

    @Column(name = "earlyby")
    private String earlyBy;

    @Column(name = "leavetype")
    private String leaveType;

    @Column(name = "leaveremarks")
    private String leaveRemarks;

    @Column(name = "overtime")
    private String overTime;

    @Column(name = "punchrecords", length = 1000)
    private String punchRecords;

    @Column(name = "status")
    private String status;

}
