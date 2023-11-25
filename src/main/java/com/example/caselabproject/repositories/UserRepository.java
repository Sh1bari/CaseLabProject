package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndDocuments_id(String username, Long documentId);

    Page<User> findByRecordStateAndDepartment_Id(Pageable pageable, RecordState recordState, Long departmentId);

    Optional<User> findByIdAndDepartment_id(Long userId, Long departmentId);
}
