package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.RecordState;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Тип документа, по которому конструируется его набор полей. Также используется
 * при генерации word файла документа.
 */
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

    /**
     * Название документа. Оно же является заголовком документа при генерации его word файла.
     * Примеры: приказ, служебная записка, договор ГПХ.
     */
    private String name;

    /**
     * Префикс кода документа, после которого следует номер. Код документа = prefix + document_id.
     */
    private String prefix;

    /**
     * Список полей, которые должен содержать документ.
     */
    @OneToMany(mappedBy = "documentConstructorType", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Field> fields = new ArrayList<>();

    /**
     * Состояние типа документа: удален или активен. По умолчанию, при создании типа, статус ACTIVE.
     */
    @Enumerated(EnumType.STRING)
    private RecordState recordState;

    /**
     * Список документов, имеющих данный тип.
     */
    @OneToMany(mappedBy = "documentConstructorType", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Document> documents = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

}
