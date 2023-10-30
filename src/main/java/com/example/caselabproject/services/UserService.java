package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.UserCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
@Validated
public interface UserService {

    @Transactional(readOnly = true)
    UserGetByIdResponseDto getById(@Min(1L) Long id);

    @Transactional
    UserCreateResponseDto create(@Valid UserCreateRequestDto userRequestDto);
    /*@Transactional
    UserResponseDto updateById(Long id, UserRequestDto userRequestDto);*/

    void deleteById(Long id);

//    List<DocumentCreateResponseDto> findDocsByCreatorId(Long id) { }
}
