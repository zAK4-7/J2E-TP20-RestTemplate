package com.example.car.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Client DTO used to deserialize the JSON returned by SERVICE-CLIENT.
 * It is never persisted in the car database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private Long id;

    private String nom;

    private Float age;
}
