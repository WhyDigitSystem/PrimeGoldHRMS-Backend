package com.efit.hrms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.efit.hrms.dto.TimeSheetDTO;
import com.efit.hrms.entity.TimeSheetVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface TimeSheetService {

	Map<String, Object> createUpdateTimeSheet(@Valid TimeSheetDTO timeSheetDTO) throws ApplicationException;

	List<TimeSheetVO> getTimeSheetByOrgId(Long orgId, String empCode, String date);

	TimeSheetVO getTimeSheetById(Long id);

	List<Map<String, Object>> getApprovedLeaveForTimeSheet(Long orgId, LocalDate date, String employeeCode);

	List<TimeSheetVO> getTimeSheetDescByOrgId(Long orgId, String empCode, String branchCode, String fromDate, String toDate);

	List<Map<String, Object>> getApprovedLeaveForTimeSheetReport(Long orgId, LocalDate fromDate, LocalDate toDate,
			String branchCode, String employeeCode);

	List<Map<String, Object>> getHolidaysForTimeSheetReport(Long orgId, LocalDate fromDate, LocalDate toDate,
			String branchCode);

}
