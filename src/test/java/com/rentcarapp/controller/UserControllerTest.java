package com.rentcarapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentcarapp.model.User;
import com.rentcarapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.BDDMockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;

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
        // given
        User user = new User();
        user.setUsername("BERK");
        user.setPassword("1234");
        user.setEmail("ulgutberk@gmail.com");

        // Service saveUser döndürmesi
        given(userService.saveUser(any(User.class))).willReturn(user);

        // when & then
        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)) // JSON gövdesi
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("BERK"))
                .andExpect(jsonPath("$.password").value("1234"))
                .andExpect(jsonPath("$.email").value("ulgutberk@gmail.com"));
    }

    @Test
    void getUser_ShouldReturnOkWhenUserFound() throws Exception {
        // given
        User user = new User();
        user.setUsername("BERK");
        user.setPassword("1234");
        user.setEmail("ulgutberk@gmail.com");

        when(userService.getUserByUsername("BERK")).thenReturn(Optional.of(user));

        // when & then
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
        User user = new User();
        user.setId(10L);
        user.setUsername("john");
        user.setPassword("pass123");

        when(userService.findById(10L)).thenReturn(Optional.of(user));

        mockMvc.perform(
                        post("/api/users/getById")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\":10}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.password").value("pass123"));
    }

    @Test
    void getUserById_ShouldReturnNotFoundWhenNull() throws Exception {
        when(userService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(
                        post("/api/users/getById")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\":999}")
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
