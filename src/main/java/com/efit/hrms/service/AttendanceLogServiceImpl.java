package com.efit.hrms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.efit.hrms.dto.DepartmentResponse;
import com.efit.hrms.dto.SubDepartmentResponse;
import com.efit.hrms.entity.AttendanceLogVO;
import com.efit.hrms.repo.AttendanceLogRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class AttendanceLogServiceImpl implements AttendanceLogService {

	public static final Logger LOGGER = LoggerFactory.getLogger(AttendanceLogServiceImpl.class);

	@Autowired
	AttendanceLogRepo attendanceLogRepo;

	@Override
	public List<AttendanceLogVO> getAllAttendanceLogDetails(String startDate, String endDate) {
		RestTemplate restTemplate = new RestTemplate();
		List<AttendanceLogVO> savedLogs = new ArrayList<>();

		try {
			String url = "http://localhost:8082/api/WebAPI/GetAttendanceInOutProcessedET" + "?AppKey=2716110845479"
					+ "&StartDate=" + startDate + "&EndDate=" + endDate;

			String response = restTemplate.getForObject(url, String.class);
	        System.out.println("Raw JSON Response: " + response);

	        // Step 2: Convert JSON into list of AttendanceLogVO
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.registerModule(new JavaTimeModule()); // handle LocalDate & LocalDateTime

	        List<AttendanceLogVO> logs = mapper.readValue(
	            response,
	            new TypeReference<List<AttendanceLogVO>>() {}
	        );

	        // Step 3: Save each log into DB
	        for (AttendanceLogVO log : logs) {
	            if (log.getAttendanceLogId() != null) {
	            	
	            	if(log.getInTime().equals("1900-01-01 00:00:00")) {log.setInTime(null);}
	            	if(log.getOutTime().equals("1900-01-01 00:00:00")) {log.setOutTime(null);}
	            	if(log.getInDevice().equals("")) {log.setInDevice(null);}
	            	if(log.getOutDevice().equals("")) {log.setOutDevice(null);}
	            	if(log.getPunchRecords().equals("")) {log.setPunchRecords(null);}
	            	log.setAttendanceStatus(log.getAttendanceStatus() != null ? log.getAttendanceStatus().trim() : null);
	                savedLogs.add(attendanceLogRepo.save(log));
	            } else {
	                System.out.println("Skipping log with null ID: " + log);
	            }
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return savedLogs; // return saved data
	}

	@Override
	public List<Map<String, Object>> getEmployeeAttendanceDetails(String date, String department, String employeeType,
			String status, String missPunch) {
		Set<Object[]>attendanceDetails=attendanceLogRepo.getEmployeeAttendance(date, department, employeeType, status, missPunch);
		
		return employeeAttendance(attendanceDetails);
	}

	private List<Map<String, Object>> employeeAttendance(Set<Object[]> attendanceDetails) {
		List<Map<String, Object>>attendance=new ArrayList<>();
		for(Object [] ch:attendanceDetails) {
			Map<String,Object> map= new HashMap<>();
			map.put("employeeCode", ch[0] != null ? ch[0].toString() : "");
			map.put("employeeName", ch[1] != null ? ch[1].toString() : "");
			map.put("department", ch[2] != null ? ch[2].toString() : "");
			map.put("desigation", ch[3] != null ? ch[3].toString() : "");
			map.put("inTime", ch[6] != null ? ch[6].toString() : "");
			map.put("outTime", ch[7] != null ? ch[7].toString() : "");
			attendance.add(map);
		}
		return attendance;
	}

	
	
	
	@Override
    public Map<String, DepartmentResponse> getDashboard(String date, String empType) {
        Map<String, DepartmentResponse> result = new LinkedHashMap<>();

        // 1. Main departments
        for (Object[] row : attendanceLogRepo.getMainDepartments(date, empType)) {
            String mainDept = (String) row[0];
            int present = ((Number) row[1]).intValue();
            int absent = ((Number) row[2]).intValue();
            int miss = ((Number) row[3]).intValue();

            DepartmentResponse dept = new DepartmentResponse();
            dept.setPresent(present);
            dept.setAbsent(absent);
            dept.setMissingPunch(miss);
            dept.setSubDepartments(new LinkedHashMap<>());

            result.put(mainDept, dept);
        }

        // 2. Sub departments
        for (Object[] row : attendanceLogRepo.getSubDepartments(date, empType)) {
            String subDept = (String) row[0];
            int present = ((Number) row[1]).intValue();
            int absent = ((Number) row[2]).intValue();
            int miss = ((Number) row[3]).intValue();

            SubDepartmentResponse sub = new SubDepartmentResponse();
            sub.setPresent(present);
            sub.setAbsent(absent);
            sub.setMissingPunch(miss);

            // TODO: Map subDept → mainDept (if needed, use a lookup table or DB relation)
            // Example: If "ELECTRICAL" belongs to "ROLLING MILL"
            String mainDept = findMainDepartmentFor(subDept);

            if (result.containsKey(mainDept)) {
                result.get(mainDept).getSubDepartments().put(subDept, sub);
            }
        }

        return result;
    }

    private String findMainDepartmentFor(String subDept) {
        // 🔥 You can map from DB or config file
        if ( subDept.equals("WORK SHOP") || subDept.equals("BILLET YARD") || subDept.equals("PRODUCTION")) {
            return "ROLLING MILL";
        }
        if (subDept.equals("SCRAP YARD") || subDept.equals("SMS LAB") || subDept.equals("ELECTRICAL") || subDept.equals("MECHANICAL")) {
            return "SMS";
        }
        return "ADMIN";
    }

}
