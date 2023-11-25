package com.example.caselabproject.multitenancy.annotations;

import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeNotFoundException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckOrganizationValidator implements ConstraintValidator<CheckOrganization, Long> {

    private final UserRepository userRepository;
    private final DocumentConstructorTypeRepository documentConstructorTypeRepository;

    private Long getCurrentUserOrganizationId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username)).getOrganization().getId();
    }

    @Override
    public boolean isValid(Long entityId, ConstraintValidatorContext context) {
        Long currentOrgId = getCurrentUserOrganizationId();
        Long targetOrgId = documentConstructorTypeRepository.findById(entityId)
                .orElseThrow(() -> new DocumentConstructorTypeNotFoundException(entityId))
                .getOrganization().getId();
        return currentOrgId.equals(targetOrgId);
    }
}
