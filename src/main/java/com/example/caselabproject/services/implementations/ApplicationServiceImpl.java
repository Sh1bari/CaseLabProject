package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.application.ApplicationAlreadyDeletedException;
import com.example.caselabproject.exceptions.application.ApplicationNotFoundException;
import com.example.caselabproject.exceptions.document.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.user.UserNotCreatorException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.application.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.application.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.request.document.DocIdRequestDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationUpdateResponseDto;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.ApplicationStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.repositories.DocumentRepository;
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
    private final DocumentRepository documentRepository;


    @Override
    public ApplicationCreateResponseDto createApplication(String username, ApplicationCreateRequestDto request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Application application = request.mapToEntity();
        application.setRecordState(RecordState.ACTIVE);
        application.setApplicationStatus(ApplicationStatus.WAITING_FOR_ANSWER);
        application.setCreatorId(user);
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
        updateApplication.setDeadlineDate(application.getDeadlineDate());
        updateApplication.setName(application.getName());
        applicationRepository.save(updateApplication);

        return ApplicationUpdateResponseDto.mapFromEntity(updateApplication);
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
        if (!application.getCreatorId().getUsername().equals(user.getUsername()) || !isAdmin.get()) {
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

    @Override
    public ApplicationFindResponseDto connectDocToApplication(Long id, DocIdRequestDto req) {
        Application getApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
        Document document = documentRepository.findById(req.getDocId())
                .orElseThrow(() -> new DocumentDoesNotExistException(req.getDocId()));
        getApplication.setDocument(document);
        applicationRepository.save(getApplication);
        return ApplicationFindResponseDto.mapFromEntity(getApplication);
    }

}
