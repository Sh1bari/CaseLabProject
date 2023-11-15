package com.example.caselabproject.services;

import java.time.LocalDateTime;

public interface ApplicationStatusScheduler {
    void save(Long id, String username, LocalDateTime dateTime);
}
