package config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("task-service", r -> r.path("/api/tasks/**")
                        .filters(f -> f.filter(new RequestLoggingFilter()))
                        .uri("http://task-service:8081"))
                .route("log-service", r -> r.path("/api/logs/**")
                        .filters(f -> f.filter(new RequestLoggingFilter()))
                        .uri("http://log-service:8082"))
                .build();
    }
}