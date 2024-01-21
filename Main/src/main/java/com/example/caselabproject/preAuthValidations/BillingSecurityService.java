package com.example.caselabproject.preAuthValidations;

import com.example.caselabproject.exceptions.organization.OrganizationNotFoundException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.repositories.OrganizationRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class BillingSecurityService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public boolean canGetBillingDetails(String userName, Long organizationId){
        System.out.println(organizationId);
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId));
        AtomicBoolean isAdmin = new AtomicBoolean(false);
        user.getRoles().forEach(r -> {
            if (r.getName().equals("ROLE_ADMIN")){
                isAdmin.set(true);
            }
        });
        return isAdmin.get();
    }
}
