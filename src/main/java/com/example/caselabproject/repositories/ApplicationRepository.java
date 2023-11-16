package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByIdAndRecordStateAndDeadlineDate(
            Long id, RecordState state, LocalDateTime deadline);
}
