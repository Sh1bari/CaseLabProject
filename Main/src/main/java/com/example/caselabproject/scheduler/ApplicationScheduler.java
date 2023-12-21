package com.example.caselabproject.scheduler;


import com.example.caselabproject.messaging.producer.implementation.KafkaApplicationStateProducer;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.enums.ApplicationStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.services.implementations.ApplicationItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationScheduler {
    private final ApplicationRepository applicationRepository;
    private final ApplicationItemServiceImpl applicationItemServiceImpl;

    /**
     *
     *
     * @see Application
     */
    @Scheduled(cron = "5 * * * * *") // every 5-th second of each minute
    public void setScheduler() {
        List<Application> applications = applicationRepository
                .findAllByRecordStateAndApplicationStatusAndDeadlineDateBefore(
                        RecordState.ACTIVE, ApplicationStatus.WAITING_FOR_ANSWER, LocalDateTime.now());

        for (Application application : applications) {
            applicationItemServiceImpl.calcApplicationItemsResult(application);
        }
    }
    
    private final KafkaApplicationStateProducer applicationStateProducer;
    @Scheduled(cron = "*/1 * * * * *") // every second
    public void test() {
        applicationStateProducer.send(Application.builder().name("asdfadf").build());
    }
}
