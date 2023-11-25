package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;

public interface DocumentPageRepository extends PagingAndSortingRepository<Document, Long> {

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
}
