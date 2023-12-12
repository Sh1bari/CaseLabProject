package com.example.caselabproject.preAuthValidations;

import com.example.caselabproject.exceptions.department.DepartmentNotFoundException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.UserRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    UserRepository userRepository;

    private boolean canDeleteUserById(@Min(value = 1L, message = "Id can't be less than 1") Long id) {
        User user = getUserById(id);
        return !user.getRecordState().equals(RecordState.DELETED);
    }

    private boolean canRecoverUserById(@Min(value = 1L, message = "Id can't be less than 1") Long id) {
        User user = getUserById(id);
        return !user.getRecordState().equals(RecordState.ACTIVE);
    }

    private boolean canAppointDirector(@Min(value = 1L, message = "Id can't be less than 1") Long userId, @Min(value = 1L, message = "Id can't be less than 1") Long departmentId) {
        User user = getDirectorByDepartment(departmentId);
        return !user.getId().equals(userId);
    }

    private User getUserByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private User getUserById(@Min(value = 1L, message = "Id can't be less than 1") Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private User getDirectorByDepartment(@Min(value = 1L, message = "Id can't be less than 1") Long departmentId) {
        return userRepository.findByIsDirectorAndDepartment_Id(true, departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
    }
}
