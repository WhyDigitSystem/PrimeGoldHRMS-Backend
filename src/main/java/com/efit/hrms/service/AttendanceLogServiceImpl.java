package com.efit.hrms.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.efit.hrms.dto.DepartmentResponse;
import com.efit.hrms.dto.SubDepartmentResponse;
import com.efit.hrms.entity.AttendanceLogVO;
import com.efit.hrms.entity.DeviceLogVO;
import com.efit.hrms.repo.AttendanceLogRepo;
import com.efit.hrms.repo.DeviceLogRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class AttendanceLogServiceImpl implements AttendanceLogService {

	public static final Logger LOGGER = LoggerFactory.getLogger(AttendanceLogServiceImpl.class);

	@Autowired
	AttendanceLogRepo attendanceLogRepo;

	@Autowired
	DeviceLogRepo deviceLogRepo;

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

			List<AttendanceLogVO> logs = mapper.readValue(response, new TypeReference<List<AttendanceLogVO>>() {
			});

			attendanceLogRepo.deleteByAttendanceDate(startDate);

			// Step 3: Save each log into DB
			for (AttendanceLogVO log : logs) {

				if (log.getInTime().equals("1900-01-01 00:00:00")) {
					log.setInTime(null);
				}
				if (log.getOutTime().equals("1900-01-01 00:00:00")) {
					log.setOutTime(null);
				}
				if (log.getInDevice().equals("")) {
					log.setInDevice(null);
				}
				if (log.getOutDevice().equals("")) {
					log.setOutDevice(null);
				}
				if (log.getPunchRecords().equals("")) {
					log.setPunchRecords(null);
				}
				log.setAttendanceStatus(log.getAttendanceStatus() != null ? log.getAttendanceStatus().trim() : null);
				savedLogs.add(attendanceLogRepo.save(log));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return savedLogs; // return saved data
	}

	 // Run every 1 hour (3600000 ms)
    @Scheduled(fixedRate = 3600000) 
    public void fetchAttendanceLogsEveryHour() {
        try {
            // Format current date as yyyy-MM-dd
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Call your method with today’s date as both from and to date
           getAllAttendanceLogDetailsSchedular(today, today);

            System.out.println("Scheduler executed for date: " + today);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public List<AttendanceLogVO> getAllAttendanceLogDetailsSchedular(String startDate, String endDate) {
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

			List<AttendanceLogVO> logs = mapper.readValue(response, new TypeReference<List<AttendanceLogVO>>() {
			});

			attendanceLogRepo.deleteByAttendanceDate(startDate);

			// Step 3: Save each log into DB
			for (AttendanceLogVO log : logs) {

				if (log.getInTime().equals("1900-01-01 00:00:00")) {
					log.setInTime(null);
				}
				if (log.getOutTime().equals("1900-01-01 00:00:00")) {
					log.setOutTime(null);
				}
				if (log.getInDevice().equals("")) {
					log.setInDevice(null);
				}
				if (log.getOutDevice().equals("")) {
					log.setOutDevice(null);
				}
				if (log.getPunchRecords().equals("")) {
					log.setPunchRecords(null);
				}
				log.setAttendanceStatus(log.getAttendanceStatus() != null ? log.getAttendanceStatus().trim() : null);
				savedLogs.add(attendanceLogRepo.save(log));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return savedLogs; // return saved data
	}
	
	@Override
	public List<Map<String, Object>> getEmployeeAttendanceDetails(String date, String department, String employeeType,
			String status, String missPunch, String mainDepartment) {
		Set<Object[]> attendanceDetails = new HashSet<>();
		if (employeeType.equals("Employee")) {
			attendanceDetails = attendanceLogRepo.getEmployeeAttendance(date, department, employeeType, status,
					missPunch);
		} else {
			attendanceDetails = attendanceLogRepo.getEmployeeAttendanceContractor(date, department, status, missPunch,
					mainDepartment);
		}

		return employeeAttendance(attendanceDetails);
	}

	private List<Map<String, Object>> employeeAttendance(Set<Object[]> attendanceDetails) {
		List<Map<String, Object>> attendance = new ArrayList<>();
		for (Object[] ch : attendanceDetails) {
			Map<String, Object> map = new HashMap<>();
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

		if (empType.equals("Employee")) {
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
		} else {
			String mainDept = null; // 1. Main departments
			for (Object[] row : attendanceLogRepo.getContractMainDepartments(date, empType)) {
				mainDept = (String) row[0];
				int present = ((Number) row[1]).intValue();
				int absent = ((Number) row[2]).intValue();
				int miss = ((Number) row[3]).intValue();

				DepartmentResponse dept = new DepartmentResponse();
				dept.setPresent(present);
				dept.setAbsent(absent);
				dept.setMissingPunch(miss);
				dept.setSubDepartments(new LinkedHashMap<>());

				result.put(mainDept, dept);
				// 2. Sub departments
				for (Object[] subRow : attendanceLogRepo.getContractorSubDepartments(date, mainDept)) {
					String subDept = (String) subRow[0];
					int presentSub = ((Number) subRow[1]).intValue();
					int absentSub = ((Number) subRow[2]).intValue();
					int missSub = ((Number) subRow[3]).intValue();

					SubDepartmentResponse sub = new SubDepartmentResponse();
					sub.setPresent(presentSub);
					sub.setAbsent(absentSub);
					sub.setMissingPunch(missSub);

					result.get(mainDept).getSubDepartments().put(subDept, sub);

				}
			}
		}

		return result;
	}

	private String findMainDepartmentFor(String subDept) {
		// 🔥 You can map from DB or config file
		if (subDept.equals("WORK SHOP") || subDept.equals("MILL") || subDept.equals("PRODUCTION")
				|| subDept.equals("MECHANICAL RM") || subDept.equals("ELECTRICAL RM")) {
			return "ROLLING MILL";
		}
		if (subDept.equals("SCRAP YARD") || subDept.equals("SMS LAB") || subDept.equals("ELECTRICAL")
				|| subDept.equals("MECHANICAL") || subDept.equals("SMS")) {
			return "SMS";
		}
		return "ADMIN";
	}

	@Scheduled(cron = "0 0 11 * * ?")
	public void scheduledFetchAndSaveDeviceLog() {
	    // Step 1: get current date
	    LocalDate currentDate = LocalDate.now();

	    // Step 2: subtract one day
	    LocalDate previousDate = currentDate.minusDays(1);

	    // Step 3: format as yyyy-MM-dd string
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String formattedDate = previousDate.format(formatter);

	    // Step 4: call your service method with strings
	    fetchAndSaveDeviceLog(formattedDate, formattedDate);
	}
	@Override
	public Map<String, String> fetchAndSaveDeviceLog(String startDate, String endDate) {

		RestTemplate restTemplate = new RestTemplate();
		List<DeviceLogVO> savedLogs = new ArrayList<>();
		String message=null;
		try {
			String url = "http://localhost:8082/api/WebAPI/GetAttendanceBetweenDates" + "?AppKey=2716110845479"
					+ "&StartDate=" + startDate + "&EndDate=" + endDate;
			String response = restTemplate.getForObject(url, String.class);
			System.out.println("Raw JSON Response: " + response);

			// Step 2: Convert JSON into list of AttendanceLogVO
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule()); // handle LocalDate & LocalDateTime

			List<DeviceLogVO> logs = mapper.readValue(response, new TypeReference<List<DeviceLogVO>>() {
			});

			// Step 3: Save each log into DB
			for (DeviceLogVO log : logs) {

				savedLogs.add(deviceLogRepo.save(log));
				message="Device Log Get SucessFully";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message=e.getMessage();
		}

		Map<String,String>response= new HashMap<>();
		response.put("message", message);
		return response; // return saved data
	}

}
