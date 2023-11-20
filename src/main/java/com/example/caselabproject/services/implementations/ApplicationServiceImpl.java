package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationUpdateResponseDto;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.ApplicationStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationItemRepository;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ApplicationItemRepository applicationItemRepository;


    @Override
    public ApplicationCreateResponseDto createApplication(String username, ApplicationCreateRequestDto request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Application application = request.mapToEntity();
        application.setRecordState(RecordState.ACTIVE);
        application.setApplicationStatus(ApplicationStatus.WAITING_FOR_ANSWER);
        application.setCreatorId(user);
        application.setOrganization(user.getOrganization());
        applicationRepository.save(application);
        return ApplicationCreateResponseDto.mapFromEntity(application);
    }

    @Override
    public ApplicationUpdateResponseDto updateApplication(Long id, String username,
                                                          ApplicationUpdateRequestDto request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Application application = request.mapToEntity();
        Application updateApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
        if (!(user.getOrganization().getId().equals(application.getOrganization().getId()))) {
            throw new AlienOrganizationException(user.getUsername(), application.getName());
        } else if (!user.getUsername().equals(application.getCreatorId().getUsername())) {
            throw new UserNotCreatorException(username);
        } else {
            updateApplication.setDeadlineDate(application.getDeadlineDate());
            applicationRepository.save(updateApplication);
        }

        return ApplicationUpdateResponseDto.mapFromEntity(application);
    }

    @Override
    public boolean deleteApplication(Long id, String username) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        AtomicBoolean isAdmin = new AtomicBoolean(false);
        user.getRoles().forEach(o -> {
            if (o.getName().equals("ROLE_ADMIN")) {
                isAdmin.set(true);
            }
        });
        if (!(user.getOrganization().getId().equals(application.getOrganization().getId()))) {
            throw new AlienOrganizationException(user.getUsername(), application.getName());
        } else if (!application.getCreatorId().getUsername().equals(user.getUsername()) || !isAdmin.get()) {
            throw new UserNotCreatorException(username);
        } else {
            if (!application.getRecordState().equals(RecordState.DELETED)) {
                application.setRecordState(RecordState.DELETED);
            } else {
                throw new ApplicationAlreadyDeletedException(application.getId());
            }
            applicationRepository.save(application);
        }
        return true;
    }

    @Override
    public ApplicationFindResponseDto getApplicationById(Long id) {
        Application getApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
        return ApplicationFindResponseDto.mapFromEntity(getApplication);
    }

    /*@Override
    public ApplicationUpdateStatusAndCommentResponseDto updateApplicationStatusAndComment(
            Long id,
            String userName,
            ApplicationItemStatus status,
            String comment) {
        Application oldApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationDoesNotExistException(id));
        List<ApplicationItem> applicationItems = oldApplication.getApplicationItems();
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserByUsernameNotFoundException(userName));
        ApplicationItem applicationItem = applicationItems.stream()
                .filter(o -> o.getApplication().getId().equals(oldApplication.getId()) &&
                        o.getDepartment().getId().equals(user.getDepartment().getId()))
                .findFirst()
                .orElseThrow(() -> new ApplicationItemDoesNotFoundException());
        applicationItems.remove(applicationItem);
        applicationItems.remove(applicationItem);
        if (comment != null) {
            applicationItem.setComment(comment);
        }
        if (status != null) {
            applicationItem.setStatus(status);
        }
        applicationItem.setApplication(oldApplication);
        applicationItems.add(applicationItem);
        applicationRepository.save(oldApplication);

        return ApplicationUpdateStatusAndCommentResponseDto.mapFromEntity(applicationItem);
    }*/
}
