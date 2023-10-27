package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {
    Page<Document> findDocumentsByNameContaining(Pageable pageable, String name);
    Optional<Document> findDocumentByIdAndCreator_usernameAndCreator_recordState(Long id, String username, RecordState recordState);
}
