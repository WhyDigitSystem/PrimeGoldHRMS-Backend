package com.efit.hrms.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.AttendanceSummaryDTO;
import com.efit.hrms.dto.CheckInOutBiometricDTO;
import com.efit.hrms.entity.AttendanceDailyVO;
import com.efit.hrms.entity.AttendanceSummaryVO;
import com.efit.hrms.entity.OtCalculationVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface CheckInOutService {


	String checkInOutUploadExcel(MultipartFile file, Long orgId, String createdBy) throws Exception;

	List<Map<String, Object>> getLeaveDetailsForAttendanceProcess(String fromDate, String toDate, Long orgId,
			String department, String branch, String type, String contractor);


	List<OtCalculationVO> generateOtAndSave(Long orgId);

	Map<String, Object> createCheckInOutBiometric(CheckInOutBiometricDTO checkInOutBiometricDTO) throws ApplicationException;

	//monthlyprocess
	List<Map<String, Object>> getMonthlyProcess(int month, int year, Long orgId, String branch, String department, String type, String contractor);

	List<AttendanceDailyVO> getAttendanceDailyByOrgId(String fromDate, String toDate, Long orgId,
			String employeeCode, String branch);

	Map<String, Object> createUpdateAttendanceSummary(@Valid List<AttendanceSummaryDTO> attendanceSummaryDTO) throws ApplicationException;

	Map<String, Object> createApprovalAttendanceSummary(Long orgId, List<Long> id, String action,
			String actionBy) throws ApplicationException;

	List<AttendanceSummaryVO> getPendingAttendanceSummaryByOrgId( Long orgId,
			 String branch);

	List<AttendanceSummaryVO> getAttendanceSummaryByOrgId(String empCode, Integer month, String finYear, Long orgId, String branch);

	List<OtCalculationVO> getPendingOTHoursByOrgId(String fromDate, String toDate, Long orgId, String employeeCode,
			String branch, String department, String type, String contractor);

	List<OtCalculationVO> getApprovedOTHoursByOrgId(String fromDate, String toDate, Long orgId, String employeeCode,
			String branch, String department, String type, String contractor);

//	List<AttendanceRecordVO> processExcel(MultipartFile file) throws Exception;





}
