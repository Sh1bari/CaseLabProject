package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
}
