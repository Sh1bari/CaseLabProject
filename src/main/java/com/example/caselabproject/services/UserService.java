package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.UserRequestDto;
import com.example.caselabproject.models.DTOs.response.UserResponseDto;

public interface UserService {

    UserResponseDto getById(Long id);

    UserResponseDto create(UserRequestDto userRequestDto);

    UserResponseDto updateById(Long id, UserRequestDto userRequestDto);

    void deleteById(Long id);

//    List<DocumentCreateResponseDto> findDocsByCreatorId(Long id) { }
}
