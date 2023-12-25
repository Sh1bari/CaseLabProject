package com.example.caselabproject.repositories;


import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @NotNull
    Optional<Organization> findById(@NotNull Long id);

}
