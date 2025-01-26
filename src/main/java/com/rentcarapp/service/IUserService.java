package com.rentcarapp.service;

import com.rentcarapp.model.dto.UserDTO;
import com.rentcarapp.model.entity.User;
import com.rentcarapp.projection.UserProjection;

import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    Optional<UserDTO> saveUser(User user);

    Optional<UserProjection> deleteUser(String idValue);

    Optional<UserProjection> getUserByUsername(String username);

    Optional<UserProjection> findById(UUID id);

}
