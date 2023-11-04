package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApplicationPageRepository extends PagingAndSortingRepository<Application, Long> {
    Page<Application> findAllByCreatorId_id(Long id, Pageable pageable);
}
