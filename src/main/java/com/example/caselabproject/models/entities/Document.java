package com.example.caselabproject.models.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;
    @Basic
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "document_constructor_type_id")
    private DocumentConstructorType documentConstructorType;


    @OneToMany(mappedBy = "document", orphanRemoval = true)
    private List<File> files;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

}
