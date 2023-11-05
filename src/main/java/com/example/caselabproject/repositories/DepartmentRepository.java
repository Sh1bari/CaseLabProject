package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Page<Department> findAll(Pageable pageable);

    Page<Department> findDepartmentsByNameContainingAndRecordState(String name, Pageable pageable, RecordState recordState);
}
