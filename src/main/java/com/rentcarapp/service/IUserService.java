package com.rentcarapp.service;

import com.rentcarapp.model.User;
import com.rentcarapp.projection.UserProjection;

import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    User saveUser(User user);

    Optional<UserProjection> deleteUser(String idValue);

    Optional<UserProjection> getUserByUsername(String username);

    Optional<UserProjection> findById(UUID id);

}
