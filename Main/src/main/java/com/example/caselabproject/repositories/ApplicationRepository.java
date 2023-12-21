package com.example.caselabproject.repositories;


import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.enums.ApplicationStatus;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByRecordStateAndApplicationStatusAndDeadlineDateBefore(
            RecordState recordState, ApplicationStatus applicationStatus, LocalDateTime now
    );
}
