package com.dan.api_example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApiExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiExampleApplication.class, args);
	}

}
