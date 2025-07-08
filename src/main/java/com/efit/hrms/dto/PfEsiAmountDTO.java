package com.efit.hrms.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PfEsiAmountDTO {

	private String heading;
	private BigDecimal amount;
}
