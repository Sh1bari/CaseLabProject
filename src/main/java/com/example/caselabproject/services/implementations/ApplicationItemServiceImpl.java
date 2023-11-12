package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.CreateApplicationItemRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationItemGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationItemTakeResponseDto;
import com.example.caselabproject.models.DTOs.response.CreateApplicationItemResponseDto;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.ApplicationItem;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationItemRepository;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.repositories.DepartmentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.ApplicationItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Service
@RequiredArgsConstructor
public class ApplicationItemServiceImpl implements ApplicationItemService {

    private final ApplicationRepository applicationRepo;
    private final ApplicationItemRepository applicationItemRepo;
    private final DepartmentRepository departmentRepo;
    private final UserRepository userRepo;

    @Override
    public List<CreateApplicationItemResponseDto> createApplicationItem(List<CreateApplicationItemRequestDto> req,
                                                                        Long applicationId,
                                                                        String username) {
        List<CreateApplicationItemResponseDto> res = new ArrayList<>();
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(applicationId));
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!application.getCreatorId().getId().equals(user.getId())) {
            throw new UserNotCreatorException(username);
        }
        req.forEach(o -> {
            if (o.getToUserId() != null) {
                User varUser = userRepo.findByIdAndDepartment_id(o.getToUserId(), o.getToDepartmentId())
                        .orElseThrow(() -> new UserNotFoundByDepartmentException(o.getToUserId(), o.getToDepartmentId()));
                userAndDepartmentAreActive(varUser);
                if (!application.getApplicationItems().isEmpty()) {
                    throw new ApplicationItemAlreadyHasBeenSentToDepartmentException(user.getDepartment().getId());
                }
                ApplicationItem applicationItem = ApplicationItem.builder()
                        .status(ApplicationItemStatus.PENDING)
                        .recordState(RecordState.ACTIVE)
                        .toDepartment(varUser.getDepartment())
                        .toUser(varUser)
                        .application(application)
                        .createTime(LocalDateTime.now())
                        .build();
                application.getApplicationItems().add(applicationItem);
                applicationRepo.save(application);
            } else {
                Department department = departmentRepo.findById(o.getToDepartmentId())
                        .orElseThrow(() -> new DepartmentNotFoundException(o.getToDepartmentId()));
                departmentIsActive(department);
                if (!application.getApplicationItems().isEmpty()) {
                    throw new ApplicationItemAlreadyHasBeenSentToDepartmentException(department.getId());
                }
                ApplicationItem applicationItem = ApplicationItem.builder()
                        .status(ApplicationItemStatus.PENDING)
                        .recordState(RecordState.ACTIVE)
                        .toDepartment(department)
                        .application(application)
                        .createTime(LocalDateTime.now())
                        .build();
                application.getApplicationItems().add(applicationItem);
                applicationRepo.save(application);
            }
        });
        application.getApplicationItems().forEach(o -> res.add(CreateApplicationItemResponseDto.builder()
                .id(o.getId())
                .build()));
        return res;
    }

    @Override
    public ApplicationItemGetByIdResponseDto getApplicationItemById(Long applicationId,
                                                                    Long applicationItemId,
                                                                    String username) {
        AtomicBoolean isAdmin = new AtomicBoolean(false);
        User user = getUserByUsername(username);
        user.getRoles().forEach(o -> {
            if (o.getName().equals("ROLE_ADMIN")) {
                isAdmin.set(true);
            }
        });
        Application application = getApplicationById(applicationId);
        ApplicationItem applicationItem = getApplicationItemById(applicationItemId);
        //Просмотр админам, создателю и всем в отделе
        if (!isAdmin.get() &&
                !application.getCreatorId().getId().equals(user.getId()) &&
                !applicationItem.getToDepartment().getId().equals(user.getDepartment().getId())) {
            throw new ApplicationItemPermissionException(applicationItem.getId());
        }

        return ApplicationItemGetByIdResponseDto.mapFromEntity(applicationItem);
    }

    @Override
    public ApplicationItemTakeResponseDto takeApplicationItem(Long applicationId, Long applicationItemId, String username) {
        User user = getUserByUsername(username);
        Application application = getApplicationById(applicationId);
        ApplicationItem applicationItem = getApplicationItemById(applicationItemId);
        departmentIsActive(user.getDepartment());
        //RecordState check
        applicationIsActive(application);
        applicationItemIsActive(applicationItem);

        if (!applicationItem.getToDepartment().getId().equals(user.getDepartment().getId())) {
            throw new ApplicationItemPermissionException(applicationItemId);
        }
        if (applicationItem.getToUser() != null) {
            throw new ApplicationItemAlreadyTakenException(applicationItemId);
        }
        applicationItem.setToUser(user);
        applicationItemRepo.save(applicationItem);
        return ApplicationItemTakeResponseDto.mapFromEntity(applicationItem);
    }

    private User getUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return user;
    }

    private Application getApplicationById(Long applicationId) {
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(applicationId));
        return application;
    }

    private ApplicationItem getApplicationItemById(Long applicationItemId) {
        ApplicationItem applicationItem = applicationItemRepo.findById(applicationItemId)
                .orElseThrow(() -> new ApplicationItemNotFoundException(applicationItemId));
        return applicationItem;
    }

    private boolean departmentIsActive(Department department) {
        if (department.getRecordState().equals(RecordState.DELETED)) {
            throw new DepartmentDeletedException(department.getId());
        }
        return true;
    }

    private boolean applicationIsActive(Application application) {
        if (application.getRecordState().equals(RecordState.DELETED)) {
            throw new ApplicationDeletedException(application.getId());
        }
        return true;
    }

    private boolean applicationItemIsActive(ApplicationItem applicationItem) {
        if (applicationItem.getRecordState().equals(RecordState.DELETED)) {
            throw new ApplicationItemDeletedException(applicationItem.getId());
        }
        return true;
    }

    private boolean userAndDepartmentAreActive(User user) {
        if (user.getRecordState().equals(RecordState.DELETED)) {
            throw new UserDeletedException(user.getId());
        }
        departmentIsActive(user.getDepartment());
        return true;
    }

}
