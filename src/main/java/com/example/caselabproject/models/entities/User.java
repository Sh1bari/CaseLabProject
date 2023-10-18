package com.example.caselabproject.models.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private AuthUserInfo authUserInfo;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"))
    private List<Role> roles;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToMany(mappedBy = "creator", orphanRemoval = true)
    private List<Document> documents;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private PersonalUserInfo personalUserInfo;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Application> applications;
}
