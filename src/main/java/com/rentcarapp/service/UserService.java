package com.rentcarapp.service;


import com.rentcarapp.exception.CustomExceptions;
import com.rentcarapp.exception.CustomExceptions.*;
import com.rentcarapp.model.dto.UserDTO;
import com.rentcarapp.model.entity.User;
import com.rentcarapp.projection.UserProjection;
import com.rentcarapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {



    @Autowired
    private UserRepository userRepository;


    @Override
    public  Optional<UserDTO> saveUser(User user) {
        Optional<UserProjection> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with username " + user.getUsername() + " already exists."
            );
        }
        User savedUser = userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(savedUser.getUsername());
        userDTO.setEmail(savedUser.getEmail());

        return Optional.of(userDTO);
    }

    @Transactional
    @Override
    public Optional<UserProjection> deleteUser(String idValue) {
        UUID userId;

        userId = UUID.fromString(idValue);

        Optional<UserProjection> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new CustomExceptions.UserNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
        return user;
    }

    @Override
    public Optional<UserProjection> getUserByUsername(String username) {
        System.out.println("CHECK " + username);
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserProjection> findById(UUID id) {
        return userRepository.findById(id);
    }

}
