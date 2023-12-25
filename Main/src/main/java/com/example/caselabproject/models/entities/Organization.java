package com.example.caselabproject.models.entities;


import com.example.caselabproject.models.enums.OrganizationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @OneToOne(mappedBy = "createdOrganization", orphanRemoval = true)
    private User creator;

    @OneToMany(mappedBy = "organization", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<User> employees;

    @OneToMany(mappedBy = "organization", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<Department> departments;

    @OneToMany(mappedBy = "organization", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<DocumentConstructorType> documentConstructorTypes;

    @OneToMany(mappedBy = "organization", orphanRemoval = true)
    private List<Application> applications;

    @OneToMany(mappedBy = "organization", orphanRemoval = true)
    private List<Bill> bills;

    @OneToMany(mappedBy = "organization", orphanRemoval = true)
    private List<Document> documents;

    @Enumerated(EnumType.STRING)
    private OrganizationStatus status;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

}