package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndRecordState(String username, RecordState recordState);
    boolean existsByUsernameAndRecordStateAndRoles_name(String username, RecordState recordState, String roleName);

}
