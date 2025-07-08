package com.efit.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyDeclarationDTO {

	private String declaration;
	private Long count;
	private Long declared;
	
	private boolean proof;
	private Long rejected;
	private Long accepted;
	private byte[] proofImage;

	
}
