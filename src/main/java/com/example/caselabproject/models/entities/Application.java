package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.ApplicationStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Basic
    private LocalDateTime creationDate;

    @Basic
    private LocalDateTime deadlineDate;

    @OneToMany(mappedBy = "application", orphanRemoval = true)
    private List<Document> documents;

    private ApplicationStatus status;

    private String comment;

}
