package com.example.caselabproject.services.implementations;

import com.example.caselabproject.models.DTOs.request.ApplicationCreateRequestDto;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.ApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

//@ExtendWith(MockitoExtension.class)
class ApplicationStatusSchedulerImpl {
    @Autowired
    private ApplicationService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationRepository applicationRepository;


    @Test
    void test() {
        given(userRepository.findByUsername(any()))
                .willReturn(Optional.of(new User()));
        given(applicationRepository.save(any()))
                .willReturn(new Object());

        ApplicationCreateRequestDto requestDto = new ApplicationCreateRequestDto();
        requestDto.setName("new application");
        requestDto.setDeadlineDate(LocalDateTime.now().plusSeconds(3));

        service.createApplication("user",
                requestDto);

        verify(userRepository.findByUsername(any()));
    }
}
