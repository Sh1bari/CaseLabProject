package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.RecordState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

}
