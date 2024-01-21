package com.example.caselabproject.services.security;

import com.example.caselabproject.exceptions.user.DeletedUserException;
import com.example.caselabproject.models.DTOs.RegistrationUserDto;
import com.example.caselabproject.models.entities.*;
import com.example.caselabproject.models.enums.OrganizationStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.AuthUserInfoRepository;
import com.example.caselabproject.repositories.SubscriptionRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SecurityRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserInfoRepository authUserInfoRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' is not found", username)
        ));
        if (user.getRecordState().equals(RecordState.DELETED)) {
            throw new DeletedUserException(username);
        }
        
        AuthUserInfo info = authUserInfoRepository.findAuthUserInfoByUserId(user.getId());
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                info.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    private final SubscriptionRepository subscriptionRepository;
    
    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        Organization org = new Organization();
        File avatar = new File();
        avatar.setUser(user);
        user.setAvatarPath(avatar);
        org.setCreator(user);
        user.setCreatedOrganization(org);
        
        Subscription subscription = subscriptionRepository.findById(1L).get();
        org.setName("Default");
        org.getEmployees().add(user);
        org.setStatus(OrganizationStatus.NOT_PAID);
        org.setSubscription(subscription);
        
        user.setOrganization(org);
        user.setIsDirector(false);
        AuthUserInfo authUserInfo = new AuthUserInfo();
        authUserInfo.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        authUserInfo.setEmail(registrationUserDto.getEmail());
        user.setAuthUserInfo(authUserInfo);
        authUserInfo.setUser(user);
        user.setUsername(registrationUserDto.getUsername());
        user.setRoles(List.of(roleService.getUserRole(), roleService.getAdminRole()));
        user.setRecordState(RecordState.ACTIVE);
        PersonalUserInfo personalUserInfo = new PersonalUserInfo();
        personalUserInfo.setUser(user);
        user.setPersonalUserInfo(personalUserInfo);
        user.setApplicationItems(new ArrayList<>());
        return userRepository.save(user);
    }
}
