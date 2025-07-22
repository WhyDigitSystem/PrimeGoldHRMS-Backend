package com.efit.hrms.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.UserNameDTO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface CheckInOutService {

//	Map<String, Object> createCheckInOut(UserNameDTO userNameDTO) throws ApplicationException;

	String checkInOutUploadExcel(MultipartFile file, Long orgId, String createdBy) throws Exception;

	List<Map<String, Object>> getLeaveDetailsForAttendanceProcess(String fromDate, String toDate, Long orgId,
			String department, String branch);



}
