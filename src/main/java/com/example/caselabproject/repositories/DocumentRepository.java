package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
