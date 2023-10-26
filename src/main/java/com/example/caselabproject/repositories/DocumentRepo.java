package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<Document, Long> {

}
