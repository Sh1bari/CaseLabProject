package com.example.caselabproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CaseLabProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaseLabProjectApplication.class, args);
    }

}
