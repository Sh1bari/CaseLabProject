package com.example.caselabproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@SpringBootApplication
public class CaseLabProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaseLabProjectApplication.class, args);
    }

}
