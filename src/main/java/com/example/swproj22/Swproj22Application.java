package com.example.swproj22;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Swproj22Application {

	public static void main(String[] args) {
		SpringApplication.run(Swproj22Application.class, args);
	}

}
