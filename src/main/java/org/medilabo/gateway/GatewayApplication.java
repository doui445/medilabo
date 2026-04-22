package org.medilabo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("patient", predicateSpec -> predicateSpec
						.path("/api/patients/**")
						.uri("http://localhost:8081"))
				.route("notes", predicateSpec -> predicateSpec
						.path("/api/notes/**")
						.uri("http://localhost:8082"))
				.route("screening", predicateSpec -> predicateSpec
						.path("/api/screening/**")
						.uri("http://localhost:8083"))
				.build();
	}
}
