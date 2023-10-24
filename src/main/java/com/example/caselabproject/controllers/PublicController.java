package com.example.caselabproject.controllers;

import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.Status;
import com.example.caselabproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class PublicController {


    @GetMapping("/unsecured")
    public String unsecuredData() {
        return "Unsecured data";
    }

    @GetMapping("/secured")
    public String securedData() {
        return "Secured data";
    }

    @GetMapping("/info")
    public String userData(Principal principal) {
        Authentication authentication = (Authentication) principal;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("User authorities: " + authorities);
        return principal.getName();
    }
}