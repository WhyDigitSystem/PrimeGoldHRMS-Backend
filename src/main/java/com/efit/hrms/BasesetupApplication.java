package com.efit.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class BasesetupApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasesetupApplication.class, args);
	}

}
