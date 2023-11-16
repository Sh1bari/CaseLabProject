package com.example.caselabproject.services.implementations;

import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.services.ApplicationStatusScheduler;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ApplicationStatusSchedulerImpl implements ApplicationStatusScheduler {
    private final ApplicationRepository applicationRepository;
    private final ScheduledExecutorService executorService;

    public ApplicationStatusSchedulerImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void setScheduler(Long applicationId, LocalDateTime deadline) {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());

        ZonedDateTime zonedDateTime1 = ZonedDateTime.of(deadline, ZoneId.systemDefault());
        long millis = zonedDateTime1.toInstant().toEpochMilli() - zonedDateTime.toInstant().toEpochMilli();
        executorService.schedule(() -> {
                    if (applicationRepository.existsByIdAndRecordStateAndDeadlineDate(
                            applicationId, RecordState.ACTIVE, deadline)) {
                        // count votes
                        // result Date
                        // delete
                    }
                },
                millis,
                TimeUnit.MILLISECONDS);
    }

    private long timer(LocalDateTime deleteTime) {
        ZonedDateTime createTimeMillis = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        ZonedDateTime deleteTimeMillis = ZonedDateTime.of(deleteTime, ZoneId.systemDefault());

        return deleteTimeMillis.toInstant().toEpochMilli() - createTimeMillis.toInstant().toEpochMilli();
    }

    @PreDestroy
    private void destroy() {
        executorService.shutdownNow();
    }
}
