package com.example.caselabproject.models.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class DocumentConstructorType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "documentConstructorType", orphanRemoval = true)
    private List<Document> documents;

    @OneToMany(mappedBy = "documentConstructorType", orphanRemoval = true)
    private List<Field> fields;
}
