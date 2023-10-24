package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.ApplicationStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class ApplicationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    private LocalDateTime voteTime;

    @Enumerated(EnumType.STRING)
    private ApplicationItemStatus status;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
