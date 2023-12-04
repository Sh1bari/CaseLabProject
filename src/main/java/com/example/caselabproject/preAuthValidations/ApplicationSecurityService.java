package com.example.caselabproject.preAuthValidations;

import com.example.caselabproject.exceptions.application.ApplicationAlreadyDeletedException;
import com.example.caselabproject.exceptions.application.ApplicationNotFoundException;
import com.example.caselabproject.exceptions.user.UserNotCreatorException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Service
@RequiredArgsConstructor
public class ApplicationSecurityService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public boolean canUpdateApplication(String username, Long applicationId){
        Application application = getApplication(applicationId);
        User user = getUser(username);
        return application.getCreatorId().getId().equals(user.getId());
    }

    public boolean canDeleteApplication(String username, Long applicationId){
        User user = getUser(username);
        Application application = getApplication(applicationId);
        AtomicBoolean isAdmin = new AtomicBoolean(false);
        user.getRoles().forEach(o -> {
            if (o.getName().equals("ROLE_ADMIN")) {
                isAdmin.set(true);
            }
        });
        boolean canDeleteApplication = true;
        if (!application.getCreatorId().getUsername().equals(user.getUsername()) || !isAdmin.get()) {
            canDeleteApplication = false;
        } else {
            if (application.getRecordState().equals(RecordState.DELETED)) {
                canDeleteApplication = false;
            }
        }
        return canDeleteApplication;
    }

    private User getUser(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Application getApplication(Long id){
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
    }
}
