package com.efit.hrms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import org.springframework.transaction.annotation.Transactional;

import com.efit.hrms.repo.OtMasterRepo;


@Service
public class OverTimeServiceImpl implements OvertimeService{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(OverTimeServiceImpl.class);
	
	@Autowired
    OtMasterRepo otMasterRepo;

	@Transactional
	 @Override
	    public List<Map<String, Object>> getOvertimeReport(
	            LocalDate fromDate,
	            LocalDate toDate,
	            Long orgId) {

	        if (fromDate == null) {
	            throw new IllegalArgumentException(
	                    "From date is required"
	            );
	        }

	        if (toDate == null) {
	            throw new IllegalArgumentException(
	                    "To date is required"
	            );
	        }

	        if (fromDate.isAfter(toDate)) {
	            throw new IllegalArgumentException(
	                    "From date cannot be greater than To date"
	            );
	        }

	        if (orgId == null) {
	            throw new IllegalArgumentException(
	                    "Organization ID is required"
	            );
	        }


	        List<Map<String, Object>> result =
	                otMasterRepo.getOvertimeReport(
	                        fromDate,
	                        toDate,
	                        orgId
	                );


	        if (result == null) {
	            return Collections.emptyList();
	        }

	        return result;
	    }

}
