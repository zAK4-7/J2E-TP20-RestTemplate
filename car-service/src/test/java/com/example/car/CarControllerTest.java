package com.example.car;

import com.example.car.entities.Car;
import com.example.car.models.CarResponse;
import com.example.car.repositories.CarRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies the car persistence and the tolerant behaviour of CarService when
 * SERVICE-CLIENT is unreachable (the car is returned without its owner).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private com.example.car.services.CarService carService;

    @BeforeEach
    void clean() {
        carRepository.deleteAll();
    }

    @Test
    void findAllShouldReturnCarsEvenWithoutTheClientService() throws Exception {
        carRepository.save(new Car(null, "Toyota", "Corolla", "A 25 333", 1L, null));

        // SERVICE-CLIENT is not running in the test: the owner stays null
        mockMvc.perform(get("/api/car"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[0].client").doesNotExist());
    }

    @Test
    void unknownCarShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/car/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void serviceShouldDegradeGracefullyWhenTheClientIsUnavailable() throws Exception {
        Car saved = carRepository.save(new Car(null, "Renault", "Megane", "B 6 3456", 42L, null));

        CarResponse response = carService.findById(saved.getId());

        assertEquals("Renault", response.getBrand());
        assertNull(response.getClient(), "The owner is null when SERVICE-CLIENT is down");
    }

    @Test
    void findByClientIdShouldFilterLocally() {
        carRepository.save(new Car(null, "Toyota", "Corolla", "A 25 333", 1L, null));
        carRepository.save(new Car(null, "Peugeot", "301", "A 55 4444", 2L, null));

        List<CarResponse> cars = carService.findByClientId(1L);

        assertEquals(1, cars.size());
        assertEquals("Corolla", cars.get(0).getModel());
    }
}
