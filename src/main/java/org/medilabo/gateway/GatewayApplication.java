package org.medilabo.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	@Value("${patient.api.url}")
	private String patientUrl;

	@Value("${notes.api.url}")
	private String notesUrl;

	@Value("${screening.api.url}")
	private String screeningUrl;


	static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("patient", predicateSpec -> predicateSpec
						.path("/api/patients/**")
						.uri(patientUrl))
				.route("notes", predicateSpec -> predicateSpec
						.path("/api/notes/**")
						.uri(notesUrl))
				.route("screening", predicateSpec -> predicateSpec
						.path("/api/screening/**")
						.uri(screeningUrl))
				.build();
	}
}
