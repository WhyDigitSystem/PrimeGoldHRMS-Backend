package com.efit.hrms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.efit.hrms.entity.AttendanceLogVO;

@Service
public interface AttendanceLogService {
	
	

	List<AttendanceLogVO> getAllAttendanceLogDetails(String startDate, String endDate);

	
	

}
