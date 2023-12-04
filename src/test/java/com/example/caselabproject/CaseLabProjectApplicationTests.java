package com.example.caselabproject;

import com.example.caselabproject.services.implementations.DocumentConstructorTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CaseLabProjectApplicationTests {
    @Autowired
    private DocumentConstructorTypeServiceImpl documentConstructorTypeService;

    @Test
    void contextLoads() {
    }
}
