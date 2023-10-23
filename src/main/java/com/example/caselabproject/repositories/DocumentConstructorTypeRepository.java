package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentConstructorTypeRepository extends JpaRepository<DocumentConstructorType, Long> {
}
