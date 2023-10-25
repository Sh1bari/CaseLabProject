package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.FieldType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "document_constructor_type_id")
    private DocumentConstructorType documentConstructorType;

    private String name;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

}
