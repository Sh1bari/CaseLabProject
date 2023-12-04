package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.application.ApplicationAlreadyDeletedException;
import com.example.caselabproject.exceptions.application.ApplicationNotFoundException;
import com.example.caselabproject.exceptions.document.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.user.UserNotCreatorException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocIdRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationUpdateResponseDto;
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
        User user = getUser(username);
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
        User user = getUser(username);
        Application updateApplication = getApplication(id);
        updateApplication.setDeadlineDate(request.getDeadline());
        updateApplication.setName(request.getName());
        applicationRepository.save(updateApplication);

        return ApplicationUpdateResponseDto.mapFromEntity(updateApplication);
    }

    @Override
    public boolean deleteApplication(Long id, String username) {
        Application application = getApplication(id);
        User user = getUser(username);
        application.setRecordState(RecordState.DELETED);
        applicationRepository.save(application);

        return true;
    }

    @Override
    public ApplicationFindResponseDto getApplicationById(Long id) {
        Application getApplication = getApplication(id);
        return ApplicationFindResponseDto.mapFromEntity(getApplication);
    }

    @Override
    public ApplicationFindResponseDto connectDocToApplication(Long id, DocIdRequestDto req) {
        Application getApplication = getApplication(id);
        Document document = documentRepository.findById(req.getDocId())
                .orElseThrow(() -> new DocumentDoesNotExistException(req.getDocId()));
        getApplication.setDocument(document);
        applicationRepository.save(getApplication);
        return ApplicationFindResponseDto.mapFromEntity(getApplication);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Application getApplication(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
    }

}
