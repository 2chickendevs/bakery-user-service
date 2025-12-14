package com.twochickendevs.bakeryuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BakeryApplication {

	static void main(String[] args) {
		SpringApplication.run(BakeryApplication.class, args);
	}

}
