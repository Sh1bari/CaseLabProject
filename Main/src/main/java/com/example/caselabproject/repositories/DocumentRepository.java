package com.example.caselabproject.repositories;


import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    boolean existsByDocumentConstructorType(DocumentConstructorType documentConstructorType);

    boolean existsByIdAndRecordState(Long id, RecordState recordState);

    List<Document> findAllByCreator_id(Long id);
}
