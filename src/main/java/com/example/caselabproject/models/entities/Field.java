package com.example.caselabproject.models.entities;

import lombok.*;

import javax.persistence.*;

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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "document_constructor_type_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DocumentConstructorType documentConstructorType;
}
