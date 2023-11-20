package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.ApplicationItemVoteRequestDto;
import com.example.caselabproject.models.DTOs.request.CreateApplicationItemRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationItemGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationItemTakeResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationItemVoteResponseDto;
import com.example.caselabproject.models.DTOs.response.CreateApplicationItemResponseDto;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.ApplicationItem;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.ApplicationStatus;
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
import java.util.concurrent.atomic.AtomicInteger;

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
        if (!(user.getOrganization().getId().equals(application.getOrganization().getId()))) {
            throw new AlienOrganizationException(user.getUsername(), application.getName());
        } else if (!application.getCreatorId().getId().equals(user.getId())) {
            throw new UserNotCreatorException(username);
        }
        req.forEach(o -> {
            if (o.getToUserId() != null) {
                User varUser = userRepo.findByIdAndDepartment_id(o.getToUserId(), o.getToDepartmentId())
                        .orElseThrow(() -> new UserNotFoundByDepartmentException(o.getToUserId(), o.getToDepartmentId()));
                userAndDepartmentAreActive(varUser);
                if (application.getApplicationItems().stream().anyMatch(k->k.getToDepartment().getId().equals(o.getToDepartmentId()))) {
                    throw new ApplicationItemAlreadyHasBeenSentToDepartmentException(o.getToDepartmentId());
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
                if (application.getApplicationItems().stream().anyMatch(k->k.getToDepartment().getId().equals(o.getToDepartmentId()))) {
                    throw new ApplicationItemAlreadyHasBeenSentToDepartmentException(o.getToDepartmentId());
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
        if (!(user.getOrganization().getId().equals(application.getOrganization().getId()))) {
            throw new AlienOrganizationException(user.getUsername(), application.getName());
        }
        ApplicationItem applicationItem = getApplicationItemByApplicationAndId(application, applicationItemId);
        //Просмотр админам, создателю и всем в отделе
        if (!isAdmin.get() &&
                !application.getCreatorId().getId().equals(user.getId()) &&
                !applicationItem.getToDepartment().getId().equals(user.getDepartment().getId())) {
            throw new ApplicationItemPermissionException(applicationItem.getId());
        }

        return ApplicationItemGetByIdResponseDto.mapFromEntity(applicationItem);
    }

    @Override
    public ApplicationItemTakeResponseDto takeApplicationItem(Long applicationId,
                                                              Long applicationItemId,
                                                              String username) {
        User user = getUserByUsername(username);
        Application application = getApplicationById(applicationId);
        if (!(user.getOrganization().getId().equals(application.getOrganization().getId()))) {
            throw new AlienOrganizationException(user.getUsername(), application.getName());
        }
        ApplicationItem applicationItem =  getApplicationItemByApplicationAndId(application, applicationItemId);
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

    @Override
    public ApplicationItemVoteResponseDto voteApplicationItem(Long applicationId,
                                                              Long applicationItemId,
                                                              String username,
                                                              ApplicationItemVoteRequestDto voteApplicationItem) {
        User user = getUserByUsername(username);
        Application application = getApplicationById(applicationId);
        if (!(user.getOrganization().getId().equals(application.getOrganization().getId()))) {
            throw new AlienOrganizationException(user.getUsername(), application.getName());
        }
        applicationIsActive(application);
        ApplicationItem applicationItem =  getApplicationItemByApplicationAndId(application, applicationItemId);
        applicationItemIsActive(applicationItem);
        departmentIsActive(user.getDepartment());
        if(!applicationItem.getToUser().getId().equals(user.getId())){
            throw new ApplicationItemPermissionException();
        }
        applicationItem.setComment(voteApplicationItem.getComment());
        applicationItem.setStatus(voteApplicationItem.getStatus());
        applicationItem.setVoteTime(LocalDateTime.now());
        applicationItemRepo.save(applicationItem);

        //Check for all vote marks
        Application applicationCheck = getApplicationById(applicationId);
        List<ApplicationItem> applicationItems = applicationCheck.getApplicationItems().stream()
                .filter(o->o.getStatus().equals(ApplicationItemStatus.PENDING))
                .toList();
        if(applicationItems.size() == 0){
            calcApplicationItemsResult(applicationCheck);
        }

        return ApplicationItemVoteResponseDto.mapFromEntity(applicationItem);
    }

    void calcApplicationItemsResult(Application application) {
        AtomicInteger pending = new AtomicInteger();
        AtomicInteger accepted = new AtomicInteger();
        AtomicInteger denied = new AtomicInteger();
        int sum = application.getApplicationItems().size();
        application.getApplicationItems().stream()
                .filter(o->o.getRecordState().equals(RecordState.ACTIVE))
                .forEach(o->{
                    switch (o.getStatus()){
                        case ACCEPTED -> accepted.getAndIncrement();
                        case DENIED -> denied.getAndIncrement();
                        case PENDING -> pending.getAndIncrement();
                    }
                });
        if(sum * 0.6 >= accepted.get() + denied.get()){
            application.setApplicationStatus(ApplicationStatus.NOT_ENOUGH_VOTES);
        }else if(accepted.get() == denied.get()){
            application.setApplicationStatus(ApplicationStatus.DRAW);
        } else if (accepted.get() > denied.get()) {
            application.setApplicationStatus(ApplicationStatus.ACCEPTED);
        }else if (accepted.get() < denied.get()) {
            application.setApplicationStatus(ApplicationStatus.DENIED);
        }
        application.setResultDate(LocalDateTime.now());
        applicationRepo.save(application);
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
    private ApplicationItem getApplicationItemByApplicationAndId(Application application, Long applicationItemId) {
        ApplicationItem applicationItem = application.getApplicationItems().stream()
                .filter(o-> o.getId().equals(applicationItemId)).findFirst()
                .orElseThrow(()->new ApplicationNotFoundException(applicationItemId));
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
