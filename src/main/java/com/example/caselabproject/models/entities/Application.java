package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.ApplicationStatus;
import com.example.caselabproject.models.enums.RecordState;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @Basic
    private LocalDateTime creationDate;

    @Basic
    private LocalDateTime deadlineDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private Document document;

    @OneToMany(mappedBy = "application", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<ApplicationItem> applicationItems;

    @Enumerated(EnumType.STRING)
    private RecordState recordState;


}
