package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyDTO {

	private Long id;
	private String country;
	private String currency;
	private String subCurrency;
	private String currencyDescription;
	private Long orgId;
	private boolean active;
	private String createdBy;
}
