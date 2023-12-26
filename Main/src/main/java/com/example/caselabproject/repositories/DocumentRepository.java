package com.example.caselabproject.repositories;


import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    boolean existsByDocumentConstructorType(DocumentConstructorType documentConstructorType);

    boolean existsByIdAndRecordState(Long id, RecordState recordState);

    List<Document> findAllByCreator_id(Long id);

    Page<Document> findAllByNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndRecordState(
            String name, Pageable pageable, LocalDateTime start, LocalDateTime end, RecordState state);

    Page<Document> findAllByCreationDateAfterAndCreationDateBeforeAndRecordState(
            Pageable pageable, LocalDateTime start, LocalDateTime end, RecordState state);

    Page<Document> findAllByCreator_idAndNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndDocumentConstructorType_IdAndRecordState(
            Long creatorId,
            String name,
            LocalDateTime creationDateFrom,
            LocalDateTime creationDateTo,
            Long documentConstructorTypeId,
            RecordState recordState,
            Pageable pageable);
    Page<Document> findAllByCreator_idAndNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndRecordState(
            Long creatorId,
            String name,
            LocalDateTime creationDateFrom,
            LocalDateTime creationDateTo,
            RecordState recordState,
            Pageable pageable);
}
