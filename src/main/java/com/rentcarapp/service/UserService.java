package com.rentcarapp.service;


import com.rentcarapp.exception.CustomExceptions.*;
import com.rentcarapp.model.User;
import com.rentcarapp.projection.UserProjection;
import com.rentcarapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        Optional<UserProjection> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with username " + user.getUsername() + " already exists."
            );
        }
        User savedUser = userRepository.save(user);

        System.out.println("User Created: Username = " + savedUser.getUsername() + ", Email = " + savedUser.getEmail());
        return savedUser;
    }

    @Override
    public Optional<UserProjection> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserProjection> findById(UUID id) {
        return userRepository.findById(id);
    }

}
