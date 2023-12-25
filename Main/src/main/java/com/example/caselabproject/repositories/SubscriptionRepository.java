package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.Subscription;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @NotNull
    Optional<Subscription> findById(@NotNull Long id);
}
