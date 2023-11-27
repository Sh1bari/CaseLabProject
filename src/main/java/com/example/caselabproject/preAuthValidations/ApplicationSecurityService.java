package com.example.caselabproject.preAuthValidations;

import com.example.caselabproject.exceptions.application.ApplicationNotFoundException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.*;
import org.springframework.stereotype.Service;

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
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(()-> new ApplicationNotFoundException(applicationId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException(username));
        return application.getCreatorId().getId().equals(user.getId());
    }
}
