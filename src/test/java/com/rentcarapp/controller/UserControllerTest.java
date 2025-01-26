package com.rentcarapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentcarapp.model.dto.UserDTO;
import com.rentcarapp.model.entity.User;
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

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("BERK");
        userDTO.setEmail("ulgutberk@gmail.com");


        when(userService.saveUser(any(User.class))).thenReturn(Optional.of(userDTO));

        mockMvc.perform(
                        post("/api/users/registerUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.username").value("BERK"))
                .andExpect(jsonPath("$.data.email").value("ulgutberk@gmail.com"))
                .andExpect(jsonPath("$.statusCode").value(201));
    }

    @Test
    void getUser_ShouldReturnOkWhenUserFound() throws Exception {
        UserProjectionWithPassword userProjectionWithPassword = new UserProjectionWithPassword() {
            @Override
            public String getUsername() {
                return "BERK";
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


        mockMvc.perform(get("/api/users/getUserByName?username=BERK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("BERK"))
                .andExpect(jsonPath("$.data.password").value("1234"));
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
                        post("http://localhost:8080/api/users/getUserById")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\":\"" + testUuid + "\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(testUuid.toString()))
                .andExpect(jsonPath("$.data.username").value("john"));
    }

    @Test
    void getUserById_ShouldReturnNotFoundWhenNull() throws Exception {
        UUID testUuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(userService.findById(testUuid)).thenReturn(Optional.empty());

        mockMvc.perform(
                        post("/api/users/getUserById")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\":\"" + testUuid + "\"}")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    void deleteUser_ShouldReturnBadRequest_WhenIdIsMissing() throws Exception {
        mockMvc.perform(
                        post("/api/users/deleteUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("ID parameter is missing"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(400));
    }


    @Test
    void deleteUser_ShouldReturnOk_WhenUserIsDeletedSuccessfully() throws Exception {
        String testId = "00000000-0000-0000-0000-000000000001";

        UserProjection mockUser = new UserProjection() {
            @Override
            public String getId() {
                return testId;
            }

            @Override
            public String getUsername() {
                return "berkulgut";
            }

            @Override
            public String toString() {
                return "UserProjection{id=" + testId + ", name=Berk Ulgut}";
            }
        };

        when(userService.deleteUser(testId)).thenReturn(Optional.of(mockUser));


        mockMvc.perform(
                        post("/api/users/deleteUser")
                                .param("id", testId) // Send 'id' as form parameter
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.data.id").value(testId)) // Assert the ID in the data
                .andExpect(jsonPath("$.data.username").value("berkulgut")) // Assert the username
                .andExpect(jsonPath("$.statusCode").value(200));
    }
}
