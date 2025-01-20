package com.rentcarapp.service;

import com.rentcarapp.model.User;
import com.rentcarapp.projection.UserProjection;
import com.rentcarapp.repository.UserRepository;
import com.rentcarapp.exception.CustomExceptions.UserAlreadyExistsException; // Örnek
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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
        user.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
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

        User userSaved = userService.saveUser(user);
        System.out.println(userSaved);

        assertNotNull(userSaved);
        assertEquals(user.getUsername(), userSaved.getUsername());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveUser_ShouldThrowExceptionWhenUserExists() {
        UserProjection userProjection = new UserProjection() {
            @Override
            public String getId() {
                return user.getId().toString();
            }
            @Override
            public String getUsername() {
                return user.getUsername();
            }
            @Override
            public String getEmail() {
                return user.getEmail();
            }
        };

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(userProjection));

        assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(user));
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, never()).save(user);
    }

    @Test
    void getUserByUsername_ShouldReturnUserWhenFound() {

        UserProjection userProjection = new UserProjection() {
            @Override
            public String getId() {
                return user.getId().toString();
            }
            @Override
            public String getUsername() {
                return user.getUsername();
            }
            @Override
            public String getEmail() {
                return user.getEmail();
            }
        };

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(userProjection));

        Optional<UserProjection> foundUserOpt = userService.getUserByUsername("TestUser");
        assertTrue(foundUserOpt.isPresent());
        assertEquals("TestUser", foundUserOpt.get().getUsername());
        verify(userRepository, times(1)).findByUsername("TestUser");
    }

    @Test
    void getUserByUsername_ShouldReturnEmptyWhenNotFound() {
        when(userRepository.findByUsername("NonExistingUser"))
                .thenReturn(Optional.empty());

        Optional<UserProjection> foundUserOpt = userService.getUserByUsername("NonExistingUser");

        assertFalse(foundUserOpt.isPresent());
        verify(userRepository, times(1)).findByUsername("NonExistingUser");
    }

    @Test
    void findById_ShouldReturnUserWhenFound() {
        UserProjection userProjection = new UserProjection() {
            @Override
            public String getId() {
                return user.getId().toString();
            }
            @Override
            public String getUsername() {
                return user.getUsername();
            }
            @Override
            public String getEmail() {
                return user.getEmail();
            }
        };

        UUID testUuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(userRepository.findById(testUuid)).thenReturn(Optional.of(userProjection));

        Optional<UserProjection> foundUser = userService.findById(testUuid);

        assertNotNull(foundUser);
        foundUser.ifPresent(projection -> assertEquals(testUuid.toString(), projection.getId()));
        verify(userRepository, times(1)).findById(testUuid);
    }

    @Test
    void findById_ShouldReturnNullWhenNotFound() {
        UUID testUuid = UUID.fromString("00000000-0000-0000-0000-100000000001");
        when(userRepository.findById(testUuid)).thenReturn(Optional.empty()); // Correct usage

        Optional<UserProjection> foundUser = userService.findById(testUuid); // Assuming this method unwraps the Optional and returns null if not found

        assertTrue(foundUser.isEmpty());
        verify(userRepository, times(1)).findById(testUuid);
    }
}