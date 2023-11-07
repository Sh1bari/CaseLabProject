package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByRecordStateAndDepartment_Id(RecordState recordState, Long departmentId);

    Optional<User> findByIdAndDepartment_id(Long userId, Long departmentId);
}
