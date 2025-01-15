package com.rentcarapp.service;

import com.rentcarapp.model.User;
import com.rentcarapp.repository.UserRepository;
import com.rentcarapp.exception.CustomExceptions.UserAlreadyExistsException; // Örnek
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setPassword("1234");
        user.setEmail("testuser@gmail.com");
    }

    @Test
    void saveUser_ShouldSaveWhenUserNotExists() {

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.empty());

        when(userRepository.save(user))
                .thenReturn(user);


        User savedUser = userService.saveUser(user);

        System.out.println(savedUser);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveUser_ShouldThrowExceptionWhenUserExists() {
        // given
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(user));
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, never()).save(user);
    }

    @Test
    void getUserByUsername_ShouldReturnUserWhenFound() {
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        Optional<User> foundUserOpt = userService.getUserByUsername("TestUser");

        assertTrue(foundUserOpt.isPresent());
        assertEquals("TestUser", foundUserOpt.get().getUsername());
        verify(userRepository, times(1)).findByUsername("TestUser");
    }

    @Test
    void getUserByUsername_ShouldReturnEmptyWhenNotFound() {
        when(userRepository.findByUsername("NonExistingUser"))
                .thenReturn(Optional.empty());

        Optional<User> foundUserOpt = userService.getUserByUsername("NonExistingUser");

        assertFalse(foundUserOpt.isPresent());
        verify(userRepository, times(1)).findByUsername("NonExistingUser");
    }

    @Test
    void findById_ShouldReturnUserWhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnNullWhenNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty()); // Correct usage

        Optional<User> foundUser = userService.findById(999L); // Assuming this method unwraps the Optional and returns null if not found


        assertTrue(foundUser.isEmpty());
        verify(userRepository, times(1)).findById(999L);
    }

}
