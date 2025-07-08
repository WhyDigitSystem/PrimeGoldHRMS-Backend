package com.efit.hrms.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocumentsDTO {
    private String employeeCode;
    private String employeeName;
    private String documentName;
//    private String documentType;
    private Long orgId;
    private MultipartFile file;
}
