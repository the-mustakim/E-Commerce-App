package com.app.ecom.service;

import com.app.ecom.dto.UserDto;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.User;

import java.util.List;

public interface UserService {

    public UserResponse addUser(UserDto userDto);

    List<UserResponse> getAllUsers();

    UserResponse getUser(Long userId);

    UserResponse updateUser(Long userId, UserDto userDto);
}
