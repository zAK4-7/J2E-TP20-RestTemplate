package ma.mlia.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * API Gateway (port 8888).
 *
 * <p>The lab uses the dynamic discovery locator so that every Eureka service is
 * reachable as http://localhost:8888/SERVICE-NAME/** . Explicit routes are also
 * declared to keep friendly short paths (/api/client/**, /api/car/**).</p>
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Friendly aliases on top of the dynamic /SERVICE-NAME/** locator
                .route("client-shortcut", r -> r.path("/api/client/**").uri("lb://SERVICE-CLIENT"))
                .route("car-shortcut", r -> r.path("/api/car/**").uri("lb://SERVICE-CAR"))
                .build();
    }
}
