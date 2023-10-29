package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.ApplicationCreateException;
import com.example.caselabproject.exceptions.ApplicationDoesNotExistException;
import com.example.caselabproject.models.DTOs.request.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationDeleteRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationDeleteResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationUpdateResponseDto;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.services.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {
    private ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository){
        this.applicationRepository = applicationRepository;
    }

    @Override
    public ApplicationCreateResponseDto createApplication(ApplicationCreateRequestDto request) {
        Application application = request.mapToEntity();
        try {
            applicationRepository.save(application);
        } catch (ApplicationCreateException e){
            throw new ApplicationCreateException();
        }
        return ApplicationCreateResponseDto.mapFromEntity(application);
    }

    @Override
    public ApplicationUpdateResponseDto updateApplication(Long id, ApplicationUpdateRequestDto request) {
        Application application = request.mapToEntity();
        Application updateApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationDoesNotExistException(id));
        updateApplication.setDeadlineDate(application.getDeadlineDate());
        applicationRepository.save(updateApplication);

        return ApplicationUpdateResponseDto.mapFromEntity(application);
    }

    @Override
    public ApplicationDeleteResponseDto deleteApplication(ApplicationDeleteRequestDto request){
        Application application = request.mapToEntity();
        Application deleteApplication = applicationRepository.findById(application.getId())
                .orElseThrow(() -> new ApplicationDoesNotExistException(application.getId()));
        applicationRepository.delete(deleteApplication);

        return ApplicationDeleteResponseDto.mapFromEntity(application);
    }

    @Override
    public ApplicationFindResponseDto getApplicationById(Long id) {
        Application getApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationDoesNotExistException(id));
        return ApplicationFindResponseDto.mapFromEntity(getApplication);
    }
}
