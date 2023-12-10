package com.example.caselabproject.repositories;


import com.example.caselabproject.models.entities.Subscription;
import com.example.caselabproject.models.enums.SubscriptionName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    
    Double findCostBySubscriptionName(SubscriptionName name);
}
