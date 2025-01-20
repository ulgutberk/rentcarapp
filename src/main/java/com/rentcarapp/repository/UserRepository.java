package com.rentcarapp.repository;

import com.rentcarapp.model.User;
import com.rentcarapp.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserProjection> findByUsername(String username);

    Optional<UserProjection> findById(UUID id);

}
