package com.example.lab2.Repository;

import com.example.lab2.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<Object> findByUsername(String username);
}
