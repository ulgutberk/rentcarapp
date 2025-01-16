package com.rentcarapp.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email address")
    private String email;
}
