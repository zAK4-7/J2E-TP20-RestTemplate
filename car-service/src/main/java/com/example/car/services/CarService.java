package com.example.car.services;

import com.example.car.entities.Car;
import com.example.car.entities.Client;
import com.example.car.models.CarResponse;
import com.example.car.repositories.CarRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Business layer of SERVICE-CAR.
 *
 * <p>The owner is resolved through the gateway using the logical service name,
 * so the call benefits from the gateway routing and load balancing.</p>
 */
@Service
public class CarService {

    /**
     * Client service through the gateway. The logical name keeps the code free
     * of any hardcoded microservice host:port.
     */
    private static final String CLIENT_SERVICE_URL = "http://SERVICE-CLIENT/api/client/";

    private final CarRepository carRepository;
    private final RestTemplate restTemplate;

    public CarService(CarRepository carRepository, RestTemplate restTemplate) {
        this.carRepository = carRepository;
        this.restTemplate = restTemplate;
    }

    public List<CarResponse> findAll() {
        return carRepository.findAll().stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());
    }

    public CarResponse findById(Long id) throws Exception {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new Exception("Voiture non trouvée avec l'ID: " + id));
        return mapToCarResponse(car);
    }

    public List<CarResponse> findByClientId(Long clientId) {
        return carRepository.findByClientId(clientId).stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());
    }

    /**
     * Calls SERVICE-CLIENT over HTTP. A failure is tolerated: the car is still
     * returned, just without its owner, instead of failing the whole request.
     */
    private CarResponse mapToCarResponse(Car car) {
        Client client = null;
        try {
            client = restTemplate.getForObject(CLIENT_SERVICE_URL + car.getClientId(), Client.class);
        } catch (Exception e) {
            System.err.println("Could not fetch client " + car.getClientId() + ": " + e.getMessage());
        }

        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .matricule(car.getMatricule())
                .client(client)
                .build();
    }
}
