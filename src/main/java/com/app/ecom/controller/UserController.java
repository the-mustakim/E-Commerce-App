package com.app.ecom.controller;

import com.app.ecom.dto.UserDto;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){

        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> userDtoList = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userDtoList);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserDto userDto){
        UserResponse newUser = userService.addUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId){
        UserResponse user = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto){
        UserResponse user = userService.updateUser(userId,userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
