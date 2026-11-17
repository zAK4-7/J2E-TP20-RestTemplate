package com.example.client;

import com.example.client.entities.Client;
import com.example.client.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * REST and persistence tests of SERVICE-CLIENT.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void clean() {
        clientRepository.deleteAll();
    }

    @Test
    void postThenGetShouldReturnTheClient() throws Exception {
        mockMvc.perform(post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Rabab SELIMANI\",\"age\":23}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Rabab SELIMANI"));

        mockMvc.perform(get("/api/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void findByIdShouldReturn404WhenUnknown() throws Exception {
        mockMvc.perform(get("/api/client/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void repositoryShouldPersistTheAge() {
        Client saved = clientRepository.save(new Client(null, "Amal RAMI", 22f));

        assertEquals(22f, clientRepository.findById(saved.getId()).orElseThrow().getAge());
    }
}
