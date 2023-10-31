package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {
    List<Document> findAllByCreator_id(Long id);
}
