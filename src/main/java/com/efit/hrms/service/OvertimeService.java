package com.efit.hrms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface OvertimeService {

	List<Map<String, Object>> getOvertimeReport(LocalDate fromDate, LocalDate toDate, Long orgId);

}
