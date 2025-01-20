package com.rentcarapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentcarapp.model.User;
import com.rentcarapp.projection.UserProjection;
import com.rentcarapp.projection.UserProjectionWithPassword;
import com.rentcarapp.service.UserService;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_ShouldReturnCreatedWhenSuccess() throws Exception {
        
        User user = new User();
        user.setUsername("BERK");
        user.setPassword("1234");
        user.setEmail("ulgutberk@gmail.com");

        
        given(userService.saveUser(any(User.class))).willReturn(user);

        
        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)) // JSON gövdesi
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("BERK"))
                .andExpect(jsonPath("$.email").value("ulgutberk@gmail.com"));
    }

    @Test
    void getUser_ShouldReturnOkWhenUserFound() throws Exception {
        UserProjectionWithPassword userProjectionWithPassword = new UserProjectionWithPassword() {
            @Override
            public String getUsername() {
                return "BERK";
            }
            @Override
            public String getEmail() {
                return "ulgutberk@gmail.com";
            }
            @Override
            public String getId() {
                return "00000000-0000-0000-0000-000000000001";
            }
            @Override
            public String getPassword() {
                return "1234";
            }
        };

        when(userService.getUserByUsername("BERK")).thenReturn(Optional.of(userProjectionWithPassword));

        
        mockMvc.perform(get("http://localhost:8080/api/users/getByName?username=BERK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("BERK"))
                .andExpect(jsonPath("$.password").value("1234"))
                .andExpect(jsonPath("$.email").value("ulgutberk@gmail.com"));
    }

    @Test
    void getUser_ShouldReturnOkWithEmptyBodyWhenNotFound() throws Exception {
        when(userService.getUserByUsername("NotExist")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/NotExist"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

    }

    @Test
    void getUserById_ShouldReturnUserWhenFound() throws Exception {
        UserProjection userProjection = new UserProjection() {
            @Override
            public String getUsername() {
                return "john";
            }
            @Override
            public String getEmail() {
                return "ulgutberk@gmail.com";
            }
            @Override
            public String getId() {
                return "00000000-0000-0000-0000-000000000001";
            }
        };

        User user = new User();
        UUID testUuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        user.setId(testUuid);
        user.setUsername("john");

        when(userService.findById(testUuid)).thenReturn(Optional.of(userProjection));

        mockMvc.perform(
                        post("/api/users/getById")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\":\"" + testUuid + "\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUuid.toString()))
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void getUserById_ShouldReturnNotFoundWhenNull() throws Exception {
        UUID testUuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(userService.findById(testUuid)).thenReturn(Optional.empty());

        mockMvc.perform(
                        post("/api/users/getById")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\":\"" + testUuid + "\"}")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void getDefaultMessage_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Define an user !"));
    }
}
