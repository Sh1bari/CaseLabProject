package com.example.caselabproject.repositories;


import com.example.caselabproject.models.entities.BillingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BillingLogRepository extends JpaRepository<BillingLog, Long> {
    
    List<BillingLog> findAllByIdAndSubscriptionStartBetweenOrderBySubscriptionStart(
            Long id, LocalDateTime before, LocalDateTime now);
}
