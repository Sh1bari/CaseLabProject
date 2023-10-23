package com.example.caselabproject.models.DTOs;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
