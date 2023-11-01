package com.deletelogs.utility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UtiltiyApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtiltiyApplication.class, args);
	}

}
