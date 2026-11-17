package com.example.car.config;

import com.example.car.entities.Car;
import com.example.car.repositories.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Seeds three cars pointing to the client ids 1 and 2 of SERVICE-CLIENT.
 */
@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner initCars(CarRepository carRepository) {
        return args -> {
            if (carRepository.count() > 0) {
                return;
            }
            carRepository.save(new Car(null, "Toyota", "Corolla", "A 25 333", 1L, null));
            carRepository.save(new Car(null, "Renault", "Megane", "B 6 3456", 1L, null));
            carRepository.save(new Car(null, "Peugeot", "301", "A 55 4444", 2L, null));
            System.out.println("Seeded " + carRepository.count() + " cars.");
        };
    }
}
