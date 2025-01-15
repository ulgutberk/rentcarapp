package com.rentcarapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RentcarappApplication {

	public static void main(String[] args) {

		SpringApplication.run(RentcarappApplication.class, args);
	}

	@RestController
	public static class DefaultController {
		@GetMapping("/")
		public String home() {
			return "Welcome to RentCar App!";
		}
	}
}
