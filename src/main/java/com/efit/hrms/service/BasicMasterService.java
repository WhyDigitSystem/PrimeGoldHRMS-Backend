package com.efit.hrms.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.AnnouncementDTO;
import com.efit.hrms.dto.CalendarDTO;
import com.efit.hrms.dto.CheckInOutAdjustmentDTO;
import com.efit.hrms.dto.CheckinRequestDTO;
import com.efit.hrms.dto.CircularDTO;
import com.efit.hrms.dto.HolidayDTO;
import com.efit.hrms.dto.PollVoteDTO;
import com.efit.hrms.dto.PollsDTO;
import com.efit.hrms.dto.PraiseDTO;
import com.efit.hrms.dto.TaskDTO;
import com.efit.hrms.dto.UserNameDTO;
import com.efit.hrms.entity.AnnouncementVO;
import com.efit.hrms.entity.CalendarVO;
import com.efit.hrms.entity.CheckInOutAdjustmentVO;
import com.efit.hrms.entity.CheckInVO;
import com.efit.hrms.entity.CircularVO;
import com.efit.hrms.entity.HolidayVO;
import com.efit.hrms.entity.PollsVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface BasicMasterService {

	Map<String, Object> createCheckInOut(UserNameDTO userNameDTO) throws ApplicationException;

	Map<String, Object> createRequestCheckOut(CheckinRequestDTO checkinRequestDTO) throws ApplicationException;

	List<Map<String, Object>> getRequestCheckOutByOrgId(Long orgId, String branch, String reportingPersoncode);

	Map<String, Object> createApprovalCheckOut(Long orgId, String employeeCode, String action, String actionBy, LocalDate localCheckInDate, String notifyCode, String notify, String screenName)
			throws ApplicationException;
	
	Map<String, Object> createUpdateHolidays(HolidayDTO holidayDTO) throws ApplicationException;

	List<HolidayVO> getAllHolidayByOrgId(Long orgId);

	HolidayVO getHolidayById(Long id);
	
	HolidayVO uploadHolidayImageInBloob(MultipartFile file, Long id) throws IOException, IOException;


	List<Map<String, Object>> getStatusByEmpcode(String empcode);

	List<Map<String, Object>> getAttendanceByEmpcode(String empcode, int  month, String orgId, String branch, String branchCode);

	void excelUploadForHolidays(MultipartFile[] files, String createdBy, Long orgId)
			throws EncryptedDocumentException, ApplicationException, java.io.IOException;

	int getTotalRows();

	int getSuccessfulUploads();
	
	//adjustment checkinout
	
	Map<String, Object> createCheckInOutAdjustment(CheckInOutAdjustmentDTO checkInOutAdjustmentDTO) throws ApplicationException;

	Map<String, Object> createApprovalCheckInOutAdjustment(Long orgId, String employeeCode, String action,
			String actionBy, LocalDate localCheckOutDate, String notifyCode, String notify, String screenName) throws ApplicationException;

	List<Map<String, Object>> getRequestCheckInOutByOrgId(Long orgId, String branch, String reportingPersoncode);

	//circular//
	
	Map<String, Object> createUpdatecircular(CircularDTO circularDTO) throws ApplicationException;

	List<CircularVO> getAllCircularByOrgId(Long orgId, String branchCode, String department, String type);
	
	CircularVO getCircularById(Long id);
	
	//polls//
	
	Map<String, Object> createUpdatepolls(PollsDTO pollsDTO) throws ApplicationException;
	
List<PollsVO> getAllPollsByOrgId(Long orgId, String branchCode, String department, String type);
	
PollsVO getPollById(Long id);

// pollVote//

Map<String, Object> createUpdatepollVote(List<PollVoteDTO> pollVoteDTO) throws ApplicationException;

List<Map<String, Object>> getPollResultForUser(Long orgId,String userName,Long pollId);

List<Map<String, Object>> getPollResultForHR(Long orgId,Long pollId);

// Announcement //

Map<String, Object> createUpdateAnnouncement(AnnouncementDTO announcementDTO) throws ApplicationException;

List<AnnouncementVO> GetAnnouncementByOrgId(Long orgId, String branchCode);

AnnouncementVO GetAnnouncementById(Long id);

CircularVO uploadPostImageInBloob(MultipartFile file, Long id) throws IOException;

//Praise //


Map<String, Object> createUpdatePraise(PraiseDTO praiseDTO) throws ApplicationException;

List<Map<String, Object>> GetCountOfPraiseByOrgIdAndCircularId(Long circularId ,Long Orgid);

//Task//

Map<String, Object> createUpdateTask(TaskDTO taskDTO) throws ApplicationException;

List<Map<String, Object>> GetNewTask(String Assignedto ,Long Orgid);

List<Map<String, Object>> GetPendingTask(String Assignedto ,Long Orgid);
	

List<Map<String, Object>> GetCountofPendingTask(String Assignedto ,Long Orgid);
	

List<Map<String, Object>> GetCountofNewTask(String Assignedto ,Long Orgid);

List<Map<String, Object>> GetNewAssignedTask(String Assignedby ,Long Orgid);

List<Map<String, Object>> GetPendingAssignedTask(String Assignedby ,Long Orgid);
	

List<Map<String, Object>> GetCountofPendingAssignedTask(String Assignedby ,Long Orgid);
	

List<Map<String, Object>> GetCountofNewAssignedTask(Long Orgid,String Assignedby);


//Dashboard

	List<Map<String, Object>> getEmpDob(Long orgId);
	
	List<Map<String, Object>> GetworkAniversary (Long OrgId);
	

	List<Map<String, Object>> GetnewJoineDetails (Long Orgid);

// payslip
	
	List<Map<String, Object>> getpayslipemployeedetails(Long orgId, String Employeecode);
	
	
	List<Map<String, Object>> getpayslipearningdetails(Long orgId, String Employeecode,Long Month,Long year);
	
	List<Map<String, Object>> getpayslipdeductiondetails(Long orgId, String Employeecode, Long month, Long year);
	

	
	List<Map<String, Object>> getpayslipCompanydetails (Long orgId);

	List<Map<String, Object>> getpaysliphandsondetails(Long orgId, String employeecode, Long month, Long year);

	//getAttandanceReport
	
	List<Map<String, Object>> getTodayAttendanceReportByOrgId(Long orgId, String branch, String date);

	Map<String, Object> createUpdateCalendar(CalendarDTO calendarDTO) throws ApplicationException;

	List<CalendarVO> getAllCalendarByOrgId(Long orgId, String branchCode, String empCode);

	CalendarVO getCalendarById(Long id);

	//ApprovalPendingCount
	List<Map<String, Object>> getApprovalPendingCountForDashBoard(Long orgId, String reportingPersonCode, String branchCode);

	
	//Calendar Notification
	List<CalendarVO> getCalendarNotificationByOrgId(Long orgId, String branchCode, String empCode);



	








	


	
	
	
}
