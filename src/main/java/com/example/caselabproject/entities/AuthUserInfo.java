package com.example.caselabproject.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table(name = "auth_user_info")
public class AuthUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @ToString.Exclude
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

}
