package com.app.ecom.dto;

import com.app.ecom.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole userRole = UserRole.CUSTOMER;
    private AddressDto address;

    public UserResponse(String id, String firstName, String lastName, String email, String phone, UserRole userRole) {
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.userRole = userRole;
    }
}
