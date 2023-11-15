package com.example.caselabproject.services.implementations;

import com.example.caselabproject.services.ApplicationService;
import com.example.caselabproject.services.ApplicationStatusScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
//@RequiredArgsConstructor
// async?
public class ApplicationStatusSchedulerImpl implements ApplicationStatusScheduler {
    private final ApplicationService applicationService;
    // static Map<Name, Executor> ->

    @Autowired
    public ApplicationStatusSchedulerImpl(@Lazy ApplicationService applicationService) {
        this.applicationService = applicationService;
    }


//    @Scheduled()
//    public void updateStatus() {
//        // int n = getVotes()
//        // count
//    }

//    private void getVotes() {
//
//    }


    @Override
    public void save(Long id, String username, LocalDateTime dateTime) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());

            ZonedDateTime zonedDateTime1 = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
            long millis = zonedDateTime1.toInstant().toEpochMilli() - zonedDateTime.toInstant().toEpochMilli();

            executor.schedule(
                    () -> {
                        applicationService.deleteApplication(id, username);
                    },
                    millis,
                    TimeUnit.MILLISECONDS);
        } finally {
            executor.shutdown();
//            executor.close(); ???
        }
    }
}
