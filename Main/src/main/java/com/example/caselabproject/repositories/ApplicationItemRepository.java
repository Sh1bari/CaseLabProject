package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.ApplicationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationItemRepository extends JpaRepository<ApplicationItem, Long> {
}
