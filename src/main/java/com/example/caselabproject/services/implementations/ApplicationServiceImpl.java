package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationDeleteRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationDeleteResponseDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;


    @Override
    public ApplicationCreateResponseDto createApplication(String username, ApplicationCreateRequestDto request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserByUsernameNotFoundException(username));
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
                .orElseThrow(() -> new UserByUsernameNotFoundException(username));
        Application application = request.mapToEntity();
        Application updateApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationDoesNotExistException(id));
        if (!user.getUsername().equals(application.getCreatorId().getUsername())){
            throw new UserNotCreatorException(username);
        } else {
            updateApplication.setDeadlineDate(application.getDeadlineDate());
            applicationRepository.save(updateApplication);
        }

        return ApplicationUpdateResponseDto.mapFromEntity(application);
    }

    @Override
    public ApplicationDeleteResponseDto deleteApplication(Long id, String username){
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationDoesNotExistException(id));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserByUsernameNotFoundException(username));
        if (!application.getCreatorId().getUsername().equals(user.getUsername())){
            throw new UserNotCreatorException(username);
        } else {
            applicationRepository.delete(application);
        }

        return ApplicationDeleteResponseDto.mapFromEntity(application);
    }

    @Override
    public ApplicationFindResponseDto getApplicationById(Long id) {
        Application getApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationDoesNotExistException(id));
        return ApplicationFindResponseDto.mapFromEntity(getApplication);
    }
}
