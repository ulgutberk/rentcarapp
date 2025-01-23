package com.rentcarapp.controller;

import com.rentcarapp.dto.UserDTO;
import com.rentcarapp.exception.CustomExceptions;
import com.rentcarapp.mapper.UserMapper;
import com.rentcarapp.model.User;
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

    @GetMapping
    public ResponseEntity<String> getDefaultMessage() {
        return ResponseEntity.badRequest().body("Define an user !");
    }

    @PostMapping("/registerUser")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        UserDTO userDTO = UserMapper.INSTANCE.entityToDto(savedUser);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam Map<String, String> body) {
        String idValue = body.get("id");
        if (idValue == null) {
            return new ResponseEntity<>("ID parameter is missing", HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<UserProjection> deletedUser = userService.deleteUser(idValue);
            return new ResponseEntity<>("User deleted successfully: " + deletedUser, HttpStatus.OK);
        } catch (CustomExceptions.UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/getUserByName")
    public ResponseEntity<?> getUser(@RequestParam String username)  {
        Optional<UserProjection> user = userService.getUserByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/getUserById")
    public ResponseEntity<?> getUserById(@RequestBody Map<String, String> body) {
        String idValue = body.get("id");
        if (idValue == null || idValue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or empty 'id' parameter.");
        }
        UUID userId = UUID.fromString(idValue);
        Optional<UserProjection> user  = userService.findById(userId);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

}
