package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.CreateApplicationItemRequestDto;
import com.example.caselabproject.models.DTOs.response.CreateApplicationItemResponseDto;
import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.entities.ApplicationItem;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationRepository;
import com.example.caselabproject.repositories.DepartmentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.ApplicationItemService;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Service
@RequiredArgsConstructor
public class ApplicationItemServiceImpl implements ApplicationItemService {

    private final ApplicationRepository applicationRepo;
    private final DepartmentRepository departmentRepo;
    private final UserRepository userRepo;

    @Override
    public List<CreateApplicationItemResponseDto> createApplicationItem(List<CreateApplicationItemRequestDto> req, Long applicationId, String username) {
        List<CreateApplicationItemResponseDto> res = new ArrayList<>();
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(()->new ApplicationNotFoundException(applicationId));
        User user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException(username));
        if(!application.getCreatorId().getId().equals(user.getId())){
            throw new UserNotCreatorException(username);
        }
        req.forEach(o->{
            if(o.getToUserId() != null) {
                User varUser = userRepo.findByIdAndDepartment_id(o.getToUserId(), o.getToDepartmentId())
                        .orElseThrow(() -> new UserNotFoundByDepartmentException(o.getToUserId(), o.getToDepartmentId()));
                userAndDepartmentAreActive(varUser);
                if (!application.getApplicationItems().isEmpty()){
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
            }else {
                Department department = departmentRepo.findById(o.getToDepartmentId())
                        .orElseThrow(()->new DepartmentNotFoundException(o.getToDepartmentId()));
                departmentIsActive(department);
                if (!application.getApplicationItems().isEmpty()){
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
        application.getApplicationItems().forEach(o->res.add(CreateApplicationItemResponseDto.builder()
                        .applicationItemId(o.getId())
                        .build()));
        return res;
    }

    private static boolean departmentIsActive(Department department){
        if (department.getRecordState().equals(RecordState.DELETED)){
            throw new DepartmentDeletedException(department.getId());
        }
        return true;
    }

    private static boolean userAndDepartmentAreActive(User user){
        if(user.getRecordState().equals(RecordState.DELETED)){
            throw new UserDeletedException(user.getId());
        }
        if (user.getDepartment().getRecordState().equals(RecordState.DELETED)){
            throw new DepartmentDeletedException(user.getDepartment().getId());
        }
        return true;
    }

}
