package com.example.caselabproject.models.DTOs.request.jwt;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
