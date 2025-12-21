package com.app.ecom.integrationtest;


import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final  ObjectMapper objectMapper = new ObjectMapper();

    @Container
    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0");


    @Test
    void testcreateProduct() throws Exception{

        ProductRequest productRequest = ProductRequest
                .builder()
                .name("Samsung")
                .description("Galaxy 15 Pro Max")
                .price(BigDecimal.valueOf(20000))
                .stockQuantity(20)
                .category("Phones")
                .imageUrl("sample-image-url")
                .build();

           mockMvc.perform(
                           post("/api/product")
                                   .contentType(MediaType.APPLICATION_JSON)
                                   .content(objectMapper.writeValueAsBytes(productRequest)))
                   .andExpect(status().isCreated())
                   .andExpect(jsonPath("$.name").value("Samsung"))
                   .andExpect(jsonPath("$.description").value("Galaxy 15 Pro Max"));

    }

    @Test
    void testupdateProduct() throws Exception{

        ProductRequest productRequest = ProductRequest
                .builder()
                .name("Samsung")
                .description("Galaxy 15 Pro Max")
                .price(BigDecimal.valueOf(20000))
                .stockQuantity(20)
                .category("Phones")
                .imageUrl("sample-image-url")
                .build();

        ProductRequest updateProduct = ProductRequest
                .builder()
                .name("Iphone")
                .description("Iphone 17 Pro Max")
                .price(BigDecimal.valueOf(60000))
                .stockQuantity(10)
                .category("Phones")
                .imageUrl("sample-image-url-1")
                .build();

            String updateResponseAsString = mockMvc.perform(
                            post("/api/product")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsBytes(productRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Samsung"))
                    .andExpect(jsonPath("$.description").value("Galaxy 15 Pro Max")).andReturn().getResponse().getContentAsString();

            ProductResponse productResponse = objectMapper.readValue(updateResponseAsString,ProductResponse.class);

            mockMvc.perform(put("/api/product/"+productResponse.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(updateProduct))).andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Iphone"))
                    .andExpect(jsonPath("$.description").value("Iphone 17 Pro Max"));

    }

    @Test
    void testgetProducts() throws Exception {
        ProductRequest productRequest = ProductRequest
                .builder()
                .name("Samsung")
                .description("Galaxy 15 Pro Max")
                .price(BigDecimal.valueOf(20000))
                .stockQuantity(20)
                .category("Phones")
                .imageUrl("sample-image-url")
                .build();

            mockMvc.perform(
                            post("/api/product")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsBytes(productRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Samsung"))
                    .andExpect(jsonPath("$.description").value("Galaxy 15 Pro Max"));

            mockMvc.perform(get("/api/product")).andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.[0].name").value("Samsung"));

    }

    @Test
    void testgetProduct() throws Exception{
        ProductRequest productRequest = ProductRequest
                .builder()
                .name("Samsung")
                .description("Galaxy 15 Pro Max")
                .price(BigDecimal.valueOf(20000))
                .stockQuantity(20)
                .category("Phones")
                .imageUrl("sample-image-url")
                .build();

        String contentAsString = mockMvc.perform(
                        post("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Samsung"))
                .andExpect(jsonPath("$.description").value("Galaxy 15 Pro Max")).andReturn().getResponse().getContentAsString();

        ProductResponse productResponse = objectMapper.readValue(contentAsString,ProductResponse.class);

        mockMvc.perform(get("/api/product/"+productResponse.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Samsung"))
                .andExpect(jsonPath("$.description").value("Galaxy 15 Pro Max"));

    }

    @Test
    void testdeleteProduct() throws Exception{
        ProductRequest productRequest = ProductRequest
                .builder()
                .name("Samsung")
                .description("Galaxy 15 Pro Max")
                .price(BigDecimal.valueOf(20000))
                .stockQuantity(20)
                .category("Phones")
                .imageUrl("sample-image-url")
                .build();

        String contentAsString = mockMvc.perform(
                        post("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Samsung"))
                .andExpect(jsonPath("$.description").value("Galaxy 15 Pro Max")).andReturn().getResponse().getContentAsString();

        ProductResponse productResponse = objectMapper.readValue(contentAsString,ProductResponse.class);

        mockMvc.perform(delete("/api/product/"+productResponse.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("product is deleted"));

    }

    @Test
    void testsearchProduct() throws Exception{
        ProductRequest productRequest = ProductRequest
                .builder()
                .name("Samsung")
                .description("Galaxy 15 Pro Max")
                .price(BigDecimal.valueOf(20000))
                .stockQuantity(20)
                .category("Phones")
                .imageUrl("sample-image-url")
                .build();

       mockMvc.perform(
                        post("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Samsung"))
                .andExpect(jsonPath("$.description").value("Galaxy 15 Pro Max"));

       mockMvc.perform(get("/api/product/search-product").param("keyword","Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}
