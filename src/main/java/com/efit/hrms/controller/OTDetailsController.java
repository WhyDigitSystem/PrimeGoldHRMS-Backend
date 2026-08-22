package com.efit.hrms.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.service.OvertimeService;

@CrossOrigin
@RestController
@RequestMapping("/api/otdetails")
public class OTDetailsController extends BaseController {

    public static final Logger LOGGER =
            LoggerFactory.getLogger(OTDetailsController.class);

    @Autowired
    private OvertimeService overtimeService;

    @GetMapping("OTDetailsForAttendance")
    public ResponseEntity<ResponseDTO> getOvertimeReport(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam Long orgId) {

        String methodName = "getOvertimeReport()";

        LOGGER.debug(
                CommonConstant.STARTING_METHOD,
                methodName
        );

        String errorMsg = null;

        Map<String, Object> responseObjectsMap =
                new HashMap<>();

        ResponseDTO responseDTO = null;

        List<Map<String, Object>> data = null;

        try {

            data = overtimeService.getOvertimeReport(
                    fromDate,
                    toDate,
                    orgId
            );

        } catch (Exception e) {

            errorMsg = e.getMessage();

            LOGGER.error(
                    UserConstants.ERROR_MSG_METHOD_NAME,
                    methodName,
                    errorMsg
            );
        }

        if (StringUtils.isEmpty(errorMsg)) {

            responseObjectsMap.put(
                    CommonConstant.STRING_MESSAGE,
                    "OT Report Loaded Successfully"
            );

            responseObjectsMap.put(
                    "fromDate",
                    fromDate
            );

            responseObjectsMap.put(
                    "toDate",
                    toDate
            );

            responseObjectsMap.put(
                    "orgId",
                    orgId
            );

            responseObjectsMap.put(
                    "data",
                    data
            );

            responseObjectsMap.put(
                    "count",
                    data != null ? data.size() : 0
            );

            responseDTO = createServiceResponse(
                    responseObjectsMap
            );

        } else {

            errorMsg = "Failed to load OT report: " + errorMsg;

            responseDTO = createServiceResponseError(
                    responseObjectsMap,
                    "OT Report not found",
                    errorMsg
            );
        }

        LOGGER.debug(
                CommonConstant.ENDING_METHOD,
                methodName
        );

        return ResponseEntity
                .ok()
                .body(responseDTO);
    }
}