package com.efit.hrms.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efit.hrms.service.OvertimeService;

@CrossOrigin
@RestController
@RequestMapping("/api/otdetails")
public class OTDetailsController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(OTDetailsController.class);

	@Autowired
	private OvertimeService overtimeService;

	@GetMapping("/report")
	public ResponseEntity<Map<String, Object>> getOvertimeReport(

			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,

			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,

			@RequestParam Long orgId

	) {

		Map<String, Object> response = new LinkedHashMap<>();

		try {

			List<Map<String, Object>> data = overtimeService.getOvertimeReport(fromDate, toDate, orgId);

			response.put("message", "OT Report Loaded Successfully");

			response.put("fromDate", fromDate);

			response.put("toDate", toDate);

			response.put("data", data);

			response.put("count", data.size());

			return ResponseEntity.ok(response);

		} catch (IllegalArgumentException e) {

			response.put("message", e.getMessage());

			return ResponseEntity.badRequest().body(response);

		} catch (Exception e) {

			response.put("message", "Failed to load OT report");

			response.put("error", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
