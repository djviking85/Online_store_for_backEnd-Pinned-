package com.example.store;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class OnlineStoreProjectApplication {

	public static void main(String[] args) {
		System.out.println("Start project");
		SpringApplication.run(OnlineStoreProjectApplication.class, args);
	}

}
