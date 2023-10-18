package com.example.caselabproject.models.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @OneToOne(mappedBy = "document", orphanRemoval = true)
    private File file;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

}
