package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentConstructorTypeRepository extends JpaRepository<DocumentConstructorType, Long> {
    Page<DocumentConstructorType> findAllByNameContainingIgnoreCaseAndRecordState(
            String name, RecordState state, Pageable pageable);
}
