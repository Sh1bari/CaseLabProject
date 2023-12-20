package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.AuthUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserInfoRepository extends JpaRepository<AuthUserInfo, Integer> {
}
