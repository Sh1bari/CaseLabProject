package com.example.caselabproject.models.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Поле документа.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Название поля документа. Например, ИНН или номер телефона.
     */
    @Column(unique = true)
    private String name;
}
