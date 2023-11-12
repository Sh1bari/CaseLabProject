package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Page<File> findAllByDocument_Id(Long id, Pageable pageable);
}
