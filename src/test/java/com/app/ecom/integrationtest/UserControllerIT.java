package com.app.ecom.integrationtest;

import com.app.ecom.dto.AddressDto;
import com.app.ecom.dto.UserDto;
import com.app.ecom.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0");

//    @DynamicPropertySource
//    static void configureProps(DynamicPropertyRegistry dynamicPropertyRegistry){
//        dynamicPropertyRegistry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
//        dynamicPropertyRegistry.add("spring.datasource.username",mySQLContainer::getUsername);
//        dynamicPropertyRegistry.add("spring.datasource.password",mySQLContainer::getPassword);
//    }

    @Test
    void testGetAllUsers() throws Exception{
        try {
            mockMvc.perform(get("/api/user"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void testCreateUser() throws Exception{
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

        try {
            mockMvc.perform(
                            post("/api/user")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsBytes(userDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.email").value("john@gmail.com"));
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void testGetUser(){

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

        long userId = 1L;
        try {

            //Create the User First
            mockMvc.perform(
                            post("/api/user")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsBytes(userDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.email").value("john@gmail.com"));

            //Try getting the same user
            mockMvc.perform(get("/api/user/"+userId)).andExpect(status().isOk()).andExpect(jsonPath("$.email").value("john@gmail.com"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testupdateUser(){
        AddressDto addressDto = new AddressDto(
                "Abbey Street",
                "Dublin",
                "Leinster",
                "Ireland",
                "D15KFT7"
        );
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Conner");
        userDto.setEmail("john@gmail.com");
        userDto.setAddress(addressDto);


        UserDto updateUserDto = new UserDto();
        updateUserDto.setFirstName("Salman");
        updateUserDto.setLastName("Khan");
        updateUserDto.setEmail("salman@gmail.com");

        try {

            //Create the user
            String contentAsString = mockMvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userDto))).andExpect(status().isCreated()).andExpect(jsonPath("$.email").value("john@gmail.com")).andReturn().getResponse().getContentAsString();

            UserResponse userResponse = objectMapper.readValue(contentAsString,UserResponse.class);

            //Update the firstName and LastName
            mockMvc.perform(put("/api/user/"+userResponse.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(updateUserDto))).andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("Salman")).andExpect(jsonPath("$.lastName").value("Khan"));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }



}
