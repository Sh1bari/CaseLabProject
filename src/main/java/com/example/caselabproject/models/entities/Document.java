package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.RecordState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @Basic
    private LocalDateTime creationDate;

    @Basic
    private LocalDateTime updateDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "document_constructor_type_id")
    private DocumentConstructorType documentConstructorType;


    @OneToMany(mappedBy = "document", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<File> files;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "application_id")
    private Application application;

    @Enumerated(EnumType.STRING)
    private RecordState recordState;

    @ElementCollection
    @CollectionTable(name = "document_field_value", joinColumns = @JoinColumn(name = "document_id"))
    @MapKeyJoinColumn(name = "field_id")
    @Column(name = "value")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Map<Field, String> fieldsValues = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

}
