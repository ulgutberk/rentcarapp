package com.rentcarapp.controller;

import com.rentcarapp.exception.CustomExceptions;
import com.rentcarapp.model.dto.UserDTO;
import com.rentcarapp.model.entity.User;
import com.rentcarapp.model.response.ApiResponse;
import com.rentcarapp.projection.UserProjection;
import com.rentcarapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/hello")
    public ResponseEntity<ApiResponse<String>> home() {
        ApiResponse<String> response = ApiResponse.of(
                true,
                "Request successful",
                "Welcome to RentCar App!",
                200
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<String>> getDefaultMessage() {
        ApiResponse<String> response = ApiResponse.of(
                false,
                "Define a user!",
                "",
                400
        );
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<ApiResponse<Optional<UserDTO>>> registerUser(@RequestBody User user) {
        Optional<UserDTO> savedUser = userService.saveUser(user);
        ApiResponse<Optional<UserDTO>> response = ApiResponse.of(
                true,
                "User created successfully",
                savedUser,
                201
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteUser")
    public  ResponseEntity<ApiResponse<UserProjection>> deleteUser(@RequestParam Map<String, String> body) {
        String idValue = body.get("id");

        if(idValue == null || idValue.isEmpty()){
            throw new CustomExceptions.MissingParameterException("ID parameter is missing");
        }

        Optional<UserProjection> deletedUser = userService.deleteUser(idValue);

        ApiResponse<UserProjection> response = ApiResponse.of(
                true,
                "User deleted successfully",
                deletedUser.get(),
                200
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUserByName")
    public ResponseEntity<ApiResponse<UserProjection>> getUser(@RequestParam String username) {
        Optional<UserProjection> user = userService.getUserByUsername(username);

        if (user.isPresent()) {
            ApiResponse<UserProjection> response = ApiResponse.of(
                    true,
                    "User found",
                    user.get(),
                    200
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserProjection> response = ApiResponse.of(
                    false,
                    "User not found",
                    null,
                    404
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/getUserById")
    public ResponseEntity<ApiResponse<UserProjection>> getUserById(@RequestBody Map<String, String> body) {

        String idValue = body.get("id");

        if(idValue == null || idValue.isEmpty()){
            throw new CustomExceptions.MissingParameterException("ID parameter is missing");
        }

        UUID userId;
        userId = UUID.fromString(idValue);

        Optional<UserProjection> user  = userService.findById(userId);

        if(user.isEmpty()) {
            throw new CustomExceptions.UserNotFoundException("User not found");
        }

        ApiResponse<UserProjection> response = ApiResponse.of(
                true,
                "User found",
                user.get(),
                200
        );

        return ResponseEntity.ok(response);
    }

}
