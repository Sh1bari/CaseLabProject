package com.example.caselabproject.models.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "department", orphanRemoval = true)
    private List<User> users;

    @OneToMany(mappedBy = "department", orphanRemoval = true)
    private List<Application> applications;
}
