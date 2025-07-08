package com.efit.hrms.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
	private Long id;
//	private Long orgId;
	private String client; // caps
	private String clientCode; // caps
	private String clientType;
	private String fifofife;

}
