package com.app.ecom.integrationtest;

import com.app.ecom.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0");

    @Test
    void testaddToCart() throws Exception{
        AddressDto addressDto = new AddressDto(
                "Abbey Street",
                "Dublin",
                "Leinster",
                "Ireland",
                "D15KFT7"
        );
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setEmail("john@gmail.com");
        userDto.setAddress(addressDto);

        ProductRequest productRequest = ProductRequest
                .builder()
                .name("Samsung")
                .description("Galaxy 15 Pro Max")
                .price(BigDecimal.valueOf(20000))
                .stockQuantity(20)
                .category("Phones")
                .imageUrl("sample-image-url")
                .build();

        String userAsString = mockMvc.perform(
                        post("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@gmail.com")).andReturn().getResponse().getContentAsString();


        String productAsString = mockMvc.perform(
                        post("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Samsung"))
                .andExpect(jsonPath("$.description").value("Galaxy 15 Pro Max")).andReturn().getResponse().getContentAsString();

        UserResponse userResponse = objectMapper.readValue(userAsString,UserResponse.class);
        ProductResponse productResponse = objectMapper.readValue(productAsString,ProductResponse.class);
        CartItemRequest cartItemRequest = CartItemRequest
                    .builder()
                    .productId(productResponse.getId())
                    .quantity(20).build();

            mockMvc.perform(post("/api/cart").header("X-USER-ID", userResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cartItemRequest)))
                    .andExpect(status().isCreated());
    }


}
