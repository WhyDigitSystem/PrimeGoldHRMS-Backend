package com.efit.hrms.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginClientAccessDTO {

	private String client;
    private String customer;
    
}
