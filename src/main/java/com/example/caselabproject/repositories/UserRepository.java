package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.User;
import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
