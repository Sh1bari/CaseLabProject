package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.RecordState;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentConstructorType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true)
    private String name;

    /**
     * Префикс кода документа, после которого следует номер. Код документа = prefix + document_id
     */
    private String prefix;

    @OneToMany(mappedBy = "documentConstructorType", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Field> fields;

    @Enumerated(EnumType.STRING)
    private RecordState recordState = RecordState.ACTIVE;

    @OneToMany(mappedBy = "documentConstructorType", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Document> documents;
}
