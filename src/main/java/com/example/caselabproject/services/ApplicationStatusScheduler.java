package com.example.caselabproject.services;

import java.time.LocalDateTime;

public interface ApplicationStatusScheduler {
    void setScheduler(Long applicationId, LocalDateTime timeToExecute);
}
