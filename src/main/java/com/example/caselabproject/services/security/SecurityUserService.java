package com.example.caselabproject.services.security;

import com.example.caselabproject.models.DTOs.RegistrationUserDto;
import com.example.caselabproject.models.entities.AuthUserInfo;
import com.example.caselabproject.models.entities.PersonalUserInfo;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.AuthUserInfoRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {
    private UserRepository userRepository;
    private SecurityRoleService roleService;
    private PasswordEncoder passwordEncoder;

    private final AuthUserInfoRepository authUserInfoRepo;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleService(SecurityRoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getAuthUserInfo().getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        AuthUserInfo authUserInfo = new AuthUserInfo();
        authUserInfo.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        authUserInfo.setEmail(registrationUserDto.getEmail());
        authUserInfo.setUser(user);
        user.setAuthUserInfo(authUserInfo);
        user.setUsername(registrationUserDto.getUsername());
        user.setRoles(List.of(roleService.getUserRole()));
        user.setRecordState(RecordState.ACTIVE);
        user.setPersonalUserInfo(new PersonalUserInfo());
        return userRepository.save(user);
    }
}
