package com.example.splitt;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SplittApplication {

	public static void main(String[] args) {
		SpringApplication.run(SplittApplication.class, args);
	}

}
