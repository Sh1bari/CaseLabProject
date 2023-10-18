package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.AuthUserInfo;
import org.springframework.data.repository.CrudRepository;

public interface AuthUserInfoRepo extends CrudRepository<AuthUserInfo, Integer> {
}
