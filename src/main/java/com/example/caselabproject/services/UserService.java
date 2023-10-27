package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserRequestDto;
import com.example.caselabproject.models.DTOs.response.UserResponseDto;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Min;

public interface UserService {

    @Transactional(readOnly = true)
    UserResponseDto getById(@Min(1L) Long id);

    @Transactional
    UserResponseDto create(UserCreateRequestDto userRequestDto);
    @Transactional
    UserResponseDto updateById(Long id, UserRequestDto userRequestDto);

    void deleteById(Long id);

//    List<DocumentCreateResponseDto> findDocsByCreatorId(Long id) { }
}
