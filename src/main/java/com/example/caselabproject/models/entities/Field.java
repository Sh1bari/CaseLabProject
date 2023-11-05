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
    private String name;

    /**
     * Ссылка на тип документа, который содержит данное поле.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "document_constructor_type_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DocumentConstructorType documentConstructorType;

}
