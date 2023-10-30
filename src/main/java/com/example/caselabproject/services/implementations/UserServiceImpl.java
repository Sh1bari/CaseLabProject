package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.UserExistsException;
import com.example.caselabproject.exceptions.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.UserCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.RoleService;
import com.example.caselabproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserGetByIdResponseDto getById(Long id) {
        return UserGetByIdResponseDto.mapFromEntity(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public UserCreateResponseDto create(UserCreateRequestDto userRequestDto) {
        User user = userRequestDto.mapToEntity();
        user.setRoles(roleService.findRolesByRoleDtoList(userRequestDto.getRoles()));
        user.getAuthUserInfo().setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistsException(user.getUsername());
        }
        return UserCreateResponseDto.mapFromEntity(user);
    }

    /*@Override
    @Transactional
    public UserResponseDto updateById(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setPosition(userRequestDto.getPosition());
        user.setRecordState(userRequestDto.getRecordState());
        user.setUsername(userRequestDto.getUsername());
        // otdel
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistsException(user.getUsername());
        }
        return UserResponseDto.mapFromEntity(user);
    }*/

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setRecordState(RecordState.DELETED);
        userRepository.save(user);
    }

    /*@Override
    @Transactional
    List<DocumentCreateResponseDto> findDocsByCreatorId(Long id) {

    }*/
}
