package com.example.caselabproject.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;

    @Basic
    private LocalDate birthDate;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

}
