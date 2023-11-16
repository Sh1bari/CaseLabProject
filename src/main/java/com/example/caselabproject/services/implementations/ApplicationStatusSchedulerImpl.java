package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.ApplicationAlreadyDeletedException;
import com.example.caselabproject.exceptions.ApplicationNotFoundException;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.enums.ApplicationStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.services.ApplicationStatusScheduler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ApplicationStatusSchedulerImpl implements ApplicationStatusScheduler {

    private final ApplicationRepository applicationRepository;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final ApplicationItemServiceImpl applicationItemServiceImpl;

    @Override
    public void setScheduler(Long applicationId, LocalDateTime deadline) {

        executorService.schedule(() -> {
                    if (applicationRepository.existsByIdAndRecordStateAndDeadlineDate(
                            applicationId, RecordState.ACTIVE, deadline)) {
                        deleteApplication(applicationId);
                    }
                },
                timer(deadline),
                TimeUnit.MILLISECONDS);
    }

    public void renewSchedulers() {

        List<Application> applications = applicationRepository.findAllByRecordState(
                RecordState.ACTIVE
        );

        for (Application application : applications) {
            executorService.schedule(
                    () -> deleteApplication(application.getId()),
                    timer(application.getDeadlineDate()),
                    TimeUnit.MILLISECONDS
            );
        }
    }

    @PostConstruct
    private void init() {
        renewSchedulers();
    }

    @PreDestroy
    private void destroy() {
        executorService.shutdownNow();
    }

    private void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
        if (application.getApplicationStatus().equals(ApplicationStatus.WAITING_FOR_ANSWER)) {
            applicationItemServiceImpl.calcApplicationItemsResult(application);
        }
        if (!application.getRecordState().equals(RecordState.DELETED)) {
            application.setRecordState(RecordState.DELETED);
        } else {
            throw new ApplicationAlreadyDeletedException(application.getId());
        }
        applicationRepository.save(application);
    }

    private long timer(LocalDateTime deleteTime) {
        ZonedDateTime createTimeMillis = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        ZonedDateTime deleteTimeMillis = ZonedDateTime.of(deleteTime, ZoneId.systemDefault());

        return deleteTimeMillis.toInstant().toEpochMilli() - createTimeMillis.toInstant().toEpochMilli();
    }
}