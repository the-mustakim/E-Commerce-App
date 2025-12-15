package com.app.ecom.service;

import com.app.ecom.dto.AddressDto;
import com.app.ecom.dto.UserDto;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.exception.NotFoundException;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;


    public UserServiceImpl(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Transactional
    @Override
    public UserResponse addUser(UserDto userDto) {
        return mapToUserResponse(userRepo.saveAndFlush(mapToUser(userDto)));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> userList = userRepo.findAll();
        return userList.stream().map(UserServiceImpl::mapToUserResponse).toList();
    }

    @Override
    public UserResponse getUser(Long userId) {
        Optional<User> user = userRepo.findById(userId);
        if(user.isPresent()){return mapToUserResponse(user.get());}
        throw new NotFoundException("No user present with the given ID: " + userId);
    }

    @Transactional
    @Override
    public UserResponse updateUser(Long userId, UserDto userDto) {
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()){throw new NotFoundException("No user present with the given ID: " + userId);}
        user.get().setFirstName(userDto.getFirstName());
        user.get().setLastName(userDto.getLastName());
        userRepo.saveAndFlush(user.get());
        return new UserResponse(String.valueOf(user.get().getId()), user.get().getFirstName(),user.get().getLastName(),user.get().getEmail(),user.get().getPhone(),user.get().getUserRole());
    }

    public static User mapToUser(UserDto userDto){
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setUserRole(userDto.getUserRole());
        user.setAddress(mapToAddress(userDto.getAddress()));
        return user;
    }

    public static UserResponse mapToUserResponse(User user){
        return new UserResponse(String.valueOf(user.getId()), user.getFirstName(), user.getLastName(),user.getEmail(),user.getPhone(),user.getUserRole(),mapToAddressDto(user.getAddress()));
    }

    public static AddressDto mapToAddressDto(Address address){
        return new AddressDto(address.getState(),address.getCity(),address.getState(),address.getCountry(),address.getZipcode());
    }

    public static Address mapToAddress(AddressDto addressDto){
        return new Address(addressDto.getState(),addressDto.getCity(),addressDto.getState(),addressDto.getCountry(),addressDto.getZipcode());
    }


}
