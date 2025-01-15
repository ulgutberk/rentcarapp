package com.rentcarapp.repository;

import com.rentcarapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// Repo yu ve repo tanimladigimiz yer.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(long id);
}
