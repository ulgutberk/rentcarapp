package com.rentcarapp.service;

import com.rentcarapp.exception.CustomExceptions.*;
import com.rentcarapp.model.User;
import com.rentcarapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {

            throw new UserAlreadyExistsException(
                    "User with username " + user.getUsername() + " already exists."
            );
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(long id){
        return userRepository.findById(id);
    }

}
