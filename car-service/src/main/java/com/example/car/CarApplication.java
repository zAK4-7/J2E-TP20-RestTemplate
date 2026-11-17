package com.example.car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * SERVICE-CAR microservice (port 8082).
 *
 * <p>RestTemplate is the synchronous HTTP client used to call SERVICE-CLIENT.
 * @LoadBalanced lets us write http://SERVICE-CLIENT/... instead of a hardcoded
 * host:port: the logical name is resolved through Eureka.</p>
 */
@EnableDiscoveryClient
@SpringBootApplication
public class CarApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarApplication.class, args);
    }

    /**
     * Load balanced RestTemplate with explicit timeouts so a dead dependency
     * cannot block the request thread forever.
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
