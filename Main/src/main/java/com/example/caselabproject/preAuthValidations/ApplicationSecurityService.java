package com.example.caselabproject.preAuthValidations;


import com.example.caselabproject.exceptions.application.ApplicationAlreadyDeletedException;
import com.example.caselabproject.exceptions.application.ApplicationNotFoundException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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
        Application application = getApplicationById(applicationId);
        User user = getUserByUserName(username);
        return application.getCreatorId().getId().equals(user.getId());
    }

    public boolean canDeleteApplication(String username, Long applicationId){
        User user = getUserByUserName(username);
        Application application = getApplicationById(applicationId);
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
                throw new ApplicationAlreadyDeletedException(application.getId());
            }
        }
        return canDeleteApplication;
    }

    private User getUserByUserName(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Application getApplicationById(Long id){
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
    }
}