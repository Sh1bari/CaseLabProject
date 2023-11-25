package com.example.caselabproject.multitenancy.annotations;

import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.EntityOrganizationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckOrganizationValidator implements ConstraintValidator<CheckOrganization, Long> {

    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;
    private Class<? extends EntityOrganizationService> serviceClass;

    @Override
    public void initialize(CheckOrganization constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.serviceClass = constraintAnnotation.serviceClass();
    }

    private Long getCurrentUserOrganizationId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username)).getOrganization().getId();
    }

    @Override
    public boolean isValid(Long entityId, ConstraintValidatorContext context) {
        EntityOrganizationService service = applicationContext.getBean(serviceClass);

        Long currentOrgId = getCurrentUserOrganizationId();
        Long targetOrgId = service.getOrganizationIdByEntityId(entityId);
        return currentOrgId.equals(targetOrgId);
    }
}
