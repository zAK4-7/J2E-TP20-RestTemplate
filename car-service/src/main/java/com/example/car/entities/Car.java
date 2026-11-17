package com.example.car.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Car entity (table: cars).
 *
 * <p>Only the client id is stored: in a microservices architecture we avoid JPA
 * relations that cross service boundaries. The full Client is transient and
 * fetched over HTTP when needed.</p>
 *
 * <p>The Java field is named clientId so the derived query findByClientId maps
 * to the single column client_id. Naming the field client_id would make Spring
 * Data look for an attribute "client", which does not exist.</p>
 */
@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String model;

    private String matricule;

    @jakarta.persistence.Column(name = "client_id")
    private Long clientId;

    @Transient
    private Client client;
}
