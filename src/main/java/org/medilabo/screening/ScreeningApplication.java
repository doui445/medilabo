package org.medilabo.screening;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ScreeningApplication {

	static void main(String[] args) {
		SpringApplication.run(ScreeningApplication.class, args);
	}

}
