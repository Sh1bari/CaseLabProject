package com.example.caselabproject;

import com.example.caselabproject.services.implementations.DocumentConstructorTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CaseLabProjectApplicationTests {
    @Autowired
    private DocumentConstructorTypeServiceImpl documentConstructorTypeService;

    @Test
    void contextLoads() {
    }

    @Test
    void document_constructor_type_service_works() {
        assertAll("trying different id",
                () -> assertEquals(Optional.empty(),
                        documentConstructorTypeService.findById(300_000L)),
                () -> assertEquals(Optional.empty(),
                        documentConstructorTypeService.findById(-1L))
        );
    }
}
