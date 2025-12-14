package com.app.ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String city;

    private String state;

    private String country;

    private String zipcode;


    public Address(String street,String city, String state, String country, String zipcode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }
}
