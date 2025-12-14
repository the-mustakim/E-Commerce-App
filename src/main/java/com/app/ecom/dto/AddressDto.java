package com.app.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDto {
    private String street;

    private String city;

    private String state;

    private String country;

    private String zipcode;
}
