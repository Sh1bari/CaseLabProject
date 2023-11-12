package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    boolean existsByDocumentConstructorType(DocumentConstructorType documentConstructorType);

    List<Document> findAllByCreator_id(Long id);
}
