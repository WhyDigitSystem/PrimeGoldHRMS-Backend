package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLogDTO {
    private Long attendanceLogId;
    private String employeeName;
    private String employeeCode;
    private String employeeCodeInDevice;
    private String attendanceDate;   // keep as String, parse later
    private String inTime;
    private String outTime;
    private String attendanceStatus;
    private String attendanceStatusCode;
    private String workDurationMinutes;
    private String shiftName;
    private String shiftBeginTime;
    private String shiftEndTime;
    private String companyName;
    private String categoryName;
    private String departmentName;
    private String designation;
    private String inDevice;
    private String outDevice;
    private String lateBy;
    private String earlyBy;
    private String leaveType;
    private String leaveRemarks;
    private String overTime;
    private String punchRecords;
    private String status;
}
