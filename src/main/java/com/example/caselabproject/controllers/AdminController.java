package com.example.caselabproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminController {

    @GetMapping("/admin")
    public String adminData() {
        return "Admin data";
    }

}
