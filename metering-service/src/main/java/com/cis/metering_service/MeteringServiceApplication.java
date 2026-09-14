package com.cis.metering_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MeteringServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeteringServiceApplication.class, args);
	}

}
