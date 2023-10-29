package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {

    Page<Document> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
