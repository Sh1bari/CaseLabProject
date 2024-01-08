package com.example.caselabproject.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String type;

    private Long size;

    private String path;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "document_id")
    private Document document;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

}
