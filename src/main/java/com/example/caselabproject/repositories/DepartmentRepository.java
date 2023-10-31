package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
