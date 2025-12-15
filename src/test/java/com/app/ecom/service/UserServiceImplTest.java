package com.app.ecom.service;
import com.app.ecom.dto.AddressDto;
import com.app.ecom.dto.UserDto;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.enums.UserRole;
import com.app.ecom.exception.NotFoundException;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) //Use Mockito’s engine to handle @Mock, @InjectMocks, stubbing, and mock lifecycle.
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;

    private User user;
    @BeforeEach
    void setUp(){
        Address address = new Address(
                1L,
                "Abbey Street",
                "Dublin",
                "Leinster",
                "Ireland",
                "D15KFT7"
        );

        AddressDto addressDto = new AddressDto(
                "Abbey Street",
                "Dublin",
                "Leinster",
                "Ireland",
                "D15KFT7"
        );

        userDto = new UserDto(
                "Mustakim",
                "Sayyed",
                "mustakimsayyed55@gmail.com",
                "7057793525",
                UserRole.CUSTOMER,
                addressDto
        );

        user = new User(
                1L,
                "Mustakim",
                "Sayyed",
                "mustakimsayyed55@gmail.com",
                "7057793525",
                UserRole.CUSTOMER,
                address,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void addUser_shouldReturnUserResponse(){
        when(userRepo.saveAndFlush(any(User.class))).thenReturn(user);
        UserResponse userResponse = userService.addUser(userDto);
        // Assert
        assertNotNull(userResponse);
        assertEquals("Mustakim", userResponse.getFirstName());
        assertEquals("Sayyed", userResponse.getLastName());
        assertEquals("7057793525", userResponse.getPhone());
        verify(userRepo).saveAndFlush(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnListOfUserResponse(){
        when(userRepo.findAll()).thenReturn(List.of(user));
        List<UserResponse> userResponses = userService.getAllUsers();
        assertNotNull(userResponses);
        assertEquals(1,userResponses.size());
        verify(userRepo).findAll();
    }

    @Test
    void getUser_shouldThrowNotFoundException() {
        when(userRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUser(5L));
    }

    @Test
    void getUser_shouldReturnUserResponse(){
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        UserResponse userResponse = userService.getUser(1L);
        // Assert
        assertNotNull(userResponse);
        assertEquals("Mustakim", userResponse.getFirstName());
        assertEquals("Sayyed", userResponse.getLastName());
        assertEquals("7057793525", userResponse.getPhone());
        verify(userRepo).findById(1L);
    }

    @Test
    void updateUser_shouldReturnUserResponse(){
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        UserResponse userResponse = userService.updateUser(1L,userDto);
        // Assert
        assertNotNull(userResponse);
        assertEquals("Mustakim", userResponse.getFirstName());
        assertEquals("Sayyed", userResponse.getLastName());
        assertEquals("7057793525", userResponse.getPhone());
        verify(userRepo).findById(1L);
        verify(userRepo).saveAndFlush(any(User.class));
    }

    @Test
    void updateUser_shouldReturnNotFoundException(){
        when(userRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,()-> userService.updateUser(2L,userDto));
        verify(userRepo).findById(2L);
    }


}
