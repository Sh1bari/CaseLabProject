package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.ApplicationItem;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApplicationItemPageRepository extends PagingAndSortingRepository<ApplicationItem, Long> {
}
