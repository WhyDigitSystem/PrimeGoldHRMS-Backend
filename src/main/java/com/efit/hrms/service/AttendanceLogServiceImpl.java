package com.efit.hrms.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.efit.hrms.entity.AttendanceLogVO;
import com.efit.hrms.repo.AttendanceLogRepo;

@Service
public class AttendanceLogServiceImpl implements AttendanceLogService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AttendanceLogServiceImpl.class);
	
	@Autowired
	AttendanceLogRepo attendanceLogRepo;
	
	

	@Override
	public List<AttendanceLogVO> getAllAttendanceLogDetails(String startDate,String endDate) {
		
		RestTemplate restTemplate = new RestTemplate();
		try {
		    String url = "http://localhost:8082/api/WebAPI/GetAttendanceInOutProcessedET"
		               + "?AppKey=2716110845479"
		               + "&StartDate=" + startDate
		               + "&EndDate=" + endDate;
		    AttendanceLogVO[] logs = restTemplate.getForObject(url, AttendanceLogVO[].class);
		    
		    if (logs != null) {
	            for (AttendanceLogVO log : logs) {
	            	Optional<AttendanceLogVO> existing = attendanceLogRepo.findById(log.getAttendanceLogId());
	                if (existing.isPresent()) {
	                    AttendanceLogVO existingLog = existing.get();
	                    attendanceLogRepo.delete(existingLog);
	                    
	                    attendanceLogRepo.save(log);
	                } else {
	                    attendanceLogRepo.save(log);
	                }
	            }
	        }	
		}catch (Exception e) {
		    e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	
	

}
