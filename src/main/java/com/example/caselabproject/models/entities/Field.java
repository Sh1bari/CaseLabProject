package com.example.caselabproject.models.entities;

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;

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

    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "document_constructor_type_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DocumentConstructorType documentConstructorType;

}
