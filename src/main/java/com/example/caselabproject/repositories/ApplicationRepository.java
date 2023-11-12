package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
