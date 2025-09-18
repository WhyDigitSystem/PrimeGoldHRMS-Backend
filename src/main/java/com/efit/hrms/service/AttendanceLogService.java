package com.efit.hrms.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.efit.hrms.dto.DepartmentResponse;
import com.efit.hrms.entity.AttendanceLogVO;

@Service
public interface AttendanceLogService {
	
	

	List<AttendanceLogVO> getAllAttendanceLogDetails(String startDate, String endDate);

	Map<String, DepartmentResponse> getDashboard(String date, String empType);

	List<Map<String, Object>> getEmployeeAttendanceDetails(String date, String department, String employeeType,
			String status, String missPunch, String mainDepartment);

	Map<String, String> fetchAndSaveDeviceLog(String startDate, String endDate);

	
	

}
