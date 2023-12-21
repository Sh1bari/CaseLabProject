package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
    List<Field> findAllByNameIn(List<String> names);

    Optional<Field> findByNameEquals(String name);
}
