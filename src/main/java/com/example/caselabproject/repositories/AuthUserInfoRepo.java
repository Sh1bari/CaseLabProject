package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.AuthUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AuthUserInfoRepo extends JpaRepository<AuthUserInfo, Integer> {
}
