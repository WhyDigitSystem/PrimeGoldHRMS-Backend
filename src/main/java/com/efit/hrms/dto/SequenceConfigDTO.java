package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SequenceConfigDTO {
    private Long id;
    private Long companyId;
    private String companyCode;
    private String branchCode;
    private String departmentCode;
    private Integer startingFrom;
    private Integer noOfDigits;
    private String separators;
	private String createdBy;


}
