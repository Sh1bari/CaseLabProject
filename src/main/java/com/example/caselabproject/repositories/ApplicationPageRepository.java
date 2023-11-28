package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApplicationPageRepository extends PagingAndSortingRepository<Application, Long> {
    Page<Application> findAllByCreatorId_idAndRecordState(Long id, RecordState recordState, Pageable pageable);
}
